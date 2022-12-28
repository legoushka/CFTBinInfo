package ru.legoushka.cftbinlist.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "history")
data class BinInfoSearchHistory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "bin") var bin: String = "",
    @ColumnInfo(name = "bin_info") var binInfo: BinInfo = BinInfo(),
    @ColumnInfo(name = "time") var time: String = "",
)

class Converters {
    @TypeConverter
    fun binInfoFromBlob(value: String?): BinInfo? {
        return value?.let { Json.decodeFromString(value) }
    }

    @TypeConverter
    fun binInfoToBlob(binInfo: BinInfo?): String? {
        return Json.encodeToString(binInfo)
    }
}
