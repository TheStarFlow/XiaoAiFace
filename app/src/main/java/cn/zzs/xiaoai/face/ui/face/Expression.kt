package cn.zzs.xiaoai.face.ui.face

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cn.zzs.xiaoai.face.locals.LocalAppNavigator
import com.elvishew.xlog.XLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


@Composable
fun Expression() {
    val nav = LocalAppNavigator.current
    val faceState = rememberFaceState(isEyeOpen = false)
    val context = LocalContext.current
    var state by remember {
        mutableIntStateOf(-1)
    }
    LaunchedEffect(key1 = Unit) {
        state = Random.nextInt(1, 10)
    }
    when (state) {
        in 1..4 -> {
            XLog.i("emoji 1")
            emoji1(faceState = faceState, nav = nav)
        }

        in 5..7 -> {
            XLog.i("emoji 2")
            emoji2(faceState = faceState, nav = nav)
        }

        in 8..10 -> {
            XLog.i("emoji 3")
            emoji3(faceState = faceState, nav = nav)
        }
    }
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
    ) {

        Face(state = faceState)
    }


}