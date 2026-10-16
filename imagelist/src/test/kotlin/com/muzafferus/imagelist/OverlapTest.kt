package com.muzafferus.imagelist

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class OverlapTest {
    private val density = Density(2f)

    @Test
    fun none_placesItemsEdgeToEdge() {
        assertEquals(IntOffset(0, 0), Overlap.None.offset(0, 3, 100, density))
        assertEquals(IntOffset(200, 0), Overlap.None.offset(2, 3, 100, density))
    }

    @Test
    fun fraction_half_overlapsByHalfTheSize() {
        val overlap = Overlap.fraction(0.5f)
        assertEquals(IntOffset(0, 0), overlap.offset(0, 4, 100, density))
        assertEquals(IntOffset(50, 0), overlap.offset(1, 4, 100, density))
        assertEquals(IntOffset(150, 0), overlap.offset(3, 4, 100, density))
    }

    @Test
    fun fraction_one_stacksEverything() {
        assertEquals(IntOffset(0, 0), Overlap.fraction(1f).offset(5, 6, 100, density))
    }

    @Test
    fun fixed_usesDpConvertedToPixels() {
        // 10.dp at density 2 = 20px overlap -> step 80px
        assertEquals(IntOffset(160, 0), Overlap.fixed(10.dp).offset(2, 3, 100, density))
    }

    @Test
    fun vertical_movesAlongYAxis() {
        assertEquals(IntOffset(0, 50), Overlap.vertical(0.5f).offset(1, 3, 100, density))
    }

    @Test
    fun customLambdaIsSupported() {
        val fan = Overlap { index, _, _, _ -> IntOffset(index * 10, index * 5) }
        assertEquals(IntOffset(20, 10), fan.offset(2, 3, 100, density))
    }
}
