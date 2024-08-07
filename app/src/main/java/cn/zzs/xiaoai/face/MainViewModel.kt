package cn.zzs.xiaoai.face

import cn.zzs.xiaoai.face.data.XIAORepository
import cn.zzs.xiaoai.face.ui.clock.default_fontSize
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel @AssistedInject constructor(
    // Hilt 注入接口实例
    @Assisted initialState: MainUIState,
    private val repository: XIAORepository,
) : MavericksViewModel<MainUIState>(initialState) {


    val uiAction = repository.uiAction

    init {
       repository.websocketConnectedFlow.execute {
           copy(isWebSocketConnected = it()?:false)
       }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTime { h, m, s ->
                setState {
                    copy(hour = h, minute = m, second = s)
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.startWebSocket(viewModelScope)
            } catch (e: Exception) {
                //
            }
        }
    }


    fun saveFontSize(fontSize: Float) = viewModelScope.launch {
        repository.saveFontSize(fontSize)
    }

    fun saveXOffset(x: Float) = viewModelScope.launch {
        repository.saveXOffset(x)
    }

    fun saveYOffset(y: Float) = viewModelScope.launch {
        repository.saveYOffset(y)
    }

    fun loadCacheConfig() {
        repository.getFontSize().execute {
            if (it is Success) {
                copy(fontSize = it.invoke())
            } else {
                this
            }
        }
        repository.getXOffset().execute {
            if (it is Success) {
                copy(xOffSet = it.invoke())
            } else {
                this
            }
        }
        repository.getYOffset().execute {
            if (it is Success) {
                copy(yOffSet = it.invoke())
            } else {
                this
            }
        }

    }

    fun reset() {
        saveXOffset(0f)
        saveYOffset(0f)
        saveFontSize(default_fontSize)
    }


    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MainViewModel, MainUIState> {
        override fun create(state: MainUIState): MainViewModel
    }

    companion object :
        MavericksViewModelFactory<MainViewModel, MainUIState> by hiltMavericksViewModelFactory()

}