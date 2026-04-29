package kr.co.yellowpass.parent.ui.boading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.co.yellowpass.parent.ui.boading.BoardingViewModel

@Composable
fun BoardingDetailScreen(
    boardingId: Long,
    viewModel: BoardingViewModel = viewModel()
) {

    val detail by viewModel.detail.collectAsState()

    LaunchedEffect(boardingId) {
        viewModel.loadDetail(boardingId)
    }

    Scaffold { padding ->

        detail?.let { data ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Text("탑승 상세", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(16.dp))

                Text("학생: ${data.studentName}")
                Text("탑승 시간: ${data.boardedAt}")
                Text("차량: ${data.vehicleNo ?: "-"}")
            }

        } ?: run {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}