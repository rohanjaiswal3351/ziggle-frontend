package com.rohan.datingapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.adapter.ShowInterestAdapter
import com.rohan.datingapp.databinding.ActivityShowUserBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.utils.Config
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class ShowUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowUserBinding
    private lateinit var instaId: String
    private lateinit var snapId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val number = intent.getStringExtra("uid")
        val check = intent.getIntExtra("check" , 0)

        loadData(number)

        binding.back.setOnClickListener {
            finish()
        }

        binding.insta.setOnClickListener {
            if(check == 0){
                Toast.makeText(this , "You must be friends to see socials" ,
                    Toast.LENGTH_SHORT).show()
            }else{
                val uri: Uri = Uri.parse("http://instagram.com/_u/$instaId")
                val instaIntent = Intent(Intent.ACTION_VIEW, uri)
                instaIntent.setPackage("com.instagram.android")

                if (isIntentAvailable(instaIntent)){
                    startActivity(instaIntent)
                } else{
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$instaId")))
                }
            }

        }

        binding.snap.setOnClickListener {
            if(check == 0){
                Toast.makeText(this , "You must be friends to see socials" ,
                    Toast.LENGTH_SHORT).show()
            }else{
                val uri: Uri = Uri.parse("http://snapchat.com/add/$snapId")
                val snapIntent = Intent(Intent.ACTION_VIEW, uri)
                snapIntent.setPackage("com.snapchat.android")

                if (isIntentAvailable(snapIntent)){
                    startActivity(snapIntent)
                } else{
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://snapchat.com/add/$snapId")))
                }
            }
        }

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun isIntentAvailable(intent: Intent): Boolean {
        val list: List<ResolveInfo>  = this.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list.isNotEmpty()
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SetTextI18n")
    private fun loadData(number: String?) {
        Config.showDialog(this)

        GlobalScope.launch {
            val userDao = UserDao()
            val currentItem: UserModel = userDao.getUserById(number!!).await().getValue(UserModel::class.java)!!

            withContext(Dispatchers.Main){
                binding.name.text = "${currentItem.name.toString().trim()}, ${currentItem.age.toString().trim()}"
                binding.profile.text = "${currentItem.name.toString().trim()}'s profile"

                if(currentItem.instaId?.isNotEmpty() == true){
                    binding.socialLayout.visibility = View.VISIBLE
                    binding.insta.visibility = View.VISIBLE
                    instaId = currentItem.instaId!!
                }

                if(currentItem.snapId?.isNotEmpty() == true){
                    binding.socialLayout.visibility = View.VISIBLE
                    binding.snap.visibility = View.VISIBLE
                    snapId = currentItem.snapId!!
                }

                if(currentItem.city?.isNotEmpty() == true){
                    binding.country.text = "${currentItem.city}"
                }else{
                    binding.country.text = "United States"
                }

                if (currentItem.interests != null){
                    binding.interestLayout.visibility = View.VISIBLE
                    val gridLayoutManager = GridLayoutManager(
                        this@ShowUserActivity , 2, GridLayoutManager.VERTICAL, false
                    )


                    val adapter = ShowInterestAdapter(this@ShowUserActivity)
                    binding.recyclerView.layoutManager = gridLayoutManager
                    //binding.recyclerView.adapter = ShowInterestAdapter(this , currentItem.interests)
                    binding.recyclerView.adapter = adapter
                    adapter.updateList(currentItem.interests!!)
                }

                if(currentItem.bio?.isNotEmpty() == true){
                    binding.bioShow.visibility = View.VISIBLE
                    binding.bio.visibility = View.VISIBLE
                    binding.bio.text = currentItem.bio
                }

                if(currentItem.height?.isNotEmpty() == true){
                    binding.height.visibility = View.VISIBLE
                    binding.heightTxt.text = "${currentItem.height}cm"
                }

                if(currentItem.exercise?.isNotEmpty() == true){
                    binding.exercise.visibility = View.VISIBLE
                    binding.exerciseTxt.text = currentItem.exercise
                }

                if(currentItem.education?.isNotEmpty() == true){
                    binding.education.visibility = View.VISIBLE
                    binding.educationTxt.text = currentItem.education
                }

                if(currentItem.gender?.isNotEmpty() == true){
                    binding.gender.visibility = View.VISIBLE
                    binding.genderTxt.text = currentItem.gender
                }

                if(currentItem.star?.isNotEmpty() == true){
                    binding.star.visibility = View.VISIBLE
                    binding.starTxt.text = currentItem.star
                }

                if(currentItem.image1?.isNotEmpty() == true){
                    binding.imgCard1.visibility = View.VISIBLE
                    Glide.with(this@ShowUserActivity).load(currentItem.image1).into(binding.image1)
                }

                if(currentItem.image2?.isNotEmpty() == true){
                    binding.imgCard2.visibility = View.VISIBLE
                    Glide.with(this@ShowUserActivity).load(currentItem.image2).into(binding.image2)
                }

                if(currentItem.image3?.isNotEmpty() == true){
                    binding.imgCard3.visibility = View.VISIBLE
                    Glide.with(this@ShowUserActivity).load(currentItem.image3).into(binding.image3)
                }

                Glide.with(this@ShowUserActivity).load(currentItem.image).into(binding.userImage)

                Config.hideDialog()
            }

        }

    }
}