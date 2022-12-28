package ru.legoushka.cftbinlist.data.source

import kotlinx.coroutines.flow.Flow
import ru.legoushka.cftbinlist.data.models.BinInfoSearchHistory

interface SearchHistorySource {
    fun getHistoryStream(): Flow<List<BinInfoSearchHistory>>

    suspend fun insertStamp(binInfoSearchHistory: BinInfoSearchHistory)

    suspend fun deleteHistory()
}