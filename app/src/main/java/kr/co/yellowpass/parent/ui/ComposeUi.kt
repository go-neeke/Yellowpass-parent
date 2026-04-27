package kr.co.yellowpass.parent.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AppScreen(type: String?) {

    if (type == "RIDE_EVENT") {
        RideEventScreen()
    } else {
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {
    Text("홈 화면")
}

@Composable
fun RideEventScreen() {
    Text("탑승 알림 화면")
}