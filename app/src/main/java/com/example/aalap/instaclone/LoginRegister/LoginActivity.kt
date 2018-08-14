package com.example.aalap.instaclone.LoginRegister

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.example.aalap.instaclone.Models.UserImage
import com.example.aalap.instaclone.R
import com.example.aalap.instaclone.Preference
import com.example.aalap.instaclone.USER_ID
import com.example.aalap.instaclone.home.HomeActivity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor

class LoginActivity : AppCompatActivity(), AnkoLogger{

    lateinit var userName: EditText
    lateinit var password: EditText
    lateinit var loginButton: View
    lateinit var authInstance: FirebaseAuth
    lateinit var progressBar: ProgressBar
    lateinit var loginText: TextView
     var loginButtonExpandedWidth: Int =0
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

        Log.d(TAG, "onCreate: $buttonWidth")
        userName = findViewById(R.id.user_name)
        password = findViewById(R.id.password)
        progressBar = findViewById(R.id.progress_bar)
        progressBar.indeterminateDrawable.setColorFilter(
                ContextCompat.getColor(this@LoginActivity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN)
        loginText = findViewById(R.id.text)

        if (preference.isSaveCreds()) {
            userName.setText(preference.getUserName())
            password.setText(preference.getPassword())
        } else {
            userName.setText("")
            password.setText("")
        }

        saveCredentials.isChecked = preference.isSaveCreds()

        authInstance = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            if (TextUtils.isEmpty(userName.text.toString()))
                Toast.makeText(this@LoginActivity, "Enter Username", Toast.LENGTH_SHORT).show()
            else if (TextUtils.isEmpty(password.text.toString()))
                Toast.makeText(this@LoginActivity, "Enter Password", Toast.LENGTH_SHORT).show()
            else {
                animateButtonWidth(true)

                preference.saveCredentials(saveCredentials.isChecked)

                if (saveCredentials.isChecked) {
                    preference.setPassword(password.text.toString().trim { it <= ' ' })
                    preference.setUserName(userName.text.toString().trim { it <= ' ' })
                } else {
                    preference.setPassword("")
                    preference.setUserName("")
                }

                loginUser(userName.text.toString().trim { it <= ' ' }, password.text.toString().trim { it <= ' ' })
            }
        }
    }

    private fun registerUser(userName: String, password: String) {

        authInstance.signInWithEmailAndPassword(userName, password).addOnSuccessListener { authResult ->
            //circular effect with for new screen
            logIn(authResult)
            Toast.makeText(this@LoginActivity, "Registered", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            e.printStackTrace()
            //set button with text again

            Handler().postDelayed({ animateButtonWidth(false) }, 500)
            Log.d(TAG, "onFailure: " + e.message)
        }
    }

    private fun logIn(authResult: AuthResult) {
        var userId = authResult.user.uid

        preference.setUserId(userId)

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginUser(userName: String, password: String) {
        authInstance!!.signInWithEmailAndPassword(userName, password).addOnSuccessListener { authResult -> logIn(authResult) }.addOnFailureListener { e ->
            e.printStackTrace()
            Log.d(TAG, "onFailure: " + e.message)
        }
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
