package cn.zzs.xiaoai.face.route

sealed class Screen(val route: String) {

    data object Clock : Screen("Clock")


    data object Face : Screen("Face")

    data object Setting :Screen("setting")

    data object WIFIQrCode : Screen("WIFIQrCode")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}