/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView


class MainActivity : AppCompatActivity() {

    lateinit var frame: FrameLayout
    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        frame = findViewById(R.id.frame)
        star = findViewById(R.id.star)
        rotateButton = findViewById(R.id.rotateButton)
        translateButton = findViewById(R.id.translateButton)
        scaleButton = findViewById(R.id.scaleButton)
        fadeButton = findViewById(R.id.fadeButton)
        colorizeButton = findViewById(R.id.colorizeButton)
        showerButton = findViewById(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotate()
        }

        translateButton.setOnClickListener {
            translate()
        }

        scaleButton.setOnClickListener {
            scale()
        }

        fadeButton.setOnClickListener {
            fade()
        }

        colorizeButton.setOnClickListener {
            colorize()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    private fun rotate() {
        ObjectAnimator.ofFloat(star, View.ROTATION, 0f, 360f).apply {
            duration = 3000
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    rotateButton.isEnabled = false
                }

                override fun onAnimationEnd(animation: Animator) {
                    rotateButton.isEnabled = true
                }
            })
            start()
        }
    }

    private fun translate() {
        ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200f).apply {
            repeatCount = 3
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(star)
            start()
        }
    }

    private fun scale() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        ObjectAnimator.ofPropertyValuesHolder(
            star, scaleX, scaleY
        ).apply {
            duration = 2000
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(scaleButton)
            start()
        }
    }

    private fun fade() {
        ObjectAnimator.ofFloat(star, View.ALPHA, 0f).apply {
            duration = 2000
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(star)
            start()
        }
    }

    private fun colorize() {
        ObjectAnimator.ofArgb(
            frame, "backgroundColor", Color.BLACK, Color.RED
        ).apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(frame)
            start()
        }
    }

    private fun shower() {
        val container = star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW: Float = star.width.toFloat()
        var starH: Float = star.height.toFloat()

        val newStar = AppCompatImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        container.addView(newStar)

        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY

        newStar.translationX = Math.random().toFloat() *
                containerW - starW / 2

        val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y,
            -starH, containerH + starH)
        mover.interpolator = AccelerateInterpolator(1f)
        val rotator = ObjectAnimator.ofFloat(newStar, View.ROTATION,
            (Math.random() * 1080).toFloat())
        rotator.interpolator = LinearInterpolator()

        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Math.random() * 1500 + 500).toLong()

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                container.removeView(newStar)
            }
        })
        set.start()
    }

}
