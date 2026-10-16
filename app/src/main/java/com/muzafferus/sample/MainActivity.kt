package com.muzafferus.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.muzafferus.imagelist.ImageList
import com.muzafferus.imagelist.ImageListDefaults
import com.muzafferus.imagelist.Overlap
import com.muzafferus.imagelist.StarShape
import com.muzafferus.imagelist.UrlImageList
import com.muzafferus.imagelist.ZOrder

private val urls = listOf(
    "https://i.ytimg.com/vi/7Xu_s1YJhyg/maxresdefault.jpg",
    "https://www.irreverentgent.com/wp-content/uploads/2018/03/Awesome-Profile-Pictures-for-Guys-look-away2.jpg",
    "https://i.ytimg.com/vi/L3wKzyIN1yk/maxresdefault.jpg",
    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=1000&q=80",
    "https://i.ytimg.com/vi/0EnrFe3Zb6k/maxresdefault.jpg",
    "https://i.ytimg.com/vi/7Xu_s1YJhyg/maxresdefault.jpg",
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) { Samples() }
            }
        }
    }
}

@Composable
private fun Samples() {
    val border = BorderStroke(1.dp, Color.Black)
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Label("Circle, half overlap (default)")
        UrlImageList(urls, border = border)

        Label("Square cards, small overlap")
        UrlImageList(urls, shape = RoundedCornerShape(8.dp), overlap = Overlap.fraction(0.2f), border = border)

        Label("Stars, no limit")
        UrlImageList(urls, shape = StarShape(), maxVisible = ImageListDefaults.Unlimited, overlap = Overlap.fraction(0.3f))

        Label("Custom overlap (stair) + last on top")
        UrlImageList(
            urls,
            overlap = Overlap { index, _, size, _ -> IntOffset(index * size / 2, index * size / 6) },
            zOrder = ZOrder.LastOnTop,
            border = border,
        )

        Label("Vertical")
        UrlImageList(urls, overlap = Overlap.vertical(0.6f), border = border)

        Label("Fully custom UI for items and badge")
        ImageList(
            items = listOf("A", "B", "C", "D", "E"),
            maxVisible = 3,
            overlap = Overlap.fixed(12.dp),
            overflowContent = { n ->
                Box(Modifier.clip(RoundedCornerShape(12.dp)).background(Color.DarkGray), Alignment.Center) {
                    Text("$n more", color = Color.White)
                }
            },
        ) { letter, index ->
            Box(
                Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(listOf(Color(0xFFE57373), Color(0xFF64B5F6), Color(0xFF81C784))[index % 3])
                    .border(2.dp, Color.White, RoundedCornerShape(12.dp)),
                Alignment.Center,
            ) { Text(letter) }
        }
    }
}

@Composable
private fun Label(text: String) = Text(text, style = MaterialTheme.typography.labelLarge)
