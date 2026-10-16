package com.muzafferus.imagelist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object ImageListDefaults {
    const val MaxVisible: Int = 3
    const val Unlimited: Int = Int.MAX_VALUE
    val ItemSize = 50.dp
    val Overlap: com.muzafferus.imagelist.Overlap = com.muzafferus.imagelist.Overlap.fraction(0.5f)

    /** The default "+N" badge. Replace it through `overflowContent` for a custom one. */
    @Composable
    fun OverflowBadge(
        hiddenCount: Int,
        modifier: Modifier = Modifier,
        shape: Shape = CircleShape,
        containerColor: Color = Color(0xFF00574B),
        contentColor: Color = Color.White,
        textStyle: TextStyle = TextStyle(fontSize = 12.sp),
        border: BorderStroke? = null,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clip(shape)
                .background(containerColor)
                .then(if (border != null) Modifier.border(border, shape) else Modifier),
            contentAlignment = Alignment.Center,
        ) {
            BasicText("+$hiddenCount", style = textStyle.copy(color = contentColor))
        }
    }
}
