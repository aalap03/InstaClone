package com.example.aalap.instaclone.home

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.aalap.instaclone.models.UserPost
import com.example.aalap.instaclone.Preference
import com.example.aalap.instaclone.R
import com.example.aalap.instaclone.Utils
import com.example.aalap.instaclone.models.UserStory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.layout_center_home_fragment.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


val PERM_CAMERA = 1
val VIDEO_URI = "video_uri"

class HomeCenterFragment : Fragment(), AnkoLogger {

    lateinit var pref: Preference
    lateinit var postAdapter: PostAdapter
    lateinit var storyAdapter: StoryAdapter
    var posts = mutableListOf<UserPost>()
    var stories = mutableListOf<UserStory>()
    var uriForFile: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.layout_center_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = Preference(requireContext().applicationContext)
        home_user_name.text = pref.getUserName()
        info { pref.getUserId() }

        postAdapter = PostAdapter(requireContext(), posts)
        storyAdapter = StoryAdapter(requireContext(), stories)
        home_feeds_recycler.layoutManager = LinearLayoutManager(requireContext())
        home_feeds_recycler.adapter = postAdapter

        story_recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        story_recycler.adapter = storyAdapter

        Glide.with(this)
                .load(pref.getProfilePic())
                .into(home_profile_pic)

        requestPostList()

        requestStories()

        home_profile_pic.setOnClickListener { handlePermissions() }

        refresh_layout.setOnRefreshListener { requestPostList() }
    }

    private fun requestStories() {
        story_progress.visibility = View.VISIBLE

        FirebaseDatabase.getInstance()
                .reference
                .child("user_stories")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        info { "fuss.. ${error.message}" }
                        Toast.makeText(requireContext(), "Could not fetch stories", Toast.LENGTH_SHORT).show()
                        story_progress.visibility = View.GONE
                    }

                    override fun onDataChange(snapShot: DataSnapshot) {
                        stories.clear()
                        info { "got stories" }
                        for (snapshotObj in snapShot.children) {
                            val userStory = snapshotObj.getValue(UserStory::class.java)
                            stories.add(userStory!!)
                        }
                        story_progress.visibility = View.GONE
                        story_recycler.visibility = View.VISIBLE

                        stories.reverse()
                        storyAdapter.notifyDataSetChanged()

                    }
                })
    }

    private fun handlePermissions() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), PERM_CAMERA)
        } else
            openCam()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERM_CAMERA) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCam()
            }
        }
    }

    fun openCam() {
        var intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

        var photoFile = Utils.createImageFile(requireContext(), ".mp4")

        uriForFile = FileProvider.getUriForFile(requireContext(), "com.example.aalap.instaclone.fileprovider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);

        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10)
        startActivityForResult(intent, PERM_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            var intent = Intent(requireContext(), AddStoryActivity::class.java)

            intent.data = data?.data
            startActivity(intent)
        }
    }

    private fun requestPostList() {

        FirebaseDatabase.getInstance()
                .reference
                .child("user_posts")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        info { "fuss.. ${error.message}" }
                        Toast.makeText(requireContext(), "Could not refresh feed", Toast.LENGTH_SHORT).show()
                        refresh_layout.isRefreshing = false
                    }

                    override fun onDataChange(snapShot: DataSnapshot) {
                        posts.clear()
                        for (snapshotObj in snapShot.children) {
                            val userPost = snapshotObj.getValue(UserPost::class.java)
                            posts.add(userPost!!)
                        }
                        refresh_layout.isRefreshing = false
                        posts.reverse()
                        postAdapter.notifyDataSetChanged()
                    }
                })
    }
}