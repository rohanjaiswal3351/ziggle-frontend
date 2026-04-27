package com.rohan.datingapp.activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.adapty.Adapty
import com.adapty.models.AdaptyPaywallProduct
import com.adapty.utils.AdaptyResult
import com.rohan.datingapp.R
import com.rohan.datingapp.databinding.ActivityBuyPremiumBinding
import com.rohan.datingapp.utils.Config

class BuyPremiumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyPremiumBinding
    private lateinit var list: MutableList<AdaptyPaywallProduct>
    private lateinit var sharedPreferences: SharedPreferences
    private var selectedPlan = -1 // Track selected plan: 0=week, 1=3months, 2=month

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("PREFS", 0)
        list = ArrayList()

        binding.back.setOnClickListener {
            finish()
        }

        loadAdapty()

        Adapty.getProfile { result ->
            when (result) {
                is AdaptyResult.Success -> {
                    val profile = result.value
                    if (profile.accessLevels["premium"]?.isActive == true) {
                        Toast.makeText(this, "You are Already a premium user.", Toast.LENGTH_SHORT).show()
                        binding.threeMonthBtn.visibility = View.GONE
                        binding.monthBtn.visibility = View.GONE
                        binding.weekBtn.visibility = View.GONE
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putInt("premium", 1)
                        editor.apply()
                    } else {
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putInt("premium", 0)
                        editor.apply()
                    }
                }
                is AdaptyResult.Error -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Set default selection to 3-month plan (best value)
        selectPlan(1)

        binding.threeMonthBtn.setOnClickListener {
            selectPlan(1)
        }

        binding.monthBtn.setOnClickListener {
            selectPlan(2)
        }

        binding.weekBtn.setOnClickListener {
            selectPlan(0)
        }

        binding.purchaseBtn.setOnClickListener {
            when (selectedPlan) {
                0 -> if (list.isNotEmpty()) purchase(list[0])
                1 -> if (list.size > 1) purchase(list[1])
                2 -> if (list.size > 2) purchase(list[2])
                else -> Toast.makeText(this, "Please select a plan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectPlan(plan: Int) {
        selectedPlan = plan
        
        // Reset all cards to default state
        binding.threeMonthBtn.strokeColor = ContextCompat.getColor(this, R.color.bg_light_grey)
        binding.monthBtn.strokeColor = ContextCompat.getColor(this, R.color.bg_light_grey)
        binding.weekBtn.strokeColor = ContextCompat.getColor(this, R.color.bg_light_grey)
        
        // Highlight selected card
        when (plan) {
            0 -> binding.weekBtn.strokeColor = ContextCompat.getColor(this, R.color.colorPrimary)
            1 -> binding.threeMonthBtn.strokeColor = ContextCompat.getColor(this, R.color.colorPrimary)
            2 -> binding.monthBtn.strokeColor = ContextCompat.getColor(this, R.color.colorPrimary)
        }
    }

    private fun loadAdapty() {
        Config.showDialog(this)
        Adapty.activate(this, com.adapty.models.AdaptyConfig.Builder("public_live_f1pOqQnH.In6h67XuImRZwtMs3J2w").build())

        Adapty.getPaywall("zigglepaywall", locale = "en") { result0 ->
            when (result0) {
                is AdaptyResult.Success -> {
                    val paywall = result0.value
                    Adapty.getPaywallProducts(paywall) { result1 ->
                        when (result1) {
                            is AdaptyResult.Success -> {
                                val products = result1.value
                                for (product in products) {
                                    list.add(product)
                                }

                                if (list.size >= 3) {
                                    binding.oneWeekTxt.text = list[0].price.localizedString
                                    binding.oneMonthTxt.text = list[2].price.localizedString
                                    binding.threemonthTxt.text = list[1].price.localizedString
                                }
                                Config.hideDialog()
                            }
                            is AdaptyResult.Error -> {
                                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                                Config.hideDialog()
                            }
                        }
                    }
                }
                is AdaptyResult.Error -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Config.hideDialog()
                }
            }
        }
    }

    private fun purchase(adaptyPaywallProduct: AdaptyPaywallProduct) {
        Adapty.makePurchase(this, adaptyPaywallProduct) { result ->
            when (result) {
                is AdaptyResult.Success -> {
                    // Verify the purchase by checking profile after successful purchase
                    Adapty.getProfile { profileResult ->
                        when (profileResult) {
                            is AdaptyResult.Success -> {
                                val profile = profileResult.value
                                if (profile.accessLevels["premium"]?.isActive == true) {
                                    Toast.makeText(this, "You are a premium user now!", Toast.LENGTH_SHORT).show()
                                    Toast.makeText(this, "It will take some time to see changes.", Toast.LENGTH_LONG).show()
                                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                    editor.putInt("premium", 1)
                                    editor.apply()
                                    finish()
                                } else {
                                    Toast.makeText(this, "Purchase was not completed.", Toast.LENGTH_SHORT).show()
                                }
                            }
                            is AdaptyResult.Error -> {
                                Toast.makeText(this, "Purchase verification failed.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                is AdaptyResult.Error -> {
                    val error = result.error
                    // Check if it's a user cancellation - handle silently
                    val errorMessage = error.message?.lowercase() ?: ""
                    if (errorMessage.contains("cancel") || errorMessage.contains("user")) {
                        // User cancelled, don't show any message
                        return@makePurchase
                    }
                    Toast.makeText(this, "Purchase failed. Please try again.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}