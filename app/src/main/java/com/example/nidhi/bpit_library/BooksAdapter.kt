package com.example.nidhi.bpit_library

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.list_item_books.view.*

const val BOOK_ADDED_TO_LIST = "bookaddedIds"

class BooksAdapter(
    private val activityRef: Activity,
    private val fragment: Fragment,
    private val books: ArrayList<BookModel>,
) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    private val prefs = activityRef.getPreferences(Context.MODE_PRIVATE)

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder =
        BookViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_books, parent, false)
        )

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.itemView.apply {
            if (fragment is Dashboard) {
                applyBtn.visibility = View.GONE
            } else {
                if (auth.currentUser != null && prefs.getStringSet(
                        auth.uid.toString() + BOOK_ADDED_TO_LIST,
                        HashSet<String>()
                    )
                        ?.contains(books[position].id.toString()) == true
                ) {
                    applyBtn.apply {
                        text = context.getString(R.string.add_to_list)
                        setBackgroundColor(
                            ContextCompat.getColor(
                                activityRef,
                                R.color.black
                            )
                        )
                        isEnabled = false
                    }
                } else {
                    applyBtn.apply {
                        text = context.getString(R.string.add_to_list)
                        setBackgroundColor(
                            ContextCompat.getColor(
                                activityRef,
                                R.color.black
                            )
                        )
                        isEnabled = true
                    }

                    applyBtn.setOnClickListener {
                        if (auth.currentUser != null && auth.currentUser?.isEmailVerified == true) {
                            activityRef.createAlertDialog(
                                "Confirm Apply",
                                "Are you sure you want to add ${books[position].title}?",
                                "Yes",
                                "Cancel"
                            ) {
                                val modifiedAppliedWorkshopsIds = HashSet<String>(
                                    prefs.getStringSet(
                                        auth.uid.toString() + BOOK_ADDED_TO_LIST,
                                        HashSet<String>()
                                    )
                                )

                                modifiedAppliedWorkshopsIds.add(books[position].id.toString())

                                prefs.edit {
                                    putStringSet(
                                        auth.uid.toString() + BOOK_ADDED_TO_LIST,
                                        modifiedAppliedWorkshopsIds
                                    )
                                }

                                applyBtn.apply {
                                    text = context.getString(R.string.add_to_list)
                                    setBackgroundColor(
                                        ContextCompat.getColor(
                                            activityRef,
                                            R.color.black
                                        )
                                    )
                                    isEnabled = false
                                }

                                Toast.makeText(
                                    activityRef,
                                    "Applied Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            (activityRef as MainActivity).setFragmentWithBackstack(AuthFragment())
                        }
                    }
                }
            }

            Glide.with(this).load(books[position].imageId).into(imgView)

            tvTitle.text = books[position].title
            tvDescription.text = books[position].description
            tvTime.text = books[position].BooksAvailable
            books_available.text = "Books Available: "
        }
    }

    override fun getItemCount() = books.size

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
