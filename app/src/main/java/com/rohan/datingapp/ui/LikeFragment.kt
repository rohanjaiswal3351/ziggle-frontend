package com.rohan.datingapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.MainActivity
import com.rohan.datingapp.activity.BuyPremiumActivity
import com.rohan.datingapp.adapter.FriendsAdapter
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.adapter.LikeAdapter
import com.rohan.datingapp.databinding.FragmentLikeBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.utils.Config
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class LikeFragment : Fragment() {

    private lateinit var binding: FragmentLikeBinding
    private lateinit var list:ArrayList<String>
    private lateinit var friendList:ArrayList<String>
    private lateinit var friendAdapterList:ArrayList<UserModel>
    private lateinit var adapterList:ArrayList<UserModel>
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLikeBinding.inflate(layoutInflater)
        list = ArrayList()
        friendList = ArrayList()

        binding.start.setOnClickListener {
            startActivity(Intent(mContext , MainActivity::class.java))
            activity?.finish()
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onResume() {
        super.onResume()
        if (::binding.isInitialized) {
            list = ArrayList()
            friendList = ArrayList()
            loadData()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        Config.showDialog(mContext)

        GlobalScope.launch {
            val userDao = UserDao()
            val data : UserModel = userDao.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                .await().getValue(UserModel::class.java)!!

            withContext(Dispatchers.Main){
                list.clear()
                friendList.clear()
                for(user in data.rightSwipeBy!!){
                    if(user != FirebaseAuth.getInstance().currentUser!!.uid){
                        list.add(user)
                    }
                }

                for(user in data.matches!!){
                    if(user != FirebaseAuth.getInstance().currentUser!!.uid){
                        friendList.add(user)
                    }
                }

                if(friendList.isNotEmpty()){
                    binding.text1.visibility = View.VISIBLE
                }

                if(list.isNotEmpty()){
                    binding.empty.visibility = View.GONE
                    binding.text2.visibility = View.VISIBLE
                    getData()
                }else{
                    binding.empty.visibility = View.VISIBLE
                    Config.hideDialog()
                }


            }

        }

    }



    @OptIn(DelicateCoroutinesApi::class)
    private fun getData() {

        GlobalScope.launch {
            val userDao = UserDao()
            val allUsers = userDao.getAllUser().await()

            withContext(Dispatchers.Main){
                if(allUsers.exists()){
                    adapterList = ArrayList()
                    friendAdapterList = ArrayList()
                    for(data in allUsers.children){
                        val model = data.getValue(UserModel::class.java)
                        if(list.contains(model?.uid)){
                            adapterList.add(model!!)
                        }
                        if(friendList.contains(model?.uid)){
                            friendAdapterList.add(model!!)
                        }
                    }

                    val friendAdapter = FriendsAdapter(mContext)
                    binding.friendRecyclerView.layoutManager = LinearLayoutManager(mContext , LinearLayoutManager.HORIZONTAL, false)
                    binding.friendRecyclerView.adapter = friendAdapter
                    friendAdapter.updateList(friendAdapterList)

                    val isPremium = mContext.getSharedPreferences("PREFS", 0)
                        .getInt("premium", 0) == 1
                    val adapter = LikeAdapter(mContext, isPremium)
                    binding.recyclerView.layoutManager = LinearLayoutManager(mContext)
                    binding.recyclerView.adapter = adapter
                    adapter.updateList(adapterList)

                    if (!isPremium) {
                        binding.upgradeBtnContainer.visibility = View.VISIBLE
                        binding.upgradeBtn.setOnClickListener {
                            startActivity(Intent(mContext, BuyPremiumActivity::class.java))
                        }
                    }
                    Config.hideDialog()
                }else{
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Config.hideDialog()
                }
            }
        }
    }

}