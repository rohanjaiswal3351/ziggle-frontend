package com.rohan.datingapp.activity

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rohan.datingapp.R
import com.rohan.datingapp.adapter.MessageAdapter
import com.rohan.datingapp.daos.MessageDao
import com.rohan.datingapp.databinding.ActivityMessageBinding
import com.rohan.datingapp.model.MessageModel
import com.rohan.datingapp.notification.ApiUtilities
import com.rohan.datingapp.notification.NotificationModel
import com.rohan.datingapp.notification.PushNotificationModel
import com.rohan.datingapp.utils.Config
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    private lateinit var fcmToken: String
    private lateinit var name: String
    private lateinit var yourName: String
    private var cFFM: Int = 0//checking for first message
    private var receiverId: String? = null

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null){
            uploadImage(it)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadContent()

        binding.back.setOnClickListener {
            finish()
        }

        binding.media.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.yourMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if(editable.toString() == ""){
                    binding.media.visibility = View.VISIBLE
                }else{
                    binding.media.visibility = View.GONE
                }
            }
        })

        binding.send.setOnClickListener {
            if(binding.yourMessage.text!!.isEmpty()){
                Toast.makeText(this , "Please enter your message", Toast.LENGTH_SHORT).show()
            }
            else{
                storeData(binding.yourMessage.text.toString() , 0)
            }
        }
    }

    private fun uploadImage(imageUri: Uri?) {
        Config.showDialog(this)

        val storageReference: StorageReference = FirebaseStorage.getInstance().getReference("chatData")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("$imageUri")

        storageReference.putFile(imageUri!!)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    Config.hideDialog()
                    storeData(it.toString() , 1)
                }.addOnFailureListener {
                    Config.hideDialog()
                    Toast.makeText(this , "something went wrong" , Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Config.hideDialog()
                Toast.makeText(this , "something went wrong" , Toast.LENGTH_SHORT).show()
            }
    }


    private fun loadContent() {
        name = intent.getStringExtra("name").toString().trim()
        yourName = intent.getStringExtra("yourName").toString().trim()
        val image = intent.getStringExtra("image")
        fcmToken = intent.getStringExtra("fcm").toString()
        receiverId = intent.getStringExtra("userid")

        Glide.with(this).load(image).placeholder(R.drawable.person).into(binding.userImage)
        binding.userName.text = name

        checkOnlineStatus()
    }

    private fun checkOnlineStatus() {
        val reference = FirebaseDatabase.getInstance().getReference("status")

        reference.child(receiverId.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val status = snapshot.getValue(String::class.java)
                    if(status == "Online"){
                        binding.userStatus.text = "Online"
                    }else{
                        binding.userStatus.text = "Offline"
                    }
                }
                verifyChatId()
            }

            override fun onCancelled(error: DatabaseError) {
                verifyChatId()
            }

        })
    }

    private var senderId: String? = null
    private var chatId: String? = null
    private fun verifyChatId() {
        senderId = FirebaseAuth.getInstance().currentUser!!.uid

        chatId = senderId + receiverId
        val reverseChatId = receiverId + senderId

        val reference = FirebaseDatabase.getInstance().getReference("chats")

        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.hasChild(chatId!!)){
                    getData(chatId)
                }else if(snapshot.hasChild(reverseChatId)){
                    chatId = reverseChatId
                    getData(chatId)
                }else{
                    cFFM = 1
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MessageActivity , "something went wrong", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun getData(chatId: String?) {
        FirebaseDatabase.getInstance().getReference("chats")
            .child(chatId!!).addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = arrayListOf<MessageModel>()

                    for(show in snapshot.children){
                        list.add(show.getValue(MessageModel::class.java)!!)
                    }

                    list.reverse()

                    binding.recyclerView2.layoutManager = LinearLayoutManager(this@MessageActivity,
                    LinearLayoutManager.VERTICAL, true)
                    binding.recyclerView2.adapter = MessageAdapter(this@MessageActivity , list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MessageActivity , error.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun storeData(msg: String , check: Int) {

        val currentDate: String = SimpleDateFormat("dd-MM-yyyy" , Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("HH:mm a" , Locale.getDefault()).format(Date())


        val messageModel: MessageModel

        if(check == 0){
            messageModel = MessageModel(
                chatId = chatId,
                senderId = senderId,
                message = msg,
                currentTime = currentTime,
                currentDate = currentDate,
                isSeen = "false"
            )
        }else{
            messageModel = MessageModel(
                chatId = chatId,
                senderId = senderId,
                media = msg,
                currentTime = currentTime,
                currentDate = currentDate,
                isSeen = "false"
            )
        }

        val messageDao = MessageDao()
        messageDao.addNewMessageRoom(messageModel)

        binding.yourMessage.text = null
        sendNotification(msg)
        if(cFFM == 1){
            getData(chatId)
            cFFM = 0
        }

    }

    private fun sendNotification(msg: String) {
        val notificationData = PushNotificationModel(NotificationModel("$yourName sent you a message", ""),
            fcmToken)

        ApiUtilities.getInstance().sendNotification(
            notificationData
        ).enqueue(object: Callback<PushNotificationModel>{
            override fun onResponse(
                call: Call<PushNotificationModel>,
                response: Response<PushNotificationModel>
            ) {

            }

            override fun onFailure(call: Call<PushNotificationModel>, t: Throwable) {

            }

        })
    }
}