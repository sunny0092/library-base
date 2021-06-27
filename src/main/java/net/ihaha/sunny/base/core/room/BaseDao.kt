package net.ihaha.sunny.base.core.room

import androidx.room.*

@Dao
interface BaseDao <T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: List<T>?)

    @Delete
    suspend fun delete(obj: T?)

    @Delete
    suspend fun delete(obj: List<T>?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(obj: T?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(obj: List<T>)
}