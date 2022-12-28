package ru.legoushka.cftbinlist.data.source

import ru.legoushka.cftbinlist.data.models.BinInfo

interface BinInfoRepository {

    suspend fun getBinInfo(bin: String): BinInfo?
}