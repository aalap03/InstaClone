package com.example.aalap.instaclone.account

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.EditText
import com.example.aalap.instaclone.BaseActivity
import com.example.aalap.instaclone.Constants
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.account_edit_profile.*
import kotlinx.android.synthetic.main.layout_account.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class AccountEditActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_edit_profile)

        edittextHints()
    }

    private fun edittextHints() {
        account_edit_bio.findViewById<TextInputLayout>(R.id.input_edit_text_layout).hint = "Bio"
        account_edit_name.findViewById<TextInputLayout>(R.id.input_edit_text_layout).hint = "Name"
        account_edit_username.findViewById<TextInputLayout>(R.id.input_edit_text_layout).hint = "Username"
        account_edit_email.findViewById<TextInputLayout>(R.id.input_edit_text_layout).hint = "Email"
        account_edit_gender.findViewById<TextInputLayout>(R.id.input_edit_text_layout).hint = "Gender"
        account_edit_phone.findViewById<TextInputLayout>(R.id.input_edit_text_layout).hint = "Phone"
        account_edit_website.findViewById<TextInputLayout>(R.id.input_edit_text_layout).hint = "Website"
    }

    private fun pickImage() {
        if( ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 5)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 5) {

            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted yooohoo...



            } else {
                ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])
            }
        }
    }

}
