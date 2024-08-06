package cn.zzs.xiaoai.face.ui.face

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cn.zzs.xiaoai.face.route.AppNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun emoji1(faceState: FaceState, nav: AppNavigator){
    LaunchedEffect(key1 = Unit) {
        delay(300)
        faceState.openEyes()
        delay(600)
        faceState.lookAround()
        delay(500)
        faceState.wink()
        faceState.wink()
        delay(1000)
        faceState.closeLeftEye()
        delay(500)
        launch {
            faceState.openLeftEye()
        }
        launch {
            delay(50)
            faceState.closeRightEye()
        }
        delay(1000)
        faceState.openRightEye()
        delay(1000)
        faceState.wink()
        delay(1000)
        faceState.badLaugh()
        delay(1000)
        faceState.wink()
        faceState.closeCompletely()
        nav.pop()
    }
}