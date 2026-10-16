package com.muzafferus.imagelist

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset

/**
 * Decides where each item of an [ImageList] is placed.
 *
 * [offset] returns the top-left position of item [index] relative to the first item.
 * The x axis is "start-relative": in RTL layouts [ImageList] mirrors it automatically.
 *
 * Write your own to get any arrangement (stair, fan, arc, ...):
 * ```
 * val stair = Overlap { index, _, size, _ -> IntOffset(index * size / 2, index * size / 6) }
 * ```
 */
public fun interface Overlap {
    /**
     * @param index position of the item, `0 until count`
     * @param count number of placed items (including the overflow badge, if any)
     * @param itemSizePx side of the (square) item in pixels
     * @param density use it to convert dp values
     */
    public fun offset(index: Int, count: Int, itemSizePx: Int, density: Density): IntOffset

    public companion object {
        /** Items are placed side by side without overlapping. */
        public val None: Overlap = Overlap { index, _, size, _ -> IntOffset(index * size, 0) }

        /**
         * Every item covers [fraction] of the previous one horizontally:
         * `0f` = no overlap, `0.5f` = half, `1f` = fully stacked. A negative value leaves a gap.
         */
        public fun fraction(fraction: Float): Overlap {
            require(fraction.isFinite()) { "fraction must be finite" }
            val step = 1f - fraction
            return Overlap { index, _, size, _ -> IntOffset((index * size * step).toInt(), 0) }
        }

        /** Every item covers the previous one by exactly [amount] horizontally. A negative value leaves a gap. */
        public fun fixed(amount: Dp): Overlap = Overlap { index, _, size, density ->
            IntOffset(index * (size - with(density) { amount.roundToPx() }), 0)
        }

        /** Same as [fraction] but stacked vertically. */
        public fun vertical(fraction: Float): Overlap {
            require(fraction.isFinite()) { "fraction must be finite" }
            val step = 1f - fraction
            return Overlap { index, _, size, _ -> IntOffset(0, (index * size * step).toInt()) }
        }
    }
}

/** Which item is drawn on top when items overlap. The "+N" badge is always above every item. */
public enum class ZOrder {
    /** The first item is on top, every next one goes below the previous. */
    FirstOnTop,

    /** The last item is on top (like a stack of cards). */
    LastOnTop,
}
