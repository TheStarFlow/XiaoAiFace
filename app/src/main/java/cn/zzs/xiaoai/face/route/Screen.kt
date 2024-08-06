package cn.zzs.xiaoai.face.route

sealed class Screen(val route: String) {

    data object Clock : Screen("Clock")


    data object Face : Screen("Face")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}