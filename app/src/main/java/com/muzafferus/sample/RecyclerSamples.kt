package com.muzafferus.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muzafferus.imagelist.ImageListView

/**
 * 200 rows with a different number of avatars each. Scroll fast: rows are recycled and always
 * show exactly their own data (the 1.x view used to accumulate children here).
 */
@Composable
fun RecyclerSamples() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            RecyclerView(context).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = RowAdapter()
            }
        },
    )
}

private class RowAdapter : RecyclerView.Adapter<RowAdapter.Holder>() {
    class Holder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.rowTitle)
        val images: ImageListView = view.findViewById(R.id.rowImages)
    }

    override fun getItemCount() = 200

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val count = position % 9 // 0..8 participants, so rows differ a lot
        holder.title.text = "Group ${position + 1} · $count people"
        holder.images.imageUrls = List(count) { SampleData.avatars[(position + it) % SampleData.avatars.size] }
    }
}
