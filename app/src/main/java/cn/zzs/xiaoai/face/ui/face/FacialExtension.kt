package cn.zzs.xiaoai.face.ui.face

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cn.zzs.xiaoai.face.route.AppNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun emoji1(faceState: FaceState, nav: AppNavigator) {
    LaunchedEffect(key1 = Unit) {
        delay(300)
        faceState.openEyes()
        delay(600)
        faceState.lookAround()
        delay(500)
        faceState.wink()
        delay(50)
        faceState.wink()
        delay(1500)
        faceState.closeCompletely()
        nav.pop()
    }
}

@Composable
fun emoji2(faceState: FaceState, nav: AppNavigator) {
    LaunchedEffect(key1 = Unit) {
        delay(300)
        faceState.openEyes()
        delay(600)
        faceState.wink()
        faceState.wink()
        delay(600)
        faceState.badLaugh()
        delay(1000)
        faceState.closeCompletely()
        nav.pop()
    }
}

@Composable
fun emoji3(faceState: FaceState, nav: AppNavigator) {
    LaunchedEffect(key1 = Unit) {
        delay(300)
        faceState.openEyes()
        delay(600)
        faceState.closeLeftEye()
        delay(100)
        faceState.closeRightEye()
        delay(300)
        faceState.wink()
        faceState.wink()
        delay(500)
        faceState.wink()
        nav.pop()

    }
}