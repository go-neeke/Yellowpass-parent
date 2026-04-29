package kr.co.yellowpass.parent.network.model

data class BoardingHistory(
    val boardingId: Long,
    val studentId: Long,
    val studentName: String,
    val schoolName: String,
    val grade: Int,
    val classNo: Int,
    val boardedAt: String,
    val vehicleNo: String?
)