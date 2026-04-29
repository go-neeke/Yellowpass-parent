package kr.co.yellowpass.parent.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.yellowpass.parent.Prefs
import kr.co.yellowpass.parent.network.RetrofitClient
import kr.co.yellowpass.parent.network.Role
import kr.co.yellowpass.parent.network.model.LoginRequest
import timber.log.Timber

class LoginViewModel : ViewModel() {

    fun login(username: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.login(
                    LoginRequest(
                        username = username,
                        phone = "01010000001",
                        password = password,
                        role = Role.PARENT
                    )
                )

                Prefs.parentId = res.userId

                onSuccess()

            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}