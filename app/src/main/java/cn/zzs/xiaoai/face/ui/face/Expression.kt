package cn.zzs.xiaoai.face.ui.face

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cn.zzs.xiaoai.face.locals.LocalAppNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun Expression() {
    val nav = LocalAppNavigator.current
    val faceState = rememberFaceState(isEyeOpen = false)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    emoji1(faceState = faceState, nav = nav)
    DisposableEffect(key1 = Unit) {
        if (context is Activity) {
            context.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        onDispose {
            if (context is Activity) {
                context.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                scope.launch {

                }
            }
    ) {

        Face(state = faceState)
    }


}