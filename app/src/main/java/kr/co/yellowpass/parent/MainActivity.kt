package kr.co.yellowpass.parent

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.yellowpass.parent.network.RetrofitClient
import kr.co.yellowpass.parent.network.model.DeviceTokenRequest
import timber.log.Timber

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d( "앱 실행됨")

        requestNotificationPermission()

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Timber.d("FCM TOKEN = $token")

                sendTokenToServer(token)
            }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
    }

    private fun sendTokenToServer(token: String) {

        CoroutineScope(Dispatchers.IO).launch {
            try {

                val response = RetrofitClient.apiService.sendDeviceToken(
                    DeviceTokenRequest(
                        parentId = 1, // 🔥 나중에 로그인 붙이면 바뀜
                        fcmToken = token
                    )
                )

                if (response.isSuccessful) {
                    Timber.d("토큰 저장 성공")
                } else {
                    Timber.e("토큰 저장 실패")
                }

            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}