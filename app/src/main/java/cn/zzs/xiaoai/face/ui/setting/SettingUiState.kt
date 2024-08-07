package cn.zzs.xiaoai.face.ui.setting

import com.airbnb.mvrx.MavericksState

data class SettingUiState(
    val webSocketUrl: String? = null,
    val wifiSSID: String? = null,
    val wifiPassword:String? = null
) : MavericksState