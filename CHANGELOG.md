# Changelog

## 2.0.0
- Rewritten in Kotlin / Jetpack Compose: `ImageList` (slot API) and `UrlImageList` (Coil).
- `ImageListView` for XML layouts and `RecyclerView`, with `il*` attributes and properties.
- Pluggable `Overlap` strategies (`None`, `fraction`, `fixed`, `vertical`, custom) and `ZOrder`.
- Any `Shape` for items and badge; added `StarShape`, `PolygonShape`, `HeartShape`.
- Custom item and badge UI; the badge is always drawn above the items.
- Accessibility: `contentDescription` for the list and per item (`UrlImageList`).
- Fixes the 1.x state bugs (accumulating views on re-bind, stale badge/images, `limit = 0` crash, call-order dependence).
- Build: AGP 8.7, Kotlin 2.0, Gradle 8.9, version catalog, `explicitApi()`, lint, CI, JitPack config.
- Removed: Glide and CircleImageView dependencies and the old View API. minSdk 21.

## 1.0.1
- RecyclerView list item error fix (child already has a parent).

## 1.0.0
- Initial release.
