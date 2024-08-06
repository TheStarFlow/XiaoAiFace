package cn.zzs.xiaoai.face.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

@Composable
fun ScreenAdapt(
    dpWidth: Float = 360.0f,
    dpHeight: Float = 576.0f,
    useWidth: Boolean = false,
    content: @Composable () -> Unit
) {
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val widthPixel = displayMetrics.widthPixels
    val heightPixel = displayMetrics.heightPixels
    val targetDensityWidth = widthPixel / dpWidth
    val targetDensityHeight = heightPixel / dpHeight
    MaterialTheme {
        CompositionLocalProvider(
            LocalDensity provides Density(
                density = if (useWidth) targetDensityWidth else targetDensityHeight
            ),
        ) {
            content()
        }
    }
}