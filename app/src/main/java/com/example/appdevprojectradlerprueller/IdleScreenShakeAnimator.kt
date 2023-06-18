package com.example.appdevprojectradlerprueller

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import kotlin.math.sin

class IdleScreenShakeAnimator {
    fun getScreenShakeAnimatorSet(amplitude:Float, view: View):AnimatorSet{
        val transX = ObjectAnimator.ofFloat(view, "translationX",(sin((Math.random() * (628))) * amplitude).toFloat() ).apply { duration = 1000 }
        val transY = ObjectAnimator.ofFloat(view, "translationY", (sin((Math.random() * (628))) * amplitude).toFloat()).apply { duration = 1000 }
        val animatorset = AnimatorSet().apply {
            play(transX).with(transY)
        }
        animatorset.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                getScreenShakeAnimatorSet(amplitude,view).start()
            }
        })
        return animatorset
    }
}