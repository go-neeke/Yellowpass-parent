package kr.co.yellowpass.parent.ui.child

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kr.co.yellowpass.parent.network.model.Child

@Composable
fun ChildCard(
    child: Child,
    onClick: (Child) -> Unit
) {

    val statusColor = when (child.status) {
        "BOARDING" -> Color(0xFF4CAF50) // 초록
        "DELAY" -> Color(0xFFFFC107)    // 노랑
        else -> Color(0xFFF44336)       // 빨강
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick(child) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 상태 원형 표시
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(statusColor, shape = CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = child.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${child.schoolName} ${child.grade}학년 ${child.classNo}반",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "최근 탑승: ${child.lastBoardedAt ?: "-"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Text(
                text = when (child.status) {
                    "BOARDING" -> "탑승 완료"
                    "DELAY" -> "지연"
                    else -> "미탑승"
                },
                color = statusColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}