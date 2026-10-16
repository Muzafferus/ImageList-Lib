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

/** Default values used by [ImageList] and [UrlImageList]. */
public object ImageListDefaults {
    public const val MaxVisible: Int = 3
    public const val Unlimited: Int = Int.MAX_VALUE
    public val ItemSize: androidx.compose.ui.unit.Dp = 50.dp
    public val Overlap: com.muzafferus.imagelist.Overlap = com.muzafferus.imagelist.Overlap.fraction(0.5f)
    public val BadgeColor: Color = Color(0xFF00574B)

    /** The default "+N" badge. Replace it through `overflowContent` for a custom one. */
    @Composable
    public fun OverflowBadge(
        hiddenCount: Int,
        modifier: Modifier = Modifier,
        shape: Shape = CircleShape,
        containerColor: Color = BadgeColor,
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
