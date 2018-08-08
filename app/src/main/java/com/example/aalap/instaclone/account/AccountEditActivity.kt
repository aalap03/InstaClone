package com.example.aalap.instaclone.account

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.EditText
import com.example.aalap.instaclone.BaseActivity
import com.example.aalap.instaclone.Constants
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.account_edit_profile.*
import kotlinx.android.synthetic.main.layout_account.*
import org.jetbrains.anko.info

class AccountEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_edit_profile)

        edittextHints()
    }

    private fun edittextHints() {
        account_edit_bio.findViewById<EditText>(R.id.input_edit_text).hint = "Bio"
        account_edit_name.findViewById<EditText>(R.id.input_edit_text).hint = "Name"
        account_edit_username.findViewById<EditText>(R.id.input_edit_text).hint = "Username"
        account_edit_email.findViewById<EditText>(R.id.input_edit_text).hint = "Email"
        account_edit_gender.findViewById<EditText>(R.id.input_edit_text).hint = "Gender"
        account_edit_phone.findViewById<EditText>(R.id.input_edit_text).hint = "Phone"
        account_edit_website.findViewById<EditText>(R.id.input_edit_text).hint = "Website"
    }

    private fun pickImage() {

    }

}
