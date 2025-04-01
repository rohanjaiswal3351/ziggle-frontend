package com.rohan.datingapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.R
import com.rohan.datingapp.activity.ShowImageActivity
import com.rohan.datingapp.daos.MessageDao
import com.rohan.datingapp.model.MessageModel

class MessageAdapter(val context: Context , val list: List<MessageModel>)
    :RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){

    val MSG_TYPE_RIGHT = 0
    val MSG_TYPE_LEFT = 1

    inner class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val text = itemView.findViewById<TextView>(R.id.messageText)
        val image = itemView.findViewById<ImageView>(R.id.imageView)
        val imageLayout = itemView.findViewById<CardView>(R.id.imageLayout)
    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position].senderId == FirebaseAuth.getInstance().currentUser!!.uid){
            MSG_TYPE_RIGHT
        }else MSG_TYPE_LEFT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if(viewType == MSG_TYPE_RIGHT){
            MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_receiver_message ,parent,false))
        }else{
            MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_sender_message ,parent,false))
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val currItem = list[position]

        if(currItem.isSeen?.isNotEmpty() == true){
            if(currItem.isSeen == "false" && currItem.senderId != FirebaseAuth.getInstance().currentUser!!.uid){
                val messageDao = MessageDao()
                messageDao.updateIsSeen(currItem.chatId!!, currItem.key!!)
            }
        }

        if(currItem.media?.isNotEmpty() == true){
            holder.imageLayout.visibility = View.VISIBLE
            Glide.with(context).load(currItem.media).into(holder.image)

            holder.imageLayout.setOnClickListener {
                val intent = Intent(context , ShowImageActivity::class.java)
                intent.putExtra("url" , currItem.media)
                context.startActivity(intent)
            }
        }else{
            holder.text.visibility = View.VISIBLE
            holder.text.text = list[position].message
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}