[![](https://jitpack.io/v/Muzafferus/ImageList-Lib.svg)](https://jitpack.io/#Muzafferus/ImageList-Lib)

ImageList-Lib
===============

Jetpack Compose library for showing items (avatars, cards, anything) side by side with a "+N" overflow badge.

<img src="https://raw.githubusercontent.com/Muzafferus/ImageList-Lib/master/screenshot.jpg" width="432" height="936">

> The screenshot shows the 1.x View-based version. 2.0 is a full Kotlin / Jetpack Compose rewrite.

Gradle
------
```kotlin
// settings.gradle.kts
dependencyResolutionManagement { repositories { maven("https://jitpack.io") } }

// build.gradle.kts
dependencies { implementation("com.github.Muzafferus:ImageList-Lib:2.0.0") }
```

Usage
-----
Avatars from URLs (loaded with Coil), any `Shape`:
```kotlin
UrlImageList(
    urls = urls,
    maxVisible = 3,
    itemSize = 50.dp,
    shape = CircleShape,            // RoundedCornerShape(8.dp), StarShape(), your own Shape...
    border = BorderStroke(1.dp, Color.Black),
    overlap = Overlap.fraction(0.5f),
)
```

Fully custom UI for the items and the badge:
```kotlin
ImageList(
    items = cards,
    maxVisible = 3,
    overlap = Overlap.fixed(12.dp),
    zOrder = ZOrder.LastOnTop,
    overflowContent = { hidden -> Text("$hidden more") },   // null = no badge
) { card, index ->
    MyCard(card)   // your slot is forced to itemSize x itemSize, just fill it
}
```

Custom overlap logic
--------------------
`Overlap` is a `fun interface`; return the top-left position of every item:
```kotlin
val stair = Overlap { index, count, itemSizePx, density -> IntOffset(index * itemSizePx / 2, index * itemSizePx / 6) }
```
Built in: `Overlap.None`, `Overlap.fraction(f)`, `Overlap.fixed(dp)`, `Overlap.vertical(f)`.
`ZOrder.FirstOnTop` / `ZOrder.LastOnTop` chooses which item is drawn above. RTL is mirrored automatically.

Changelog
---------
* **2.0.0**
    * Rewritten in Jetpack Compose (no XML/View API, no Glide/CircleImageView)
    * `ImageList` slot API: custom item and overflow UI, any shape
    * Pluggable `Overlap` strategies and `ZOrder`
    * Fixes the old state bugs (views accumulating on re-bind, stale images/badge)
    * Requires AGP 8+, minSdk 21
* **1.0.1**
    * RecyclerView list item error fix (child already has a parent)
* **1.0.0**
    * Initial release

License
--------

       Copyright 2020 Muzaffar Pashazada
       
       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0
       
       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.
