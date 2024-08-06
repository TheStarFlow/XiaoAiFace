package cn.zzs.xiaoai.face.init

import android.content.Context
import androidx.startup.Initializer
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog


class XLogInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        XLog.init(LogLevel.ALL)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}