package com.rohan.datingapp.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rohan.datingapp.R
import com.rohan.datingapp.adapter.ShowInterestAdapter
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.databinding.ActivityEditProfileBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.utils.Config
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream


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

    private fun showDeleteDialog(check: Int){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.delete_dialog)
        val delete = dialog.findViewById<Button>(R.id.deleteBtn)
        val cancel = dialog.findViewById<TextView>(R.id.cancel)

        delete.setOnClickListener {
            val userDao = UserDao()
            val mFirebaseStorage = FirebaseStorage.getInstance()

            when (check) {
                1 -> {
                    userDao.deleteImage1(FirebaseAuth.getInstance().currentUser!!.uid)
                    val photoRef: StorageReference = mFirebaseStorage.getReferenceFromUrl(imageUrl1)
                    photoRef.delete().addOnSuccessListener {
                        Toast.makeText(this , "Deleted", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }.addOnFailureListener {
                        dialog.dismiss()
                        Toast.makeText(this , "something went wrong", Toast.LENGTH_SHORT).show()
                    }
                    binding.delete1.visibility = View.GONE
                    binding.image1.visibility = View.GONE
                    binding.show1.visibility = View.VISIBLE
                }
                2 -> {
                    userDao.deleteImage2(FirebaseAuth.getInstance().currentUser!!.uid)
                    val photoRef: StorageReference = mFirebaseStorage.getReferenceFromUrl(imageUrl2)
                    photoRef.delete().addOnSuccessListener {
                        Toast.makeText(this , "Deleted", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }.addOnFailureListener {
                        Toast.makeText(this , "something went wrong", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    binding.delete2.visibility = View.GONE
                    binding.image2.visibility = View.GONE
                    binding.show2.visibility = View.VISIBLE
                    binding.delete1.visibility = View.VISIBLE
                }
                3 -> {
                    userDao.deleteImage3(FirebaseAuth.getInstance().currentUser!!.uid)
                    val photoRef: StorageReference = mFirebaseStorage.getReferenceFromUrl(imageUrl3)
                    photoRef.delete().addOnSuccessListener {
                        Toast.makeText(this , "Deleted", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }.addOnFailureListener {
                        Toast.makeText(this , "something went wrong", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    binding.delete3.visibility = View.GONE
                    binding.image3.visibility = View.GONE
                    binding.show3.visibility = View.VISIBLE
                    binding.delete2.visibility = View.VISIBLE
                }
            }

        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun storeSocial(check: Int, id: String) {
        Config.showDialog(this)
        if(check == 0){
            val userDao = UserDao()
            userDao.updateInsta(FirebaseAuth.getInstance().currentUser!!.uid , id)
            binding.checkInsta.visibility = View.GONE
            Config.hideDialog()
            Toast.makeText(this , "Instagram updated successfully", Toast.LENGTH_SHORT).show()
        }else if(check == 1){
            val userDao = UserDao()
            userDao.updateSnap(FirebaseAuth.getInstance().currentUser!!.uid , id)
            binding.checkSnap.visibility = View.GONE
            Config.hideDialog()
            Toast.makeText(this , "Snapchat updated successfully", Toast.LENGTH_SHORT).show()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadContent() {
        Config.showDialog(this)

        val userDao = UserDao()

        GlobalScope.launch {
            val user:UserModel = userDao.getUserById(FirebaseAuth.getInstance().currentUser!!.uid).await().getValue(UserModel::class.java)!!

            withContext(Dispatchers.Main){
                if (user.interests != null){
                    binding.interestLayout.visibility = View.VISIBLE
                    val gridLayoutManager = GridLayoutManager(
                        this@EditProfileActivity , 2, GridLayoutManager.VERTICAL, false
                    )

                    val interestAdapter = ShowInterestAdapter(this@EditProfileActivity)
                    binding.recyclerView.layoutManager = gridLayoutManager
                    binding.recyclerView.adapter = interestAdapter
                    interestAdapter.updateList(user.interests!!)
                }

                if(user.bio!!.isNotEmpty()){
                    binding.bio.setText(user.bio)
                }

                if(user.instaId!!.isNotEmpty()){
                    binding.addInsta.setText(user.instaId)
                    binding.checkInsta.visibility = View.GONE
                }

                if(user.snapId!!.isNotEmpty()){
                    binding.addSnap.setText(user.snapId)
                    binding.checkSnap.visibility = View.GONE
                }

                if(user.image!!.isNotEmpty()){
                    binding.show.visibility = View.GONE
                    binding.image.visibility = View.VISIBLE
                    Glide.with(this@EditProfileActivity).load(user.image).into(binding.image)
                }

                if(user.image1!!.isNotEmpty()){
                    binding.delete1.visibility = View.VISIBLE
                    binding.show1.visibility = View.GONE
                    binding.image1.visibility = View.VISIBLE
                    Glide.with(this@EditProfileActivity).load(user.image1).into(binding.image1)
                    imageUrl1 = user.image1!!
                }

                if(user.image2!!.isNotEmpty()){
                    binding.delete1.visibility = View.GONE
                    binding.delete2.visibility = View.VISIBLE
                    binding.show2.visibility = View.GONE
                    binding.image2.visibility = View.VISIBLE
                    Glide.with(this@EditProfileActivity).load(user.image2).into(binding.image2)
                    imageUrl2 = user.image2!!
                }

                if(user.image3!!.isNotEmpty()){
                    binding.delete2.visibility = View.GONE
                    binding.delete3.visibility = View.VISIBLE
                    binding.show3.visibility = View.GONE
                    binding.image3.visibility = View.VISIBLE
                    Glide.with(this@EditProfileActivity).load(user.image3).into(binding.image3)
                    imageUrl3 = user.image3!!
                }

                if(user.education!!.isNotEmpty()){
                    binding.educationTxt.text = user.education.toString()
                    binding.educationTxt.setTextColor(Color.BLACK)
                }

                if(user.gender!!.isNotEmpty()){
                    binding.genderTxt.text = user.gender.toString()
                    binding.genderTxt.setTextColor(Color.BLACK)
                }

                if(user.city!!.isNotEmpty()){
                    binding.locationTxt.text = user.city.toString()
                    binding.locationTxt.setTextColor(Color.BLACK)
                }

                if(user.height!!.isNotEmpty()){
                    binding.heightTxt.text = user.height.toString()
                    binding.heightTxt.setTextColor(Color.BLACK)
                }

                if(user.exercise!!.isNotEmpty()){
                    binding.exerciseTxt.text = user.exercise.toString()
                    binding.exerciseTxt.setTextColor(Color.BLACK)
                }

                if(user.star!!.isNotEmpty()){
                    binding.starTxt.text = user.star.toString()
                    binding.starTxt.setTextColor(Color.BLACK)
                }

                Config.hideDialog()
            }

        }
    }

    private fun storeBio(bio: String) {
        Config.showDialog(this)

        val  userDao = UserDao()
        userDao.updateBio(FirebaseAuth.getInstance().currentUser!!.uid , bio)
        
        Toast.makeText(this@EditProfileActivity , "Bio updated successfully!", Toast.LENGTH_SHORT).show()
        Config.hideDialog()
    }

    private fun uploadImage(imageUri: Uri?, check: Int) {
        Config.showDialog(this)

        val storageReference:StorageReference

        if(check == 0){
            storageReference = FirebaseStorage.getInstance().getReference("profile")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).child("profile.jpg")
        }
        else if(check == 1){
            storageReference = FirebaseStorage.getInstance().getReference("profile")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).child("image1.jpg")
        }
        else if(check == 2){
            storageReference = FirebaseStorage.getInstance().getReference("profile")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).child("image2.jpg")
        }
        else{
            storageReference = FirebaseStorage.getInstance().getReference("profile")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).child("image3.jpg")
        }

        val bmp: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        val bao = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, bao)
        val data = bao.toByteArray()

        storageReference.putBytes(data)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    storeData(it , check)
                }.addOnFailureListener {
                    Config.hideDialog()
                    Toast.makeText(this , "something went wrong" , Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Config.hideDialog()
                Toast.makeText(this , "something went wrong" , Toast.LENGTH_SHORT).show()
            }

    }

    private fun storeData(imageUrl: Uri? , check:Int) {
        val userDao = UserDao()

        when (check) {
            0 -> {
                userDao.updateImage(FirebaseAuth.getInstance().currentUser!!.uid , imageUrl.toString())
                Config.hideDialog()
            }
            1 -> {
                imageUrl1 = imageUrl.toString()
                userDao.updateImage1(FirebaseAuth.getInstance().currentUser!!.uid , imageUrl.toString())
                Config.hideDialog()
                binding.delete1.visibility = View.VISIBLE
            }
            2 -> {
                imageUrl2 = imageUrl.toString()
                userDao.updateImage2(FirebaseAuth.getInstance().currentUser!!.uid , imageUrl.toString())
                Config.hideDialog()
                binding.delete1.visibility = View.GONE
                binding.delete2.visibility = View.VISIBLE
            }
            else -> {
                imageUrl3 = imageUrl.toString()
                userDao.updateImage3(FirebaseAuth.getInstance().currentUser!!.uid , imageUrl.toString())
                Config.hideDialog()
                binding.delete2.visibility = View.GONE
                binding.delete3.visibility = View.VISIBLE
            }
        }

        Toast.makeText(this@EditProfileActivity , "Image change successfully!", Toast.LENGTH_SHORT).show()
    }
}