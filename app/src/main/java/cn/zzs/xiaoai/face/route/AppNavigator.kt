package cn.zzs.xiaoai.face.route

import android.app.Activity
import android.content.Context
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.navOptions

interface AppNavigator {

  fun push(route: Screen, isSingleTop: Boolean = false) = push(route.route, isSingleTop)

  fun push(route: String, isSingleTop: Boolean = false): Boolean

  fun pop(): Boolean

  val navController: NavHostController
}

class AppNavigatorImpl(context: Context) : AppNavigator {

  override val navController = NavHostController(context).apply {
    navigatorProvider.addNavigator(ComposeNavigator())
    navigatorProvider.addNavigator(DialogNavigator())
  }

  override fun push(route: String, isSingleTop: Boolean): Boolean {
    navController.navigate(
      route,
      navOptions {
        launchSingleTop = isSingleTop
      }
    )
    return true
  }

  override fun pop(): Boolean {
    // navGraph + backStack
    return  navController.navigateUp()
  }
}
