package org.jetbrains.squash.dialects.postgres

import org.jetbrains.squash.definition.ColumnDefinition
import org.jetbrains.squash.definition.TableDefinition

/**
 * Creates a [Json] column
 */
fun TableDefinition.json(name: String): ColumnDefinition<Json> = createColumn(name, JsonColumnType)

/**
 * Creates a [JsonB] column
 */
fun TableDefinition.jsonb(name: String): ColumnDefinition<JsonB> = createColumn(name, JsonBColumnType)