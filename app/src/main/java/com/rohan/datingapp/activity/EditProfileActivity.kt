package com.rohan.datingapp.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.R
import com.rohan.datingapp.adapter.ShowInterestAdapter
import com.rohan.datingapp.databinding.ActivityEditProfileBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.repository.StorageRepository
import com.rohan.datingapp.repository.UserRepository
import com.rohan.datingapp.request.UploadFileRequest
import com.rohan.datingapp.utils.Config
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import androidx.core.graphics.drawable.toDrawable


class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var imageUrl1: String
    private lateinit var imageUrl2: String
    private lateinit var imageUrl3: String

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null){
            binding.show.visibility = View.GONE
            binding.image.visibility = View.VISIBLE
            binding.image.setImageURI(it)
            uploadImage(it , 0)
        }

    }

    private val selectImage1 = registerForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null){
            binding.show1.visibility = View.GONE
            binding.image1.visibility = View.VISIBLE
            binding.image1.setImageURI(it)
            uploadImage(it , 1)
        }

    }

    private val selectImage2 = registerForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null){
            binding.show2.visibility = View.GONE
            binding.image2.visibility = View.VISIBLE
            binding.image2.setImageURI(it)
            uploadImage(it , 2)
        }
    }

    private val selectImage3 = registerForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null){
            binding.show3.visibility = View.GONE
            binding.image3.visibility = View.VISIBLE
            binding.image3.setImageURI(it)
            uploadImage(it , 3)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadContent()

        binding.back.setOnClickListener {
            finish()
        }

        binding.editInterest.setOnClickListener {
            startActivity(Intent(this , UpdateInterestActivity::class.java))
        }

        binding.setImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.setImage1.setOnClickListener {
            selectImage1.launch("image/*")
        }

        binding.setImage2.setOnClickListener {
            selectImage2.launch("image/*")
        }

        binding.setImage3.setOnClickListener {
            selectImage3.launch("image/*")
        }

        binding.updateBio.setOnClickListener {
            if(binding.bio.text.isEmpty()){
                Toast.makeText(this , "Please enter Bio!" , Toast.LENGTH_SHORT).show()
            }
            else{
                storeBio(binding.bio.text.toString())
            }
        }

        binding.checkInsta.setOnClickListener {
            storeSocial(0 , binding.addInsta.text.toString())
        }

        binding.checkSnap.setOnClickListener {
            storeSocial(1 , binding.addSnap.text.toString())
        }

        binding.addInsta.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if(editable.toString() == ""){
                    binding.checkInsta.visibility = View.GONE
                }else{
                    binding.checkInsta.visibility = View.VISIBLE
                }
            }
        })

        binding.addSnap.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if(editable.toString() == ""){
                    binding.checkSnap.visibility = View.GONE
                }else{
                    binding.checkSnap.visibility = View.VISIBLE
                }
            }
        })

        binding.education.setOnClickListener {
            val intent = Intent(this , UpdateBasicsActivity::class.java)
            intent.putExtra("check" , "education")
            startActivity(intent)
        }

        binding.gender.setOnClickListener {
            val intent = Intent(this , UpdateBasicsActivity::class.java)
            intent.putExtra("check" , "gender")
            startActivity(intent)
        }

        binding.location.setOnClickListener {
            val intent = Intent(this , UpdateBasicsActivity::class.java)
            intent.putExtra("check" , "location")
            startActivity(intent)
        }

        binding.height.setOnClickListener {
            val intent = Intent(this , UpdateBasicsActivity::class.java)
            intent.putExtra("check" , "height")
            startActivity(intent)
        }

        binding.exercise.setOnClickListener {
            val intent = Intent(this , UpdateBasicsActivity::class.java)
            intent.putExtra("check" , "exercise")
            startActivity(intent)
        }

        binding.star.setOnClickListener {
            val intent = Intent(this , UpdateBasicsActivity::class.java)
            intent.putExtra("check" , "star")
            startActivity(intent)
        }

        binding.delete1.setOnClickListener {
            showDeleteDialog(1)
        }

        binding.delete2.setOnClickListener {
            showDeleteDialog(2)
        }

        binding.delete3.setOnClickListener {
            showDeleteDialog(3)
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun showDeleteDialog(check: Int){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.delete_dialog)
        val delete = dialog.findViewById<Button>(R.id.deleteBtn)
        val cancel = dialog.findViewById<TextView>(R.id.cancel)

        delete.setOnClickListener {
            GlobalScope.launch {
                val userRepository = UserRepository()
                val storageRepository = StorageRepository()
                var user = UserModel()

                userRepository.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                    .onSuccess {
                        user = it
                    }
                    .onFailure {
                        Toast.makeText(this@EditProfileActivity , "something went wrong", Toast.LENGTH_SHORT).show()
                    }

                withContext(Dispatchers.Main){
                    when (check) {
                        1 -> {
                            user.image1 = null
                            userRepository.updateUser(user)
                                .onSuccess {
                                    storageRepository.deleteFile("profile/${FirebaseAuth.getInstance().currentUser!!.uid}/image1.jpg")
                                        .onSuccess {
                                            Toast.makeText(this@EditProfileActivity , "Deleted", Toast.LENGTH_SHORT).show()
                                            dialog.dismiss()
                                        }
                                        .onFailure {
                                            dialog.dismiss()
                                            Toast.makeText(this@EditProfileActivity , "something went wrong", Toast.LENGTH_SHORT).show()
                                        }
                                }.onFailure {
                                    Toast.makeText(this@EditProfileActivity , "something went wrong", Toast.LENGTH_SHORT).show()
                                }
                            binding.delete1.visibility = View.GONE
                            binding.image1.visibility = View.GONE
                            binding.show1.visibility = View.VISIBLE
                        }
                        2 -> {
                            user.image2 = null
                            userRepository.updateUser(user)
                                .onSuccess {
                                    storageRepository.deleteFile("profile/${FirebaseAuth.getInstance().currentUser!!.uid}/image2.jpg")
                                        .onSuccess {
                                            Toast.makeText(this@EditProfileActivity , "Deleted", Toast.LENGTH_SHORT).show()
                                            dialog.dismiss()
                                        }
                                        .onFailure {
                                            dialog.dismiss()
                                            Toast.makeText(this@EditProfileActivity , "something went wrong", Toast.LENGTH_SHORT).show()
                                        }
                                }.onFailure {
                                    Toast.makeText(this@EditProfileActivity , "something went wrong", Toast.LENGTH_SHORT).show()
                                }
                            binding.delete2.visibility = View.GONE
                            binding.image2.visibility = View.GONE
                            binding.show2.visibility = View.VISIBLE
                            binding.delete1.visibility = View.VISIBLE
                        }
                        3 -> {
                            user.image3 = null
                            userRepository.updateUser(user)
                                .onSuccess {
                                    storageRepository.deleteFile("profile/${FirebaseAuth.getInstance().currentUser!!.uid}/image3.jpg")
                                        .onSuccess {
                                            Toast.makeText(this@EditProfileActivity , "Deleted", Toast.LENGTH_SHORT).show()
                                            dialog.dismiss()
                                        }
                                        .onFailure {
                                            dialog.dismiss()
                                            Toast.makeText(this@EditProfileActivity , "something went wrong", Toast.LENGTH_SHORT).show()
                                        }
                                }.onFailure {
                                    Toast.makeText(this@EditProfileActivity , "something went wrong", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                }
                            binding.delete3.visibility = View.GONE
                            binding.image3.visibility = View.GONE
                            binding.show3.visibility = View.VISIBLE
                            binding.delete2.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.show()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun storeSocial(check: Int, id: String) {
        Config.showDialog(this)

        GlobalScope.launch {
            val userRepository = UserRepository()

            userRepository.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                .onSuccess {
                    val user = it
                    if(check == 0){
                        user.instaId = id
                        binding.checkInsta.visibility = View.GONE
                    }else{
                        user.snapId = id
                        binding.checkSnap.visibility = View.GONE
                    }
                    userRepository.updateUser(user)
                        .onSuccess {
                            Config.hideDialog()
                            Toast.makeText(this@EditProfileActivity , "Social updated successfully", Toast.LENGTH_SHORT).show()
                        }
                        .onFailure {
                            Config.hideDialog()
                        }

                }
                .onFailure {
                    Config.hideDialog()
                }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadContent() {
        Config.showDialog(this)
        GlobalScope.launch {
            val userRepository = UserRepository()

            userRepository.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                .onSuccess {
                    withContext(Dispatchers.Main){
                        if (it.interests != null){
                            binding.interestLayout.visibility = View.VISIBLE
                            val gridLayoutManager = GridLayoutManager(
                                this@EditProfileActivity , 2, GridLayoutManager.VERTICAL, false
                            )

                            val interestAdapter = ShowInterestAdapter(this@EditProfileActivity)
                            binding.recyclerView.layoutManager = gridLayoutManager
                            binding.recyclerView.adapter = interestAdapter
                            interestAdapter.updateList(it.interests!!)
                        }

                        if(it.bio!!.isNotEmpty()){
                            binding.bio.setText(it.bio)
                        }

                        if(it.instaId!!.isNotEmpty()){
                            binding.addInsta.setText(it.instaId)
                            binding.checkInsta.visibility = View.GONE
                        }

                        if(it.snapId!!.isNotEmpty()){
                            binding.addSnap.setText(it.snapId)
                            binding.checkSnap.visibility = View.GONE
                        }

                        if(it.image!!.isNotEmpty()){
                            binding.show.visibility = View.GONE
                            binding.image.visibility = View.VISIBLE
                            Glide.with(this@EditProfileActivity).load(it.image).into(binding.image)
                        }

                        if(it.image1!!.isNotEmpty()){
                            binding.delete1.visibility = View.VISIBLE
                            binding.show1.visibility = View.GONE
                            binding.image1.visibility = View.VISIBLE
                            Glide.with(this@EditProfileActivity).load(it.image1).into(binding.image1)
                            imageUrl1 = it.image1!!
                        }

                        if(it.image2!!.isNotEmpty()){
                            binding.delete1.visibility = View.GONE
                            binding.delete2.visibility = View.VISIBLE
                            binding.show2.visibility = View.GONE
                            binding.image2.visibility = View.VISIBLE
                            Glide.with(this@EditProfileActivity).load(it.image2).into(binding.image2)
                            imageUrl2 = it.image2!!
                        }

                        if(it.image3!!.isNotEmpty()){
                            binding.delete2.visibility = View.GONE
                            binding.delete3.visibility = View.VISIBLE
                            binding.show3.visibility = View.GONE
                            binding.image3.visibility = View.VISIBLE
                            Glide.with(this@EditProfileActivity).load(it.image3).into(binding.image3)
                            imageUrl3 = it.image3!!
                        }

                        if(it.education!!.isNotEmpty()){
                            binding.educationTxt.text = it.education.toString()
                            binding.educationTxt.setTextColor(Color.BLACK)
                        }

                        if(it.gender!!.isNotEmpty()){
                            binding.genderTxt.text = it.gender.toString()
                            binding.genderTxt.setTextColor(Color.BLACK)
                        }

                        if(it.city!!.isNotEmpty()){
                            binding.locationTxt.text = it.city.toString()
                            binding.locationTxt.setTextColor(Color.BLACK)
                        }

                        if(it.height!!.isNotEmpty()){
                            binding.heightTxt.text = it.height.toString()
                            binding.heightTxt.setTextColor(Color.BLACK)
                        }

                        if(it.exercise!!.isNotEmpty()){
                            binding.exerciseTxt.text = it.exercise.toString()
                            binding.exerciseTxt.setTextColor(Color.BLACK)
                        }

                        if(it.star!!.isNotEmpty()){
                            binding.starTxt.text = it.star.toString()
                            binding.starTxt.setTextColor(Color.BLACK)
                        }

                        Config.hideDialog()
                    }
                }
                .onFailure {
                    Toast.makeText(this@EditProfileActivity , "something went wrong", Toast.LENGTH_SHORT).show()
                    Config.hideDialog()
                }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun storeBio(bio: String) {
        Config.showDialog(this)

        GlobalScope.launch {
            val userRepository = UserRepository()
            userRepository.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                .onSuccess {
                    val user = it
                    user.bio = bio
                    userRepository.updateUser(user)
                        .onSuccess {
                            Toast.makeText(this@EditProfileActivity , "Bio updated successfully!", Toast.LENGTH_SHORT).show()
                            Config.hideDialog()
                        }
                        .onFailure {
                            Toast.makeText(this@EditProfileActivity , "something went wrong", Toast.LENGTH_SHORT).show()
                            Config.hideDialog()
                        }

                }.onFailure {
                    Toast.makeText(this@EditProfileActivity , "something went wrong", Toast.LENGTH_SHORT).show()
                    Config.hideDialog()
                }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun uploadImage(imageUri: Uri?, check: Int) {
        Config.showDialog(this)

        val file = File(getRealPathFromURI(imageUri))
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        GlobalScope.launch {
            val storageRepository = StorageRepository()
            storageRepository.uploadFile(
                UploadFileRequest(
                    body,
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    check
                )
            )
                .onSuccess {
                    storeData(it , check)
                    Config.hideDialog()
                }
                .onFailure {
                    Config.hideDialog()
                    Toast.makeText(this@EditProfileActivity , "something went wrong" , Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun getRealPathFromURI(uri: Uri?): String {
        val cursor = uri?.let { this.contentResolver.query(it, null, null, null, null) }
        cursor?.moveToFirst()
        val idx = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val path = cursor?.getString(idx ?: 0)
        cursor?.close()
        return path ?: ""
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun storeData(imageUrl: String, check:Int) {
        val userRepository = UserRepository()
        var user: UserModel

        when (check) {
            0 -> {
                GlobalScope.launch {
                    userRepository.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                        .onSuccess {
                            user = it
                            user.image = imageUrl
                            userRepository.updateUser(user)
                                .onSuccess {
                                    Config.hideDialog()
                                }
                                .onFailure {
                                    Config.hideDialog()
                                }

                        }
                        .onFailure {
                            Config.hideDialog()
                            Toast.makeText(
                                this@EditProfileActivity,
                                "something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                }
            }
            1 -> {
                GlobalScope.launch {
                    userRepository.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                        .onSuccess {
                            user = it
                            user.image1 = imageUrl
                            userRepository.updateUser(user)
                                .onSuccess {
                                    Config.hideDialog()
                                }
                                .onFailure {
                                    Config.hideDialog()
                                }

                        }
                        .onFailure {
                            Config.hideDialog()
                            Toast.makeText(
                                this@EditProfileActivity,
                                "something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                }
                imageUrl1 = imageUrl
                binding.delete1.visibility = View.VISIBLE
            }
            2 -> {
                GlobalScope.launch {
                    userRepository.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                        .onSuccess {
                            user = it
                            user.image2 = imageUrl
                            userRepository.updateUser(user)
                                .onSuccess {
                                    Config.hideDialog()
                                }
                                .onFailure {
                                    Config.hideDialog()
                                }

                        }
                        .onFailure {
                            Config.hideDialog()
                            Toast.makeText(
                                this@EditProfileActivity,
                                "something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                }
                imageUrl2 = imageUrl
                binding.delete1.visibility = View.GONE
                binding.delete2.visibility = View.VISIBLE
            }
            else -> {
                GlobalScope.launch {
                    userRepository.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                        .onSuccess {
                            user = it
                            user.image3 = imageUrl
                            userRepository.updateUser(user)
                                .onSuccess {
                                    Config.hideDialog()
                                }
                                .onFailure {
                                    Config.hideDialog()
                                }

                        }
                        .onFailure {
                            Config.hideDialog()
                            Toast.makeText(
                                this@EditProfileActivity,
                                "something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                }
                imageUrl3 = imageUrl
                binding.delete2.visibility = View.GONE
                binding.delete3.visibility = View.VISIBLE
            }
        }

        Toast.makeText(this@EditProfileActivity , "Image change successfully!", Toast.LENGTH_SHORT).show()
    }
}