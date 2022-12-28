package ru.legoushka.cftbinlist.data.source.local

import ru.legoushka.cftbinlist.data.models.BinInfo
import ru.legoushka.cftbinlist.data.source.BinInfoDataSource

class BinInfoLocalDataSource: BinInfoDataSource {
    override suspend fun getBinInfo(bin: String): BinInfo? {
        return null
    }
}