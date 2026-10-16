package com.muzafferus.imagelist

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.activity.ComponentActivity
import android.graphics.Canvas
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Renders the README preview with real Compose rendering (Robolectric native graphics).
 * Output: build/screenshots/preview.png. Tiles are solid colours so the test needs no network.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w420dp-h900dp-xxhdpi")
class ScreenshotTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val colors = listOf(
        Color(0xFFE57373), Color(0xFF64B5F6), Color(0xFF81C784), Color(0xFFFFB74D),
        Color(0xFFBA68C8), Color(0xFF4DB6AC), Color(0xFFF06292), Color(0xFFA1887F),
    )

    @Composable
    private fun Tile(shape: Shape, index: Int) {
        Box(
            Modifier.clip(shape).background(colors[index % colors.size]).border(2.dp, Color.White, shape),
            Alignment.Center,
        ) { BasicText("${index + 1}", style = TextStyle(color = Color.White, fontSize = 16.sp)) }
    }

    @Composable
    private fun Row(label: String, content: @Composable () -> Unit) {
        Row(Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            BasicText(label, Modifier.width(150.dp), style = TextStyle(color = Color(0xFF444444), fontSize = 13.sp))
            content()
        }
    }

    @Test
    fun renderPreview() {
        val items = (0 until 8).toList()
        rule.setContent {
            Column(
                Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(20.dp),
                Arrangement.spacedBy(2.dp),
            ) {
                Row("Circle") { ImageList(items) { _, i -> Tile(CircleShape, i) } }
                Row("Rounded cards") {
                    ImageList(items, overlap = Overlap.fraction(0.3f), zOrder = ZOrder.LastOnTop) { _, i -> Tile(RoundedCornerShape(12.dp), i) }
                }
                Row("Square") { ImageList(items, overlap = Overlap.fraction(0.15f)) { _, i -> Tile(RectangleShape, i) } }
                Row("Star") { ImageList(items, itemSize = 60.dp, overlap = Overlap.fraction(0.2f)) { _, i -> Tile(StarShape(), i) } }
                Row("Hexagon") { ImageList(items, itemSize = 60.dp, overlap = Overlap.fraction(0.2f)) { _, i -> Tile(PolygonShape(6, 0f), i) } }
                Row("Heart") { ImageList(items, itemSize = 60.dp, overlap = Overlap.fraction(0.2f)) { _, i -> Tile(HeartShape(), i) } }
                Row("Vertical") { ImageList(items, overlap = Overlap.vertical(0.5f), maxVisible = 4) { _, i -> Tile(CircleShape, i) } }
                Row("Custom (stair)") {
                    ImageList(
                        items,
                        maxVisible = 4,
                        overlap = Overlap { index, _, size, _ -> IntOffset(index * size / 2, index * size / 5) },
                        zOrder = ZOrder.LastOnTop,
                    ) { _, i -> Tile(CircleShape, i) }
                }
                Row("No overlap, no limit") {
                    ImageList(items, maxVisible = ImageListDefaults.Unlimited, itemSize = 30.dp, overlap = Overlap.None) { _, i -> Tile(CircleShape, i) }
                }
                Row("Custom badge") {
                    ImageList(
                        items,
                        overflowContent = { n ->
                            Box(Modifier.clip(CircleShape).background(Color(0xFF263238)), Alignment.Center) {
                                BasicText("$n more", style = TextStyle(color = Color.White, fontSize = 11.sp))
                            }
                        },
                    ) { _, i -> Tile(CircleShape, i) }
                }
            }
        }
        rule.waitForIdle()
        val decor = rule.activity.findViewById<android.view.View>(android.R.id.content)
        val bitmap = Bitmap.createBitmap(decor.width, decor.height, Bitmap.Config.ARGB_8888)
        decor.draw(Canvas(bitmap))
        val out = File("build/screenshots/preview.png").apply { parentFile?.mkdirs() }
        out.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
