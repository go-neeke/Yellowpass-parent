package kr.co.yellowpass.parent.network.model

data class Child(
    val studentId: Long,
    val name: String,
    val schoolName: String,
    val grade: Int,
    val classNo: Int,
    val lastBoardedAt: String?,
    val status: String // BOARDING / NOT_BOARD / DELAY
)