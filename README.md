[![](https://jitpack.io/v/Muzafferus/ImageList-Lib.svg)](https://jitpack.io/#Muzafferus/ImageList-Lib)

ImageList-Lib
===============

Android library for showing images side by side. This is based on [CircleImageView from  hdodenhof ](https://github.com/hdodenhof/CircleImageView).

<img src="https://raw.githubusercontent.com/Muzafferus/ImageList-Lib/master/screenshot.jpg" width="432" height="936">

Used Glide for image.

Gradle
------
```
allprojects {
    repositories {
    ...
        maven { url 'https://jitpack.io' }    
    }
}
```

```
dependencies {
    ...
    implementation 'com.github.Muzafferus:ImageList-Lib:1.0.1'
}
```

Usage
-----
```xml
<com.muzafferus.imagelist.ImageList
                    android:id="@+id/imageList"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content" />


```

```
        val list:List<String>  = ArrayList();
        imageList.setImageCount(4)
        imageList.setImageList(list)
        imageList.setImageSize(50)
        imageList.setBorder(1, ContextCompat.getColor(applicationContext, R.color.colorBlack))
        imageList.setTextSize(10)
        imageList.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorText))
        imageList.setTextBackground(ContextCompat.getColor(applicationContext, R.color.colorPrimaryDark))
```

Changelog
---------
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
