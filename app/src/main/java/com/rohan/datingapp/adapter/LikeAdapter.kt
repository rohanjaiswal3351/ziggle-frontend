package com.rohan.datingapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.activity.ShowUserActivity
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.databinding.ItemLikeLayoutBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.notification.ApiUtilities
import com.rohan.datingapp.notification.NotificationModel
import com.rohan.datingapp.notification.PushNotificationModel
import com.rohan.datingapp.utils.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikeAdapter(val context: Context) : RecyclerView.Adapter<LikeAdapter.LikeViewHolder>()  {

    private val list = ArrayList<UserModel>()

    inner class LikeViewHolder(val binding: ItemLikeLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
        return LikeViewHolder(ItemLikeLayoutBinding.inflate(LayoutInflater.from(context) , parent , false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        val currentItem = list[position]

        Glide.with(context).load(currentItem.image).into(holder.binding.userImage)
        holder.binding.userName.text = "${currentItem.name.toString().trim()}, ${currentItem.age.toString().trim()}"

        holder.binding.userImage.setOnClickListener {
            val intent = Intent(context , ShowUserActivity::class.java)
            intent.putExtra("uid" , currentItem.uid)
            intent.putExtra("check" , 0)
            context.startActivity(intent)
        }

        holder.binding.no.setOnClickListener {
            Config.showDialog(context)
            val userDao = UserDao()
            userDao.updateSwipeRightBy(FirebaseAuth.getInstance().currentUser!!.uid , 1 , currentItem.uid.toString())
            val newList: ArrayList<UserModel> = ArrayList()
            newList.addAll(list)
            newList.remove(currentItem)
            updateList(newList)
            Config.hideDialog()
        }

        holder.binding.yes.setOnClickListener {
            Config.showDialog(context)
            GlobalScope.launch {
                val userDao = UserDao()
                val user1 = userDao.getUserById(currentItem.uid.toString()).await().getValue(UserModel::class.java)!!
                val user2 = userDao.getUserById(FirebaseAuth.getInstance().currentUser!!.uid).await().getValue(UserModel::class.java)!!
                userDao.updateSwipeRightByAndMatches(FirebaseAuth.getInstance().currentUser!!.uid, currentItem.uid.toString())
                userDao.updateMatches(currentItem.uid.toString() , 0 , FirebaseAuth.getInstance().currentUser!!.uid)

                withContext(Dispatchers.Main){
                    val newList: ArrayList<UserModel> = ArrayList()
                    newList.addAll(list)
                    newList.remove(currentItem)
                    updateList(newList)
                    val notificationData = PushNotificationModel(
                        NotificationModel("${user2.name} accepted your friend request!", ""),
                        user1.fcmToken)

                    ApiUtilities.getInstance().sendNotification(
                        notificationData
                    ).enqueue(object: Callback<PushNotificationModel> {
                        override fun onResponse(
                            call: Call<PushNotificationModel>,
                            response: Response<PushNotificationModel>
                        ) {
                        }

                        override fun onFailure(call: Call<PushNotificationModel>, t: Throwable) {
                        }

                    })
                    Config.hideDialog()
                    Toast.makeText(context, "You both are friends now go to message segment to text"
                        , Toast.LENGTH_LONG).show()
                }
            }
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