package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity

const val LOG_IN_RESULT_CODE = 1001

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthenticationViewModel>()
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_authentication
        )

        binding.loginButton.setOnClickListener {
            launchSignInFlow()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOG_IN_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                startActivity(
                    Intent(
                        this,
                        RemindersActivity::class.java
                    )
                )
            } else {
                Toast
                    .makeText(applicationContext, "Log In Failed", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI
                .getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    providers
                ).build(), LOG_IN_RESULT_CODE
        )
    }

    private fun observeAuthenticationState() {
        viewModel.authenticationState.observe(this, Observer { authenticationState ->
            when (authenticationState) {
                AuthenticationViewModel.AuthenticationState.AUTHENTICATED -> {
//                    binding.welcomeText.text = getFactWithPersonalization(factToDisplay)
//
//                    binding.authButton.text = getString(R.string.logout_button_text)
//                    binding.authButton.setOnClickListener {
//                        AuthUI.getInstance().signOut(requireContext())
//                    }
                }
                else -> {
//                    binding.welcomeText.text = factToDisplay
//
//                    binding.authButton.text = getString(R.string.login_button_text)
//                    binding.authButton.setOnClickListener {
//                        launchSignInFlow()
//                    }
                }
            }
        })
    }
}
