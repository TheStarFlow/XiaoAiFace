package cn.zzs.xiaoai.face

import androidx.compose.ui.geometry.Offset
import cn.zzs.xiaoai.face.route.Screen
import cn.zzs.xiaoai.face.ui.clock.default_fontSize
import com.airbnb.mvrx.MavericksState

data class MainUIState(
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0,
    val fontSize: Float = default_fontSize,
    val showSec: Boolean = false,
    val xOffSet: Float = 0f,
    val yOffSet: Float = 0f,
    val isWebSocketConnected: Boolean = false,
) : MavericksState


sealed class UiEvent {
    data object JumpToFace : UiEvent()

    data object JumpToWifiQrCode : UiEvent()
}