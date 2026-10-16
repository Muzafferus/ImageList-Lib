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
public fun StarShape(points: Int = 5, innerRadiusRatio: Float = 0.5f): Shape {
    require(points >= 3) { "A star needs at least 3 points" }
    require(innerRadiusRatio in 0f..1f) { "innerRadiusRatio must be in 0..1" }
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

/** A regular polygon with [sides] sides (3 = triangle, 6 = hexagon, ...). */
public fun PolygonShape(sides: Int = 6, rotationDegrees: Float = -90f): Shape {
    require(sides >= 3) { "A polygon needs at least 3 sides" }
    return GenericShape { size, _ ->
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = minOf(cx, cy)
        val step = 2 * PI / sides
        val start = rotationDegrees * PI / 180
        for (i in 0 until sides) {
            val angle = start + i * step
            val x = cx + (radius * cos(angle)).toFloat()
            val y = cy + (radius * sin(angle)).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
}

/** A heart. */
public fun HeartShape(): Shape = GenericShape { size, _ ->
    val w = size.width
    val h = size.height
    moveTo(w / 2f, h * 0.95f)
    cubicTo(-w * 0.15f, h * 0.55f, w * 0.1f, -h * 0.05f, w / 2f, h * 0.28f)
    cubicTo(w * 0.9f, -h * 0.05f, w * 1.15f, h * 0.55f, w / 2f, h * 0.95f)
    close()
}
