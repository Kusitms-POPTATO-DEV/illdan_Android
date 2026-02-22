package com.poptato.ui.util

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

object FCMManager {
    fun getFCMToken(callback: (String?) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.d("FCM 토큰 가져오기 실패: ${task.exception}")
                callback(null)
                return@addOnCompleteListener
            }
            val token = task.result
            callback(token)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getFCMTokenSuspend(): String? = suspendCancellableCoroutine { cont ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                cont.resume(task.result, null)
            } else {
                Timber.e("FCM 토큰 가져오기 실패: ${task.exception}")
                cont.resume(null, null)
            }
        }
    }
}