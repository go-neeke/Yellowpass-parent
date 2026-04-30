package kr.co.yellowpass.parent

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kr.co.yellowpass.parent.network.RetrofitClient
import kr.co.yellowpass.parent.network.model.DeviceTokenRequest
import kr.co.yellowpass.parent.ui.ParentApp
import kr.co.yellowpass.parent.ui.boading.BoardingDetailScreen
import kr.co.yellowpass.parent.ui.boading.BoardingHistoryScreen
import kr.co.yellowpass.parent.ui.child.ChildListScreen
import kr.co.yellowpass.parent.ui.login.LoginScreen
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val navEvent = MutableSharedFlow<DeepLinkData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNotificationPermission()
        enableEdgeToEdge() // 🔥 이거 추가

        setContent {
            AppRoot(
                navEvent = navEvent,
                onLoginSuccess = {
                    initFcm() // 로그인 성공 후 토큰 등록
                }
            )
        }

        // 이미 로그인된 사용자만 앱 시작 시 토큰 등록
        if (Prefs.parentId != -1L) {
            initFcm()
            handleIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val childId = intent?.getIntExtra("childId", -1) ?: -1
        val vehicleId = intent?.getIntExtra("vehicleId", -1) ?: -1

        if (childId != -1 && vehicleId != -1) {
            lifecycleScope.launch {
                navEvent.emit(DeepLinkData(childId, vehicleId))
            }
        }
    }

    private fun initFcm() {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                lifecycleScope.launch(Dispatchers.IO) {
                    Timber.d("token = $token")
                    sendTokenToServer(token)
                }
            }
    }

    private suspend fun sendTokenToServer(token: String) {
        val parentId = Prefs.parentId

        if (parentId == -1L) {
            Timber.w("parentId 없음. FCM 토큰 전송 생략")
            return
        }

        try {
            RetrofitClient.apiService.sendDeviceToken(
                DeviceTokenRequest(
                    parentId = parentId,
                    fcmToken = token
                )
            )
            Timber.d("FCM 토큰 서버 전송 성공")
        } catch (e: Exception) {
            Timber.e(e)
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
}

@Composable
fun AppRoot(
    navEvent: SharedFlow<DeepLinkData>,
    onLoginSuccess: () -> Unit
) {
    var isLogin by remember {
        mutableStateOf(Prefs.parentId != -1L)
    }

    if (!isLogin) {
        LoginScreen(
            onLoginSuccess = {
                isLogin = true
                onLoginSuccess()
            }
        )
    } else {
        ParentApp(navEvent)
    }
}

data class DeepLinkData(val childId: Int, val boardingId: Int)