package org.jetbrains.squash.dialects.postgres.tests

import org.jetbrains.squash.definition.IntColumnType
import org.jetbrains.squash.definition.TableDefinition
import org.jetbrains.squash.dialects.postgres.json
import org.jetbrains.squash.dialects.postgres.jsonb
import org.jetbrains.squash.tests.AllColumnTypesTests
import org.jetbrains.squash.tests.DatabaseTests
import org.junit.Test

class PgAllColumnTypesTests : AllColumnTypesTests(), DatabaseTests by PgDatabaseTests() {
    override val allColumnsTableSQL: String
        get() = "CREATE TABLE IF NOT EXISTS AllColumnTypes (" +
                "id ${getIdColumnType(IntColumnType)}, " +
                "\"varchar\" VARCHAR(42) NOT NULL, " +
                "\"char\" CHAR NOT NULL, " +
                "enum INT NOT NULL, " +
                "\"decimal\" DECIMAL(5, 2) NOT NULL, " +
                "long BIGINT NOT NULL, " +
                "\"date\" DATE NOT NULL, " +
                "bool BOOLEAN NOT NULL, " +
                "datetime TIMESTAMP NOT NULL, " +
                "text TEXT NOT NULL, " +
                "\"binary\" BYTEA NOT NULL, " +
                "\"blob\" BYTEA NOT NULL, " +
                "uuid UUID NOT NULL, " +
                "CONSTRAINT PK_AllColumnTypes PRIMARY KEY (id))"

    object PgColumnTypes : TableDefinition() {
        val json = json("json")
        val jsonb = jsonb("jsonb")
    }

    @Test
    fun `create table for json types`() {
        withTransaction {
            connection.dialect.definition.tableSQL(PgColumnTypes).assertSQL {
                """CREATE TABLE IF NOT EXISTS PgColumnTypes (json JSON NOT NULL, jsonb JSONB NOT NULL)"""
            }
        }
    }
}