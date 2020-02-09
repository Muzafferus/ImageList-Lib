package com.muzafferus.imagelist

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView


class ImageList(
    context: Context?,
    attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

    private var textView: TextView
    private var defaultImageLimit = 3
    private var defaultTextSize = 12

    private val imageList = ArrayList<CircleImageView>()

    init {
        inflate(
            context,
            R.layout.list_item_images,
            this
        )
        textView = TextView(context)
    }

    fun setImageCount(count: Int, limit: Int = defaultImageLimit) {
        if (count <= 0) return
        for (item in 0 until count) {
            imageList.add(CircleImageView(context))
        }

        defaultImageLimit = limit
        create()
        setImageSize(50)
    }

    fun setImageList(list: ArrayList<String>) {
        for ((index, url) in list.withIndex()) {
            if (index == imageList.size) return
            if (index == defaultImageLimit) return
            Glide
                .with(imageList[index].context)
                .load(url)
                .into(imageList[index])
        }
    }

    fun setBorder(size: Int, color: Int) {
        setBorderWith(size)
        setBorderColor(color)
        setTextBackground(color)
    }

    fun setImageSize(size: Int) {
        setTextViewSize(size)
        for ((index, image) in imageList.withIndex()) {
            if (index == defaultImageLimit) return
            image.layoutParams.height = getPixels(size)
            image.layoutParams.width = getPixels(size)
        }
    }

    fun setTextBackground(color: Int) {
        val r = 100f
        val shape =
            ShapeDrawable(RoundRectShape(floatArrayOf(r, r, r, r, r, r, r, r), null, null))
        shape.paint.color = color
        textView.background = shape
    }

    fun setTextColor(color: Int) {
        textView.setTextColor(color)
        val set = ConstraintSet()
        set.applyTo(this)
    }

    fun setTextSize(size: Int) {
        textView.textSize = size.toFloat()
        textView.setPadding(0, 0, getPixels(size), 0)
    }

    private fun setTextViewSize(size: Int) {
        if (imageList.size <= defaultImageLimit) return
        if (defaultImageLimit == -1) return
        textView.layoutParams.height = getPixels(size)
        textView.layoutParams.width = getPixels(size)
        textView.gravity = Gravity.CENTER or Gravity.END
    }

    private fun create() {
        val set = ConstraintSet()

        for ((index, image) in imageList.withIndex()) {
            if (defaultImageLimit == index) {
                createText(set)
                return
            }
            image.id = View.generateViewId()
            this.addView(image, 0)
            set.clone(this)
            set.connect(
                image.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
            )
            set.connect(
                image.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
            )

            if (index == 0) {
                set.connect(
                    image.id,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START,
                    0
                )
            } else {
                val imageID = imageList[(index - 1)].id
                set.connect(
                    image.id,
                    ConstraintSet.END,
                    imageID,
                    ConstraintSet.END,
                    0
                )
                set.connect(
                    image.id,
                    ConstraintSet.START,
                    imageID,
                    ConstraintSet.END,
                    0
                )
            }
            set.applyTo(this)
        }
    }

    private fun getPixels(size: Int): Int {
        val scale: Float = resources.displayMetrics.density
        return (size * scale + 0.5f).toInt()
    }

    private fun setBorderWith(size: Int) {
        for ((index, image) in imageList.withIndex()) {
            if (index == defaultImageLimit) return
            image.borderWidth = getPixels(size)
        }
    }

    private fun setBorderColor(color: Int) {
        for ((index, image) in imageList.withIndex()) {
            if (index == defaultImageLimit) return
            image.borderColor = color
        }
    }

    private fun createText(set: ConstraintSet) {
        textView.id = View.generateViewId()
        this.addView(textView, 0)
        set.clone(this)
        set.connect(
            textView.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            0
        )

        set.connect(
            textView.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            0
        )

        val imageID = imageList[(defaultImageLimit - 1)].id
        set.connect(
            textView.id,
            ConstraintSet.END,
            imageID,
            ConstraintSet.END,
            0
        )
        set.connect(
            textView.id,
            ConstraintSet.START,
            imageID,
            ConstraintSet.END,
            0
        )

        set.applyTo(this)
        textView.text = String.format("+%s", (imageList.size - defaultImageLimit))
        setTextColor(Color.WHITE)
        setTextSize(defaultTextSize)
    }

}
