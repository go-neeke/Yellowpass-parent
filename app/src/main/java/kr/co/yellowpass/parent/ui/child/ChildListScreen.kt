package kr.co.yellowpass.parent.ui.child

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.co.yellowpass.parent.network.model.Child

@Composable
fun ChildListScreen(
    viewModel: ChildViewModel = viewModel(),
    onClick: (Child) -> Unit
) {

    val list by viewModel.children.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load(parentId = 1) // 🔥 로그인 붙이면 변경
    }

    Scaffold { padding ->

        if (list.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("데이터 없음 또는 로딩중")
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(list) { child ->
                ChildCard(child = child, onClick = onClick)
            }
        }
    }
}