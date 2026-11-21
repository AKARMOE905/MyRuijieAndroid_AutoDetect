package com.example.myruijie

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.random.Random

@Composable
fun HackerCodeRain() {
    val transition = rememberInfiniteTransition(label = "hacker")
    val progress = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "codeRain"
    )

    val columns = remember {
        List(7) {
            Random(it * 31).nextInt(0, 10000)
        }
    }

    Canvas(Modifier.fillMaxSize()) {
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.rgb(0, 255, 45)
            textSize = 48f
            alpha = 255
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.MONOSPACE, android.graphics.Typeface.BOLD)
            isAntiAlias = true
            setShadowLayer(20f, 0f, 0f, android.graphics.Color.rgb(0, 255, 45))
        }

        val bluePaint = android.graphics.Paint(paint).apply {
            color = android.graphics.Color.rgb(40, 170, 255)
            alpha = 255
        }

        val canvas = drawContext.canvas.nativeCanvas
        val spacing = size.width / columns.size

        columns.forEachIndexed { index, seed ->
            val x = index * spacing
            val offset = (progress.value * size.height * 2 +
                seed % 500) % (size.height + 250)

            repeat(9) { line ->
                val y = offset - line * 60f
                if (y in 0f..size.height) {
                    val char = "01ABCDEF{}[]<>".get(
                        (seed + line * 7 + index) % 14
                    ).toString()
                    canvas.drawText(
                        char,
                        x,
                        y,
                        paint
                    )
                }
            }
        }
    }
}
