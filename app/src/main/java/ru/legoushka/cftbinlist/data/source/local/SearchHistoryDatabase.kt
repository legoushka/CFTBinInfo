package ru.legoushka.cftbinlist.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.legoushka.cftbinlist.data.models.BinInfoSearchHistory
import ru.legoushka.cftbinlist.data.models.Converters


@Database(entities = [BinInfoSearchHistory::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SearchHistoryDatabase : RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDao
}