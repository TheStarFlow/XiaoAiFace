package cn.zzs.xiaoai.face

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cn.zzs.xiaoai.face.locals.LocalAppNavigator
import cn.zzs.xiaoai.face.route.AppNavigatorImpl
import cn.zzs.xiaoai.face.route.Screen
import cn.zzs.xiaoai.face.ui.ScreenAdapt
import cn.zzs.xiaoai.face.ui.clock.ClockLandscape
import cn.zzs.xiaoai.face.ui.face.Expression
import cn.zzs.xiaoai.face.ui.face.Face
import cn.zzs.xiaoai.face.ui.qrcode.WIFIQrCode
import cn.zzs.xiaoai.face.ui.setting.Settings
import cn.zzs.xiaoai.face.ui.theme.XiaoAiFaceTheme
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import kotlinx.coroutines.flow.collectLatest


fun Activity.hideSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XiaoAiFaceTheme {
                CompositionLocalProvider(
                    LocalAppNavigator provides AppNavigatorImpl(this)
                ) {
                    ScreenAdapt {
                        MainScreen()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

}

@Composable
fun MainScreen() {
    val nav = LocalAppNavigator.current
    val navController = nav.navController
    val mainViewmodel: MainViewModel = mavericksActivityViewModel()
    val owner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = Unit) {
        owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            mainViewmodel.uiAction.collectLatest {
                when (it) {
                    is UiEvent.JumpToFace -> {
                        nav.push(Screen.Face,true)
                    }
                    is UiEvent.JumpToWifiQrCode -> {
                        nav.push(Screen.WIFIQrCode,true)
                    }
                }
            }
        }
    }
    NavHost(navController = navController, startDestination = Screen.Clock.route) {
        composable(route = Screen.Clock.route) {
            ClockLandscape()
        }
        composable(route = Screen.Face.route){
            Expression()
        }
        composable(route = Screen.Setting.route){
            Settings()
        }
        composable(route = Screen.WIFIQrCode.route){
            WIFIQrCode()
        }
    }
}
