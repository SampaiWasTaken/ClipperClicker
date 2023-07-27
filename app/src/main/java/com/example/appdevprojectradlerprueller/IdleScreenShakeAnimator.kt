package com.example.appdevprojectradlerprueller

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import kotlin.math.sin

/**
 * IdleScreenShakeAnimator is a utility class that provides a method to create an AnimatorSet
 * that applies a screen shake animation to a given view with random displacement along the
 * X and Y axes.
 */
class IdleScreenShakeAnimator {

    /**
     * Generates an AnimatorSet that creates a screen shake effect for the specified view.
     *
     * @param amplitude The amplitude of the screen shake effect. The higher the amplitude, the more
     *                  pronounced the shake will be.
     * @param view The view that will be animated with the screen shake effect.
     * @return An AnimatorSet that applies the screen shake animation to the given view.
     */
    fun getScreenShakeAnimatorSet(amplitude: Float, view: View): AnimatorSet {
        // Generate random displacement along the X and Y axes using sine function with the given amplitude.
        val transX = ObjectAnimator.ofFloat(view, "translationX", (sin((Math.random() * (628))) * amplitude).toFloat()).apply { duration = 1000 }
        val transY = ObjectAnimator.ofFloat(view, "translationY", (sin((Math.random() * (628))) * amplitude).toFloat()).apply { duration = 1000 }

        // Create an AnimatorSet to combine both X and Y translations in parallel.
        val animatorSet = AnimatorSet().apply {
            play(transX).with(transY)
        }

        // Add an AnimatorListenerAdapter to the AnimatorSet, which restarts the animation
        // when it ends, creating an infinite looping effect.
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                getScreenShakeAnimatorSet(amplitude, view).start()
            }
        })

        return animatorSet
    }
}