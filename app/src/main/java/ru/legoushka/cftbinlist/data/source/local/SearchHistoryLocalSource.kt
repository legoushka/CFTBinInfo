package ru.legoushka.cftbinlist.data.source.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.legoushka.cftbinlist.data.models.BinInfo
import ru.legoushka.cftbinlist.data.models.BinInfoSearchHistory
import ru.legoushka.cftbinlist.data.source.SearchHistorySource

class SearchHistoryLocalSource(
    private val dao: SearchHistoryDao
) : SearchHistorySource {
    override fun getHistoryStream(): Flow<List<BinInfoSearchHistory>> {
        return dao.observeHistory()
    }

    override suspend fun getBinInfo(bin: String): BinInfo? = withContext(context = Dispatchers.IO){
        return@withContext dao.getBinInfo().firstOrNull { it.bin == bin }?.binInfo
    }

    override suspend fun insertStamp(binInfoSearchHistory: BinInfoSearchHistory) = withContext(context = Dispatchers.IO) {
        dao.insertStamp(binInfoSearchHistory)
    }

    override suspend fun deleteHistory() = withContext(context = Dispatchers.IO){
        dao.deleteHistory()
    }
}