package org.jetbrains.squash.drivers

import org.jetbrains.squash.connection.*
import java.sql.*
import java.time.*
import kotlin.reflect.*

open class JDBCDataConversion {
    open fun convertValueToDatabase(value: Any?, connection: Connection): Any? {
        if (value == null)
            return null
        return when (value) {
            is Enum<*> -> value.ordinal
            is LocalDate -> Date.valueOf(value)
            is LocalTime -> Time.valueOf(value)
            is LocalDateTime -> Timestamp.valueOf(value)
            is JDBCBinaryObject -> value.bytes
            else -> value
        }
    }

    open fun convertValueFromDatabase(value: Any?, type: KClass<*>): Any? {
        return when {
            value == null -> null
            value is Int && type.java.superclass == java.lang.Enum::class.java -> type.java.enumConstants[value]
            value is Clob -> value.characterStream.readText()
            value is Timestamp -> value.toLocalDateTime()
            value is Date -> value.toLocalDate()
            value is Time -> value.toLocalTime()
            value is Blob -> JDBCBinaryObject(value.getBytes(1, value.length().toInt()))
            value is ByteArray && type == BinaryObject::class -> JDBCBinaryObject(value)
            type.javaObjectType.isInstance(value) -> value
            value is Long && type.javaObjectType == Int::class.javaObjectType -> value.toInt()
            value is Int && type.javaObjectType == Long::class.javaObjectType -> value.toLong()
            else -> error("Cannot convert value of type `${value.javaClass}` to type `$type`")
        }
    }
}
