package com.example.assignment02.data.repository

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assignment02.data.model.Word
import kotlinx.coroutines.flow.Flow

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordDatabase: RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var instance: WordDatabase? = null

        @Synchronized
        fun getInstance(context: Context): WordDatabase? {
            if (instance == null) {
                synchronized(WordDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordDatabase::class.java,
                        "word-database"
                    ).build().also { instance = it }
                }
            }
            return instance
        }
    }
}

@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    fun getData(): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: Word)

    @Delete
    suspend fun delete(word: Word)
}
