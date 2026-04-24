package com.rohan.datingapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.MainActivity
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.adapter.MessageUserAdapter
import com.rohan.datingapp.databinding.FragmentMessageBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.utils.Config
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding
    private lateinit var list: ArrayList<String>
    private lateinit var mContext: Context
    private lateinit var adapter: MessageUserAdapter
    private lateinit var yourName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessageBinding.inflate(layoutInflater)

        list = ArrayList()

        getUser()

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

    private fun getUser() {
        Config.showDialog(mContext)

        GlobalScope.launch {
            val userDao = UserDao()
            val currData: UserModel = userDao.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
                .await().getValue(UserModel::class.java)!!

            withContext(Dispatchers.Main){
                list.clear()
                list.addAll(currData.matches!!)
                list.remove(FirebaseAuth.getInstance().currentUser!!.uid)
                yourName = currData.name!!

                if (list.size == 0){
                    binding.empty.visibility = View.VISIBLE
                }

                try {
                    adapter = MessageUserAdapter(
                        mContext,
                        list,
                        yourName
                    )
                    binding.recyclerView.adapter = adapter
                    Config.hideDialog()
                }
                catch (e: Exception){
                    Config.hideDialog()
                }
            }

        }
    }

}