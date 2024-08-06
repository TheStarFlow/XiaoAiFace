package cn.zzs.xiaoai.face.ui.clock

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.zzs.xiaoai.face.MainUIState
import cn.zzs.xiaoai.face.MainViewModel
import cn.zzs.xiaoai.face.R
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import com.elvishew.xlog.XLog


const val default_fontSize = 16f

@Composable
fun ClockLandscape() {
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val mainViewmodel: MainViewModel = mavericksActivityViewModel()
    val fontSize by mainViewmodel.collectAsState(prop1 = MainUIState::fontSize)
    val hour by mainViewmodel.collectAsState(prop1 = MainUIState::hour)
    val minute by mainViewmodel.collectAsState(prop1 = MainUIState::minute)
    val second by mainViewmodel.collectAsState(prop1 = MainUIState::second)
    val showSec by mainViewmodel.collectAsState(prop1 = MainUIState::showSec)
    val xOffset by mainViewmodel.collectAsState(prop1 = MainUIState::xOffSet)
    val yOffset by mainViewmodel.collectAsState(prop1 = MainUIState::yOffSet)
    val url by mainViewmodel.collectAsState(prop1 = MainUIState::webSocketUrl)
    var currXOffset by remember(xOffset) {
        mutableFloatStateOf(xOffset)
    }
    var currYOffset by remember(yOffset) {

        mutableFloatStateOf(yOffset)
    }
    val color: Color = Color.White
    val screenWidth = displayMetrics.widthPixels
    val screenHeight = displayMetrics.heightPixels
    val density = LocalDensity.current
    var targetFontSize by remember {
        mutableStateOf(fontSize.sp)
    }
    var showFontSize by remember(fontSize) {
        mutableFloatStateOf(fontSize)
    }
    var showControl by remember {
        mutableStateOf(false)
    }
    var showUrl by remember(url) {
        mutableStateOf(url)
    }
    LaunchedEffect(key1 = showFontSize) {
        with(density) {
            targetFontSize = showFontSize.toSp()
        }
    }
    LaunchedEffect(key1 = fontSize) {
        XLog.i(
            """
            width  = ${displayMetrics.widthPixels}
            height = ${displayMetrics.heightPixels}
        """.trimIndent()
        )
        if (fontSize == default_fontSize) {
            targetFontSize = with(density) {
                (displayMetrics.heightPixels /1.35f).apply {
                    showFontSize = this
                }.toSp()
            }
        }
        mainViewmodel.loadCacheConfig()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .clickable {
                showControl = !showControl
            }
    ) {
        Row(modifier = Modifier
            .align(Alignment.Center)
            .offset {
                IntOffset(currXOffset.toInt(), currYOffset.toInt())
            }) {
            Text(
                text = timeFormat(hour),
                color = color,
                fontSize = targetFontSize,
                maxLines = 1,
                fontFamily = FontFamily(Font(R.font.jianheiled))
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = ":",
                color = color,
                fontSize = targetFontSize,
                maxLines = 1,
                fontFamily = FontFamily(Font(R.font.jianheiled))
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = timeFormat(minute),
                color = color,
                fontSize = targetFontSize,
                maxLines = 1,
                fontFamily = FontFamily(Font(R.font.jianheiled))
            )
            if (showSec) {
                Text(
                    text = ":",
                    color = color,
                    fontSize = targetFontSize,
                    maxLines = 1,
                    fontFamily = FontFamily(Font(R.font.jianheiled))
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = timeFormat(second),
                    color = color,
                    fontSize = targetFontSize,
                    maxLines = 1,
                    fontFamily = FontFamily(Font(R.font.jianheiled))
                )
            }
        }
        if (showControl) {
            Column(
                modifier = Modifier
                    .width(460.dp)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Black, Color.Gray.copy(alpha = 0.5f))
                        )
                    )
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "X轴:", color = color, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Slider(
                        value = currXOffset,
                        onValueChange = {
                            currXOffset = it
                        },
                        valueRange = (-screenWidth / 2f).rangeTo(screenWidth / 2f),
                        onValueChangeFinished = {
                            mainViewmodel.saveXOffset(currXOffset)
                        }
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Y轴:", color = color, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Slider(
                        value = currYOffset,
                        onValueChange = {
                            currYOffset = it
                        },
                        valueRange = (-screenHeight / 2f).rangeTo(screenHeight / 2f),
                        onValueChangeFinished = {
                            mainViewmodel.saveYOffset(currYOffset)
                        }
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "字体:", color = color, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Slider(
                        value = showFontSize,
                        onValueChange = {
                            showFontSize = it
                        },
                        valueRange = 0f.rangeTo(screenWidth.toFloat()),
                        onValueChangeFinished = {
                            mainViewmodel.saveFontSize(showFontSize)
                        }
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "WebSocket :", color = color, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    TextField(value = showUrl ?: "", onValueChange = {
                        showUrl = it
                    })
                    Text(
                        text = "保存",
                        fontSize = 20.sp,
                        color = color,
                        modifier = Modifier.clickable {
                            showUrl?.run {
                                mainViewmodel.saveWebSocketUrl(this)
                                showControl = false
                            }
                        })
                }
                Text(text = "重置", fontSize = 28.sp, color = color, modifier = Modifier.clickable {
                    mainViewmodel.reset()
                })
            }
        }
    }
}

private fun timeFormat(time: Int): String {
    if (time < 10) return "0$time"
    return "$time"
}