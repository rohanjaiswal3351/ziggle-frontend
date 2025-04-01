package com.rohan.datingapp.daos

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.rohan.datingapp.model.MessageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MessageDao {

    private val db = FirebaseDatabase.getInstance()
    private val usersCollection = db.getReference("chats")

    fun addNewMessageRoom(messageModel: MessageModel?){
        messageModel?.let{
            GlobalScope.launch(Dispatchers.IO) {
                messageModel.key = usersCollection.push().key
                usersCollection.child(messageModel.chatId!!).child(messageModel.key!!).setValue(messageModel)
            }
        }
    }

    private fun getMessageChatRoomById(chatId: String, key: String): Task<DataSnapshot> {
        return usersCollection.child(chatId).child(key).get()
    }

    fun updateIsSeen(chatId: String, key: String){
        GlobalScope.launch {
            val message = getMessageChatRoomById(chatId, key).await().getValue(MessageModel::class.java)
            message!!.isSeen = "true"
            usersCollection.child(chatId).child(key).setValue(message)
        }
    }
}