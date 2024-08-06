package cn.zzs.xiaoai.face.ui.face

import android.content.Context
import android.graphics.Paint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.toRect

sealed class EyesState {

    data object Close : EyesState()

    data object Open : EyesState()
}


@Composable
fun rememberFaceState(
    eyesRadiusFactor: Float = 0.25f,
    eyesRadius: Float = -1f,
    isEyeOpen: Boolean = false,
    context: Context = LocalContext.current
): FaceState {
    return remember {
        FaceState(context, eyesRadiusFactor, eyesRadius, isEyeOpen)
    }
}


@Composable
fun Face(state: FaceState = rememberFaceState()) {
    val scope = rememberCoroutineScope()
    state.scope = scope
    Box(modifier = Modifier
        .fillMaxSize()
        .drawWithCache {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).asComposePaint()
            paint.style = PaintingStyle.Fill
            val leftEyePath = Path()
            val rightEyePath = Path()
            onDrawWithContent {
                val canvas = drawContext.canvas
                canvas.translate(size.width / 2f, size.height / 2f)
                when (state.drawMode) {
                    drawMode_normal -> {
                        normalDraw(state, canvas, paint)
                    }

                    drawMode_1 -> {
                        drawMode1(state, canvas, paint, leftEyePath, rightEyePath)
                    }
                }
            }

        })
}

fun drawMode1(
    state: FaceState,
    canvas: Canvas,
    paint: androidx.compose.ui.graphics.Paint,
    leftEyePath: Path,
    rightEyePath: Path
) {
    state.run {
        leftEyeCenter = leftEyeCenter.copy(-(eyeRadius + eyeRadius * 0.95f) + leftEyeOffset.x, 0f)
        rightEyeCenter = rightEyeCenter.copy(eyeRadius + eyeRadius * 0.95f + rightEyeOffset.x, 0f)
        leftEyeRect.set(
            leftEyeCenter.x - eyeRadius,
            leftEyeCenter.y - eyeRadius,
            leftEyeCenter.x + eyeRadius,
            leftEyeCenter.y + eyeRadius
        )
        rightEyeRect.set(
            rightEyeCenter.x - eyeRadius,
            rightEyeCenter.y - eyeRadius,
            rightEyeCenter.x + eyeRadius,
            rightEyeCenter.y + eyeRadius
        )
        canvas.translate(shadowOffset, shadowOffset)
        paint.color = state.shadowColor
        leftEyePath.rewind()
        leftEyePath.moveTo(leftEyeRect.centerX(), leftEyeRect.centerY())
        leftEyePath.addArc(leftEyeRect.toRect().toComposeRect(), 0f, -180f)
        val rectLeft = state.autoRect.copy(
            leftEyeRect.left,
            leftEyeRect.top + leftEyeCloseSpace * 0.8f,
            leftEyeRect.right,
            leftEyeRect.bottom - leftEyeCloseSpace * 0.8f
        )
        leftEyePath.addArc(rectLeft, 0f, 180f)
        rightEyePath.rewind()
        rightEyePath.moveTo(rightEyeRect.centerX(), rightEyeRect.centerY())
        rightEyePath.addArc(rightEyeRect.toRect().toComposeRect(), 0f, -180f)
        val rect = state.autoRect.copy(
            rightEyeRect.left,
            rightEyeRect.top + rightEyeCloseSpace * 0.8f,
            rightEyeRect.right,
            rightEyeRect.bottom - rightEyeCloseSpace * 0.8f
        )
        rightEyePath.addArc(rect, 0f, 180f)
//        drawPath.relativeQuadraticBezierTo(0f, 50f, 50f, 50f)
//        drawPath.relativeLineTo(eyeRadius * 2 - 100f, 0f)
//        drawPath.relativeQuadraticBezierTo(50f, 0f, 50f, -50f)
        canvas.drawPath(leftEyePath, paint)
        canvas.drawPath(rightEyePath, paint)
        canvas.translate(-shadowOffset, -shadowOffset)
        paint.color = state.eyeColor
        canvas.drawPath(leftEyePath, paint)
        canvas.drawPath(rightEyePath, paint)

    }

}


private fun normalDraw(
    state: FaceState,
    canvas: Canvas,
    paint: androidx.compose.ui.graphics.Paint
) {
    state.run {
        //眼间距是眼直径的0.9
        leftEyeCenter = leftEyeCenter.copy(-(eyeRadius + eyeRadius * 0.95f) + leftEyeOffset.x, 0f)
        rightEyeCenter = rightEyeCenter.copy(eyeRadius + eyeRadius * 0.95f + rightEyeOffset.x, 0f)
        leftEyeRect.set(
            leftEyeCenter.x - eyeRadius - horizontalLeftSpace,
            leftEyeCenter.y - eyeRadius + leftEyeCloseSpace,
            leftEyeCenter.x + eyeRadius + horizontalLeftSpace,
            leftEyeCenter.y + eyeRadius - leftEyeCloseSpace
        )
        rightEyeRect.set(
            rightEyeCenter.x - eyeRadius - horizontalRightSpace,
            rightEyeCenter.y - eyeRadius + rightEyeCloseSpace,
            rightEyeCenter.x + eyeRadius + horizontalRightSpace,
            rightEyeCenter.y + eyeRadius - rightEyeCloseSpace
        )
        paint.color = state.shadowColor
        leftEyeRect.offset(state.shadowOffset, state.shadowOffset)
        rightEyeRect.offset(state.shadowOffset, state.shadowOffset)
        canvas.drawRoundRect(
            leftEyeRect.left,
            leftEyeRect.top,
            leftEyeRect.right,
            leftEyeRect.bottom,
            radiusX = eyeRadius,
            radiusY = eyeRadius,
            paint
        )
        canvas.drawRoundRect(
            rightEyeRect.left,
            rightEyeRect.top,
            rightEyeRect.right,
            rightEyeRect.bottom,
            radiusX = eyeRadius,
            radiusY = eyeRadius,
            paint
        )
        paint.color = state.eyeColor
        leftEyeRect.offset(-state.shadowOffset, -state.shadowOffset)
        rightEyeRect.offset(-state.shadowOffset, -state.shadowOffset)
        canvas.drawRoundRect(
            leftEyeRect.left,
            leftEyeRect.top,
            leftEyeRect.right,
            leftEyeRect.bottom,
            radiusX = eyeRadius,
            radiusY = eyeRadius,
            paint
        )
        canvas.drawRoundRect(
            rightEyeRect.left,
            rightEyeRect.top,
            rightEyeRect.right,
            rightEyeRect.bottom,
            radiusX = eyeRadius,
            radiusY = eyeRadius,
            paint
        )
    }
}

