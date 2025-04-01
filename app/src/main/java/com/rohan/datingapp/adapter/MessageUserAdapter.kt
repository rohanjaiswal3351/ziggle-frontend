package com.rohan.datingapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.activity.MessageActivity
import com.rohan.datingapp.databinding.UserItemLayoutBinding
import com.rohan.datingapp.model.MessageModel
import com.rohan.datingapp.model.UserModel
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class MessageUserAdapter(val context: Context, private var list: ArrayList<String>, private val yourName: String)
    :RecyclerView.Adapter<MessageUserAdapter.MessageUserViewHolder>(){

    inner class MessageUserViewHolder(val binding: UserItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageUserViewHolder {
        return MessageUserViewHolder(UserItemLayoutBinding.inflate(LayoutInflater.from(context) , parent , false))
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: MessageUserViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        var name = ""
        var image = ""
        var fcmToken = ""

        GlobalScope.launch {
            val userDao = UserDao()
            //val data: UserModel? = userDao.getUserById(list[position]).await().getValue(UserModel::class.java)
            val snapshot: DataSnapshot = userDao.getUserById(list[position]).await()

            withContext(Dispatchers.Main){
                if(snapshot.exists()){
                    val data = snapshot.getValue(UserModel::class.java)
                    Glide.with(context).load(data?.image).into(holder.binding.userImage)
                    holder.binding.userName.text = data?.name.toString().trim()
                    name = data?.name!!
                    image = data.image!!
                    fcmToken = data.fcmToken.toString()

                    val senderId = FirebaseAuth.getInstance().currentUser!!.uid
                    val chatId: String = senderId + data.uid
                    val reverseChatId: String = data.uid + senderId

                    val reference = FirebaseDatabase.getInstance().getReference("chats")

                    reference.addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            if(snapshot.hasChild(chatId)){
                                FirebaseDatabase.getInstance().getReference("chats")
                                    .child(chatId).addValueEventListener(object: ValueEventListener{
                                        @SuppressLint("SetTextI18n")
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            var count = 0

                                            for(currData in snapshot.children.reversed()){
                                                val currMsg = currData.getValue(MessageModel::class.java)
                                                if(currMsg?.isSeen?.isNotEmpty() == true){
                                                    if(currMsg.isSeen == "false" && currMsg.senderId == data.uid){
                                                        count++
                                                    }else{
                                                        break
                                                    }
                                                }else{
                                                    break
                                                }
                                            }

                                            if(count > 0){
                                                holder.binding.newMsgLayout.visibility = View.VISIBLE
                                                holder.binding.countMsg.text = "$count"
                                            }

                                            val lastMsg = snapshot.children.last().getValue(MessageModel::class.java)
                                            if(lastMsg?.message?.isNotEmpty() == true){
                                                holder.binding.lastMessage.text = lastMsg.message
                                            }else{
                                                holder.binding.lastMessage.text = "Photo"
                                            }

                                        }
                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(context , "something went wrong", Toast.LENGTH_SHORT).show()
                                        }

                                    })
                            }else if(snapshot.hasChild(reverseChatId)){
                                FirebaseDatabase.getInstance().getReference("chats")
                                    .child(reverseChatId).addValueEventListener(object: ValueEventListener{
                                        @SuppressLint("SetTextI18n")
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            var count = 0

                                            for(currData in snapshot.children.reversed()){
                                                val currMsg = currData.getValue(MessageModel::class.java)
                                                if(currMsg?.isSeen?.isNotEmpty() == true){
                                                    if(currMsg.isSeen == "false" && currMsg.senderId == data.uid){
                                                        count++
                                                    }else{
                                                        break
                                                    }
                                                }else{
                                                    break
                                                }
                                            }

                                            if(count > 0){
                                                holder.binding.newMsgLayout.visibility = View.VISIBLE
                                                holder.binding.countMsg.text = "$count"
                                            }

                                            val lastMsg = snapshot.children.last().getValue(MessageModel::class.java)
                                            if(lastMsg?.message?.isNotEmpty() == true){
                                                holder.binding.lastMessage.text = lastMsg.message
                                            }else{
                                                holder.binding.lastMessage.text = "Photo"
                                            }

                                        }
                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(context , "something went wrong", Toast.LENGTH_SHORT).show()
                                        }

                                    })
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(context , "something went wrong", Toast.LENGTH_SHORT).show()

                        }
                    })
                }

            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context , MessageActivity::class.java)
            intent.putExtra("userid" , list[position])
            intent.putExtra("name" , name)
            intent.putExtra("image" , image)
            intent.putExtra("fcm" , fcmToken)
            intent.putExtra("yourName" , yourName)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}