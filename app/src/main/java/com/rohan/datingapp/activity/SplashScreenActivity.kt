package com.rohan.datingapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.adapty.Adapty
import com.adapty.utils.AdaptyResult
//import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.rohan.datingapp.MainActivity
import com.rohan.datingapp.R
import com.rohan.datingapp.auth.RegisterActivity
import com.rohan.datingapp.auth.SignInActivity
import com.rohan.datingapp.daos.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        FirebaseApp.initializeApp(this)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.colorPrimary)

        val user = FirebaseAuth.getInstance().currentUser
        FirebaseMessaging.getInstance().subscribeToTopic("common")

        val sharedPreferences : SharedPreferences = this.getSharedPreferences("PREFS" , 0)

        //MobileAds.initialize(this) {}

        Handler(Looper.getMainLooper()).postDelayed({
            if(user == null){
                startActivity(Intent(this , SignInActivity::class.java))
                finish()
            }
            else {
                GlobalScope.launch {
                    val userDao = UserDao()
                    val snapshot:DataSnapshot = userDao.getUserById(FirebaseAuth.getInstance().currentUser!!.uid).await()

                    if(snapshot.exists()){
                        withContext(Dispatchers.Main){
                            val myConnectionsRef = FirebaseDatabase.getInstance().getReference("status")

                            val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
                            connectedRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val connected = snapshot.getValue(Boolean::class.java)
                                    if (connected == true) {
                                        myConnectionsRef.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue("Online").addOnSuccessListener {
                                            startActivity(Intent(this@SplashScreenActivity , MainActivity::class.java))
                                            finish()
                                        }
                                        myConnectionsRef.child(FirebaseAuth.getInstance().currentUser!!.uid).onDisconnect().setValue("Offline")
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                }
                            })

                            Adapty.getProfile { result ->
                                when (result) {
                                    is AdaptyResult.Success -> {
                                        val profile = result.value
                                        if (profile.accessLevels["premium"]?.isActive == true) {
                                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                            editor.putInt("premium" , 1)
                                            editor.apply()
                                        }else{
                                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                            editor.putInt("premium" , 0)
                                            editor.apply()
                                        }
                                    }
                                    is AdaptyResult.Error -> {
                                    }
                                }
                            }
                        }
                    }
                    else{
                        startActivity(Intent(this@SplashScreenActivity , RegisterActivity::class.java))
                        finish()
                    }
                }
            }
        } , 1000)
    }

}