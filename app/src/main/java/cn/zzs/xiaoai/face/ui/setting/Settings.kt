package cn.zzs.xiaoai.face.ui.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel

@Composable
fun Settings() {
    val settingViewModel: SettingViewModel = mavericksViewModel()
    val url by settingViewModel.collectAsState(prop1 = SettingUiState::webSocketUrl)
    var showUrl by remember(url) {
        mutableStateOf(url)
    }
    val ssid by settingViewModel.collectAsState(prop1 = SettingUiState::wifiSSID)
    val pwd by settingViewModel.collectAsState(prop1 = SettingUiState::wifiPassword)
    var wifiSSID by remember(ssid) {
        mutableStateOf(ssid)
    }
    var wifiPwd by remember(pwd) {
        mutableStateOf(pwd)
    }
    LaunchedEffect(key1 = Unit) {
        settingViewModel.loadCacheConfig()
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item {
            OutlineMenu {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.width(5.dp))
                    OutlinedTextField(value = showUrl ?: "", onValueChange = {
                        showUrl = it
                    }, label = {
                        Text(text = "WebSocket地址")
                    })
                    Spacer(modifier = Modifier.width(20.dp))
                    OutlinedButton(onClick = {
                        showUrl?.let { settingViewModel.saveWebSocketUrl(it) }
                    }) {
                        Text(text = "保存", fontSize = 12.sp)
                    }
                }
            }
        }

        item {
            OutlineMenu {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.width(5.dp))
                    OutlinedTextField(value = wifiSSID ?: "", onValueChange = {
                        wifiSSID = it
                    }, label = {
                        Text(text = "WiFi")
                    })
                    OutlinedTextField(value = wifiPwd ?: "", onValueChange = {
                        wifiPwd = it
                    }, label = {
                        Text(text = "WiFi密码")
                    })
                    Spacer(modifier = Modifier.width(5.dp))
                    OutlinedButton(onClick = {
                        if (wifiSSID != null && wifiPwd != null) {
                            settingViewModel.saveWifiConfig(wifiSSID!!, wifiPwd!!)
                        }
                    }) {
                        Text(text = "保存", fontSize = 12.sp)
                    }
                }
            }
        }

    }
}

@Composable
fun OutlineMenu(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.wrapContentSize(),
        shape = RoundedCornerShape(3.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Box(modifier = modifier.padding(10.dp).wrapContentSize()) {
            content()
        }
    }

}

