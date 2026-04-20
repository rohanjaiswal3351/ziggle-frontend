package com.rohan.datingapp.ui

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
import com.rohan.datingapp.R
import com.rohan.datingapp.activity.BuyPremiumActivity
import com.rohan.datingapp.adapter.DatingAdapter
import com.rohan.datingapp.adapter.DatingAdapterInterface
import com.rohan.datingapp.databinding.FragmentDatingBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.utils.Config
import com.rohan.datingapp.viewModel.DatingViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting
import kotlinx.coroutines.launch

class DatingFragment : Fragment(), DatingAdapterInterface {

    private lateinit var binding: FragmentDatingBinding
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: DatingAdapter
    private lateinit var mContext: Context
    private lateinit var settings: SharedPreferences

    private val viewModel: DatingViewModel by viewModels()

    private var genderCheck = 3
    private var distanceCheck = 0
    private var isPremium = 0
    private var countSwipe = 0

    companion object {
        var list: ArrayList<UserModel>? = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDatingBinding.inflate(layoutInflater)
        settings = requireActivity().getSharedPreferences("PREFS", 0)

        genderCheck = settings.getInt("gender", 3)
        distanceCheck = settings.getInt("distance", 0)
        isPremium = settings.getInt("premium", 0)
        countSwipe = settings.getInt("count", 0)

        adapter = DatingAdapter(mContext, this)
        setupCardStack()
        setupClickListeners()
        observeViewModel()

        viewModel.fetchUsers(genderCheck, distanceCheck)

        return binding.root
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { loading ->
                if (loading) Config.showDialog(mContext) else Config.hideDialog()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.users.collect { users ->
                if (users.isNotEmpty()) {
                    list = ArrayList(users)
                    adapter.updateList(list!!)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { msg ->
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCardStack() {
        manager = CardStackLayoutManager(mContext, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {}
            override fun onCardRewound() {}
            override fun onCardCanceled() {}
            override fun onCardAppeared(view: View?, position: Int) {}
            override fun onCardDisappeared(view: View?, position: Int) {}

            override fun onCardSwiped(direction: Direction?) {
                countSwipe++
                settings.edit().putInt("count", countSwipe).apply()

                val swiped = list!![manager.topPosition - 1]
                viewModel.onSwiped(swiped.uid!!, direction!!.name, swiped.fcmToken)

                if (manager.topPosition == list!!.size) {
                    viewModel.fetchUsers(genderCheck, distanceCheck)
                }

                if (countSwipe >= 12) {
                    countSwipe = 0
                    settings.edit().putInt("count", 0).apply()
                }
            }
        })

        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.6f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setSwipeThreshold(0.7f)
        manager.setCanScrollVertical(false)

        binding.cardStackView.layoutManager = manager
        binding.cardStackView.itemAnimator = DefaultItemAnimator()
        binding.cardStackView.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.info.setOnClickListener { showInfoDialog() }
        binding.filterBtn.setOnClickListener { showFilterDialog() }
        binding.premiumBtn.setOnClickListener { startActivity(Intent(mContext, BuyPremiumActivity::class.java)) }
        binding.rewind.setOnClickListener {
            if (isPremium == 0) {
                showRewindDialog()
            } else if (viewModel.lastUserUid.isNotEmpty()) {
                viewModel.rewind()
                viewModel.fetchUsers(genderCheck, distanceCheck)
            } else {
                Toast.makeText(mContext, "Please do at least one swipe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showInfoDialog() {
        val dialog = Dialog(mContext)
        dialog.setContentView(R.layout.info_dialog)
        dialog.findViewById<Button>(R.id.ok).setOnClickListener { dialog.dismiss() }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showFilterDialog() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.filter_dialog)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)

        val guysBtn = dialog.findViewById<CardView>(R.id.guys)
        val girlsBtn = dialog.findViewById<CardView>(R.id.girls)
        val allBtn = dialog.findViewById<CardView>(R.id.all)
        val countryBtn = dialog.findViewById<CardView>(R.id.country)
        val globalBtn = dialog.findViewById<CardView>(R.id.global)

        val active = mContext.resources.getColor(R.color.main_background)
        val inactive = mContext.resources.getColor(R.color.bg_light_grey)

        fun highlightGender(selected: CardView) {
            listOf(guysBtn, girlsBtn, allBtn).forEach { it.setCardBackgroundColor(inactive) }
            selected.setCardBackgroundColor(active)
        }
        fun highlightDistance(selected: CardView) {
            listOf(countryBtn, globalBtn).forEach { it.setCardBackgroundColor(inactive) }
            selected.setCardBackgroundColor(active)
        }

        when (genderCheck) { 1 -> highlightGender(guysBtn); 2 -> highlightGender(girlsBtn); 3 -> highlightGender(allBtn) }
        when (distanceCheck) { 0 -> highlightDistance(globalBtn); 1 -> highlightDistance(countryBtn) }

        val editor = settings.edit()
        guysBtn.setOnClickListener { genderCheck = 1; editor.putInt("gender", 1).apply(); highlightGender(guysBtn) }
        girlsBtn.setOnClickListener { genderCheck = 2; editor.putInt("gender", 2).apply(); highlightGender(girlsBtn) }
        allBtn.setOnClickListener { genderCheck = 3; editor.putInt("gender", 3).apply(); highlightGender(allBtn) }
        countryBtn.setOnClickListener { distanceCheck = 1; editor.putInt("distance", 1).apply(); highlightDistance(countryBtn) }
        globalBtn.setOnClickListener { distanceCheck = 0; editor.putInt("distance", 0).apply(); highlightDistance(globalBtn) }

        dialog.setOnDismissListener {
            viewModel.resetPagination()
            viewModel.fetchUsers(genderCheck, distanceCheck)
        }
        dialog.show()
    }

    private fun showRewindDialog() {
        val dialog = Dialog(mContext)
        dialog.setContentView(R.layout.rewind_dialog)
        dialog.findViewById<Button>(R.id.buyBtn).setOnClickListener { startActivity(Intent(mContext, BuyPremiumActivity::class.java)) }
        dialog.findViewById<TextView>(R.id.cancel).setOnClickListener { dialog.dismiss() }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun add() {
        manager.setSwipeAnimationSetting(
            SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
        )
    }

    override fun remove() {
        manager.setSwipeAnimationSetting(
            SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
        )
    }
}
