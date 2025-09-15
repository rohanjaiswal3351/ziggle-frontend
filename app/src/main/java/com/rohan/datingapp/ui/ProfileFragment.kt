package com.rohan.datingapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.R
import com.rohan.datingapp.activity.BuyPremiumActivity
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.activity.EditProfileActivity
import com.rohan.datingapp.activity.SettingsActivity
import com.rohan.datingapp.activity.ShowUserActivity
import com.rohan.datingapp.adapter.MessageUserAdapter
import com.rohan.datingapp.adapter.ShowInterestAdapter
import com.rohan.datingapp.databinding.FragmentProfileBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.repository.UserRepository
import com.rohan.datingapp.utils.Config
import com.rohan.datingapp.viewModel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Objects
import kotlin.getValue

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var instaId: String
    private lateinit var snapId: String
    private var progress = 30
    private lateinit var mContext: Context

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        //progressSetup()

        binding.buyBtn.setOnClickListener {
            startActivity(Intent(mContext, BuyPremiumActivity::class.java))
        }

        binding.editProfile.setOnClickListener {
            startActivity(Intent(mContext , EditProfileActivity::class.java))
        }

        binding.settings.setOnClickListener {
            startActivity(Intent(mContext , SettingsActivity::class.java))
        }

        binding.insta.setOnClickListener {
            val uri: Uri = Uri.parse("http://instagram.com/_u/$instaId")
            val instaIntent = Intent(Intent.ACTION_VIEW, uri)
            instaIntent.setPackage("com.instagram.android")

            if (isIntentAvailable(instaIntent)){
                startActivity(instaIntent)
            } else{
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$instaId")))
            }
        }

        binding.snap.setOnClickListener {
            val uri: Uri = Uri.parse("http://snapchat.com/add/$snapId")
            val instaIntent = Intent(Intent.ACTION_VIEW, uri)
            instaIntent.setPackage("com.snapchat.android")

            if (isIntentAvailable(instaIntent)){
                startActivity(instaIntent)
            } else{
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://snapchat.com/add/$snapId")))
            }
        }

        binding.userImage.setOnClickListener {
            val intent = Intent(mContext , ShowUserActivity::class.java)
            intent.putExtra("uid" , FirebaseAuth.getInstance().currentUser!!.uid)
            intent.putExtra("check" , 1)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    @SuppressLint("SetTextI18n")
    private fun updateProgress(){
        binding.profileProgress.progress = progress
        binding.progressText.text = "${progress}%"
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun isIntentAvailable(intent: Intent): Boolean {
        val list: List<ResolveInfo>  = mContext.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list.isNotEmpty()
    }

    @SuppressLint("SetTextI18n")
    private fun loadContent() {
        Config.showDialog(mContext)
        progress = 30

        GlobalScope.launch {
            val userRepository = UserRepository()
            userRepository.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                .onSuccess {

                    withContext(Dispatchers.Main){
                        binding.name.text = "${it.name.toString()}, ${it.age.toString()} "

                        if(it.instaId?.isNotEmpty() == true){
                            binding.socialLayout.visibility = View.VISIBLE
                            binding.insta.visibility = View.VISIBLE
                            instaId = it.instaId!!
                            progress += 5
                        }

                        if(it.snapId?.isNotEmpty() == true){
                            binding.socialLayout.visibility = View.VISIBLE
                            binding.snap.visibility = View.VISIBLE
                            snapId = it.snapId!!
                            progress += 5
                        }

                        if(it.bio?.isNotEmpty() == true){
                            binding.textBio.visibility = View.VISIBLE
                            binding.bio.text = it.bio.toString()
                            progress += 5
                        }

                        if (it.interests != null){
                            progress += 5
                            binding.interestLayout.visibility = View.VISIBLE
                            val gridLayoutManager = GridLayoutManager(
                                mContext , 2, GridLayoutManager.VERTICAL, false
                            )


                            val adapter = ShowInterestAdapter(mContext)
                            binding.recyclerView.layoutManager = gridLayoutManager
                            //binding.recyclerView.adapter = ShowInterestAdapter(requireContext() , data.interests)
                            binding.recyclerView.adapter = adapter
                            adapter.updateList(it.interests!!)
                        }

                        if(it.height?.isNotEmpty() == true){
                            progress += 5
                            binding.height.visibility = View.VISIBLE
                            binding.heightTxt.text = "${it.height}cm"
                        }

                        if(it.exercise?.isNotEmpty() == true){
                            progress += 5
                            binding.exercise.visibility = View.VISIBLE
                            binding.exerciseTxt.text = it.exercise
                        }

                        if(it.education?.isNotEmpty() == true){
                            progress += 5
                            binding.education.visibility = View.VISIBLE
                            binding.educationTxt.text = it.education
                        }

                        if(it.gender?.isNotEmpty() == true){
                            progress += 5
                            binding.gender.visibility = View.VISIBLE
                            binding.genderTxt.text = it.gender
                        }

                        if(it.star?.isNotEmpty() == true){
                            progress += 5
                            binding.star.visibility = View.VISIBLE
                            binding.starTxt.text = it.star
                        }

                        if(it.city?.isNotEmpty() == true){
                            progress += 5
                        }

                        if(it.image2?.isNotEmpty() == true){
                            progress += 10
                        }

                        if(it.image3?.isNotEmpty() == true){
                            progress += 10
                        }

                        Glide.with(mContext).load(it.image).placeholder(R.drawable.person).into(binding.userImage)
                        updateProgress()
                        Config.hideDialog()
                    }

                }
                .onFailure {
                    Toast.makeText(mContext , it.message , Toast.LENGTH_SHORT).show()
                }

        }

//        GlobalScope.launch {
//            val userDao = UserDao()
//            val data: UserModel = userDao.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
//                .await().getValue(UserModel::class.java)!!
//
//            withContext(Dispatchers.Main){
//                binding.name.text = "${data.name.toString()}, ${data.age.toString()} "
//
//                if(data.instaId?.isNotEmpty() == true){
//                    binding.socialLayout.visibility = View.VISIBLE
//                    binding.insta.visibility = View.VISIBLE
//                    instaId = data.instaId!!
//                    progress += 5
//                }
//
//                if(data.snapId?.isNotEmpty() == true){
//                    binding.socialLayout.visibility = View.VISIBLE
//                    binding.snap.visibility = View.VISIBLE
//                    snapId = data.snapId!!
//                    progress += 5
//                }
//
//                if(data.bio?.isNotEmpty() == true){
//                    binding.textBio.visibility = View.VISIBLE
//                    binding.bio.text = data.bio.toString()
//                    progress += 5
//                }
//
//                if (data.interests != null){
//                    progress += 5
//                    binding.interestLayout.visibility = View.VISIBLE
//                    val gridLayoutManager = GridLayoutManager(
//                        mContext , 2, GridLayoutManager.VERTICAL, false
//                    )
//
//
//                    val adapter = ShowInterestAdapter(mContext)
//                    binding.recyclerView.layoutManager = gridLayoutManager
//                    //binding.recyclerView.adapter = ShowInterestAdapter(requireContext() , data.interests)
//                    binding.recyclerView.adapter = adapter
//                    adapter.updateList(data.interests!!)
//                }
//
//                if(data.height?.isNotEmpty() == true){
//                    progress += 5
//                    binding.height.visibility = View.VISIBLE
//                    binding.heightTxt.text = "${data.height}cm"
//                }
//
//                if(data.exercise?.isNotEmpty() == true){
//                    progress += 5
//                    binding.exercise.visibility = View.VISIBLE
//                    binding.exerciseTxt.text = data.exercise
//                }
//
//                if(data.education?.isNotEmpty() == true){
//                    progress += 5
//                    binding.education.visibility = View.VISIBLE
//                    binding.educationTxt.text = data.education
//                }
//
//                if(data.gender?.isNotEmpty() == true){
//                    progress += 5
//                    binding.gender.visibility = View.VISIBLE
//                    binding.genderTxt.text = data.gender
//                }
//
//                if(data.star?.isNotEmpty() == true){
//                    progress += 5
//                    binding.star.visibility = View.VISIBLE
//                    binding.starTxt.text = data.star
//                }
//
//                if(data.city?.isNotEmpty() == true){
//                    progress += 5
//                }
//
//                if(data.image2?.isNotEmpty() == true){
//                    progress += 10
//                }
//
//                if(data.image3?.isNotEmpty() == true){
//                    progress += 10
//                }
//
//                Glide.with(mContext).load(data.image).placeholder(R.drawable.person).into(binding.userImage)
//                updateProgress()
//                Config.hideDialog()
//            }
//
//        }
    }

    override fun onStart() {
        super.onStart()
        loadContent()
    }

}