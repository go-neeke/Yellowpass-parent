package kr.co.yellowpass.parent.ui.boading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.co.yellowpass.parent.network.RetrofitClient
import kr.co.yellowpass.parent.network.model.BoardingHistory
import timber.log.Timber

class BoardingViewModel : ViewModel() {

    private val _history = MutableStateFlow<List<BoardingHistory>>(emptyList())
    val history: StateFlow<List<BoardingHistory>> = _history

    private val _detail = MutableStateFlow<BoardingHistory?>(null)
    val detail: StateFlow<BoardingHistory?> = _detail

    fun loadHistory(parentId: Long) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getBoardingHistory(parentId)

                _history.value = res.map {
                    BoardingHistory(
                        boardingId = it.boardingId,
                        studentName = it.studentName,
                        boardedAt = it.boardedAt,
                        vehicleNo = it.vehicleNo,
                        studentId = it.studentId,
                        classNo = it.classNo,
                        grade = it.grade,
                        schoolName = it.schoolName
                    )
                }

            } catch (e: Exception) {
                Timber.Forest.e(e)
            }
        }
    }

    fun loadDetail(boardingId: Long) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getBoardingDetail(boardingId)

                _detail.value = BoardingHistory(
                    boardingId = res.boardingId,
                    studentName = res.studentName,
                    boardedAt = res.boardedAt,
                    vehicleNo = res.vehicleNo,
                    studentId = res.studentId,
                    classNo = res.classNo,
                    grade = res.grade,
                    schoolName = res.schoolName
                )

            } catch (e: Exception) {
                Timber.Forest.e(e)
            }
        }
    }
}