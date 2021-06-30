package com.example.nidhi.bpit_library

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils

object BookTable {
    private const val TABLE_NAME = "Books"

    object Columns {
        const val ID = "id"
        const val IMAGE_ID = "image_id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val BOOKS_AVAILABLE = "number"
    }

    val CMD_CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME
        (
        ${Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Columns.IMAGE_ID} INTEGER,
        ${Columns.TITLE} TEXT,
        ${Columns.DESCRIPTION} TEXT,
        ${Columns.BOOKS_AVAILABLE} TEXT
        );
        """.trimIndent()

    fun insertBooks(db: SQLiteDatabase, book: BookModel) {
        val row = ContentValues()
        row.put(Columns.IMAGE_ID, book.imageId)
        row.put(Columns.TITLE, book.title)
        row.put(Columns.DESCRIPTION, book.description)
        row.put(Columns.BOOKS_AVAILABLE, book.BooksAvailable)

        db.insert(TABLE_NAME, null, row)
    }

    fun getAllBooks(db: SQLiteDatabase): ArrayList<BookModel> {
        val books = ArrayList<BookModel>()
        val c = db.query(
            TABLE_NAME,
            null,
            null, null,
            null, null,
            null
        )

        while (c.moveToNext()) {
            val workshop = BookModel(
                c.getInt(1), c.getString(2), c.getString(3), c.getString(4),
                c.getInt(0)
            )
            books.add(workshop)
        }

        c.close()
        return books
    }

    fun getBooksAddedToList(
        db: SQLiteDatabase,
        bookaddedIds : MutableSet<String>
    ): ArrayList<BookModel> {
        val books = ArrayList<BookModel>()

        val selectionArgs = bookaddedIds.toTypedArray()
        val params = ArrayList<String>()

        for (i in 0..selectionArgs.size) {
            params.add("?")
        }

        val selection = "${Columns.ID} in (${TextUtils.join(",", params)})"

        val c = db.query(
            TABLE_NAME,
            null,
            selection, selectionArgs,
            null, null,
            null
        )

        while (c.moveToNext()) {
            val book = BookModel(
                c.getInt(1), c.getString(2), c.getString(3), c.getString(4),
                c.getInt(0)
            )
            books.add(book)
        }

        c.close()
        return books
    }
}