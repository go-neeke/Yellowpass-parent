package kr.co.yellowpass.parent.ui.child

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.co.yellowpass.parent.network.RetrofitClient
import kr.co.yellowpass.parent.network.model.Child
import timber.log.Timber

class ChildViewModel : ViewModel() {

    private val _children = MutableStateFlow<List<Child>>(emptyList())
    val children: StateFlow<List<Child>> = _children

    fun load(parentId: Long) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getChildren(parentId)

                _children.value = res

            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}