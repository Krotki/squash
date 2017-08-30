package org.jetbrains.squash.dialects.postgres

import org.jetbrains.squash.definition.ColumnType

object JsonColumnType : ColumnType(Json::class)
object JsonBColumnType : ColumnType(JsonB::class)

sealed class PgJson

data class Json(val json: String) : PgJson() {
    override fun toString(): String {
        return json
    }
}

data class JsonB(val json: String) : PgJson() {
    override fun toString(): String {
        return json
    }
}

fun String.toJson() = Json(this)
fun String.toJsonB() = JsonB(this)
