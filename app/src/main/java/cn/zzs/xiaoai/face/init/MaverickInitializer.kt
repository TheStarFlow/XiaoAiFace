package cn.zzs.xiaoai.face.init

import android.content.Context
import androidx.startup.Initializer
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.navigation.DefaultNavigationViewModelDelegateFactory

class MaverickInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        Mavericks.initialize(
            context,
            viewModelDelegateFactory = DefaultNavigationViewModelDelegateFactory()
        )
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}