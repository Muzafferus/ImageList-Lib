package com.muzafferus.sample

/** Stable demo images: pravatar for faces, picsum for pictures. */
object SampleData {
    val avatars: List<String> = (1..12).map { "https://i.pravatar.cc/200?img=$it" }
    val pictures: List<String> = listOf(10, 20, 29, 42, 57, 64, 76, 82, 91, 103).map { "https://picsum.photos/id/$it/200/200" }
}
