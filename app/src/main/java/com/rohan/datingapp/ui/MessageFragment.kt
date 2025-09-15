package com.rohan.datingapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.MainActivity
import com.rohan.datingapp.adapter.FriendsAdapter
import com.rohan.datingapp.adapter.LikeAdapter
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.adapter.MessageUserAdapter
import com.rohan.datingapp.databinding.FragmentMessageBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.utils.Config
import com.rohan.datingapp.viewModel.UserViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.tasks.await
import java.util.Objects
import kotlin.getValue

class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding
    private lateinit var list: ArrayList<String>
    private lateinit var mContext: Context
    private lateinit var adapter: MessageUserAdapter
    private lateinit var yourName: String
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(layoutInflater)

        list = ArrayList()


        //adapter = MessageUserAdapter(mContext, list, "Name")
        setUpViewModel()
        getUser()

        binding.start.setOnClickListener {
            startActivity(Intent(mContext , MainActivity::class.java))
            activity?.finish()
        }

        return binding.root
    }

    private fun setUpViewModel() {
        lifecycleScope.launch {
            userViewModel.user.collectLatest { user ->
                if(Objects.nonNull(user) && user.uid?.isNotEmpty() == true){
                    list.clear()
                    list.addAll(user.matches!!)
                    list.remove(user.uid)
                    yourName = user.name!!

                    if(list.isEmpty()){
                        binding.empty.visibility = View.VISIBLE
                    }

                    try {
                        adapter = MessageUserAdapter(
                            mContext,
                            list,
                            yourName
                        )
                        binding.recyclerView.adapter = adapter
                    }
                    catch (e: Exception){
                        Log.e("TAG", "setUpViewModel: ${e.message}")
                    }

                    Config.hideDialog()
                }
            }
        }

        lifecycleScope.launch {
            userViewModel.errorMessage.collectLatest { error ->
                error?.let {
                    println("Error: $it")
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun getUser() {
        Config.showDialog(mContext)

        userViewModel.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)

//        GlobalScope.launch {
//            val userDao = UserDao()
//            val currData: UserModel = userDao.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
//                .await().getValue(UserModel::class.java)!!
//
//            withContext(Dispatchers.Main){
//                list.clear()
//                list.addAll(currData.matches!!)
//                list.remove(FirebaseAuth.getInstance().currentUser!!.uid)
//                yourName = currData.name!!
//
//                if (list.size == 0){
//                    binding.empty.visibility = View.VISIBLE
//                }
//
//                try {
//                    adapter = MessageUserAdapter(
//                        mContext,
//                        list,
//                        yourName
//                    )
//                    binding.recyclerView.adapter = adapter
//                    Config.hideDialog()
//                }
//                catch (e: Exception){
//                    Config.hideDialog()
//                }
//            }
//
//        }
    }

}