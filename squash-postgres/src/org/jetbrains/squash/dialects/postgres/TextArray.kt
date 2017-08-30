package org.jetbrains.squash.dialects.postgres

import java.util.*

data class TextArray(val array: Array<out String>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextArray

        if (!Arrays.equals(array, other.array)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(array)
    }
}