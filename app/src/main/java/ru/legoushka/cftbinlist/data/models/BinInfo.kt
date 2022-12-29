package ru.legoushka.cftbinlist.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BinInfo(
    @SerialName("bank")
    val bank: Bank = Bank(),
    @SerialName("brand")
    val brand: String? = null,
    @SerialName("country")
    val country: Country = Country(),
    @SerialName("number")
    val number: Number = Number(),
    @SerialName("prepaid")
    val prepaid: Boolean? = null,
    @SerialName("scheme")
    val scheme: String? = null,
    @SerialName("type")
    val type: String? = null
)