package com.muzafferus.imagelist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage

/**
 * Shows [items] side by side (overlapping by default) and collapses the ones that do not
 * fit into a "+N" badge.
 *
 * Everything visual is yours: [itemContent] draws one item, [overflowContent] draws the badge,
 * [overlap] decides how items are arranged. Each slot is forced to be an [itemSize] square, so
 * your content simply fills it; clip it with any [Shape] (circle, rounded square, star...).
 *
 * @param items data to show
 * @param maxVisible how many items are drawn before the rest are folded into the badge.
 *   Use [Int.MAX_VALUE] (default of [ImageListDefaults.Unlimited]) to show everything.
 * @param itemSize side of every (square) slot
 * @param overlap placement strategy, see [Overlap]
 * @param zOrder which item is on top when they overlap
 * @param overflowContent badge for the hidden items; `null` to draw no badge at all
 * @param itemContent draws a single item
 */
@Composable
fun <T> ImageList(
    items: List<T>,
    modifier: Modifier = Modifier,
    maxVisible: Int = ImageListDefaults.MaxVisible,
    itemSize: Dp = ImageListDefaults.ItemSize,
    overlap: Overlap = ImageListDefaults.Overlap,
    zOrder: ZOrder = ZOrder.FirstOnTop,
    overflowContent: (@Composable (hiddenCount: Int) -> Unit)? = { ImageListDefaults.OverflowBadge(it) },
    itemContent: @Composable (item: T, index: Int) -> Unit,
) {
    val visible = maxVisible.coerceAtLeast(0).coerceAtMost(items.size)
    val hidden = items.size - visible

    Layout(
        modifier = modifier,
        content = {
            for (index in 0 until visible) {
                Box(propagateMinConstraints = true) { itemContent(items[index], index) }
            }
            if (hidden > 0 && overflowContent != null) {
                Box(propagateMinConstraints = true) { overflowContent(hidden) }
            }
        },
    ) { measurables, _ ->
        val sizePx = itemSize.roundToPx()
        val childConstraints = Constraints.fixed(sizePx, sizePx)
        val placeables = measurables.map { it.measure(childConstraints) }
        val count = placeables.size

        val offsets = List(count) { overlap.offset(it, count, sizePx, this) }
        // Strategies may return negative values, normalise so nothing is clipped.
        val minX = offsets.minOfOrNull { it.x } ?: 0
        val minY = offsets.minOfOrNull { it.y } ?: 0
        val width = (offsets.maxOfOrNull { it.x } ?: 0) - minX + if (count > 0) sizePx else 0
        val height = (offsets.maxOfOrNull { it.y } ?: 0) - minY + if (count > 0) sizePx else 0

        layout(width, height) {
            placeables.forEachIndexed { index, placeable ->
                val z = when (zOrder) {
                    ZOrder.FirstOnTop -> (count - index).toFloat()
                    ZOrder.LastOnTop -> index.toFloat()
                }
                placeable.placeRelative(offsets[index].x - minX, offsets[index].y - minY, z)
            }
        }
    }
}

/**
 * Convenience overload for the classic "avatars from URLs" case. Images are loaded with Coil.
 *
 * @param shape any [Shape]: `CircleShape`, `RoundedCornerShape(8.dp)`, [StarShape], your own...
 * @param border optional outline drawn with the same [shape]
 */
@Composable
fun UrlImageList(
    urls: List<String>,
    modifier: Modifier = Modifier,
    maxVisible: Int = ImageListDefaults.MaxVisible,
    itemSize: Dp = ImageListDefaults.ItemSize,
    overlap: Overlap = ImageListDefaults.Overlap,
    zOrder: ZOrder = ZOrder.FirstOnTop,
    shape: Shape = CircleShape,
    border: BorderStroke? = null,
    overflowContent: (@Composable (hiddenCount: Int) -> Unit)? = {
        ImageListDefaults.OverflowBadge(it, shape = shape, border = border)
    },
) {
    ImageList(
        items = urls,
        modifier = modifier,
        maxVisible = maxVisible,
        itemSize = itemSize,
        overlap = overlap,
        zOrder = zOrder,
        overflowContent = overflowContent,
    ) { url, _ ->
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(shape)
                .then(if (border != null) Modifier.border(border, shape) else Modifier),
        )
    }
}
