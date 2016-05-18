package org.jetbrains.squash.tests

import kotlinx.support.jdk7.*
import org.jetbrains.squash.*
import org.jetbrains.squash.tests.data.*
import org.junit.*
import kotlin.test.*

abstract class SchemaTests : DatabaseTests {
    @Test
    fun emptySchema() {
        withTransaction {
            val tables = querySchema().tables().toList()
            assertEquals(0, tables.size)
        }
    }

    protected open val sqlSingleTableSchema = "CREATE TABLE TEST(ID INT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR(255))"

    @Test
    fun singleTableSchema() {
        withTransaction {
            executeStatement(sqlSingleTableSchema)

            val schema = querySchema()
            val tables = schema.tables().toList()
            assertEquals(1, tables.size)
            assertEquals("TEST", tables[0].name)
            val columns = tables[0].columns().toList()
            assertEquals(2, columns.size)
            assertEquals("ID", columns[0].name)
            assertEquals(true, columns[0].autoIncrement)
            assertEquals(10, columns[0].size)
            assertEquals("NAME", columns[1].name)
            assertEquals(false, columns[1].autoIncrement)
            assertEquals(255, columns[1].size)
        }
    }


    @Test
    fun citiesDDL() {
        withCities {
            connection.dialect.definition.tableSQL(Cities).assertSQL(sqlCitiesDDL)
            connection.dialect.definition.tableSQL(Citizens).assertSQL {
                "CREATE TABLE IF NOT EXISTS Citizens (id VARCHAR(10) NOT NULL, name VARCHAR(50) NOT NULL, city_id INT NULL, CONSTRAINT PK_Citizens PRIMARY KEY (id))"
            }
        }
    }

    protected open val sqlCitiesDDL: String
        get() = "CREATE TABLE IF NOT EXISTS Cities (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(50) NOT NULL, CONSTRAINT PK_Cities PRIMARY KEY (id))"

    @Test
    fun citiesSchema() {
        withTransaction {
            val database = Database(connection, listOf(Cities, Citizens))
            database.createSchema()
            val validationResult = database.validateSchema()
            if (validationResult.any()) {
                fail(validationResult.joinToString("\n") { it.message })
            }

            connection.createTransaction().use { transaction ->
                val schema = transaction.querySchema()
                val tables = schema.tables().toList()
                assertEquals(2, tables.size)

                with(tables[0]) {
                    kotlin.test.assertEquals("CITIES", name)
                    val columns = columns().toList()
                    kotlin.test.assertEquals(2, columns.size)
                    with(columns[0]) {
                        kotlin.test.assertEquals("ID", name)
                        kotlin.test.assertEquals(true, autoIncrement)
                        kotlin.test.assertEquals(false, nullable)
                        kotlin.test.assertEquals(10, size)
                    }
                    with(columns[1]) {
                        kotlin.test.assertEquals("NAME", name)
                        kotlin.test.assertEquals(false, autoIncrement)
                        kotlin.test.assertEquals(50, size)
                        kotlin.test.assertEquals(false, nullable)
                    }
                }

                with(tables[1]) {
                    kotlin.test.assertEquals("CITIZENS", name)
                    val columns = columns().toList()
                    kotlin.test.assertEquals(3, columns.size)
                    with(columns[0]) {
                        kotlin.test.assertEquals("ID", name)
                        kotlin.test.assertEquals(false, autoIncrement)
                        kotlin.test.assertEquals(false, nullable)
                        kotlin.test.assertEquals(10, size)
                    }
                    with(columns[1]) {
                        kotlin.test.assertEquals("NAME", name)
                        kotlin.test.assertEquals(false, autoIncrement)
                        kotlin.test.assertEquals(50, size)
                        kotlin.test.assertEquals(false, nullable)
                    }
                    with(columns[2]) {
                        kotlin.test.assertEquals("CITY_ID", name)
                        kotlin.test.assertEquals(false, autoIncrement)
                        kotlin.test.assertEquals(10, size)
                        kotlin.test.assertEquals(true, nullable)
                    }
                }
            }
        }
    }
}