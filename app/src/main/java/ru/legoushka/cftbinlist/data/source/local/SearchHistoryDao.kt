package ru.legoushka.cftbinlist.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.legoushka.cftbinlist.data.models.BinInfoSearchHistory

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM History")
    fun observeHistory(): Flow<List<BinInfoSearchHistory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStamp(binInfoSearchHistory: BinInfoSearchHistory)

    @Query("DELETE FROM History")
    suspend fun deleteHistory()
}