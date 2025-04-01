package com.rohan.datingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.databinding.ActivityMainBinding
import com.rohan.datingapp.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Suppress("DEPRECATED_IDENTITY_EQUALS", "DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationIndicator: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationIndicator = LayoutInflater.from(this)
            .inflate(R.layout.layout_indicator, binding.bottomNavigationView, false)

        checkForNotification()
        checkForUpdate()

        val navController = findNavController(R.id.fragment)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.likeFragment) {
                if (notificationIndicator != null) {
                    if (notificationIndicator.visibility == View.VISIBLE) {
                        notificationIndicator.visibility = View.GONE
                        val userDao = UserDao()
                        userDao.updateLikeNotify(FirebaseAuth.getInstance().currentUser!!.uid)
                    }
                }
            }
        }

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

    }

    private fun checkForNotification() {
        GlobalScope.launch {
            val userDao = UserDao()
            val user: UserModel = userDao.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                .await().getValue(UserModel::class.java)!!

            withContext(Dispatchers.Main) {
                if (user.likeNotify?.isNotEmpty() == true) {
                    if (user.likeNotify == "Yes") {
                        showNotificationIndicator()
                    }
                }
            }
        }
    }

    private val listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADING) {
            val bytesDownloaded = state.bytesDownloaded()
            val totalBytesToDownload = state.totalBytesToDownload()
        }
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackbarForCompleteUpdate()
        }
    }

    private lateinit var appUpdateManager: AppUpdateManager

    private fun checkForUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE, this, 123
                )
            }
        }

        appUpdateManager.registerListener(listener)
    }

    fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            setActionTextColor(resources.getColor(R.color.main_background))
            show()
        }
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
            }
    }

//    private fun initializeAds() {
//        val adRequest = AdRequest.Builder().build()
//        binding.adView.loadAd(adRequest)
//
//        binding.adView.adListener = object: AdListener() {
//            override fun onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                super.onAdLoaded()
//                binding.adView.visibility = View.VISIBLE
//            }
//
//            override fun onAdFailedToLoad(adError : LoadAdError) {
//                // Code to be executed when an ad request fails.
//                super.onAdFailedToLoad(adError)
//                Log.d("dbharry" , "failed to load $adError")
//                binding.adView.loadAd(adRequest)
//            }
//
//            override fun onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            override fun onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            override fun onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        }
//    }

    private fun showNotificationIndicator() {
        val itemView: BottomNavigationItemView =
            binding.bottomNavigationView.findViewById(R.id.likeFragment)
        //notificationIndicator = LayoutInflater.from(this).inflate(R.layout.layout_indicator, binding.bottomNavigationView, false)
        itemView.addView(notificationIndicator)
    }

}