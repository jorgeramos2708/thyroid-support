package com.jorgeramos.thyroidsupport.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jorgeramos.thyroidsupport.domain.MealPlanEntry

@Dao
interface MealPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MealPlanEntry): Long

    @Query("SELECT * FROM meal_plan_entry WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC")
    suspend fun getEntriesForDateRange(startDate: Long, endDate: Long): List<MealPlanEntry>

    @Query("SELECT * FROM meal_plan_entry WHERE date = :date ORDER BY id ASC")
    suspend fun getEntriesForDate(date: Long): List<MealPlanEntry>

    @Query("DELETE FROM meal_plan_entry WHERE id = :id")
    suspend fun delete(id: Long)
}
