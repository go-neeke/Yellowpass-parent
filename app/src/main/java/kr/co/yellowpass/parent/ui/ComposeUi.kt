package kr.co.yellowpass.parent.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.SharedFlow
import kr.co.yellowpass.parent.DeepLinkData
import kr.co.yellowpass.parent.ui.boading.BoardingDetailScreen
import kr.co.yellowpass.parent.ui.boading.BoardingHistoryScreen
import kr.co.yellowpass.parent.ui.child.ChildListScreen

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