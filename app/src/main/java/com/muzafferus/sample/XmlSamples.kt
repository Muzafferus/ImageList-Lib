package com.muzafferus.sample

import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxSize
import com.muzafferus.imagelist.ImageListView

/** The same widget used from a classic XML layout (see res/layout/sample_xml.xml). */
@Composable
fun XmlSamples() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val root = View.inflate(context, R.layout.sample_xml, null)
            root.forEachImageListView { it.imageUrls = SampleData.avatars }
            // These two get pictures instead of faces, set from code:
            root.findViewById<ImageListView>(R.id.xmlCards).imageUrls = SampleData.pictures
            root.findViewById<ImageListView>(R.id.xmlStars).imageUrls = SampleData.pictures
            root.findViewById<ImageListView>(R.id.xmlHearts).imageUrls = SampleData.pictures
            root.findViewById<ImageListView>(R.id.xmlHex).imageUrls = SampleData.pictures
            root
        },
    )
}

private fun View.forEachImageListView(action: (ImageListView) -> Unit) {
    if (this is ImageListView) action(this)
    if (this is ViewGroup) for (i in 0 until childCount) getChildAt(i).forEachImageListView(action)
}
