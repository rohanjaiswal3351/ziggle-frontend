package com.rohan.datingapp.auth

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.rohan.datingapp.MainActivity
import com.rohan.datingapp.R
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.databinding.ActivitySignInBinding
import com.rohan.datingapp.utils.Config
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    private val RC_SIGN_IN: Int = 123
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.colorPrimary)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)
        mAuth = FirebaseAuth.getInstance()

        binding.signInButton.setOnClickListener {
            signIn()
        }

        binding.termsOfUse.setOnClickListener {
            val intent1 =
                Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.terms_of_use)))
            Toast.makeText(this , "Opening..." , Toast.LENGTH_SHORT).show()
            startActivity(intent1)
        }

        binding.privacyPolicy.setOnClickListener {
            val intent1 =
                Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.privacy_policy)))
            Toast.makeText(this , "Opening..." , Toast.LENGTH_SHORT).show()
            startActivity(intent1)
        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)

        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            // Google Sign In was successful, authenticate with Firebase
                Config.showDialog(this)
            val account = completedTask.getResult(ApiException::class.java)!!
            //Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(ContentValues.TAG, "Google sign in failed", e)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)

        binding.signInButton.visibility = View.GONE
        //binding.progressBar.visibility = View.VISIBLE


        GlobalScope.launch(Dispatchers.IO){
            val auth = mAuth.signInWithCredential(credential).await()
            //Main thread
            withContext(Dispatchers.Main){
                checkUserExist(auth.user!!.uid)
            }
        }
    }

    private fun checkUserExist(uid: String) {

        GlobalScope.launch {
            val userDao = UserDao()
            val snapshot:DataSnapshot = userDao.getUserById(uid).await()

            if(snapshot.exists()){
                withContext(Dispatchers.Main){
                    FirebaseMessaging.getInstance().token.addOnCompleteListener(
                        OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                return@OnCompleteListener
                            }
                            val token = task.result
                            userDao.updateFcmToken(uid , token)
                            Config.hideDialog()
                            startActivity(Intent(this@SignInActivity , MainActivity::class.java))
                            finish()
                        })
                }
            }
            else{
                Config.hideDialog()
                startActivity(Intent(this@SignInActivity , RegisterActivity::class.java))
                finish()
            }
        }

    }

}