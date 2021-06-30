package com.example.nidhi.bpit_library

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }



    fun setFragmentWithoutBackstack(fragmentToSet: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.fragment_container, fragmentToSet)
            .commit()
    }

    fun setFragmentWithBackstack(fragmentToSet: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.fragment_container, fragmentToSet)
            .addToBackStack(null)
            .commit()
    }
}

fun Context.createProgressDialog(message: String, isCancelable: Boolean): ProgressDialog {
    return ProgressDialog(this).apply {
        setMessage(message)
        setCancelable(isCancelable)
        setCanceledOnTouchOutside(false)
    }
}

fun Context.createAlertDialog(
    title: String,
    msg: String,
    positiveText: String,
    negativeText: String,
    positiveCallback: () -> Unit
) {
    val tvTitle = TextView(this)

    tvTitle.apply {
        text = title
        setTextAppearance(android.R.style.TextAppearance_Material_Title)
        setTextColor(ContextCompat.getColor(this@createAlertDialog, R.color.black))
        setPadding(45, 30, 0, 0)
    }

    val dialog = MaterialAlertDialogBuilder(this)
        .setCustomTitle(tvTitle)
        .setMessage(msg)
        .setPositiveButton(positiveText) { _, _ ->
            positiveCallback.invoke()
        }
        .setNegativeButton(negativeText) { dialog, _ ->
            dialog.dismiss()
        }
        .show()

    val tvMessage = dialog.findViewById<TextView>(android.R.id.message)
    tvMessage?.apply {
        textSize = 15F
        setTextColor(
            ContextCompat.getColor(
                this@createAlertDialog,
                R.color.black
            )
        )
    }
}