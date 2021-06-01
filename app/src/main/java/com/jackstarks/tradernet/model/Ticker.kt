package com.jackstarks.tradernet.model

data class Ticker(
    val c: String = "",
    val pcp: Double = 0.0,
    val ltr: String = "",
    val name: String = "",
    val ltp: String = "",
    val chg: Double = 0.0,
) {
    override fun hashCode(): Int {
        return c.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Ticker -> this.c == other.c
            else -> false
        }
    }
}
