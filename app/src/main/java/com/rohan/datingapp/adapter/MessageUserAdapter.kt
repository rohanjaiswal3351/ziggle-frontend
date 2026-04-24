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
    : RecyclerView.Adapter<MessageUserAdapter.MessageUserViewHolder>() {

    // Cache user data so we don't re-fetch on every scroll
    private val userCache = HashMap<String, UserModel>()

    inner class MessageUserViewHolder(val binding: UserItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        var rootListener: ValueEventListener? = null
        var chatListener: ValueEventListener? = null
        var chatRef: com.google.firebase.database.DatabaseReference? = null
        var rootRef: com.google.firebase.database.DatabaseReference? = null

        fun clearListeners() {
            chatListener?.let { chatRef?.removeEventListener(it) }
            rootListener?.let { rootRef?.removeEventListener(it) }
            rootListener = null
            chatListener = null
            chatRef = null
            rootRef = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageUserViewHolder {
        return MessageUserViewHolder(UserItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: MessageUserViewHolder, position: Int) {
        // Remove old listeners before rebinding
        holder.clearListeners()

        val uid = list[position]

        fun bindUser(data: UserModel) {
            Glide.with(context).load(data.image).into(holder.binding.userImage)
            holder.binding.userName.text = data.name.toString().trim()

            val senderId = FirebaseAuth.getInstance().currentUser!!.uid
            val chatId = senderId + data.uid
            val reverseChatId = data.uid + senderId
            val chatsRef = FirebaseDatabase.getInstance().getReference("chats")

            val rootListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val activeChatId = when {
                        snapshot.hasChild(chatId) -> chatId
                        snapshot.hasChild(reverseChatId) -> reverseChatId
                        else -> return
                    }
                    val activeChatRef = chatsRef.child(activeChatId)
                    holder.chatRef = activeChatRef

                    val chatListener = object : ValueEventListener {
                        @SuppressLint("SetTextI18n")
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists() || snapshot.childrenCount == 0L) return

                            var count = 0
                            for (currData in snapshot.children.reversed()) {
                                val currMsg = currData.getValue(MessageModel::class.java)
                                if (currMsg?.isSeen?.isNotEmpty() == true) {
                                    if (currMsg.isSeen == "false" && currMsg.senderId == data.uid) count++
                                    else break
                                } else break
                            }

                            if (count > 0) {
                                holder.binding.newMsgLayout.visibility = View.VISIBLE
                                holder.binding.countMsg.text = "$count"
                            } else {
                                holder.binding.newMsgLayout.visibility = View.GONE
                            }

                            val lastMsg = snapshot.children.last().getValue(MessageModel::class.java)
                            holder.binding.lastMessage.text =
                                if (lastMsg?.message?.isNotEmpty() == true) lastMsg.message else "Photo"
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                    holder.chatListener = chatListener
                    activeChatRef.addValueEventListener(chatListener)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            holder.rootRef = chatsRef
            holder.rootListener = rootListener
            chatsRef.addValueEventListener(rootListener)

            holder.itemView.setOnClickListener {
                val intent = Intent(context, MessageActivity::class.java)
                intent.putExtra("userid", uid)
                intent.putExtra("name", data.name)
                intent.putExtra("image", data.image)
                intent.putExtra("fcm", data.fcmToken)
                intent.putExtra("yourName", yourName)
                context.startActivity(intent)
            }
        }

        val cached = userCache[uid]
        if (cached != null) {
            bindUser(cached)
        } else {
            GlobalScope.launch {
                val snapshot: DataSnapshot = UserDao().getUserById(uid).await()
                withContext(Dispatchers.Main) {
                    if (snapshot.exists()) {
                        val data = snapshot.getValue(UserModel::class.java) ?: return@withContext
                        userCache[uid] = data
                        bindUser(data)
                    }
                }
            }
        }
    }

    override fun onViewRecycled(holder: MessageUserViewHolder) {
        super.onViewRecycled(holder)
        holder.clearListeners()
    }

    override fun getItemCount() = list.size
}
