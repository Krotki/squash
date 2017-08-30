package org.jetbrains.squash.dialects.postgres

import org.jetbrains.squash.drivers.*
import org.postgresql.util.PGobject
import java.sql.Connection
import kotlin.reflect.KClass

class PgDataConversion : JDBCDataConversion() {

    override fun convertValueFromDatabase(value: Any?, type: KClass<*>): Any? = when {
        value is PGobject && value.type.toLowerCase() == "json"
                && type.javaObjectType == Json::class.javaObjectType -> Json(value.value)
        value is PGobject && value.type.toLowerCase() == "jsonb"
                && type.javaObjectType == JsonB::class.javaObjectType -> JsonB(value.value)
        else -> super.convertValueFromDatabase(value, type)
    }

    override fun convertValueToDatabase(value: Any?, connection: Connection): Any? = when (value) {
        is TextArray -> connection.createArrayOf("text", value.array)
        is Json -> PGobject().apply {
            this.type = "json"
            this.value = value.json
        }
        is JsonB -> PGobject().apply {
            this.type = "jsonb"
            this.value = value.json
        }
        else -> super.convertValueToDatabase(value, connection)
    }
}