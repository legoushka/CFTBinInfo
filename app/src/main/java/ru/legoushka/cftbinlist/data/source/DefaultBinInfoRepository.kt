package ru.legoushka.cftbinlist.data.source

import kotlinx.coroutines.flow.Flow
import ru.legoushka.cftbinlist.data.models.BinInfo
import ru.legoushka.cftbinlist.data.models.BinInfoSearchHistory
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class DefaultBinInfoRepository(
    private val remoteDataSource: BinInfoDataSource,
    private val searchHistorySource: SearchHistorySource
) : BinInfoRepository {
    override suspend fun getBinInfo(bin: String): BinInfo? {
        val remote = remoteDataSource.getBinInfo(bin)
        if (remote != null){
            return remoteDataSource.getBinInfo(bin)
        }
        else{
            return searchHistorySource.getBinInfo(bin)
        }
    }

    override fun getHistoryStream(): Flow<List<BinInfoSearchHistory>> {
        return searchHistorySource.getHistoryStream()
    }

    override suspend fun deleteHistory() {
        searchHistorySource.deleteHistory()
    }

    override suspend fun insertStamp(binInfo: BinInfo, bin: String) {
        val time = Instant.ofEpochMilli(Instant.now().toEpochMilli())
            .atZone( ZoneId.systemDefault() )
            .toLocalDateTime()
            .format(
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            ).toString()
        val stamp = BinInfoSearchHistory(bin = bin, binInfo = binInfo, time = time, id = 0)
        searchHistorySource.insertStamp(stamp)
    }
}