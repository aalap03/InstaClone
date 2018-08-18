package com.example.aalap.instaclone.loginRegister

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.example.aalap.instaclone.Models.UserAccountDetails
import com.example.aalap.instaclone.R
import com.example.aalap.instaclone.Preference
import com.example.aalap.instaclone.home.HomeActivity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

class LoginActivity : AppCompatActivity(), AnkoLogger {

    lateinit var userEmail: EditText
    lateinit var password: EditText
    lateinit var loginButton: View
    lateinit var authInstance: FirebaseAuth
    lateinit var progressBar: ProgressBar
    lateinit var loginText: TextView
    var loginButtonExpandedWidth: Int = 0
    lateinit var saveCredentials: CheckBox
    lateinit var preference: Preference

    private val fabWidth: Int
        get() = resources.getDimension(R.dimen.fab_width).toInt()

    private val buttonWidth: Int
        get() = resources.getDimension(R.dimen.button_width).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        loginButton = findViewById(R.id.login_button)
        saveCredentials = findViewById(R.id.save_creds)
        loginButtonExpandedWidth = loginButton.measuredWidth
        preference = Preference(applicationContext)
        preference.setProfilePic("")

        info { "Date: ${Date()}" }


        Log.d(TAG, "onCreate: $buttonWidth")
        userEmail = findViewById(R.id.user_email)
        password = findViewById(R.id.password)
        progressBar = findViewById(R.id.progress_bar)
        progressBar.indeterminateDrawable.setColorFilter(
                ContextCompat.getColor(this@LoginActivity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN)
        loginText = findViewById(R.id.text)

        if (preference.isSaveCreds()) {
            userEmail.setText(preference.getUserEmail())
            password.setText(preference.getPassword())
        } else {
            userEmail.setText("")
            password.setText("")
        }

        saveCredentials.isChecked = preference.isSaveCreds()

        authInstance = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            if (TextUtils.isEmpty(userEmail.text.toString()))
                Toast.makeText(this@LoginActivity, "Enter Username", Toast.LENGTH_SHORT).show()
            else if (TextUtils.isEmpty(password.text.toString()))
                Toast.makeText(this@LoginActivity, "Enter Password", Toast.LENGTH_SHORT).show()
            else {
                animateButtonWidth(true)

                preference.saveCredentials(saveCredentials.isChecked)
                preference.setUserEmail(userEmail.text.toString().trim { it <= ' ' })

                if (saveCredentials.isChecked)
                    preference.setPassword(password.text.toString().trim { it <= ' ' })
                else
                    preference.setPassword("")

                loginUser(userEmail.text.toString().trim { it <= ' ' }, password.text.toString().trim { it <= ' ' })
            }
        }
    }

    private fun loginUser(userName: String, password: String) {
        authInstance!!.signInWithEmailAndPassword(userName, password)
                .addOnSuccessListener { authResult -> logIn(authResult) }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    Log.d(TAG, "onFailure: " + e.message)
                }
    }

    private fun logIn(authResult: AuthResult) {
        var userId = authResult.user.uid

        FirebaseDatabase.getInstance().reference.child("users_account_setting").child(userId)
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(dbError: DatabaseError) {
                        Toast.makeText(this@LoginActivity, "Error logging in: ${dbError.message}", Toast.LENGTH_SHORT)
                                .show()
                    }

                    override fun onDataChange(snapShot: DataSnapshot) {
                        if( snapShot.childrenCount > 0) {
                            val userAccountDetails = snapShot.getValue(UserAccountDetails::class.java)

                            preference.setProfilePic(userAccountDetails?.profilePic!!)
                            preference.setUserName(userAccountDetails.username)
                            preference.setUserId(userId)

                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                })



    }

    private fun animateButtonWidth(isShrink: Boolean) {

        val anim: ValueAnimator
        if (isShrink)
            anim = ValueAnimator.ofInt(loginButton.measuredWidth, fabWidth)
        else
            anim = ValueAnimator.ofInt(fabWidth, buttonWidth)

        anim.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = loginButton.layoutParams
            layoutParams.width = `val`
            loginButton.layoutParams = layoutParams

            progressBar.visibility = if (isShrink) View.VISIBLE else View.INVISIBLE
            loginText.visibility = if (isShrink) View.INVISIBLE else View.VISIBLE
        }
        anim.duration = 500
        anim.start()
    }

    companion object {
        private val TAG = "LoginActivity:"
        val EMAIL = "Email"
    }
}
