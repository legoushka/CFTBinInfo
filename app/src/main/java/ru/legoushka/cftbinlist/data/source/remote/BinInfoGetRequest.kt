package ru.legoushka.cftbinlist.data.source.remote

import kotlinx.serialization.Serializable

@Serializable
data class BinInfoGetRequest(
    val bin: String
)

@Serializable
data class BinInfoGetResponse(
    val title: String,
    val author: String,
    val startDate: Int,
    val daysDuration: Int
)
