package cn.zzs.xiaoai.face.ui.qrcode

import android.graphics.Bitmap
import com.airbnb.mvrx.MavericksState

data class WIFIQrCodeState(
    val qrBitmap: Bitmap? = null
) : MavericksState