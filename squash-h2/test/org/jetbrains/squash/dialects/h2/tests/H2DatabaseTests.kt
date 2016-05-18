package org.jetbrains.squash.dialects.h2.tests

import kotlinx.support.jdk7.*
import org.jetbrains.squash.*
import org.jetbrains.squash.definition.*
import org.jetbrains.squash.dialects.h2.*
import org.jetbrains.squash.tests.*

class H2DatabaseTests : DatabaseTests {
    fun withConnection(block: (DatabaseConnection) -> Unit) {
        H2Dialect.createMemoryConnection().use(block)
    }

    override fun withTables(vararg tables: Table, statement: Transaction.() -> Unit) {
        withConnection { connection ->
            val database = Database(connection, tables.toList())
            database.createSchema()
            connection.createTransaction().use(statement)
        }
    }

    override fun withTransaction(statement: Transaction.() -> Unit) {
        withConnection { connection ->
            connection.createTransaction().use(statement)
        }
    }
}