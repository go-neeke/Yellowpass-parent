package kr.co.yellowpass.parent.ui.boading

import android.R.attr.onClick
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.co.yellowpass.parent.network.model.BoardingHistory
import kr.co.yellowpass.parent.ui.boading.BoardingViewModel

@Composable
fun BoardingHistoryScreen(
    childId: Long,
    viewModel: BoardingViewModel = viewModel(),
    onClick: (BoardingHistory) -> Unit
) {

    val list by viewModel.history.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHistory(parentId = 1) // 🔥 로그인 붙이면 변경
    }

    Scaffold { padding ->
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            items(list) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onClick(item) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(item.studentName)
                        Text(item.boardedAt)
                        Text("차량: ${item.vehicleNo ?: "-"}")
                    }
                }
            }
        }
    }
}