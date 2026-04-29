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
        initFcm()

        handleIntent(intent)

        enableEdgeToEdge() // 🔥 이거 추가

        setContent {
            AppRoot(navEvent)
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
                    sendTokenToServer(token)
                }
            }
    }

    private suspend fun sendTokenToServer(token: String) {
        try {
            RetrofitClient.apiService.sendDeviceToken(
                DeviceTokenRequest(parentId = Prefs.parentId, fcmToken = token)
            )
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
fun ParentApp(navEvent: SharedFlow<DeepLinkData>) {

    val navController = rememberNavController()

    // 🔥 푸시 딥링크
    LaunchedEffect(Unit) {
        navEvent.collect { data ->
            navController.navigate("detail/${data.boardingId}")
        }
    }

    NavHost(navController, startDestination = "childList") {

        composable("childList") {
            ChildListScreen(
                onClick = { child ->
                    navController.navigate("history/${child.studentId}")
                }
            )
        }

        composable("history/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")?.toLong() ?: 0L

            BoardingHistoryScreen(
                childId = childId,
                onClick = { boarding ->
                    navController.navigate("detail/${boarding.boardingId}")
                }
            )
        }

        composable("detail/{boardingId}") { backStackEntry ->
            val boardingId = backStackEntry.arguments?.getString("boardingId")?.toLong() ?: 0L

            BoardingDetailScreen(boardingId = boardingId)
        }
    }
}

@Composable
fun AppRoot(navEvent: SharedFlow<DeepLinkData>) {

    var isLogin by remember { mutableStateOf(Prefs.parentId != -1L) }

    if (!isLogin) {
        LoginScreen(
            onLoginSuccess = {
                isLogin = true
            }
        )
    } else {
        ParentApp(navEvent)
    }
}

data class DeepLinkData(val childId: Int, val boardingId: Int)