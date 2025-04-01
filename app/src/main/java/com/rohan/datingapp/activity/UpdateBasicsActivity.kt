package com.rohan.datingapp.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.databinding.ActivityUpdateBasicsBinding
import com.rohan.datingapp.utils.Config


class UpdateBasicsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBasicsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBasicsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        val check:String = intent.getStringExtra("check")!!

        when (check) {
            "name"->{
                binding.name.visibility = View.VISIBLE
                initName()
            }
            "age"->{
                binding.age.visibility = View.VISIBLE
                initAge()
            }
            "education" -> {
                binding.education.visibility = View.VISIBLE
                initEducation()
            }
            "gender" -> {
                binding.gender.visibility = View.VISIBLE
                initGender()
            }
            "location" -> {
                binding.location.visibility = View.VISIBLE
                initLocation()
            }
            "height" -> {
                binding.height.visibility = View.VISIBLE
                initHeight()
            }
            "exercise" -> {
                binding.exercise.visibility = View.VISIBLE
                initExercise()
            }
            else -> {
                binding.star.visibility = View.VISIBLE
                initStar()
            }
        }

    }

    private fun initLocation() {
        var country = "United States"
        binding.ccp.setOnCountryChangeListener{
            country = it.name.toString()
        }

        binding.saveLocation.setOnClickListener {
            uploadData(country , 2)
        }
    }

    private fun initAge() {
        val age = intent.getStringExtra("age")
        binding.ageTxt.setText(age)
        binding.saveAge.setOnClickListener {
            if(binding.ageTxt.text!!.isEmpty()){
                Toast.makeText(this , "Please enter age", Toast.LENGTH_SHORT).show()
            }else{
                uploadData(binding.ageTxt.text.toString().trim() , 8)
            }
        }
    }

    private fun initName() {
        val name = intent.getStringExtra("name")
        binding.nameTxt.setText(name)
        binding.saveName.setOnClickListener {
            if(binding.nameTxt.text!!.isEmpty()){
                Toast.makeText(this , "Please enter name", Toast.LENGTH_SHORT).show()
            }else{
                uploadData(binding.nameTxt.text.toString().trim() , 7)
            }
        }
    }

    private fun initStar() {
        binding.s1.setOnClickListener {
            uploadData(binding.s1.text.toString(),6)
        }
        binding.s2.setOnClickListener {
            uploadData(binding.s2.text.toString(),6)
        }
        binding.s3.setOnClickListener {
            uploadData(binding.s3.text.toString(),6)
        }
        binding.s4.setOnClickListener {
            uploadData(binding.s4.text.toString(),6)
        }
        binding.s5.setOnClickListener {
            uploadData(binding.s5.text.toString(),6)
        }
        binding.s6.setOnClickListener {
            uploadData(binding.s6.text.toString(),6)
        }
        binding.s7.setOnClickListener {
            uploadData(binding.s7.text.toString(),6)
        }
        binding.s8.setOnClickListener {
            uploadData(binding.s8.text.toString(),6)
        }
        binding.s9.setOnClickListener {
            uploadData(binding.s9.text.toString(),6)
        }
        binding.s10.setOnClickListener {
            uploadData(binding.s10.text.toString(),6)
        }
        binding.s11.setOnClickListener {
            uploadData(binding.s11.text.toString(),6)
        }
        binding.s12.setOnClickListener {
            uploadData(binding.s12.text.toString(),6)
        }
    }

    private fun initExercise() {
        binding.ex1.setOnClickListener {
            uploadData(binding.ex1.text.toString(),4)
        }
        binding.ex2.setOnClickListener {
            uploadData(binding.ex1.text.toString(),4)
        }
        binding.ex3.setOnClickListener {
            uploadData(binding.ex1.text.toString(),4)
        }
    }

    private fun initHeight() {
        binding.saveHeight.setOnClickListener {
            if(binding.enterHeight.text.isEmpty()){
                Toast.makeText(this , "Please enter height", Toast.LENGTH_SHORT).show()
            }
            else{
                uploadData(binding.enterHeight.text.toString(), 3)
            }
        }
    }

    private fun initGender() {
        binding.gen1.setOnClickListener {
            uploadData(binding.gen1.text.toString(), 1)
        }
        binding.gen2.setOnClickListener {
            uploadData(binding.gen2.text.toString(), 1)
        }
        binding.gen3.setOnClickListener {
            uploadData(binding.gen3.text.toString(), 1)
        }
    }

    private fun initEducation() {
        binding.edu1.setOnClickListener {
            uploadData(binding.edu1.text.toString(), 0)
        }

        binding.edu2.setOnClickListener {
            uploadData(binding.edu2.text.toString(), 0)
        }

        binding.edu3.setOnClickListener {
            uploadData(binding.edu3.text.toString(), 0)
        }

        binding.edu4.setOnClickListener {
            uploadData(binding.edu4.text.toString(), 0)
        }

        binding.edu5.setOnClickListener {
            uploadData(binding.edu5.text.toString(), 0)
        }

        binding.edu6.setOnClickListener {
            uploadData(binding.edu6.text.toString(), 0)
        }
    }

    private fun uploadData(text: String?, check:Int) {
        Config.showDialog(this)
        val userDao = UserDao()

        when(check){
            0->{
                userDao.updateEducation(this, FirebaseAuth.getInstance().currentUser!!.uid , text.toString())
            }
            1->{
                userDao.updateGender(this, FirebaseAuth.getInstance().currentUser!!.uid , text.toString())
            }
            2->{
                userDao.updateCity(this, FirebaseAuth.getInstance().currentUser!!.uid , text.toString())
            }
            3->{
                userDao.updateHeight(this, FirebaseAuth.getInstance().currentUser!!.uid , text.toString())
            }
            4->{
                userDao.updateExercise(this, FirebaseAuth.getInstance().currentUser!!.uid , text.toString())
            }
            6->{
                userDao.updateStar(this, FirebaseAuth.getInstance().currentUser!!.uid , text.toString())
            }
            7->{
                userDao.updateName(this, FirebaseAuth.getInstance().currentUser!!.uid , text.toString())
            }
            8->{
                userDao.updateAge(this, FirebaseAuth.getInstance().currentUser!!.uid , text.toString())
            }
        }

        Config.hideDialog()
        Toast.makeText(this , "Updated successfully!", Toast.LENGTH_SHORT).show()
    }
}