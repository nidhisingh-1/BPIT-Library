package com.example.nidhi.bpit_library

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_dashboard.*


class Dashboard : Fragment() {


    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = DbHelper(requireContext()).writableDatabase

        val prefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val userName = prefs.getString(auth.uid.toString() + USER_NAME, "Anonymous")

        val booksAdded =
            prefs.getStringSet(auth.uid.toString() + BOOK_ADDED_TO_LIST, HashSet<String>())
                ?.let {
                    BookTable.getBooksAddedToList(
                        db,
                        it
                    )
                } ?: ArrayList()

        tvUserName.text = requireContext().getString(R.string.welcome, userName)
        tvAppliedCount.text = booksAdded.size.toString()

        rvAppliedWorkshops.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            adapter =
                BooksAdapter(requireActivity(), this@Dashboard, booksAdded)
            scrollToPosition(booksAdded.size - 1)
        }
    }
}