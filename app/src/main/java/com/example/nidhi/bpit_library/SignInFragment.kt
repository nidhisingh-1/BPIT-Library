package com.example.nidhi.bpit_library

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment() {

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpannableStrings()

        signInBtn.setOnClickListener {
            if (validateCredentials()) {
                signInUser(emailEt.text.toString(), pwdEt.text.toString())
            }
        }

        emailEt.addTextChangedListener {
            emailInput.isErrorEnabled = false
        }

        emailEt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                checkEmail()
            }
        }

        pwdEt.addTextChangedListener {
            pwdInput.isErrorEnabled = false
        }

        pwdEt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                checkPassword()
            }
        }
    }

    private fun validateCredentials(): Boolean {
        var isValid = true

        if (!checkEmail()) {
            isValid = false
        }

        if (!checkPassword()) {
            isValid = false
        }

        return isValid
    }

    private fun checkEmail(): Boolean {
        if (emailEt.text.isNullOrEmpty()) {
            emailInput.error = "Email cannot be empty!"
            return false
        }
        return true
    }

    private fun checkPassword(): Boolean {
        if (pwdEt.text.isNullOrEmpty()) {
            pwdInput.error = "Password cannot be empty!"
            return false
        }
        return true
    }

    private fun signInUser(email: String, pwd: String) {
        val progressDialog =
            requireContext().createProgressDialog("Signing in, Please wait...", false)
        progressDialog.show()
        auth.signInWithEmailAndPassword(email, pwd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    if (auth.currentUser?.isEmailVerified == true) {
                        (activity as MainActivity).onBackPressed()
                    } else {
                        showToast("Please verify your e-mail address!")
                    }
                } else {
                    progressDialog.dismiss()
                    showToast(task.exception?.message.toString())
                }
            }
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, -65)
        toast.show()
    }

    private fun setSpannableStrings() {
        //New user ? Sign up
        val spanNewUser = SpannableString("New user ? Sign up")

        val clickableSpanNewUser = object : ClickableSpan() {
            override fun onClick(widget: View) {
                (parentFragment as AuthFragment).setFragment(SignUpFragment())
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(requireContext(), R.color.white)
            }
        }

        spanNewUser.setSpan(
            clickableSpanNewUser,
            spanNewUser.length - 7,
            spanNewUser.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvSignUp.movementMethod = LinkMovementMethod.getInstance()
        tvSignUp.text = spanNewUser

        //Forgot Password?
        val spanForgotPwd = SpannableString("Forgot Password ?")
        val clickableSpanForgotPwd = object : ClickableSpan() {
            /**
             * Performs the click action associated with this span.
             */
            override fun onClick(widget: View) {
                if (emailEt.text.isNullOrEmpty()) {
                    showToast("Enter your registered Email id")
                } else {
                    auth.sendPasswordResetEmail(emailEt.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                showToast("We have sent you instructions to reset password on your mail")
                            } else {
                                showToast(task.exception?.message.toString())
                            }
                        }
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(requireContext(), R.color.white)
            }
        }

        spanForgotPwd.setSpan(
            clickableSpanForgotPwd,
            0,
            spanForgotPwd.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvForgotPwd.movementMethod = LinkMovementMethod.getInstance()
        tvForgotPwd.text = spanForgotPwd

        //Send Verification mail
        val spanVerification = SpannableString("Resend Verification mail")

        val clickableSpanVerification = object : ClickableSpan() {
            override fun onClick(widget: View) {
                if (validateCredentials()) {
                    val countDownTimer = object : CountDownTimer(30000, 1000) {

                        override fun onTick(millisUntilFinished: Long) {
                            tvSendVerificationMail.text =
                                getString(R.string.countdown, millisUntilFinished / 1000)
                        }

                        override fun onFinish() {
                            tvSendVerificationMail.text = spanVerification
                        }
                    }.start()

                    auth.signInWithEmailAndPassword(emailEt.text.toString(), pwdEt.text.toString())
                        .addOnSuccessListener {
                            if (auth.currentUser?.isEmailVerified == true) {
                                countDownTimer.cancel()
                                tvSendVerificationMail.text = spanVerification
                                auth.signOut()
                                showToast("Email Address has already been verified")
                            } else {
                                auth.currentUser?.sendEmailVerification()
                                    ?.addOnSuccessListener {
                                        showToast("We have sent a verification link on your e-mail address.")
                                    }
                                    ?.addOnFailureListener {
                                        showToast(it.message.toString())
                                    }
                            }
                        }
                        .addOnFailureListener {
                            countDownTimer.cancel()
                            tvSendVerificationMail.text = spanVerification
                            showToast(it.message.toString())
                        }
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(requireContext(), R.color.white)
            }
        }

        spanVerification.setSpan(
            clickableSpanVerification,
            0,
            spanVerification.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvSendVerificationMail.movementMethod = LinkMovementMethod.getInstance()
        tvSendVerificationMail.text = spanVerification
    }

}