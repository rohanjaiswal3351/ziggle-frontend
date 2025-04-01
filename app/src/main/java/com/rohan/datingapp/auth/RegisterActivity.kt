package com.rohan.datingapp.auth

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.rohan.datingapp.MainActivity
import com.rohan.datingapp.R
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.databinding.ActivityRegisterBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.utils.Config
import java.io.ByteArrayOutputStream


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private var name: String? = null
    private var age: String? = null
    private var gender: String? = null
    private var imageUri: Uri? = null
    private var imageUri1: Uri? = null
    private var country: String? = "United States"


    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it
        binding.show.visibility = View.GONE
        binding.image.visibility = View.VISIBLE
        binding.image.setImageURI(imageUri)
    }

    private val selectImage1 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri1 = it
        binding.show1.visibility = View.GONE
        binding.image1.visibility = View.VISIBLE
        binding.image1.setImageURI(imageUri1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.colorPrimary)

        binding.setImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.setImage1.setOnClickListener {
            selectImage1.launch("image/*")
        }

        binding.man.setOnClickListener {
            gender = "Man"
            binding.layout3.visibility = View.GONE
            binding.layout4.visibility = View.VISIBLE
        }

        binding.woman.setOnClickListener {
            gender = "Woman"
            binding.layout3.visibility = View.GONE
            binding.layout4.visibility = View.VISIBLE
        }

        binding.nonBinary.setOnClickListener {
            gender = "Nonbinary"
            binding.layout3.visibility = View.GONE
            binding.layout4.visibility = View.VISIBLE
        }

        binding.next1.setOnClickListener {
            validateData1()
        }

        binding.next2.setOnClickListener {
            validateData2()
        }

        binding.next3.setOnClickListener {
            validateData3()
        }

        binding.next4.setOnClickListener {
            uploadImage()
        }

        binding.ccp.setOnCountryChangeListener{
            country = it.name.toString()
        }

    }

    private fun validateData1() {
        if(binding.userName.text.toString().isEmpty()){
            Toast.makeText(this , "Please enter your name." , Toast.LENGTH_SHORT).show()
        }
        else{
            name = binding.userName.text.toString().trim()
            binding.layout1.visibility = View.GONE
            binding.layout2.visibility = View.VISIBLE
        }
    }

    private fun validateData2() {
        if(imageUri == null || imageUri1 == null){
            Toast.makeText(this , "Please select images." , Toast.LENGTH_SHORT).show()
        }
        else{
            binding.layout2.visibility = View.GONE
            binding.layout3.visibility = View.VISIBLE
            //binding.selectGender.clearCheck()

        }
    }

    private fun validateData3() {
        if(binding.userAge.text.toString().isEmpty()){
            Toast.makeText(this , "Please enter your age." , Toast.LENGTH_SHORT).show()
        }
        else{
            age = binding.userAge.text.toString().trim()
            binding.layout4.visibility = View.GONE
            binding.layout5.visibility = View.VISIBLE
            //uploadImage()
        }
    }

    private fun uploadImage() {
        Config.showDialog(this)

        val storageReference = FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("profile.jpg")

        val storageReference1 = FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("image1.jpg")

        val bmp: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        val bao = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, bao)
        val data = bao.toByteArray()

        val bmp1: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri1)
        val bao1 = ByteArrayOutputStream()
        bmp1.compress(Bitmap.CompressFormat.JPEG, 25, bao1)
        val data1 = bao1.toByteArray()

        storageReference.putBytes(data)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    //storeData(it)
                    val it1 = it

                    storageReference1.putBytes(data1)
                        .addOnSuccessListener {
                            storageReference1.downloadUrl.addOnSuccessListener {it2->
                                storeData(it1 , it2)
                            }.addOnFailureListener {
                                Config.hideDialog()
                                Toast.makeText(this , it.message , Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener{
                            Config.hideDialog()
                            Toast.makeText(this , it.message , Toast.LENGTH_SHORT).show()
                        }

                }.addOnFailureListener {
                    Config.hideDialog()
                    Toast.makeText(this , it.message , Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Config.hideDialog()
                Toast.makeText(this , it.message , Toast.LENGTH_SHORT).show()
            }


    }

    private fun storeData(imageUrl: Uri? , imageUrl1: Uri?) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result

            val list:ArrayList<String> = ArrayList()
            list.add(FirebaseAuth.getInstance().currentUser!!.uid)

            val userDao = UserDao()
            val user = UserModel(
                uid = FirebaseAuth.getInstance().currentUser!!.uid,
                email = FirebaseAuth.getInstance().currentUser!!.email,
                name = name,
                age = age,
                city = country,
                image = imageUrl.toString(),
                image1 = imageUrl1.toString(),
                gender = gender,
                rightSwipeBy = list,
                matches = list,
                interact = list,
                fcmToken = token
            )

            userDao.addUser(user)

            Config.hideDialog()
            startActivity(Intent(this , MainActivity::class.java))
            finish()
        })

    }
}