package cn.zzs.xiaoai.face.locals

import androidx.compose.runtime.staticCompositionLocalOf
import cn.zzs.xiaoai.face.route.AppNavigator


val LocalAppNavigator = staticCompositionLocalOf<AppNavigator> { error("Navigator not provide") }
