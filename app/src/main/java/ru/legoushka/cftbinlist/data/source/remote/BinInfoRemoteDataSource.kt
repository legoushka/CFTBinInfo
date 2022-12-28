package ru.legoushka.cftbinlist.data.source.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.legoushka.cftbinlist.data.models.BinInfo
import ru.legoushka.cftbinlist.data.source.BinInfoDataSource

class BinInfoRemoteDataSource (
    private val client: HttpClient
): BinInfoDataSource {

    override suspend fun getBinInfo(bin: String): BinInfo? = withContext(context = Dispatchers.IO) {
        try {
            val calendarDataResponse = client.get {
                url(BASE_URL + bin)
            }
            return@withContext calendarDataResponse.body<BinInfo>()
        } catch (e: Exception) {
            return@withContext null
        }
    }

    companion object {
        private val BASE_URL = "https://lookup.binlist.net/"
    }

}