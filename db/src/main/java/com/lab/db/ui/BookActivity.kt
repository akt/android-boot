package com.lab.db.ui

import android.app.Activity
import android.os.Bundle
import com.lab.db.R
import com.lab.db.model.Book
import com.lab.db.model.BookDatabase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_book.*

class BookActivity : Activity() {

    private val disposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        update_book_button.setOnClickListener {
            updateBookName()
        }

    }

    override fun onStart() {
        super.onStart()
        // Subscribe to the emissions of the user name from the view model.
        // Update the user name text view, at every onNext emission.
        // In case of error, log the exception.
        BookDatabase.getInstance(applicationContext).bookDao().getBookById("1")
            .map { t: Book -> t.bookName }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ book_name.text = it }, {}).let {
                disposable.add(it)
            }
    }

    override fun onStop() {
        super.onStop()

        // clear all the subscription
        disposable.clear()
    }

    private fun updateBookName() {
        val bookName = book_name_input.text.toString()
        update_book_button.isEnabled = false

        Completable.fromAction {
            BookDatabase.getInstance(applicationContext).bookDao().insertBook(Book("1", bookName))
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                update_book_button.isEnabled = true
            }, {})
            .let { disposable.add(it) }
    }


}