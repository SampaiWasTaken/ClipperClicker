package com.example.appdevprojectradlerprueller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

/**
 * NonInterpolatingDrawable is a custom StateListDrawable that prevents interpolating the
 * button drawable when pressed or released, ensuring no blending occurs during state changes.
 * This is useful to maintain a crisp appearance for button drawables.
 */
class NonInterpolatingDrawable : StateListDrawable() {

    /**
     * Overrides the draw method of StateListDrawable to save the canvas state,
     * set the Paint flags to disable bitmap filtering, call the super draw method,
     * and then restore the canvas state.
     *
     * @param canvas The Canvas on which to draw the drawable.
     */
    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.drawFilter = PaintFlagsDrawFilter(Paint.FILTER_BITMAP_FLAG, 0)
        super.draw(canvas)
        canvas.restore()
    }
}

/**
 * NonInterpolatingBtn is a custom AppCompatButton that uses a NonInterpolatingDrawable as its background.
 * This ensures that when the button is pressed or released, the background drawable does not interpolate,
 * preserving a sharp appearance without blending during state changes.
 *
 * @param context The context of the calling activity or view.
 * @param attrs The attributes of the XML tag that is inflating the view.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource.
 */
class NonInterpolatingBtn @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(
    context, attrs, defStyleAttr
) {

    /**
     * Initialization block that sets up the NonInterpolatingDrawable as the background
     * for the NonInterpolatingBtn, defining different drawables for different states.
     */
    init {
        val drawable = NonInterpolatingDrawable()

        // Add the drawable for the pressed state (when the button is clicked)
        drawable.addState(intArrayOf(android.R.attr.state_pressed), resources.getDrawable(R.drawable.button_clicked))

        // Add the default drawable for the unpressed state (default state)
        drawable.addState(intArrayOf(), resources.getDrawable(R.drawable.button2, null))

        // Set the NonInterpolatingDrawable as the background for the button
        background = drawable
    }
}
