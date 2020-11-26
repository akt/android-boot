package com.lab.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "books")
class Book(
    @PrimaryKey
    @ColumnInfo(name = "bookId")
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "bookName")
    val bookName: String
)
