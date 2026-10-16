package com.muzafferus.imagelist

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * View-system (XML) entry point of the library. It hosts [ImageList] inside a classic `View`,
 * so it can be used from layouts, `ViewBinding` and `RecyclerView` items.
 *
 * ```xml
 * <com.muzafferus.imagelist.ImageListView
 *     android:layout_width="wrap_content"
 *     android:layout_height="wrap_content"
 *     app:ilMaxVisible="3"
 *     app:ilItemSize="50dp"
 *     app:ilShape="circle" />
 * ```
 * ```kotlin
 * imageListView.imageUrls = listOf("https://…", "https://…")
 * ```
 *
 * Every property can be changed at any time, in any order, and the view updates itself.
 */
public class ImageListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private val density = resources.displayMetrics.density
    private val scaledDensity = density * resources.configuration.fontScale

    /** Image urls to show (loaded with Coil). Ignored while [itemContent] is set. */
    public var imageUrls: List<String> by mutableStateOf(emptyList())

    /** Number of items drawn before the rest are folded into "+N". `-1` shows all. */
    public var maxVisible: Int by mutableStateOf(ImageListDefaults.MaxVisible)

    /** Side of every (square) item, in dp. */
    public var itemSizeDp: Float by mutableStateOf(ImageListDefaults.ItemSize.value)

    /** How much every item covers the previous one: `0` none, `0.5` half, `1` fully; negative leaves a gap. */
    public var overlapFraction: Float by mutableStateOf(0.5f)

    /** Stack vertically instead of horizontally. */
    public var isVertical: Boolean by mutableStateOf(false)

    /** Fully custom placement; when set it replaces [overlapFraction] and [isVertical]. */
    public var overlap: Overlap? by mutableStateOf(null)

    public var zOrder: ZOrder by mutableStateOf(ZOrder.FirstOnTop)

    /** Clip shape of the images and the badge. Any Compose [Shape] works. */
    public var shape: Shape by mutableStateOf(CircleShape)

    /** Border width in dp, `0` for none. */
    public var borderWidthDp: Float by mutableStateOf(0f)

    @get:ColorInt
    public var borderColor: Int by mutableStateOf(android.graphics.Color.BLACK)

    public var showBadge: Boolean by mutableStateOf(true)

    @get:ColorInt
    public var badgeColor: Int by mutableStateOf(ImageListDefaults.BadgeColor.toArgbCompat())

    @get:ColorInt
    public var badgeTextColor: Int by mutableStateOf(android.graphics.Color.WHITE)

    /** Badge text size in sp. */
    public var badgeTextSizeSp: Float by mutableStateOf(12f)

    /**
     * Optional custom item UI. Gets the entry of [items] and its index; the slot is an
     * [itemSizeDp] square, so just fill it.
     */
    public var itemContent: (@Composable (item: Any, index: Int) -> Unit)? by mutableStateOf(null)

    /** Data for [itemContent]. */
    public var items: List<Any> by mutableStateOf(emptyList())

    /** Optional custom badge UI; gets the number of hidden items. */
    public var badgeContent: (@Composable (hiddenCount: Int) -> Unit)? by mutableStateOf(null)

    init {
        // Safe for RecyclerView: the composition is dropped when the view is detached or recycled.
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool)
        if (attrs != null) readAttributes(attrs, defStyleAttr)
    }

    /** Convenience for `borderWidthDp` + `borderColor`. */
    public fun setBorder(widthDp: Float, @ColorInt color: Int) {
        borderWidthDp = widthDp
        borderColor = color
    }

    private fun readAttributes(attrs: AttributeSet, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ImageListView, defStyleAttr, 0)
        try {
            maxVisible = a.getInt(R.styleable.ImageListView_ilMaxVisible, maxVisible)
            if (a.hasValue(R.styleable.ImageListView_ilItemSize)) {
                itemSizeDp = a.getDimension(R.styleable.ImageListView_ilItemSize, 0f) / density
            }
            overlapFraction = a.getFloat(R.styleable.ImageListView_ilOverlap, overlapFraction)
            isVertical = a.getInt(R.styleable.ImageListView_ilOrientation, 0) == 1
            zOrder = if (a.getInt(R.styleable.ImageListView_ilZOrder, 0) == 1) ZOrder.LastOnTop else ZOrder.FirstOnTop
            val cornerDp = a.getDimension(R.styleable.ImageListView_ilCornerRadius, 8f * density) / density
            shape = when (a.getInt(R.styleable.ImageListView_ilShape, 0)) {
                1 -> RectangleShape
                2 -> RoundedCornerShape(cornerDp.dp)
                3 -> StarShape()
                4 -> PolygonShape(6)
                5 -> HeartShape()
                else -> CircleShape
            }
            borderWidthDp = a.getDimension(R.styleable.ImageListView_ilBorderWidth, 0f) / density
            borderColor = a.getColor(R.styleable.ImageListView_ilBorderColor, borderColor)
            showBadge = a.getBoolean(R.styleable.ImageListView_ilShowBadge, true)
            badgeColor = a.getColor(R.styleable.ImageListView_ilBadgeColor, badgeColor)
            badgeTextColor = a.getColor(R.styleable.ImageListView_ilBadgeTextColor, badgeTextColor)
            if (a.hasValue(R.styleable.ImageListView_ilBadgeTextSize)) {
                badgeTextSizeSp = a.getDimension(R.styleable.ImageListView_ilBadgeTextSize, 0f) / scaledDensity
            }
        } finally {
            a.recycle()
        }
    }

    @Composable
    override fun Content() {
        val border = if (borderWidthDp > 0f) BorderStroke(borderWidthDp.dp, Color(borderColor)) else null
        val placement = overlap
            ?: if (isVertical) Overlap.vertical(overlapFraction) else Overlap.fraction(overlapFraction)
        val limit = if (maxVisible < 0) ImageListDefaults.Unlimited else maxVisible
        val badge: (@Composable (Int) -> Unit)? = when {
            !showBadge -> null
            else -> badgeContent ?: { hidden ->
                ImageListDefaults.OverflowBadge(
                    hiddenCount = hidden,
                    shape = shape,
                    containerColor = Color(badgeColor),
                    contentColor = Color(badgeTextColor),
                    textStyle = TextStyle(fontSize = badgeTextSizeSp.sp),
                    border = border,
                )
            }
        }

        val custom = itemContent
        if (custom != null) {
            ImageList(
                items = items,
                modifier = Modifier,
                maxVisible = limit,
                itemSize = itemSizeDp.dp,
                overlap = placement,
                zOrder = zOrder,
                overflowContent = badge,
                itemContent = custom,
            )
        } else {
            UrlImageList(
                urls = imageUrls,
                maxVisible = limit,
                itemSize = itemSizeDp.dp,
                overlap = placement,
                zOrder = zOrder,
                shape = shape,
                border = border,
                overflowContent = badge,
            )
        }
    }
}

private fun Color.toArgbCompat(): Int = toArgb()
