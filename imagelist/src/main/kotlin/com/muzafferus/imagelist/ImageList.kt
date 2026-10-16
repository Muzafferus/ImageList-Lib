package com.muzafferus.imagelist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage

/**
 * Shows [items] side by side (overlapping by default) and folds the ones that do not fit into
 * a "+N" badge.
 *
 * Everything visual is yours: [itemContent] draws one item, [overflowContent] draws the badge,
 * [overlap] decides how items are arranged. Each slot is forced to be an [itemSize] square, so
 * your content simply fills it; clip it with any [Shape] (circle, rounded square, star...).
 *
 * @param items data to show
 * @param maxVisible how many items are drawn before the rest are folded into the badge.
 *   Negative values are treated as 0. Use [ImageListDefaults.Unlimited] to show everything.
 * @param itemSize side of every (square) slot
 * @param overlap placement strategy, see [Overlap]
 * @param zOrder which item is on top when they overlap (the overflow badge is always above all items)
 * @param contentDescription accessibility description of the whole list (optional)
 * @param overflowContent badge for the hidden items; `null` to draw no badge at all
 * @param itemContent draws a single item
 */
@Composable
public fun <T> ImageList(
    items: List<T>,
    modifier: Modifier = Modifier,
    maxVisible: Int = ImageListDefaults.MaxVisible,
    itemSize: Dp = ImageListDefaults.ItemSize,
    overlap: Overlap = ImageListDefaults.Overlap,
    zOrder: ZOrder = ZOrder.FirstOnTop,
    contentDescription: String? = null,
    overflowContent: (@Composable (hiddenCount: Int) -> Unit)? = { ImageListDefaults.OverflowBadge(it) },
    itemContent: @Composable (item: T, index: Int) -> Unit,
) {
    val visible = maxVisible.coerceIn(0, items.size)
    val hidden = items.size - visible
    val semanticsModifier = if (contentDescription != null) {
        modifier.semantics { this.contentDescription = contentDescription }
    } else {
        modifier
    }

    Layout(
        modifier = semanticsModifier,
        content = {
            for (index in 0 until visible) {
                key(index) {
                    Box(propagateMinConstraints = true) { itemContent(items[index], index) }
                }
            }
            if (hidden > 0 && overflowContent != null) {
                Box(propagateMinConstraints = true) { overflowContent(hidden) }
            }
        },
    ) { measurables, _ ->
        val sizePx = itemSize.roundToPx().coerceAtLeast(0)
        val childConstraints = Constraints.fixed(sizePx, sizePx)
        val placeables = measurables.map { it.measure(childConstraints) }
        val count = placeables.size
        val hasBadge = hidden > 0 && overflowContent != null

        val offsets = List(count) { overlap.offset(it, count, sizePx, this) }
        // Strategies may return negative values; normalise so nothing is clipped.
        val minX = offsets.minOfOrNull { it.x } ?: 0
        val minY = offsets.minOfOrNull { it.y } ?: 0
        val width = if (count == 0) 0 else offsets.maxOf { it.x } - minX + sizePx
        val height = if (count == 0) 0 else offsets.maxOf { it.y } - minY + sizePx

        layout(width, height) {
            placeables.forEachIndexed { index, placeable ->
                val isBadge = hasBadge && index == count - 1
                val z = when {
                    // The badge is always on top, otherwise its text would be hidden by the items.
                    isBadge -> count + 1f
                    zOrder == ZOrder.FirstOnTop -> (count - index).toFloat()
                    else -> index.toFloat()
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
 * @param placeholder painter shown while loading
 * @param error painter shown when loading fails
 * @param itemContentDescription accessibility description of the item at the given index
 */
@Composable
public fun UrlImageList(
    urls: List<String>,
    modifier: Modifier = Modifier,
    maxVisible: Int = ImageListDefaults.MaxVisible,
    itemSize: Dp = ImageListDefaults.ItemSize,
    overlap: Overlap = ImageListDefaults.Overlap,
    zOrder: ZOrder = ZOrder.FirstOnTop,
    shape: Shape = CircleShape,
    border: BorderStroke? = null,
    placeholder: Painter? = null,
    error: Painter? = null,
    contentDescription: String? = null,
    itemContentDescription: (index: Int) -> String? = { null },
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
        contentDescription = contentDescription,
        overflowContent = overflowContent,
    ) { url, index ->
        AsyncImage(
            model = url,
            contentDescription = itemContentDescription(index),
            contentScale = ContentScale.Crop,
            placeholder = placeholder,
            error = error,
            modifier = Modifier
                .clip(shape)
                .then(if (border != null) Modifier.border(border, shape) else Modifier),
        )
    }
}
