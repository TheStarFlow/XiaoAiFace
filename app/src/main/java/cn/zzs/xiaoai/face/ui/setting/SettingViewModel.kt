package cn.zzs.xiaoai.face.ui.setting

import cn.zzs.xiaoai.face.data.XIAORepository
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class SettingViewModel @AssistedInject constructor(
    @Assisted initialState: SettingUiState,
    private val repository: XIAORepository,
) : MavericksViewModel<SettingUiState>(initialState) {

    fun loadCacheConfig() {
        repository.getWebSocketUrl().execute {
            if (it is Success) {
                copy(webSocketUrl = it.invoke())
            } else {
                this
            }
        }
        repository.getWifiSSID().execute {
            if (it is Success) {
                copy(wifiSSID = it.invoke())
            } else {
                this
            }
        }
        repository.getWifiPwd().execute {
            if (it is Success) {
                copy(wifiPassword = it.invoke())
            } else {
                this
            }
        }
    }


    fun saveWebSocketUrl(url: String) = viewModelScope.launch {
        repository.saveWebSocketUrl(url)
        try {
            repository.startWebSocket(viewModelScope)
        } catch (e: Exception) {
            //
        }
    }

    fun saveWifiConfig(ssid: String,password:String) = viewModelScope.launch {
        repository.saveWifiConfig(ssid,password)
    }


    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SettingViewModel, SettingUiState> {
        override fun create(state: SettingUiState): SettingViewModel
    }

    companion object :
        MavericksViewModelFactory<SettingViewModel, SettingUiState> by hiltMavericksViewModelFactory()
}