package com.muzafferus.imagelist

import android.graphics.Color
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ImageListViewTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val density get() = rule.activity.resources.displayMetrics.density
    private fun dp(value: Float) = (value * density).toInt()

    private fun attach(view: ImageListView) {
        rule.runOnUiThread {
            val frame = FrameLayout(rule.activity)
            frame.addView(view, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
            rule.activity.setContentView(frame)
        }
        rule.waitForIdle()
    }

    @Test
    fun propertiesCanBeChangedInAnyOrderAndTheViewUpdates() {
        val view = ImageListView(rule.activity)
        view.items = listOf(1, 2, 3, 4, 5)
        view.itemContent = { _, _ -> Box(Modifier.fillMaxSize()) }
        view.itemSizeDp = 40f
        view.maxVisible = 3
        attach(view)
        // 3 items + badge, half overlap
        assertEquals(dp(100f), view.width)

        rule.runOnUiThread { view.maxVisible = 1 }
        rule.waitForIdle()
        assertEquals(dp(60f), view.width) // 1 item + badge

        rule.runOnUiThread { view.maxVisible = -1 }
        rule.waitForIdle()
        assertEquals(dp(120f), view.width) // 5 items, no badge
    }

    @Test
    fun badgeCanBeHidden() {
        val view = ImageListView(rule.activity)
        view.items = listOf(1, 2, 3)
        view.itemContent = { _, _ -> Box(Modifier.fillMaxSize()) }
        view.itemSizeDp = 40f
        view.maxVisible = 1
        view.showBadge = false
        attach(view)
        assertEquals(dp(40f), view.width)
    }

    @Test
    fun customBadgeContentIsUsed() {
        val view = ImageListView(rule.activity)
        view.items = listOf(1, 2, 3)
        view.itemContent = { _, _ -> Box(Modifier.fillMaxSize()) }
        view.badgeContent = { androidx.compose.foundation.text.BasicText("more:$it") }
        view.maxVisible = 1
        attach(view)
        rule.onNodeWithText("more:2").assertIsDisplayed()
    }

    @Test
    fun customOverlapOverridesFractionAndOrientation() {
        val view = ImageListView(rule.activity)
        view.items = listOf(1, 2)
        view.itemContent = { _, _ -> Box(Modifier.fillMaxSize()) }
        view.itemSizeDp = 40f
        view.isVertical = true
        view.overlap = Overlap.None
        attach(view)
        assertEquals(dp(80f), view.width)
        assertEquals(dp(40f), view.height)
    }

    @Test
    fun xmlAttributesAreParsed() {
        val attrs = Robolectric.buildAttributeSet()
            .addAttribute(R.attr.ilMaxVisible, "2")
            .addAttribute(R.attr.ilItemSize, "40dp")
            .addAttribute(R.attr.ilOverlap, "0.25")
            .addAttribute(R.attr.ilOrientation, "vertical")
            .addAttribute(R.attr.ilZOrder, "lastOnTop")
            .addAttribute(R.attr.ilShape, "star")
            .addAttribute(R.attr.ilBorderWidth, "2dp")
            .addAttribute(R.attr.ilBorderColor, "#FF0000")
            .addAttribute(R.attr.ilShowBadge, "false")
            .addAttribute(R.attr.ilBadgeColor, "#00FF00")
            .addAttribute(R.attr.ilBadgeTextColor, "#0000FF")
            .build()
        val view = ImageListView(rule.activity, attrs)

        assertEquals(2, view.maxVisible)
        assertEquals(40f, view.itemSizeDp, 0.01f)
        assertEquals(0.25f, view.overlapFraction, 0.001f)
        assertTrue(view.isVertical)
        assertEquals(ZOrder.LastOnTop, view.zOrder)
        assertEquals(2f, view.borderWidthDp, 0.01f)
        assertEquals(Color.RED, view.borderColor)
        assertEquals(false, view.showBadge)
        assertEquals(Color.GREEN, view.badgeColor)
        assertEquals(Color.BLUE, view.badgeTextColor)
        assertTrue(view.shape !== CircleShape)
    }

    @Test
    fun defaultsMatchTheComposeApi() {
        val view = ImageListView(rule.activity)
        assertEquals(ImageListDefaults.MaxVisible, view.maxVisible)
        assertEquals(ImageListDefaults.ItemSize.value, view.itemSizeDp, 0.01f)
        assertEquals(0.5f, view.overlapFraction, 0.001f)
        assertEquals(ZOrder.FirstOnTop, view.zOrder)
        assertTrue(view.showBadge)
    }
}
