package ru.legoushka.cftbinlist.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Number(
    @SerialName("length")
    val length: Int = 0,
    @SerialName("luhn")
    val luhn: Boolean = false
)