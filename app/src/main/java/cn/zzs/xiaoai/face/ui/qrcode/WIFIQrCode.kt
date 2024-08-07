package cn.zzs.xiaoai.face.ui.qrcode

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.zzs.xiaoai.face.locals.LocalAppNavigator
import cn.zzs.xiaoai.face.ui.setting.SettingViewModel
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import kotlinx.coroutines.delay


@Composable
fun WIFIQrCode() {
    val viewmodel: WIFIQrCodeViewModel = mavericksViewModel()
    val bitmap by viewmodel.collectAsState(prop1 = WIFIQrCodeState::qrBitmap)
    val nav = LocalAppNavigator.current
    LaunchedEffect(key1 = Unit) {
        delay(30 * 1000)
        nav.pop()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "扫\n码",
            fontSize = 120.sp,
            color = Color.White,
            lineHeight = 120.sp,
            modifier = Modifier
                .align(alignment = Alignment.CenterStart)
                .offset(x = 80.dp)
        )
        Text(
            text = "连\n接",
            fontSize = 120.sp,
            color = Color.White,
            lineHeight = 120.sp,
            modifier = Modifier
                .align(alignment = Alignment.CenterEnd)
                .offset(x = (-80).dp)
        )
        if (bitmap != null) {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .align(Alignment.Center)
            )
        }
    }
}