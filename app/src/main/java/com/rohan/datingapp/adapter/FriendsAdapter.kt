package com.rohan.datingapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rohan.datingapp.activity.ShowUserActivity
import com.rohan.datingapp.databinding.ShowFriendsLayoutBinding
import com.rohan.datingapp.model.UserModel

class FriendsAdapter(val context: Context) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>()  {

    private val list = ArrayList<UserModel>()

    inner class FriendsViewHolder(val binding: ShowFriendsLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder(ShowFriendsLayoutBinding.inflate(LayoutInflater.from(context) , parent , false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val currentItem = list[position]

        Glide.with(context).load(currentItem.image).into(holder.binding.userImage)
        holder.binding.userName.text = "${currentItem.name.toString().trim()}, ${currentItem.age.toString().trim()}"

        holder.binding.userImage.setOnClickListener {
            val intent = Intent(context , ShowUserActivity::class.java)
            intent.putExtra("uid" , currentItem.uid)
            intent.putExtra("check" , 1)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList:ArrayList<UserModel>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}