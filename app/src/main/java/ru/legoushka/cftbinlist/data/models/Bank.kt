package ru.legoushka.cftbinlist.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bank(
    @SerialName("city")
    val city: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("url")
    val url: String? = null
)