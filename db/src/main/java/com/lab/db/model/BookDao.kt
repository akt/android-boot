package com.lab.db.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

interface BaseDao<T> {

    @Insert
    fun insert(vararg obj: T)
}

@Dao
interface BookDao : BaseDao<Book> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(user: Book)

    @Query("SELECT * FROM Books Where bookId = :id")
    fun getBookById(id: String): Flowable<Book>


    @Query("DELETE FROM Books")
    fun deleteAllBooks()


}