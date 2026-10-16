package com.muzafferus.imagelist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ImageListLayoutTest {
    @get:Rule
    val rule = createComposeRule()

    private val five = listOf("a", "b", "c", "d", "e")

    private fun content(
        items: List<String> = five,
        maxVisible: Int = 3,
        overlap: Overlap = Overlap.fraction(0.5f),
        zOrder: ZOrder = ZOrder.FirstOnTop,
        overflow: (@androidx.compose.runtime.Composable (Int) -> Unit)? = { ImageListDefaults.OverflowBadge(it) },
    ) {
        rule.setContent {
            ImageList(
                items = items,
                maxVisible = maxVisible,
                itemSize = 40.dp,
                overlap = overlap,
                zOrder = zOrder,
                overflowContent = overflow,
                modifier = Modifier.testTag("list"),
            ) { item, index -> Box(Modifier.fillMaxSize().testTag("item$index")) }
        }
    }

    @Test
    fun showsOnlyMaxVisibleItemsAndHiddenCountBadge() {
        content()
        rule.onNodeWithTag("item0").assertIsDisplayed()
        rule.onNodeWithTag("item2").assertIsDisplayed()
        rule.onNodeWithTag("item3").assertDoesNotExist()
        rule.onNodeWithText("+2").assertIsDisplayed()
    }

    @Test
    fun noBadgeWhenEverythingFits() {
        content(items = listOf("a", "b"))
        rule.onNodeWithTag("item1").assertIsDisplayed()
        rule.onNodeWithText("+0").assertDoesNotExist()
    }

    @Test
    fun noBadgeWhenOverflowContentIsNull() {
        content(overflow = null)
        rule.onNodeWithText("+2").assertDoesNotExist()
        rule.onNodeWithTag("list").assertWidthIsEqualTo(40.dp + 20.dp * 2)
    }

    @Test
    fun maxVisibleZeroShowsOnlyTheBadge() {
        content(maxVisible = 0)
        rule.onNodeWithTag("item0").assertDoesNotExist()
        rule.onNodeWithText("+5").assertIsDisplayed()
    }

    @Test
    fun negativeMaxVisibleIsTreatedAsZero() {
        content(maxVisible = -4)
        rule.onNodeWithTag("item0").assertDoesNotExist()
        rule.onNodeWithText("+5").assertIsDisplayed()
    }

    @Test
    fun unlimitedShowsEveryItem() {
        content(maxVisible = ImageListDefaults.Unlimited)
        rule.onNodeWithTag("item4").assertIsDisplayed()
    }

    @Test
    fun emptyListHasNoSize() {
        content(items = emptyList())
        rule.onNodeWithTag("list").assertWidthIsEqualTo(0.dp).assertHeightIsEqualTo(0.dp)
    }

    @Test
    fun halfOverlapPlacesItemsAtHalfStep() {
        content()
        rule.onNodeWithTag("item0").assertLeftIs(0.dp).assertWidthIsEqualTo(40.dp).assertHeightIsEqualTo(40.dp)
        rule.onNodeWithTag("item1").assertLeftIs(20.dp)
        rule.onNodeWithTag("item2").assertLeftIs(40.dp)
        // 3 images + badge = 4 slots -> 3 steps of 20dp + one item
        rule.onNodeWithTag("list").assertWidthIsEqualTo(100.dp)
    }

    @Test
    fun noOverlapPlacesItemsEdgeToEdge() {
        content(overlap = Overlap.None)
        rule.onNodeWithTag("item1").assertLeftIs(40.dp)
        rule.onNodeWithTag("list").assertWidthIsEqualTo(160.dp)
    }

    @Test
    fun negativeFractionLeavesAGap() {
        content(overlap = Overlap.fraction(-0.25f))
        rule.onNodeWithTag("item1").assertLeftIs(50.dp)
    }

    @Test
    fun verticalOverlapStacksDownwards() {
        content(overlap = Overlap.vertical(0.5f))
        rule.onNodeWithTag("item1").assertLeftIs(0.dp).assertTopIs(20.dp)
        rule.onNodeWithTag("list").assertWidthIsEqualTo(40.dp).assertHeightIsEqualTo(100.dp)
    }

    @Test
    fun customOverlapAndNegativeOffsetsAreNormalised() {
        content(overlap = { index, _, _, _ -> androidx.compose.ui.unit.IntOffset(-index * 10, 0) })
        // offsets are 0,-10,-20,-30 (badge): everything is shifted right by 30
        rule.onNodeWithTag("item0").assertLeftIs(30.dp)
        rule.onNodeWithTag("item2").assertLeftIs(10.dp)
    }

    @Test
    fun rtlMirrorsTheOrder() {
        rule.setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                ImageList(items = five, itemSize = 40.dp) { _, index -> Box(Modifier.fillMaxSize().testTag("item$index")) }
            }
        }
        val first = rule.onNodeWithTag("item0").getUnclippedBoundsInRoot().left
        val second = rule.onNodeWithTag("item1").getUnclippedBoundsInRoot().left
        assertEquals(true, first > second)
    }

    @Test
    fun itemContentChangesAreReflectedWhenListShrinks() {
        var items by androidx.compose.runtime.mutableStateOf(five)
        rule.setContent {
            ImageList(items = items, itemSize = 40.dp) { _, index -> Box(Modifier.fillMaxSize().testTag("item$index")) }
        }
        rule.onNodeWithText("+2").assertIsDisplayed()
        items = listOf("a")
        rule.waitForIdle()
        rule.onNodeWithText("+2").assertDoesNotExist()
        rule.onNodeWithTag("item1").assertDoesNotExist()
        rule.onNodeWithTag("item0").assertIsDisplayed()
    }

    private fun SemanticsNodeInteraction.assertLeftIs(expected: Dp): SemanticsNodeInteraction {
        assertEquals(expected.value, getUnclippedBoundsInRoot().left.value, 0.6f)
        return this
    }

    private fun SemanticsNodeInteraction.assertTopIs(expected: Dp): SemanticsNodeInteraction {
        assertEquals(expected.value, getUnclippedBoundsInRoot().top.value, 0.6f)
        return this
    }
}
