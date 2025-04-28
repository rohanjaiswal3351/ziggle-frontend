package com.rohan.datingapp.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
//import com.google.android.gms.ads.AdError
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.FullScreenContentCallback
//import com.google.android.gms.ads.LoadAdError
//import com.google.android.gms.ads.interstitial.InterstitialAd
//import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.R
import com.rohan.datingapp.activity.BuyPremiumActivity
import com.rohan.datingapp.adapter.DatingAdapter
import com.rohan.datingapp.adapter.DatingAdapterInterface
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.databinding.FragmentDatingBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.notification.ApiUtilities
import com.rohan.datingapp.notification.NotificationModel
import com.rohan.datingapp.notification.PushNotificationModel
import com.rohan.datingapp.utils.Config
import com.rohan.datingapp.utils.OverlapDecoration
import com.rohan.datingapp.viewModel.UserViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting
import com.yuyakaido.android.cardstackview.SwipeableMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DatingFragment : Fragment(), DatingAdapterInterface {

    private lateinit var binding: FragmentDatingBinding
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: DatingAdapter
    private lateinit var dialog: Dialog
    private var name: String? = null
    private lateinit var mContext: Context
    private var genderCheck: Int = 3
    private var distanceCheck: Int = 0
    //private var mInterstitialAd: InterstitialAd? = null
    private var countSwipe: Int = 0
    private var lastUserUid: String = ""
    private var rewindCheck: Int = 0
    private var isPremium: Int = 0
    private lateinit var settings: SharedPreferences
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDatingBinding.inflate(layoutInflater)

        adapter = DatingAdapter(mContext, this)

        getData()

        dialog = Dialog(mContext)

        binding.info.setOnClickListener {
            dialog.setContentView(R.layout.info_dialog)
            val ok = dialog.findViewById<Button>(R.id.ok)

            ok.setOnClickListener {
                dialog.dismiss()
            }

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        settings = requireActivity().getSharedPreferences("PREFS" , 0)
        genderCheck = settings.getInt("gender" , 3)
        distanceCheck = settings.getInt("distance" , 0)
        isPremium = settings.getInt("premium" , 0)
        countSwipe = settings.getInt("count", 0)

        if(isPremium == 0){
            //loadInterstitialAds()
        }

        binding.filterBtn.setOnClickListener {
            val dialog = Dialog(mContext)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.filter_dialog)

            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val guysBtn = dialog.findViewById<CardView>(R.id.guys)
            val girlsBtn = dialog.findViewById<CardView>(R.id.girls)
            val allBtn = dialog.findViewById<CardView>(R.id.all)

            val countryBtn = dialog.findViewById<CardView>(R.id.country)
            val globalBtn = dialog.findViewById<CardView>(R.id.global)


            when(genderCheck){
                1-> guysBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.main_background))
                2-> girlsBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.main_background))
                3-> allBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.main_background))
            }

            when(distanceCheck){
                0-> globalBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.main_background))
                1-> countryBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.main_background))
            }

            val editor: SharedPreferences.Editor = settings.edit()

            guysBtn.setOnClickListener {
                genderCheck = 1
                editor.putInt("gender" , 1)
                editor.apply()

                guysBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.main_background))
                girlsBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.bg_light_grey))
                allBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.bg_light_grey))
            }

            girlsBtn.setOnClickListener {
                genderCheck = 2
                editor.putInt("gender" , 2)
                editor.apply()

                guysBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.bg_light_grey))
                girlsBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.main_background))
                allBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.bg_light_grey))
            }

            allBtn.setOnClickListener {
                genderCheck = 3
                editor.putInt("gender" , 3)
                editor.apply()

                guysBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.bg_light_grey))
                girlsBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.bg_light_grey))
                allBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.main_background))
            }

            countryBtn.setOnClickListener {
                distanceCheck = 1
                editor.putInt("distance" , 1)
                editor.apply()

                globalBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.bg_light_grey))
                countryBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.main_background))
            }

            globalBtn.setOnClickListener {
                distanceCheck = 0
                editor.putInt("distance" , 0)
                editor.apply()

                countryBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.bg_light_grey))
                globalBtn.setCardBackgroundColor(mContext.resources.getColor(R.color.main_background))
            }

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window!!.setGravity(Gravity.BOTTOM)

            dialog.setOnDismissListener {
                getData()
            }

            dialog.show()
        }

        binding.premiumBtn.setOnClickListener{
            startActivity(Intent(mContext, BuyPremiumActivity::class.java))
        }

        binding.rewind.setOnClickListener {
            if(isPremium == 0){
                showRewindDialog()
            }else{
                if(lastUserUid != ""){
                    rewindCheck = 1
                    val userDao = UserDao()
                    userDao.updateInteract(FirebaseAuth.getInstance().currentUser!!.uid , 1 , lastUserUid)
                    getData()
                }else{
                    Toast.makeText(mContext, "Please do at least one swipe", Toast.LENGTH_SHORT).show()
                }
            }

        }

        return binding.root
    }

    private fun showRewindDialog() {
        val dialog = Dialog(mContext)
        dialog.setContentView(R.layout.rewind_dialog)
        val buyBtn = dialog.findViewById<Button>(R.id.buyBtn)
        val cancel = dialog.findViewById<TextView>(R.id.cancel)

        buyBtn.setOnClickListener {
            startActivity(Intent(mContext, BuyPremiumActivity::class.java))
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

//    @SuppressLint("VisibleForTests")
//    private fun loadInterstitialAds() {
//        val adRequest = AdRequest.Builder().build()
//        InterstitialAd.load(mContext, "ca-app-pub-2535385936024567/6069306054", adRequest,
//            object : InterstitialAdLoadCallback() {
//                override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                    // The mInterstitialAd reference will be null until
//                    // an ad is loaded.
//                    mInterstitialAd = interstitialAd
//
//                    mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
//                        override fun onAdClicked() {
//                            // Called when a click is recorded for an ad.
//                            //Log.d(TAG, "Ad was clicked.")
//                        }
//
//                        override fun onAdDismissedFullScreenContent() {
//                            // Called when ad is dismissed.
//                            //Log.d(TAG, "Ad dismissed fullscreen content.")
//                            mInterstitialAd = null
//                            loadInterstitialAds()
//                        }
//
//                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                            // Called when ad fails to show.
//                            //Log.e(TAG, "Ad failed to show fullscreen content.")
//                            mInterstitialAd = null
//                        }
//
//                        override fun onAdImpression() {
//                            // Called when an impression is recorded for an ad.
//                            //Log.d(TAG, "Ad recorded an impression.")
//                        }
//
//                        override fun onAdShowedFullScreenContent() {
//                            // Called when ad is shown.
//                            //Log.d(TAG, "Ad showed fullscreen content.")
//                        }
//                    }
//
//                }
//
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    // Handle the error
//                    mInterstitialAd = null
//                }
//            })
//
//
//    }


    private fun init(){
        manager = CardStackLayoutManager(mContext , object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {
                countSwipe++
                val editor: SharedPreferences.Editor = settings.edit()
                editor.putInt("count" , countSwipe)
                editor.apply()
                val currUser = list!![manager.topPosition - 1]
                val number = currUser.uid
                lastUserUid = number!!
                val fcmToken = currUser.fcmToken

                val userDao = UserDao()
                userDao.updateInteract(FirebaseAuth.getInstance().currentUser!!.uid , 0 , number)

                if(direction!!.name == "Right"){
                    userDao.updateSwipeRightBy(number , 0 , FirebaseAuth.getInstance().currentUser!!.uid)

                    val notificationData = PushNotificationModel(
                        NotificationModel("$name sent you a friend request!", ""),
                        fcmToken)

                    ApiUtilities.getInstance().sendNotification(
                        notificationData
                    ).enqueue(object: Callback<PushNotificationModel> {
                        override fun onResponse(
                            call: Call<PushNotificationModel>,
                            response: Response<PushNotificationModel>
                        ) {
                        }

                        override fun onFailure(call: Call<PushNotificationModel>, t: Throwable) {
                        }

                    })
                }

                if(manager.topPosition == list!!.size){
                    getData()
                    //Toast.makeText(mContext, "This is last card", Toast.LENGTH_SHORT).show()
                }

                if(countSwipe >= 12){
                    countSwipe = 0
                    editor.putInt("count" , countSwipe)
                    editor.apply()
//                    if (mInterstitialAd != null) {
//                        if(isPremium == 0){
//                            mInterstitialAd?.show(requireActivity())
//                        }
//                    }
                }
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View?, position: Int) {
            }

            override fun onCardDisappeared(view: View?, position: Int) {
            }

        })

//        manager.setCanScrollVertical(true)
//        manager.setSwipeableMethod(SwipeableMethod.Automatic)

        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.6f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setSwipeThreshold(0.7f)
        manager.setCanScrollVertical(false)
    }

    companion object{
        var list: ArrayList<UserModel>? = null
    }

    private fun getData() {
        Config.showDialog(mContext)

        lifecycleScope.launch {
            userViewModel.users.collect { userList ->
                // Update UI with the list of users
                if (userList.isNotEmpty()) {
                    // Example: Display users in a RecyclerView or log them

                }
            }
        }

        // Observe errorMessage flow from ViewModel
        lifecycleScope.launch {
            userViewModel.errorMessage.collect { error ->
                error?.let {
                    // Handle error (e.g., show a Toast or Snackbar)
                    println("Error: $it")
                }
            }
        }

        // Trigger API call to fetch next users
        userViewModel.loadNextUsers(uid = FirebaseAuth.getInstance().currentUser!!.uid, pageSize = 10)


        GlobalScope.launch {
            val userDao = UserDao()

            val currUser: UserModel = userDao.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                .await().getValue(UserModel::class.java)!!

            name = currUser.name

            val interact:ArrayList<String> = ArrayList()
            val blockedUsersByMain:ArrayList<String> = ArrayList()

            currUser.interact?.let { interact.addAll(it) }
            currUser.blockedUsers?.let { blockedUsersByMain.addAll(it) }

            if(rewindCheck == 1){
                interact.remove(lastUserUid)
            }

            val allUsers = userDao.getAllUser().await()

            list = arrayListOf()
            for(data in allUsers.children){
                if(list!!.size >= 15){
                    break
                }
                val model = data.getValue(UserModel::class.java)
                val interactUser : ArrayList<String> = ArrayList()
                val blockedUsersBy : ArrayList<String> = ArrayList()
                if(model?.interact != null){
                    interactUser.addAll(model.interact)
                }
                if(model?.blockedUsers != null){
                    blockedUsersBy.addAll(model.blockedUsers!!)
                }
                if(!blockedUsersByMain.contains(model?.uid)
                    && !interact.contains(model?.uid)
                    && !interactUser.contains(FirebaseAuth.getInstance().currentUser!!.uid)
                    && model?.uid != FirebaseAuth.getInstance().currentUser!!.uid
                    && !blockedUsersBy.contains(FirebaseAuth.getInstance().currentUser!!.uid)){

                    when(genderCheck){
                        1->{
                            if(model?.gender == "Man"){
                                when(distanceCheck){
                                    0->{
                                        list!!.add(model)
                                    }
                                    1->{
                                        if(model.city == currUser.city){
                                            list!!.add(model)
                                        }
                                    }
                                }
                            }
                        }
                        2->{
                            if(model?.gender == "Woman"){
                                when(distanceCheck){
                                    0->{
                                        list!!.add(model)
                                    }
                                    1->{
                                        if(model.city == currUser.city){
                                            list!!.add(model)
                                        }
                                    }
                                }
                            }
                        }
                        3->{
                            when(distanceCheck){
                                0->{
                                    list!!.add(model!!)
                                }
                                1->{
                                    if(model?.city == currUser.city){
                                        list!!.add(model!!)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            withContext(Dispatchers.Main){
                init()

                binding.cardStackView.layoutManager = manager
                binding.cardStackView.itemAnimator = DefaultItemAnimator()
                binding.cardStackView.adapter = adapter

                adapter.updateList(list!!)
                Config.hideDialog()
            }
        }
    }

    override fun add() {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Right)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager.setSwipeAnimationSetting(setting)
        //binding.cardStackView.swipe()
    }

    override fun remove() {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Left)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager.setSwipeAnimationSetting(setting)
        //binding.cardStackView.swipe()
    }

}