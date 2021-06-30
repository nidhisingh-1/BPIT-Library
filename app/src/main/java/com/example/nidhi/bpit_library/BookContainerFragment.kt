package com.example.nidhi.bpit_library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_book_container.*


class BookContainerFragment : Fragment() {

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (auth.currentUser != null && auth.currentUser?.isEmailVerified == true) {
            btnHome.visibility = View.VISIBLE
            btnSignOut.visibility = View.VISIBLE
            setDashboard()
        } else {
            setAvailableBooks()
        }

        btnHome.setOnClickListener {
            btnAvbWorkshops.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            setDashboard()
        }

        btnAvbWorkshops.setOnClickListener {
            btnHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
            setAvailableBooks()
        }

        btnSignOut.setOnClickListener {
            requireContext().createAlertDialog(
                "Confirm Sign Out",
                "Are you sure you want to sign out of BPIT-Library",
                "Sign Out",
                "Cancel"
            ) {
                auth.signOut()
                (activity as MainActivity).setFragmentWithoutBackstack(BookContainerFragment())
            }
        }
    }

    private fun setDashboard() {
        btnHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
        tvHeading.text = requireContext().getString(R.string.student_dashboard)
        setFragment(Dashboard())
    }

    private fun setAvailableBooks() {
        btnAvbWorkshops.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black
            )
        )
        tvHeading.text = getString(R.string.available_books)
        setFragment(BooksFragment())
    }

    private fun setFragment(fragmentToSet: Fragment) {
        childFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
            .replace(R.id.child_fragment_container, fragmentToSet)
            .commit()
    }
}
