package cn.zzs.xiaoai.face.ui.face

import android.content.Context
import android.graphics.RectF
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val drawMode_normal = 0
const val drawMode_1 = 1

class FaceState(
    context: Context,
    eyesRadiusFactor: Float = 0.25f,
    eyesRadius: Float = -1f,
    isEyesOpen: Boolean = false,
    val eyeColor: Color = Color.White,
    val shadowColor: Color = Color(0xff0008A9)
) {

    internal var drawMode = drawMode_normal

    var scope: CoroutineScope? = null
    val eyeRadius =
        if (eyesRadius > 0) eyesRadius else context.resources.displayMetrics.heightPixels * eyesRadiusFactor

    val maxCloseSpace = eyeRadius * 0.9f
    private val horizontalMaxSpace = eyeRadius * 0.3f

    val shadowOffset = eyeRadius * 0.15f


    val leftEyeRect by mutableStateOf(RectF(0f, 0f, 0f, 0f))

    val rightEyeRect by mutableStateOf(RectF(0f, 0f, 0f, 0f))

    var leftEyeCenter by mutableStateOf(Offset(0f, 0f))

    var rightEyeCenter by mutableStateOf(Offset(0f, 0f))

    var leftEyeOffset by mutableStateOf(Offset.Zero)

    var rightEyeOffset by mutableStateOf(Offset.Zero)

    var horizontalLeftSpace by mutableFloatStateOf(0f)

    var horizontalRightSpace by mutableFloatStateOf(0f)


    var leftEyeCloseSpace by mutableFloatStateOf(if (!isEyesOpen) maxCloseSpace else 0f)

    var rightEyeCloseSpace by mutableFloatStateOf(if (!isEyesOpen) maxCloseSpace else 0f)

    //机动矩形 方便计算随时拿来用
    val autoRect by mutableStateOf(Rect(0f, 0f, 0f, 0f))


    private suspend fun horizontalLeftOpen(duration: Int = 150) {
        launch {
            Animatable(horizontalLeftSpace).animateTo(0f, tween(duration)) {
                horizontalLeftSpace = value
            }
        }
    }

    private suspend fun horizontalRightOpen(duration: Int = 150) {
        launch {
            Animatable(horizontalLeftSpace).animateTo(0f, tween(duration)) {
                horizontalRightSpace = value
            }
        }
    }


    suspend fun openEyes(duration: Int = 80) {
        val anim = Animatable(leftEyeCloseSpace)
        horizontalLeftOpen()
        horizontalRightOpen()
        anim.animateTo(0f, tween(duration)) {
            leftEyeCloseSpace = value
            rightEyeCloseSpace = value
        }
    }


    private suspend fun horizontalLeftClose(space: Float, duration: Int = 150) {
        launch {
            Animatable(horizontalLeftSpace).animateTo(
                horizontalMaxSpace * (space / maxCloseSpace),
                tween(duration)
            ) {
                horizontalLeftSpace = value
            }
        }
    }

    private suspend fun horizontalRightClose(space: Float, duration: Int = 150) {
        launch {
            Animatable(horizontalRightSpace).animateTo(
                horizontalMaxSpace * (space / maxCloseSpace),
                tween(duration)
            ) {
                horizontalRightSpace = value
            }
        }
    }

    suspend fun closeEyes(space: Float = maxCloseSpace, duration: Int = 80) {
        val anim = Animatable(0f)
        horizontalLeftClose(space, duration)
        horizontalRightClose(space,duration)
        anim.animateTo(space, tween(duration)) {
            leftEyeCloseSpace = value
            rightEyeCloseSpace = value
        }
    }

    suspend fun wink() {
        leftEyeCloseSpace = 0f
        rightEyeCloseSpace = 0f
        closeEyes()
        openEyes()
    }

    suspend fun lookRight(right: Float, duration: Int = 150) {
        leftEyeCloseSpace = 0f
        rightEyeCloseSpace = 0f
        launch {
            closeEyes(horizontalMaxSpace)
        }
        launch {
            Animatable(leftEyeOffset.x).animateTo(leftEyeOffset.x + right, tween(duration)) {
                leftEyeOffset = leftEyeOffset.copy(x = value)
                rightEyeOffset = rightEyeOffset.copy(x = value)
            }
        }
        delay(70)
        openEyes()

    }

    suspend fun lookLeft(left: Float, duration: Int = 150) {
        leftEyeCloseSpace = 0f
        rightEyeCloseSpace = 0f
        launch {
            closeEyes(horizontalMaxSpace)
        }
        launch {
            Animatable(leftEyeOffset.x).animateTo(leftEyeOffset.x - left, tween(duration)) {
                leftEyeOffset = leftEyeOffset.copy(x = value)
                rightEyeOffset = rightEyeOffset.copy(x = value)
            }
        }
        delay(70)
        openEyes()
    }

    suspend fun badLaugh() {
        drawMode = drawMode_1
        closeEyes(maxCloseSpace)
        delay(1000)
        openEyes()
        drawMode = drawMode_normal
    }


    suspend fun closeLeftEye(space: Float = maxCloseSpace / 2f) {
        horizontalLeftClose(space)
        Animatable(leftEyeCloseSpace).animateTo(leftEyeCloseSpace + space) {
            leftEyeCloseSpace = value
        }
    }

    suspend fun closeRightEye(space: Float = maxCloseSpace / 2f) {
        horizontalRightClose(space)
        Animatable(rightEyeCloseSpace).animateTo(rightEyeCloseSpace + space) {
            rightEyeCloseSpace = value
        }
    }

    suspend fun openLeftEye() {
        horizontalLeftOpen()
        Animatable(leftEyeCloseSpace).animateTo(0f) {
            leftEyeCloseSpace = value
        }
    }

    suspend fun openRightEye() {
        horizontalRightOpen()
        Animatable(rightEyeCloseSpace).animateTo(0f) {
            rightEyeCloseSpace = value
        }
    }

    suspend fun closeCompletely() {
        closeEyes(eyeRadius)
    }


    suspend fun lookAround() {
        lookLeft(150f)
        delay(500)
        lookRight(300f)
        delay(500)
        lookLeft(150f)
    }


    private fun launch(block: suspend () -> Unit) {
        scope?.launch {
            block()
        }
    }

    private val duration = tween<Float>(100)
}