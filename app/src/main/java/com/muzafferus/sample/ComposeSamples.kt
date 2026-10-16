package com.muzafferus.sample

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muzafferus.imagelist.HeartShape
import com.muzafferus.imagelist.ImageList
import com.muzafferus.imagelist.ImageListDefaults
import com.muzafferus.imagelist.Overlap
import com.muzafferus.imagelist.PolygonShape
import com.muzafferus.imagelist.StarShape
import com.muzafferus.imagelist.UrlImageList
import com.muzafferus.imagelist.ZOrder
import kotlin.math.abs

private val Teal = Color(0xFF00574B)
private val palette = listOf(
    Color(0xFFE57373), Color(0xFF64B5F6), Color(0xFF81C784), Color(0xFFFFB74D),
    Color(0xFFBA68C8), Color(0xFF4DB6AC), Color(0xFFF06292), Color(0xFFA1887F),
)
private val loading = ColorPainter(Color(0xFFE0E0E0))
private val failed = ColorPainter(Color(0xFFBDBDBD))
private val whiteBorder = BorderStroke(2.dp, Color.White)
private val blackBorder = BorderStroke(1.dp, Color.Black)

@Composable
fun ComposeSamples() {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        val avatars = SampleData.avatars
        val pictures = SampleData.pictures

        Section("Default circle (3 visible + badge)") {
            UrlImageList(avatars, placeholder = loading, error = failed)
        }
        Section("Circle + white border, 64dp, 4 visible") {
            UrlImageList(avatars, maxVisible = 4, itemSize = 64.dp, border = whiteBorder, placeholder = loading)
        }
        Section("Rounded square cards") {
            UrlImageList(pictures, shape = RoundedCornerShape(12.dp), overlap = Overlap.fraction(0.3f), border = whiteBorder, placeholder = loading)
        }
        Section("Sharp squares, small overlap") {
            UrlImageList(pictures, shape = RectangleShape, overlap = Overlap.fraction(0.15f), border = blackBorder, placeholder = loading)
        }
        Section("Star") {
            UrlImageList(pictures, shape = StarShape(), itemSize = 64.dp, overlap = Overlap.fraction(0.25f), placeholder = loading)
        }
        Section("Star with 7 tips, deep notches") {
            UrlImageList(avatars, shape = StarShape(points = 7, innerRadiusRatio = 0.7f), itemSize = 64.dp, overlap = Overlap.fraction(0.2f), placeholder = loading)
        }
        Section("Hexagon") {
            UrlImageList(pictures, shape = PolygonShape(6, rotationDegrees = 0f), itemSize = 64.dp, overlap = Overlap.fraction(0.2f), placeholder = loading)
        }
        Section("Triangle") {
            UrlImageList(pictures, shape = PolygonShape(3), itemSize = 64.dp, overlap = Overlap.fraction(0.3f), placeholder = loading)
        }
        Section("Heart") {
            UrlImageList(avatars, shape = HeartShape(), itemSize = 64.dp, overlap = Overlap.fraction(0.2f), placeholder = loading)
        }
        Section("No overlap") {
            UrlImageList(avatars, overlap = Overlap.None, placeholder = loading)
        }
        Section("Gap between items (negative overlap)") {
            UrlImageList(avatars, overlap = Overlap.fraction(-0.15f), placeholder = loading)
        }
        Section("Fixed 16dp overlap") {
            UrlImageList(avatars, overlap = Overlap.fixed(16.dp), border = whiteBorder, placeholder = loading)
        }
        Section("Last on top (card stack)") {
            UrlImageList(pictures, zOrder = ZOrder.LastOnTop, shape = RoundedCornerShape(10.dp), overlap = Overlap.fraction(0.6f), border = whiteBorder, placeholder = loading)
        }
        Section("Vertical") {
            UrlImageList(avatars, overlap = Overlap.vertical(0.5f), border = whiteBorder, placeholder = loading)
        }
        Section("Custom overlap: stair") {
            UrlImageList(
                avatars,
                maxVisible = 4,
                overlap = Overlap { index, _, size, _ -> IntOffset(index * size / 2, index * size / 5) },
                zOrder = ZOrder.LastOnTop,
                border = whiteBorder,
                placeholder = loading,
            )
        }
        Section("Custom overlap: arc / fan") {
            UrlImageList(
                avatars,
                maxVisible = 6,
                itemSize = 44.dp,
                overlap = Overlap { index, count, size, _ ->
                    val mid = (count - 1) / 2f
                    IntOffset((index * size * 0.6f).toInt(), (abs(index - mid) * abs(index - mid) * size * 0.08f).toInt())
                },
                border = whiteBorder,
                placeholder = loading,
            )
        }
        Section("Show everyone (no limit, no badge)") {
            UrlImageList(avatars, maxVisible = ImageListDefaults.Unlimited, itemSize = 40.dp, overlap = Overlap.fraction(0.4f), border = whiteBorder, placeholder = loading)
        }
        Section("Badge disabled") {
            UrlImageList(avatars, overflowContent = null, placeholder = loading)
        }
        Section("Custom badge: pill") {
            UrlImageList(
                avatars,
                overflowContent = { hidden ->
                    Box(Modifier.clip(CircleShape).background(Teal).border(2.dp, Color.White, CircleShape), Alignment.Center) {
                        Text("$hidden more", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                },
                border = whiteBorder,
                placeholder = loading,
            )
        }
        Section("Sizes 28 / 40 / 56 / 80dp") {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                listOf(28, 40, 56, 80).forEach { UrlImageList(avatars, maxVisible = 2, itemSize = it.dp, border = whiteBorder, placeholder = loading) }
            }
        }

        Section("Your own UI: initials on colour chips") {
            val names = listOf("Anar", "Banu", "Cavid", "Dilara", "Elnur", "Fidan")
            ImageList(items = names, itemSize = 48.dp, overlap = Overlap.fraction(0.3f), maxVisible = 4) { name, index ->
                Box(Modifier.clip(CircleShape).background(palette[index % palette.size]).border(2.dp, Color.White, CircleShape), Alignment.Center) {
                    Text(name.first().toString(), color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
        Section("Your own UI: rating cards") {
            val ratings = listOf(4.9, 4.7, 4.5, 4.2, 3.9, 3.5)
            ImageList(items = ratings, itemSize = 64.dp, overlap = Overlap.fraction(0.2f), maxVisible = 4, zOrder = ZOrder.LastOnTop) { rating, index ->
                Column(
                    Modifier.clip(RoundedCornerShape(14.dp)).background(palette[index % palette.size]).border(2.dp, Color.White, RoundedCornerShape(14.dp)),
                    Arrangement.Center,
                    Alignment.CenterHorizontally,
                ) {
                    Text("★", color = Color.White, fontSize = 18.sp)
                    Text("$rating", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Section("Your own UI: star-shaped numbers") {
            ImageList(items = (1..6).toList(), itemSize = 56.dp, overlap = Overlap.fraction(0.25f), maxVisible = 4) { n, index ->
                Box(Modifier.clip(StarShape()).background(palette[(index + 3) % palette.size]), Alignment.Center) {
                    Text("$n", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
        Section("Your own UI: image + caption card") {
            ImageList(items = pictures, itemSize = 72.dp, overlap = Overlap.fraction(0.35f), maxVisible = 3, zOrder = ZOrder.LastOnTop) { url, index ->
                Box(Modifier.clip(RoundedCornerShape(8.dp)).border(2.dp, Color.White, RoundedCornerShape(8.dp))) {
                    coil.compose.AsyncImage(url, null, Modifier.fillMaxSize(), placeholder = loading, contentScale = androidx.compose.ui.layout.ContentScale.Crop)
                    Text(
                        "#${index + 1}",
                        color = Color.White,
                        fontSize = 11.sp,
                        modifier = Modifier.align(Alignment.BottomStart).background(Color(0x99000000)).padding(horizontal = 4.dp),
                    )
                }
            }
        }
        Section("Right-to-left layout") {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Box(Modifier.fillMaxWidth()) { UrlImageList(avatars, border = whiteBorder, placeholder = loading, modifier = Modifier.align(Alignment.CenterStart)) }
            }
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        content()
    }
}
