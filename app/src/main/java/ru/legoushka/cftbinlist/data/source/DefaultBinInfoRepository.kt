package ru.legoushka.cftbinlist.data.source

import ru.legoushka.cftbinlist.data.models.BinInfo

class DefaultBinInfoRepository(
    private val remoteDataSource: BinInfoDataSource,
    private val localDataSource: BinInfoDataSource
) : BinInfoRepository {
    override suspend fun getBinInfo(bin: String): BinInfo? {
        return remoteDataSource.getBinInfo(bin)
    }
}