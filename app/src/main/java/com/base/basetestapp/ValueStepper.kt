package com.base.basetestapp

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import com.base.basetestapp.databinding.LayoutValueStepperBinding
import com.daasuu.ei.Ease
import com.daasuu.ei.EasingInterpolator

@SuppressLint("Recycle")
class ValueStepper @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) :
    ConstraintLayout(context, attributeSet, defStyle) {

    companion object {
        private const val DURATION = 400L
    }

    private val binding =
        LayoutValueStepperBinding.inflate(LayoutInflater.from(context), this, true)

    private val translation = "translationX"

    private var currentValue: Int = 0

    private var _minValue: Int = 0
    private var _maxValue: Int? = null

    private var minValue: Int
        get() = _minValue
        set(value) {
            _minValue = value
        }

    private var maxValue: Int?
        get() = _maxValue
        set(value) {
            _maxValue = value
        }

    var value: String
        get() = currentValue.toString()
        set(value) {
            currentValue = value.split(" ").first().toInt()
            binding.valueInput.text =
                Editable.Factory.getInstance().newEditable("$currentValue $postfix")
        }

    var postfix: String = ""

    init {
        val attrs = context.obtainStyledAttributes(
            attributeSet, R.styleable.ValueStepper, defStyle, 0
        )

        minValue = attrs.getInteger(R.styleable.ValueStepper_minValue, 0)
        maxValue = attrs.getInteger(R.styleable.ValueStepper_maxValue, Int.MAX_VALUE)
        postfix = attrs.getString(R.styleable.ValueStepper_postfix) ?: ""
        value = "${attrs.getString(R.styleable.ValueStepper_maxValue) ?: "0"} $postfix"

        binding.btnDecrease.setOnClickListener {
            if (currentValue <= minValue) {
                wobble(binding.valueInput)
            } else {
                animateInDecrease()
                val increasedValue: Int = currentValue.dec()
                currentValue = increasedValue
                animateOutDecrease()
            }
        }
        binding.btnIncrease.setOnClickListener {
            if (currentValue >= maxValue!!) {
                wobble(binding.valueInput)
            } else {
                animateInIncrease()
                val increasedValue: Int = currentValue.inc()
                currentValue = increasedValue
                animateOutIncrease()
            }
        }

        attrs.recycle()
    }

    private fun animateInDecrease() {
        val animator = ObjectAnimator.ofFloat(binding.btnDecrease, translation, 0f, -160f)
        animator.interpolator = EasingInterpolator(Ease.BACK_IN)
        animator.start()

        val animator2 = ObjectAnimator.ofFloat(binding.btnDecrease, translation, 0f, -16f)
        animator2.interpolator = EasingInterpolator(Ease.BACK_IN)
        animator2.start()
    }

    private fun animateOutDecrease() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.valueInput.text =
                    Editable.Factory.getInstance().newEditable("$currentValue $postfix")

                val animator = ObjectAnimator.ofFloat(binding.valueInput, translation, -160f, 0f)
                animator.interpolator = EasingInterpolator(Ease.BACK_OUT)
                animator.start()

                val animator2 = ObjectAnimator.ofFloat(binding.btnDecrease, translation, -16f, 0f)
                animator2.interpolator = EasingInterpolator(Ease.BACK_OUT)
                animator2.start()
            }, DURATION
        )
    }

    private fun animateInIncrease() {
        val animator = ObjectAnimator.ofFloat(binding.btnIncrease, translation, 0f, 160f)
        animator.interpolator = EasingInterpolator(Ease.BACK_IN)
        animator.start()

        val animator2 = ObjectAnimator.ofFloat(binding.btnIncrease, translation, 0f, 16f)
        animator2.interpolator = EasingInterpolator(Ease.BACK_IN)
        animator2.start()
    }

    private fun animateOutIncrease() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.valueInput.text =
                    Editable.Factory.getInstance().newEditable("$currentValue $postfix")

                val animator = ObjectAnimator.ofFloat(binding.valueInput, translation, 160f, 0f)
                animator.interpolator = EasingInterpolator(Ease.BACK_OUT)
                animator.start()

                val animator2 = ObjectAnimator.ofFloat(binding.btnIncrease, translation, 16f, 0f)
                animator2.interpolator = EasingInterpolator(Ease.BACK_OUT)
                animator2.start()
            }, DURATION
        )
    }

    private fun wobble(view: View): View {
        val anim: Animation = TranslateAnimation(-16F, 16F, 0f, 0f)
        anim.duration = 50L
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = 3
        view.startAnimation(anim)
        return view
    }

}