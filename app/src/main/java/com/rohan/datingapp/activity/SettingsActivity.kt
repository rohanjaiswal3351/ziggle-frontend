package com.rohan.datingapp.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.R
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.auth.SignInActivity
import com.rohan.datingapp.databinding.ActivitySettingsBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.utils.Config
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySettingsBinding
    private var mStoreLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadContent()

        mStoreLink = "market://details?id=" + this.packageName

        binding.back.setOnClickListener {
            finish()
        }

        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this , SignInActivity::class.java))
            finish()
        }

        binding.name.setOnClickListener {
            val intent = Intent(this , UpdateBasicsActivity::class.java)
            intent.putExtra("name" , binding.nameTxt.text)
            intent.putExtra("check" , "name")
            startActivity(intent)
        }

        binding.age.setOnClickListener {
            val intent = Intent(this , UpdateBasicsActivity::class.java)
            intent.putExtra("age" , binding.ageTxt.text)
            intent.putExtra("check" , "age")
            startActivity(intent)
        }

        binding.terms.setOnClickListener {
            val intent1 =
                Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.terms_of_use)))
            Toast.makeText(this , "Opening..." , Toast.LENGTH_SHORT).show()
            startActivity(intent1)
        }

        binding.rateUs.setOnClickListener {
            Toast.makeText(this , "Opening..." , Toast.LENGTH_SHORT).show()
            val marketUri: Uri = Uri.parse(mStoreLink)
            try {
                startActivity(Intent(Intent.ACTION_VIEW, marketUri))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    "Couldn't find PlayStore on this device",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.shareApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val body = "Download the app now"
            val sub = "https://play.google.com/store/apps/details?id=com.rohan.datingapp&pli=1"
            intent.putExtra(Intent.EXTRA_TEXT , body)
            intent.putExtra(Intent.EXTRA_TEXT , sub)
            startActivity(Intent.createChooser(intent , "share using "))
        }

        binding.help.setOnClickListener {
            var body = ""
            try {
                body = "How can we help?\n\n\nPlease do not delete below contents\n"
            } catch (e: PackageManager.NameNotFoundException) {
            }
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL,
                arrayOf("oceantech.studio@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Customer Support Ziggle")
            intent.putExtra(Intent.EXTRA_TEXT, body)
            intent.setPackage("com.google.android.gm")
            startActivity(intent)
            //startActivity(Intent.createChooser(intent, "Send email:"))
        }

        binding.suggestion.setOnClickListener {
            var body = ""
            try {
                body = "Give us suggestion?\n\n\nWe value your suggestion\n"
            } catch (e: PackageManager.NameNotFoundException) {
            }
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL,
                arrayOf("oceantech.studio@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Suggestion for Ziggle")
            intent.putExtra(Intent.EXTRA_TEXT, body)
            intent.setPackage("com.google.android.gm")
            startActivity(intent)
            //startActivity(Intent.createChooser(intent, "Send email:"))
        }

        binding.delete.setOnClickListener {
            Toast.makeText(
                this,
                "This feature is coming soon.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadContent() {
        Config.showDialog(this)

        GlobalScope.launch {
            val userDao = UserDao()
            val data:UserModel = userDao.getUserById(FirebaseAuth.getInstance().currentUser!!.uid).await().getValue(UserModel::class.java)!!

            withContext(Dispatchers.Main){
                binding.nameTxt.text = data.name.toString().trim()
                binding.ageTxt.text = data.age.toString().trim()
                Config.hideDialog()
            }
        }
    }
}