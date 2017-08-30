package org.jetbrains.squash.dialects.postgres.tests

import org.jetbrains.squash.tests.DatabaseTests
import org.jetbrains.squash.tests.DefinitionTests
import org.junit.Test

class PgDefinitionTests : DefinitionTests(), DatabaseTests by PgDatabaseTests() {

    @Test
    fun `json`() {

    }
}