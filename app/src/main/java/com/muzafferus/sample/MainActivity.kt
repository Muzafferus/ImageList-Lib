package com.muzafferus.sample

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = ArrayList<String>()
        list.add("https://i.ytimg.com/vi/7Xu_s1YJhyg/maxresdefault.jpg")
        list.add("https://www.irreverentgent.com/wp-content/uploads/2018/03/Awesome-Profile-Pictures-for-Guys-look-away2.jpg")
        list.add("https://i.ytimg.com/vi/L3wKzyIN1yk/maxresdefault.jpg")
        list.add("https://images.unsplash.com/photo-1534528741775-53994a69daeb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80")
        list.add("https://i.ytimg.com/vi/0EnrFe3Zb6k/maxresdefault.jpg")
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSyGWk7O0sye0eBnxWCQq43s884B5NYLIee_J3I6F05EfN1JZXR")

        imageList1.setImageCount(1)
        imageList1.setImageList(list)
        imageList1.setImageSize(50)
        imageList1.setBorder(1, ContextCompat.getColor(applicationContext, R.color.colorBlack))

        imageList2.setImageCount(2)
        imageList2.setImageList(list)
        imageList2.setImageSize(50)
        imageList2.setBorder(1, ContextCompat.getColor(applicationContext, R.color.colorBlack))

        imageList3.setImageCount(3)
        imageList3.setImageList(list)
        imageList3.setImageSize(50)
        imageList3.setBorder(1, ContextCompat.getColor(applicationContext, R.color.colorBlack))

        imageList4.setImageCount(4)
        imageList4.setImageList(list)
        imageList4.setImageSize(50)
        imageList4.setBorder(1, ContextCompat.getColor(applicationContext, R.color.colorBlack))
        imageList4.setTextSize(10)
        imageList4.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorText))
        imageList4.setTextBackground(ContextCompat.getColor(applicationContext, R.color.colorPrimaryDark))

        imageList5.setImageCount(5, 4)
        imageList5.setImageList(list)
        imageList5.setImageSize(50)
        imageList5.setBorder(1, ContextCompat.getColor(applicationContext, R.color.colorBlack))

        imageList6.setImageCount(6, -1)
        imageList6.setImageList(list)
        imageList6.setImageSize(50)
    }
}
