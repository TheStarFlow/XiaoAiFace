package cn.zzs.xiaoai.face.ui.qrcode

import cn.zzs.xiaoai.face.data.XIAORepository
import cn.zzs.xiaoai.face.ui.setting.SettingUiState
import cn.zzs.xiaoai.face.ui.setting.SettingViewModel
import cn.zzs.xiaoai.face.utils.QrCodeUtils
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class WIFIQrCodeViewModel @AssistedInject constructor(
    @Assisted initialState: WIFIQrCodeState,
    private val repository: XIAORepository,
) : MavericksViewModel<WIFIQrCodeState>(initialState) {


    init {
        viewModelScope.launch {
            repository.getWifiSSID().combine(repository.getWifiPwd()) { ssid, pwd ->
                val content = "WIFI:T:WPA2;S:$ssid;P:$pwd;"
                QrCodeUtils.createQRCodeBitmap(content, 400, 400)
            }.execute {
                if (it is Success) {
                    copy(qrBitmap = it())
                } else {
                    this
                }
            }
        }
    }


    @AssistedFactory
    interface Factory : AssistedViewModelFactory<WIFIQrCodeViewModel, WIFIQrCodeState> {
        override fun create(state: WIFIQrCodeState): WIFIQrCodeViewModel
    }

    companion object :
        MavericksViewModelFactory<WIFIQrCodeViewModel, WIFIQrCodeState> by hiltMavericksViewModelFactory()
}