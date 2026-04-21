package com.rohan.datingapp.daos

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.rohan.datingapp.activity.EditProfileActivity
import com.rohan.datingapp.activity.SettingsActivity
import com.rohan.datingapp.model.UserModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(DelicateCoroutinesApi::class)
class UserDao {

    private val db = FirebaseDatabase.getInstance()
    private val usersCollection = db.getReference("users")

    fun addUser(user:UserModel?){
        user?.let{
            GlobalScope.launch(Dispatchers.IO) {
                usersCollection.child(user.uid!!).setValue(it)
            }
        }
    }

    fun getUserById(uId:String): Task<DataSnapshot> {
        return usersCollection.child(uId).get()
    }

    fun getAllUser(): Task<DataSnapshot> {
        return usersCollection.get()
    }

    fun getUsersPaginated(lastKey: String?, limit: Int): Task<DataSnapshot> {
        val query = if (lastKey == null) {
            usersCollection.orderByKey().limitToFirst(limit)
        } else {
            usersCollection.orderByKey().startAfter(lastKey).limitToFirst(limit)
        }
        return query.get()
    }

    fun updateName(context: Context, uid: String, name: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.name = name
            usersCollection.child(uid).setValue(user).addOnSuccessListener {
                val intent = Intent(context , SettingsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)
                (context as Activity).finish()
            }
        }
    }

    fun updateLikeNotify(uid: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.likeNotify = "No"
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateCity(context: Context, uid: String , city: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.city = city
            usersCollection.child(uid).setValue(user).addOnSuccessListener {
                val intent = Intent(context , EditProfileActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)
                (context as Activity).finish()
            }
        }
    }

    fun updateGender(context: Context, uid: String , gender: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.gender = gender
            usersCollection.child(uid).setValue(user).addOnSuccessListener {
                val intent = Intent(context , EditProfileActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)
                (context as Activity).finish()
            }
        }
    }

    fun updateStar(context: Context, uid: String , star: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.star = star
            usersCollection.child(uid).setValue(user).addOnSuccessListener {
                val intent = Intent(context , EditProfileActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)
                (context as Activity).finish()
            }
        }
    }

    fun updateImage(uid: String , img: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.image = img
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateImage1(uid: String , img: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.image1 = img
            usersCollection.child(uid).setValue(user)
        }
    }

    fun deleteImage1(uid: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.image1 = null
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateImage2(uid: String , img: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.image2 = img
            usersCollection.child(uid).setValue(user)
        }
    }

    fun deleteImage2(uid: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.image2 = null
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateImage3(uid: String , img: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.image3 = img
            usersCollection.child(uid).setValue(user)
        }
    }

    fun deleteImage3(uid: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.image3 = null
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateAge(context: Context, uid: String , age: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.age = age
            usersCollection.child(uid).setValue(user).addOnSuccessListener {
                val intent = Intent(context , SettingsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)
                (context as Activity).finish()
            }
        }
    }

    fun updateBio(uid: String , bio: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.bio = bio
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateSwipeRightBy(uid: String, check: Int, updateUid: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            if(check == 0){
                user!!.rightSwipeBy!!.add(updateUid)
                user.likeNotify = "Yes"
            }else if(check == 1){
                user!!.rightSwipeBy!!.remove(updateUid)
            }
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateSwipeRightByAndMatches(uid: String, updateUid: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.rightSwipeBy!!.remove(updateUid)
            user.matches!!.add(updateUid)
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateHeight(context: Context, uid: String , height: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.height = height
            usersCollection.child(uid).setValue(user).addOnSuccessListener {
                val intent = Intent(context , EditProfileActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)
                (context as Activity).finish()
            }
        }
    }

    fun updateFcmToken(uid: String , fcmToken: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.fcmToken = fcmToken
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateInsta(uid: String , insta: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.instaId = insta
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateSnap(uid: String , snap: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.snapId = snap
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateExercise(context: Context, uid: String , exercise: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.exercise = exercise
            usersCollection.child(uid).setValue(user).addOnSuccessListener {
                val intent = Intent(context , EditProfileActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)
                (context as Activity).finish()
            }
        }
    }

    fun updateEducation(context: Context, uid: String , education: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            user!!.education = education
            usersCollection.child(uid).setValue(user).addOnSuccessListener {
                val intent = Intent(context , EditProfileActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)
                (context as Activity).finish()
            }
        }
    }

    fun updateMatches(uid: String, check: Int, updateUid: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            if(check == 0){
                user!!.matches!!.add(updateUid)
            }else{
                user!!.matches!!.remove(updateUid)
            }
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateInterests(uid: String, check: Int, interest: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            if(check == 0){
                if(user!!.interests == null){
                    val list = ArrayList<String>()
                    list.add(interest)
                    user.interests = list
                }else{
                    user.interests?.add(interest)
                }
            }else{
                user!!.interests?.remove(interest)
            }
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateBlockedUsers(uid: String, userToBlocked: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            if(user!!.blockedUsers == null){
                val list = ArrayList<String>()
                list.add(userToBlocked)
                user.blockedUsers = list
            }else{
                user.blockedUsers?.add(userToBlocked)
            }
            usersCollection.child(uid).setValue(user)
        }
    }

    fun updateInteract(uid: String, check: Int, updateUid: String){
        GlobalScope.launch {
            val user = getUserById(uid).await().getValue(UserModel::class.java)
            if(check == 0){
                user!!.interact!!.add(updateUid)
            }else{
                user!!.interact!!.remove(updateUid)
            }
            usersCollection.child(uid).setValue(user)
        }
    }

}