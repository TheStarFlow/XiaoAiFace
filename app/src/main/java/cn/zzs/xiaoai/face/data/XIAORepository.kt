package cn.zzs.xiaoai.face.data

import android.icu.util.Calendar
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import cn.zzs.xiaoai.face.UiEvent
import cn.zzs.xiaoai.face.ui.clock.default_fontSize
import com.elvishew.xlog.XLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Exception

@Singleton
class XIAORepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private var mClient: WebSocketClient? = null

    private val _channel = Channel<UiEvent>()
    val uiAction = _channel.receiveAsFlow()

    private val websocketConnected = MutableStateFlow(false)
    val websocketConnectedFlow: Flow<Boolean> = websocketConnected


    companion object {
        val FONT_SIZE_KEY = floatPreferencesKey("font_size")
        val X_OFFSET_KEY = floatPreferencesKey("x_offset")
        val Y_OFFSET_KEY = floatPreferencesKey("y_offset")
        val WEB_SOCKET_URL = stringPreferencesKey("websocket_url")
        val WIFI_SSID = stringPreferencesKey("wifi_ssid")
        val WIFI_PWD = stringPreferencesKey("wifi_pwd")
    }


    suspend fun startWebSocket(scope: CoroutineScope, count: Int = 0) {
        val url = dataStore.data.map {
            it[WEB_SOCKET_URL]
        }.firstOrNull() ?: return
        val uri = URI.create(url)
        mClient = object : WebSocketClient(uri) {

            override fun onOpen(handshakedata: ServerHandshake?) {
                websocketConnected.tryEmit(true)
                XLog.i("Websocket Open ${handshakedata?.httpStatusMessage}")
            }

            override fun onMessage(message: String?) {
                websocketConnected.tryEmit(true)
                XLog.i("onMessage :$message")
                if (message != null) {
                    messageHandle(_channel, message, scope)
                }

            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                XLog.i("WebSocket onClose :$reason isRemote :$remote  code:$code")
                websocketConnected.tryEmit(false)
                if (remote) {
                    scope.launch(Dispatchers.IO) {
                        delay(5000)
                        var sCount = count
                        sCount++
                        startWebSocket(scope, sCount)
                    }
                }
            }

            override fun onError(ex: Exception?) {
                websocketConnected.tryEmit(false)
                XLog.e("Websocket error: $ex")
                scope.launch(Dispatchers.IO) {
                    delay(5000)
                    var sCount = count
                    sCount++
                    startWebSocket(scope, sCount)
                }
            }
        }
        try {
            if (count >= 5)
                throw IllegalStateException("reconnect many times failed ")
            mClient?.connectBlocking()
        } catch (e: Exception) {
            e.printStackTrace()
            delay(5000)
            var sCount = count
            if (count < 5) {
                sCount++
                startWebSocket(scope, sCount)
            }
        }

    }

    private fun messageHandle(channel: Channel<UiEvent>, message: String, scope: CoroutineScope) {
        scope.launch {
            if (message == "1") {
                channel.send(UiEvent.JumpToFace)
            } else if (message == "2") {
                channel.send(UiEvent.JumpToWifiQrCode)
            }
        }
    }


    suspend fun getTime(timeCallback: (Int, Int, Int) -> Unit) {
        while (true) {
            val calendar: Calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val second = calendar.get(Calendar.SECOND)
            timeCallback(hour, minute, second)
            delay(250)
        }
    }


    fun getFontSize(): Flow<Float> {
        return dataStore.data.map {
            it[FONT_SIZE_KEY] ?: default_fontSize
        }
    }

    fun getXOffset(): Flow<Float> {
        return dataStore.data.map {
            it[X_OFFSET_KEY] ?: 0f
        }
    }

    fun getYOffset(): Flow<Float> {
        return dataStore.data.map {
            it[Y_OFFSET_KEY] ?: 0f
        }
    }

    fun getWebSocketUrl(): Flow<String> {
        return dataStore.data.map {
            it[WEB_SOCKET_URL] ?: ""
        }
    }

    fun getWifiSSID(): Flow<String> {
        return dataStore.data.map {
            it[WIFI_SSID] ?: ""
        }
    }

    suspend fun saveFontSize(fontSize: Float) {
        dataStore.edit {
            it[FONT_SIZE_KEY] = fontSize
        }
    }

    suspend fun saveXOffset(x: Float) {
        dataStore.edit {
            it[X_OFFSET_KEY] = x
        }
    }

    suspend fun saveYOffset(y: Float) {
        dataStore.edit {
            it[Y_OFFSET_KEY] = y
        }
    }

    suspend fun saveWebSocketUrl(url: String) {
        dataStore.edit {
            it[WEB_SOCKET_URL] = url
        }
    }

    suspend fun saveWifiConfig(content: String, password: String) {
        dataStore.edit {
            it[WIFI_SSID] = content
            it[WIFI_PWD] = password
        }
    }

    fun getWifiPwd() = dataStore.data.map {
        it[WIFI_PWD] ?: ""
    }


}