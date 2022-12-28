package ru.legoushka.cftbinlist.data.source

import kotlinx.coroutines.flow.Flow
import ru.legoushka.cftbinlist.data.models.BinInfo
import ru.legoushka.cftbinlist.data.models.BinInfoSearchHistory

interface BinInfoRepository {

    suspend fun getBinInfo(bin: String): BinInfo?

    fun getHistoryStream(): Flow<List<BinInfoSearchHistory>>

    suspend fun deleteHistory()

    suspend fun insertStamp(binInfo: BinInfo, bin: String)
}