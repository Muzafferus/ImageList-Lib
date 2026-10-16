package com.muzafferus.imagelist

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.graphics.Shape
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A regular star with [points] tips. [innerRadiusRatio] (0..1) is how deep the notches are.
 * Works with `Modifier.clip(...)` and `Modifier.border(...)`.
 */
fun StarShape(points: Int = 5, innerRadiusRatio: Float = 0.5f): Shape {
    require(points >= 3) { "A star needs at least 3 points" }
    return GenericShape { size, _ ->
        val cx = size.width / 2f
        val cy = size.height / 2f
        val outer = minOf(cx, cy)
        val inner = outer * innerRadiusRatio
        val step = PI / points
        for (i in 0 until points * 2) {
            val radius = if (i % 2 == 0) outer else inner
            val angle = -PI / 2 + i * step
            val x = cx + (radius * cos(angle)).toFloat()
            val y = cy + (radius * sin(angle)).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
}
