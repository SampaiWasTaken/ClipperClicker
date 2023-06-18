package com.example.appdevprojectradlerprueller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class NonInterpolatingDrawable : StateListDrawable() {
    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.drawFilter = PaintFlagsDrawFilter(Paint.FILTER_BITMAP_FLAG, 0)
        super.draw(canvas)
        canvas.restore()
    }
}

class NonInterpolatingBtn @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(
    context, attrs, defStyleAttr
) {
        init {
            val drawable = NonInterpolatingDrawable()
            drawable.addState(intArrayOf(android.R.attr.state_pressed), resources.getDrawable(R.drawable.button_clicked))
            drawable.addState(intArrayOf(), resources.getDrawable(R.drawable.button2, null))
            background = drawable
        }
}