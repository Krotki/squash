package org.jetbrains.squash.dialects.postgres

import org.jetbrains.squash.definition.*
import org.jetbrains.squash.dialect.*
import org.jetbrains.squash.expressions.*

object PgDialect : BaseSQLDialect("Postgres") {

    override val definition: DefinitionSQLDialect = object : BaseDefinitionSQLDialect(this) {
        override fun columnTypeSQL(builder: SQLStatementBuilder, column: Column<*>) {
            if (column.hasProperty<AutoIncrementProperty>()) {
                require(!column.hasProperty<NullableProperty>()) { "Column ${column.name} cannot be both AUTOINCREMENT and NULL" }
                val type = column.type
                val autoincrement = when (type) {
                    is IntColumnType -> "SERIAL"
                    is LongColumnType -> "BIGSERIAL"
                    else -> error("AutoIncrement column for '$type' is not supported by $this")
                }
                builder.append(autoincrement)
            } else super.columnTypeSQL(builder, column)
        }

        override fun columnAutoIncrementProperty(builder: SQLStatementBuilder, property: AutoIncrementProperty?) {
            // do nothing, we already handled AutoIncrementProperty as SERIAL
        }

        override fun columnTypeSQL(builder: SQLStatementBuilder, type: ColumnType) {
            when (type) {
                is UUIDColumnType -> builder.append("UUID")
                is BlobColumnType -> builder.append("BYTEA")
                is BinaryColumnType -> builder.append("BYTEA")
                is DateTimeColumnType -> builder.append("TIMESTAMP")
                is JsonColumnType -> builder.append("JSON")
                is JsonBColumnType -> builder.append("JSONB")
                else -> super.columnTypeSQL(builder, type)
            }
        }
    }

    override fun appendBinaryOperator(builder: SQLStatementBuilder, expression: BinaryExpression<*, *, *>) = with(builder) {
        when (expression) {
            is JsonElementExpression -> append("->")
            is JsonElementAsTextExpression  -> append("->>")
            is JsonPathExpression -> append("#>")
            is JsonPathAsTextExpression -> append("#>>")
            is JsonContainsExpression -> append("@>")
            is JsonContainedInExpression -> append("<@")
            is JsonContainsKeyExpression -> append("??")
            is JsonContainsAnyKeyExpression -> append("??|")
            is JsonContainsAllKeysExpression -> append("??&")
            else -> super.appendBinaryOperator(builder, expression)
        }
    }
}