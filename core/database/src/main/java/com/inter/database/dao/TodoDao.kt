package com.inter.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.inter.database.entities.TodoEntity

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodo(vararg todo: TodoEntity)

    @Transaction
    @Query("SELECT * FROM TODO")
    fun getAllTodos(): List<TodoEntity>
}