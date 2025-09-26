package com.bs.flexi

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes

object FlexiAttributeParser {
    fun parse(context: Context, attrs: AttributeSet?, helper: FlexiBackgroundHelper) {
        context.withStyledAttributes(attrs, R.styleable.Flexi) {
            // Fill
            helper.fillType = FillType.entries.toTypedArray()[getInt(R.styleable.Flexi_fillType, 0)]
            helper.fillColor = getColor(R.styleable.Flexi_fillColor, Color.WHITE)
            helper.fillGradientStart = getColor(R.styleable.Flexi_fillGradientStartColor, Color.TRANSPARENT)
            helper.fillGradientCenter = getColor(R.styleable.Flexi_fillGradientCenterColor, Color.TRANSPARENT)
            helper.fillGradientEnd = getColor(R.styleable.Flexi_fillGradientEndColor, Color.TRANSPARENT)
            helper.fillGradientAngle = getFloat(R.styleable.Flexi_fillGradientAngle, 0f)

            helper.cornerRadius = getDimension(R.styleable.Flexi_cornerRadius, 0f)

            val tl = getDimension(R.styleable.Flexi_cornerRadiusTopLeft, -1f)
            val tr = getDimension(R.styleable.Flexi_cornerRadiusTopRight, -1f)
            val br = getDimension(R.styleable.Flexi_cornerRadiusBottomRight, -1f)
            val bl = getDimension(R.styleable.Flexi_cornerRadiusBottomLeft, -1f)

            if (tl >= 0f) helper.cornerRadiusTopLeft = tl
            if (tr >= 0f) helper.cornerRadiusTopRight = tr
            if (br >= 0f) helper.cornerRadiusBottomRight = br
            if (bl >= 0f) helper.cornerRadiusBottomLeft = bl

            helper.borderWidth = getDimension(R.styleable.Flexi_borderWidth, 0f)
            helper.borderColor = getColor(R.styleable.Flexi_borderColor, Color.BLACK)
            helper.borderType = BorderType.entries.toTypedArray()[getInt(R.styleable.Flexi_borderType, 0)]
            helper.borderAnimation = getBoolean(R.styleable.Flexi_borderAnimation, false)
            helper.borderAnimationSpeed = getInt(R.styleable.Flexi_borderAnimationSpeed, 30)
            helper.borderAnimationDirection =
                if (getInt(R.styleable.Flexi_borderAnimationDirection, 0) == 0)
                    BorderAnimationDirection.CLOCKWISE
                else BorderAnimationDirection.ANTICLOCKWISE

            val colorsResId = getResourceId(R.styleable.Flexi_borderGradientColors, 0)
            if (colorsResId != 0) {
                val ta = resources.obtainTypedArray(colorsResId)
                val colors = IntArray(ta.length()) { i -> ta.getColor(i, Color.TRANSPARENT) }
                ta.recycle()
                helper.borderGradientColors = colors
            }

            // Shadow
            helper.flexiShadowColor = getColor(R.styleable.Flexi_flexiShadowColor, Color.TRANSPARENT)
            helper.flexiShadowOffsetX = getDimension(R.styleable.Flexi_flexiShadowOffsetX, 0f)
            helper.flexiShadowOffsetY = getDimension(R.styleable.Flexi_flexiShadowOffsetY, 0f)
            helper.flexiShadowBlur = getDimension(R.styleable.Flexi_flexiShadowBlur, 0f)
            helper.flexiShadowSpread = getDimension(R.styleable.Flexi_flexiShadowSpread, 0f)
        }
    }
}