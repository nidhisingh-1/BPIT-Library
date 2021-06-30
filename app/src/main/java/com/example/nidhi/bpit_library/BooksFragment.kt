package com.example.nidhi.bpit_library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_books.*
import kotlinx.android.synthetic.main.list_item_books.*

class BooksFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tempArray = arrayListOf<BookModel>()


        val db = DbHelper(requireContext()).writableDatabase

        var availableBooks = BookTable.getAllBooks(db)

        if (availableBooks.size == 0) {
            val hardCodedWorkshops = BookModel.getalltheBooks()

            for (workshop in hardCodedWorkshops) {
                BookTable.insertBooks(db, workshop)
            }

            availableBooks = BookTable.getAllBooks(db)
        }

        availableBooks[availableBooks.size - 1].let { mostRecent ->

        }

        rvBooks.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            adapter = BooksAdapter(
                requireActivity(),
                this@BooksFragment,
                availableBooks
            )
            scrollToPosition(availableBooks.size - 1)
        }
    }
}