package com.rohan.datingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.notification.ApiUtilities
import com.rohan.datingapp.notification.NotificationModel
import com.rohan.datingapp.notification.PushNotificationModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DatingViewModel : ViewModel() {

    private val userDao = UserDao()
    private val currentUid get() = FirebaseAuth.getInstance().currentUser!!.uid

    private val _users = MutableStateFlow<List<UserModel>>(emptyList())
    val users = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    var currentUserName: String? = null
    var lastUserUid: String = ""

    private var lastFetchedKey: String? = null
    private val seenUids = HashSet<String>()
    private var rewindCheck = false

    fun resetPagination() {
        lastFetchedKey = null
    }

    fun fetchUsers(genderCheck: Int, distanceCheck: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currUser = userDao.getUserById(currentUid).await()
                    .getValue(UserModel::class.java) ?: return@launch
                currentUserName = currUser.name

                val interact = HashSet<String>().apply {
                    currUser.interact?.let { addAll(it) }
                    addAll(seenUids)
                }
                if (rewindCheck) interact.remove(lastUserUid)
                rewindCheck = false

                val blockedByMain = currUser.blockedUsers?.toHashSet() ?: hashSetOf()
                val result = arrayListOf<UserModel>()
                var batchLastKey = lastFetchedKey

                while (result.size < 10) {
                    val snapshot = userDao.getUsersPaginated(batchLastKey, 20).await()
                    if (!snapshot.exists() || snapshot.childrenCount == 0L) break

                    for (data in snapshot.children) {
                        batchLastKey = data.key
                        val model = data.getValue(UserModel::class.java) ?: continue
                        val uid = model.uid ?: continue

                        if (uid == currentUid) continue
                        if (interact.contains(uid)) continue
                        if (blockedByMain.contains(uid)) continue
                        if (model.interact?.contains(currentUid) == true) continue
                        if (model.blockedUsers?.contains(currentUid) == true) continue

                        val genderMatch = when (genderCheck) {
                            1 -> model.gender == "Man"
                            2 -> model.gender == "Woman"
                            else -> true
                        }
                        val distanceMatch = distanceCheck == 0 || model.city == currUser.city

                        if (genderMatch && distanceMatch) {
                            result.add(model)
                            if (result.size >= 10) break
                        }
                    }
                    if (snapshot.childrenCount < 20) break
                }

                lastFetchedKey = batchLastKey
                _users.value = result
            } catch (e: Exception) {
                _error.emit(e.message ?: "Failed to load users")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSwiped(uid: String, direction: String, fcmToken: String?) {
        lastUserUid = uid
        seenUids.add(uid)
        userDao.updateInteract(currentUid, 0, uid)

        if (direction == "Right") {
            userDao.updateSwipeRightBy(uid, 0, currentUid)
            sendLikeNotification(fcmToken)
        }
    }

    fun rewind() {
        if (lastUserUid.isEmpty()) return
        rewindCheck = true
        seenUids.remove(lastUserUid)
        userDao.updateInteract(currentUid, 1, lastUserUid)
    }

    private fun sendLikeNotification(fcmToken: String?) {
        val notification = PushNotificationModel(
            NotificationModel("$currentUserName sent you a friend request!", ""),
            fcmToken
        )
        ApiUtilities.getInstance().sendNotification(notification)
            .enqueue(object : Callback<PushNotificationModel> {
                override fun onResponse(call: Call<PushNotificationModel>, response: Response<PushNotificationModel>) {}
                override fun onFailure(call: Call<PushNotificationModel>, t: Throwable) {}
            })
    }
}
