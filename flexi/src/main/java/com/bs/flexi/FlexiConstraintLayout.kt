package com.bs.flexi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class FlexiConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val helper = FlexiBackgroundHelper(this)

    init {
        FlexiAttributeParser.parse(context, attrs, helper)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        helper.onSizeChanged(w, h)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun dispatchDraw(canvas: Canvas) {
        helper.onDraw(canvas)
        super.dispatchDraw(canvas)
    }

    var fillType: FillType
        get() = helper.fillType
        set(value) { helper.fillType = value }

    var fillColor: Int
        get() = helper.fillColor
        set(value) { helper.fillColor = value }

    var fillGradientStart: Int
        get() = helper.fillGradientStart
        set(value) { helper.fillGradientStart = value }

    var fillGradientCenter: Int
        get() = helper.fillGradientCenter
        set(value) { helper.fillGradientCenter = value }

    var fillGradientEnd: Int
        get() = helper.fillGradientEnd
        set(value) { helper.fillGradientEnd = value }

    var fillGradientAngle: Float
        get() = helper.fillGradientAngle
        set(value) { helper.fillGradientAngle = value }

    fun setFillGradient(start: Int, end: Int, center: Int = Color.TRANSPARENT, angle: Float = 0f) {
        helper.fillGradientStart = start
        helper.fillGradientEnd = end
        helper.fillGradientCenter = center
        helper.fillGradientAngle = angle
        helper.fillType = FillType.LINEAR
    }

    var borderType: BorderType
        get() = helper.borderType
        set(value) { helper.borderType = value }

    var borderWidth: Float
        get() = helper.borderWidth
        set(value) { helper.borderWidth = value }

    var borderColor: Int
        get() = helper.borderColor
        set(value) { helper.borderColor = value }

    var borderGradientColors: IntArray
        get() = helper.borderGradientColors
        set(value) { helper.borderGradientColors = value }

    var borderAnimation: Boolean
        get() = helper.borderAnimation
        set(value) { helper.borderAnimation = value }

    var borderAnimationSpeed: Int
        get() = helper.borderAnimationSpeed
        set(value) { helper.borderAnimationSpeed = value }

    var borderAnimationDirection: BorderAnimationDirection
        get() = helper.borderAnimationDirection
        set(value) { helper.borderAnimationDirection = value }

    fun setBorderGradient(colors: IntArray) {
        helper.borderGradientColors = colors
        helper.borderType = BorderType.GRADIENT
    }

    var cornerRadius: Float
        get() = helper.cornerRadius
        set(value) { helper.cornerRadius = value }

    var cornerRadiusTopLeft: Float
        get() = helper.cornerRadiusTopLeft
        set(value) { helper.cornerRadiusTopLeft = value }

    var cornerRadiusTopRight: Float
        get() = helper.cornerRadiusTopRight
        set(value) { helper.cornerRadiusTopRight = value }

    var cornerRadiusBottomRight: Float
        get() = helper.cornerRadiusBottomRight
        set(value) { helper.cornerRadiusBottomRight = value }

    var cornerRadiusBottomLeft: Float
        get() = helper.cornerRadiusBottomLeft
        set(value) { helper.cornerRadiusBottomLeft = value }

    var flexiShadowColor: Int
        get() = helper.flexiShadowColor
        set(value) { helper.flexiShadowColor = value }

    var flexiShadowOffsetX: Float
        get() = helper.flexiShadowOffsetX
        set(value) { helper.flexiShadowOffsetX = value }

    var flexiShadowOffsetY: Float
        get() = helper.flexiShadowOffsetY
        set(value) { helper.flexiShadowOffsetY = value }

    var flexiShadowBlur: Float
        get() = helper.flexiShadowBlur
        set(value) { helper.flexiShadowBlur = value }

    var flexiShadowSpread: Float
        get() = helper.flexiShadowSpread
        set(value) { helper.flexiShadowSpread = value }
}