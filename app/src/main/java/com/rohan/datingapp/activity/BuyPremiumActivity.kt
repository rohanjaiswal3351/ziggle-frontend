package com.rohan.datingapp.activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.adapty.Adapty
import com.adapty.models.AdaptyPaywallProduct
import com.adapty.utils.AdaptyResult
import com.rohan.datingapp.databinding.ActivityBuyPremiumBinding
import com.rohan.datingapp.utils.Config

class BuyPremiumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyPremiumBinding

    private lateinit var list: MutableList<AdaptyPaywallProduct>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("PREFS" , 0)

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
                        editor.putInt("premium" , 1)
                        editor.apply()
                    }else{
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putInt("premium" , 0)
                        editor.apply()
                    }
                }
                is AdaptyResult.Error -> {
                    //val error = result.error
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.threeMonthBtn.setOnClickListener {
            purchase(list[1])
        }

        binding.monthBtn.setOnClickListener {
            purchase(list[2])
        }

        binding.weekBtn.setOnClickListener {
            purchase(list[0])
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
                                for(product in products){
                                    list.add(product)
                                }

                                binding.oneWeekTxt.text = list[0].price.localizedString
                                binding.oneMonthTxt.text = list[2].price.localizedString
                                binding.threemonthTxt.text = list[1].price.localizedString
                                Config.hideDialog()
                            }
                            is AdaptyResult.Error -> {
                                //val error = result1.error
                                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                                Config.hideDialog()
                            }
                        }
                    }
                }
                is AdaptyResult.Error -> {
                    //val error = result0.error
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                    Config.hideDialog()
                }
            }
        }

    }

    private fun purchase(adaptyPaywallProduct: AdaptyPaywallProduct) {
        Adapty.makePurchase(this, adaptyPaywallProduct) { result ->
            when (result) {
                is AdaptyResult.Success -> {
                    //val profile = result.value
                    Toast.makeText(this, "You are a premium user now", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "It will take some time to see changes.", Toast.LENGTH_LONG).show()
                }
                is AdaptyResult.Error -> {
                    //val error = result.error
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}