package com.example.aalap.instaclone.account

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.account_edit_profile.*
import kotlinx.android.synthetic.main.include_layout_toolbar.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

const val CODE_PICK_IMAGE = 10
const val CODE_MEDIA_PERMISSION = 5

class AccountEditActivity : AppCompatActivity(), AnkoLogger {

    lateinit var newPicURI: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_edit_profile)
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Edit profile")
        val background = toolbar.background

        (background as LayerDrawable).findDrawableByLayerId(R.id.toolbar_drawable_item)

        (background.findDrawableByLayerId(R.id.toolbar_drawable_item) as GradientDrawable)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))

        account_edit_profile_pic.setOnClickListener { pickImage() }
        account_edit_change_photo.setOnClickListener { pickImage() }
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    , CODE_MEDIA_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CODE_MEDIA_PERMISSION) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted yooohoo...
                var intent = Intent(Intent.ACTION_PICK)
                intent.setType("image/*")
                startActivityForResult(intent, CODE_PICK_IMAGE)


            }
            else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])){
                Toast.makeText(this, "Please allow application to use image for profile", Toast.LENGTH_SHORT)
                pickImage()
            } else {
                Toast.makeText(this, "FOOK OFF...", Toast.LENGTH_SHORT)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            newPicURI = data?.data!!
            account_edit_profile_pic.setImageURI(newPicURI)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.account_edit_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId) {
            R.id.account_edit_menu_save->Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
            R.id.account_edit_menu_signout->Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

}
