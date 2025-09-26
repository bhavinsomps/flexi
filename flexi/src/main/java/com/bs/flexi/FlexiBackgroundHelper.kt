package com.bs.flexi

import android.animation.ValueAnimator
import android.graphics.BitmapShader
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.graphics.createBitmap
import androidx.core.graphics.withTranslation
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

enum class FillType { SOLID, LINEAR, RADIAL }
enum class BorderType { NONE, FILL, GRADIENT }
enum class BorderAnimationDirection { CLOCKWISE, ANTICLOCKWISE }

class FlexiBackgroundHelper(private val view: View) {

    var fillType: FillType = FillType.SOLID
        set(value) { field = value; updateFillShader(); view.invalidate() }

    var fillColor: Int = Color.WHITE
        set(value) { field = value; updateFillShader(); view.invalidate() }

    var fillGradientStart: Int = Color.TRANSPARENT
        set(value) { field = value; updateFillShader(); view.invalidate() }

    var fillGradientCenter: Int = Color.TRANSPARENT
        set(value) { field = value; updateFillShader(); view.invalidate() }

    var fillGradientEnd: Int = Color.TRANSPARENT
        set(value) { field = value; updateFillShader(); view.invalidate() }

    var fillGradientAngle: Float = 0f
        set(value) { field = value; updateFillShader(); view.invalidate() }

    var borderType: BorderType = BorderType.NONE
        set(value) { field = value; updateBorderShader(); view.invalidate() }

    var borderWidth: Float = 0f
        set(value) { field = value; paintBorder.strokeWidth = value; view.invalidate() }

    var borderColor: Int = Color.BLACK
        set(value) { field = value; paintBorder.color = value; view.invalidate() }

    var borderGradientColors: IntArray = intArrayOf()
        set(value) { field = value; updateBorderShader(); view.invalidate() }

    private var _cornerRadii = floatArrayOf(0f, 0f, 0f, 0f)

    var cornerRadius: Float = 0f
        set(value) {
            field = value
            _cornerRadii = floatArrayOf(value, value, value, value)
            view.invalidate()
        }

    var cornerRadiusTopLeft: Float
        get() = _cornerRadii[0]
        set(value) { _cornerRadii[0] = value; view.invalidate() }

    var cornerRadiusTopRight: Float
        get() = _cornerRadii[1]
        set(value) { _cornerRadii[1] = value; view.invalidate() }

    var cornerRadiusBottomRight: Float
        get() = _cornerRadii[2]
        set(value) { _cornerRadii[2] = value; view.invalidate() }

    var cornerRadiusBottomLeft: Float
        get() = _cornerRadii[3]
        set(value) { _cornerRadii[3] = value; view.invalidate() }

    var borderAnimation: Boolean = false
        set(value) {
            field = value
            if (value) startBorderAnimation() else stopBorderAnimation()
            view.invalidate()
        }

    var borderAnimationSpeed: Int = 30
        set(value) {
            field = value
            if (borderAnimation) startBorderAnimation()
            view.invalidate()
        }

    var borderAnimationDirection: BorderAnimationDirection = BorderAnimationDirection.CLOCKWISE

    var flexiShadowColor: Int = Color.TRANSPARENT
        set(value) { field = value; view.invalidate() }

    var flexiShadowOffsetX: Float = 0f
        set(value) { field = value; view.invalidate() }

    var flexiShadowOffsetY: Float = 0f
        set(value) { field = value; view.invalidate() }

    var flexiShadowBlur: Float = 0f
        set(value) { field = value; view.invalidate() }

    var flexiShadowSpread: Float = 0f
        set(value) { field = value; view.invalidate() }

    private val paintFill = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val paintBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }
    private val paintShadow = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val path = Path()
    private val contentRect = RectF()
    private val shadowRect = RectF()

    private var fillShader: Shader? = null
    private var borderShader: Shader? = null
    private val shaderMatrix = Matrix()
    private var animator: ValueAnimator? = null
    private var rotationAngle = 0f
    private var cachedShadowBlur = -1f
    private var cachedMaskFilter: BlurMaskFilter? = null

    fun onSizeChanged(w: Int, h: Int) {
        updateFillShader()
        updateBorderShader()
        if (borderAnimation && borderShader != null) startBorderAnimation()
    }

    fun onDraw(canvas: Canvas) {
        val inset = borderWidth / 2f
        contentRect.set(
            inset,
            inset,
            view.width - inset,
            view.height - inset
        )

        drawShadow(canvas)
        drawFill(canvas)
        drawBorder(canvas)
    }

    private fun drawShadow(canvas: Canvas) {
        if (flexiShadowColor == Color.TRANSPARENT) return

        shadowRect.set(
            contentRect.left - flexiShadowSpread,
            contentRect.top - flexiShadowSpread,
            contentRect.right + flexiShadowSpread,
            contentRect.bottom + flexiShadowSpread
        )

        path.reset()
        path.addRoundRect(shadowRect, getCornerRadiiArray(), Path.Direction.CW)

        paintShadow.color = flexiShadowColor

        if (flexiShadowBlur > 0f) {
            if (flexiShadowBlur != cachedShadowBlur) {
                cachedMaskFilter = BlurMaskFilter(flexiShadowBlur, BlurMaskFilter.Blur.NORMAL)
                cachedShadowBlur = flexiShadowBlur
            }
            paintShadow.maskFilter = cachedMaskFilter
        } else {
            paintShadow.maskFilter = null // sharp edges
        }

        canvas.withTranslation(flexiShadowOffsetX, flexiShadowOffsetY) {
            drawPath(path, paintShadow)
        }
    }

    private fun drawFill(canvas: Canvas) {
        path.reset()
        path.addRoundRect(contentRect, getCornerRadiiArray(), Path.Direction.CW)
        paintFill.shader = fillShader
        if (fillShader == null) paintFill.color = fillColor
        canvas.drawPath(path, paintFill)
    }

    private fun drawBorder(canvas: Canvas) {
        if (borderWidth > 0 && borderType != BorderType.NONE) {
            when (borderType) {
                BorderType.FILL -> {
                    paintBorder.shader = null
                    paintBorder.color = borderColor
                }
                BorderType.GRADIENT -> {
                    borderShader?.let {
                        shaderMatrix.reset()
                        if (borderAnimation) {
                            shaderMatrix.postRotate(rotationAngle, view.width / 2f, view.height / 2f)
                        }
                        it.setLocalMatrix(shaderMatrix)
                        paintBorder.shader = it
                    }
                }
                else -> {}
            }
            paintBorder.strokeWidth = borderWidth
            path.reset()
            path.addRoundRect(contentRect, getCornerRadiiArray(), Path.Direction.CW)
            canvas.drawPath(path, paintBorder)
        }
    }

    private fun getCornerRadiiArray(): FloatArray {
        return floatArrayOf(
            _cornerRadii[0], _cornerRadii[0], // top-left (x,y)
            _cornerRadii[1], _cornerRadii[1], // top-right
            _cornerRadii[2], _cornerRadii[2], // bottom-right
            _cornerRadii[3], _cornerRadii[3]  // bottom-left
        )
    }

    private fun updateFillShader() {
        if (view.width == 0 || view.height == 0) return
        fillShader = when (fillType) {
            FillType.LINEAR -> {
                val angleRad = Math.toRadians(fillGradientAngle.toDouble())
                val cx = view.width / 2f
                val cy = view.height / 2f
                val radius = (hypot(view.width.toDouble(), view.height.toDouble()) / 2).toFloat()
                val dx = (cos(angleRad) * radius).toFloat()
                val dy = (sin(angleRad) * radius).toFloat()
                val x0 = cx - dx
                val y0 = cy - dy
                val x1 = cx + dx
                val y1 = cy + dy
                val colors = if (fillGradientCenter != Color.TRANSPARENT)
                    intArrayOf(fillGradientStart, fillGradientCenter, fillGradientEnd)
                else intArrayOf(fillGradientStart, fillGradientEnd)
                LinearGradient(x0, y0, x1, y1, colors, null, Shader.TileMode.CLAMP)
            }
            FillType.RADIAL -> RadialGradient(
                view.width / 2f, view.height / 2f,
                max(view.width, view.height) / 2f,
                intArrayOf(fillGradientStart, fillGradientEnd),
                null, Shader.TileMode.CLAMP
            )
            FillType.SOLID -> null
        }
    }

    private fun updateBorderShader() {
        if (view.width == 0 || view.height == 0 || borderGradientColors.isEmpty()) return
        borderShader = createMultiColorGradientBorder(borderGradientColors)
    }

    private fun createMultiColorGradientBorder(colors: IntArray): Shader {
        val bmp = createBitmap(view.width, view.height)
        return when (colors.size) {
            0 -> BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            1 -> {
                bmp.eraseColor(colors[0])
                BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            }
            2 -> createTwoColorGradient(colors)
            3 -> createThreeColorGradient(colors)
            4 -> createFourColorGradient(colors)
            else -> createPerimeterGradient(colors)
        }
    }

    private fun createTwoColorGradient(colors: IntArray): Shader {
        return LinearGradient(
            0f, 0f, view.width.toFloat(), view.height.toFloat(),
            colors[0], colors[1],
            Shader.TileMode.CLAMP
        )
    }

    private fun createThreeColorGradient(colors: IntArray): Shader {
        val bmp = createBitmap(view.width, view.height)
        val pixels = IntArray(view.width * view.height)
        val positions = arrayOf(floatArrayOf(0.5f, 0f), floatArrayOf(0f, 1f), floatArrayOf(1f, 1f))
        for (y in 0 until view.height) {
            for (x in 0 until view.width) {
                val u = x.toFloat() / (view.width - 1).coerceAtLeast(1)
                val v = y.toFloat() / (view.height - 1).coerceAtLeast(1)
                var tw = 0f; var a = 0f; var r = 0f; var g = 0f; var b = 0f
                for (i in colors.indices) {
                    val dx = u - positions[i][0]; val dy = v - positions[i][1]
                    val d = sqrt((dx * dx + dy * dy).toDouble()).toFloat()
                    val w = 1f / (d + 0.01f)
                    tw += w; a += Color.alpha(colors[i]) * w
                    r += Color.red(colors[i]) * w; g += Color.green(colors[i]) * w; b += Color.blue(colors[i]) * w
                }
                pixels[y * view.width + x] = Color.argb((a / tw).toInt(), (r / tw).toInt(), (g / tw).toInt(), (b / tw).toInt())
            }
        }
        bmp.setPixels(pixels, 0, view.width, 0, 0, view.width, view.height)
        return BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    private fun createFourColorGradient(colors: IntArray): Shader {
        val bmp = createBitmap(view.width, view.height)
        val pixels = IntArray(view.width * view.height)
        val cTL = colors[0]; val cTR = colors[1]; val cBR = colors[2]; val cBL = colors[3]
        for (y in 0 until view.height) {
            for (x in 0 until view.width) {
                val u = x.toFloat() / (view.width - 1).coerceAtLeast(1)
                val v = y.toFloat() / (view.height - 1).coerceAtLeast(1)
                val up = u.pow(2); val vp = v.pow(2); val uInv = (1 - u).pow(2); val vInv = (1 - v).pow(2)
                val wTL = uInv * vInv; val wTR = up * vInv; val wBR = up * vp; val wBL = uInv * vp
                val wSum = (wTL + wTR + wBR + wBL)
                val a = ((Color.alpha(cTL) * wTL + Color.alpha(cTR) * wTR + Color.alpha(cBR) * wBR + Color.alpha(cBL) * wBL) / wSum).toInt()
                val r = ((Color.red(cTL) * wTL + Color.red(cTR) * wTR + Color.red(cBR) * wBR + Color.red(cBL) * wBL) / wSum).toInt()
                val g = ((Color.green(cTL) * wTL + Color.green(cTR) * wTR + Color.green(cBR) * wBR + Color.green(cBL) * wBL) / wSum).toInt()
                val b = ((Color.blue(cTL) * wTL + Color.blue(cTR) * wTR + Color.blue(cBR) * wBR + Color.blue(cBL) * wBL) / wSum).toInt()
                pixels[y * view.width + x] = Color.argb(a,r,g,b)
            }
        }
        bmp.setPixels(pixels,0,view.width,0,0,view.width,view.height)
        return BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    private fun createPerimeterGradient(colors: IntArray): Shader {
        val bmp = createBitmap(view.width, view.height)
        val pixels = IntArray(view.width * view.height)
        val positions = mutableListOf<FloatArray>()
        val n = colors.size
        for (i in 0 until n) {
            val angle = (i * 2 * Math.PI / n) - Math.PI/2
            val cosA = cos(angle).toFloat(); val sinA = sin(angle).toFloat()
            val abscos = abs(cosA); val abssin = abs(sinA)
            val u:Float; val v:Float
            if (abscos > abssin) {
                u = if(cosA>0)1f else 0f; v = (sinA/cosA*0.5f+0.5f).coerceIn(0f,1f)
            } else {
                u = (cosA/sinA*0.5f+0.5f).coerceIn(0f,1f); v = if(sinA>0)1f else 0f
            }
            positions.add(floatArrayOf(u,v))
        }
        for (y in 0 until view.height) {
            for (x in 0 until view.width) {
                val u = x.toFloat()/(view.width-1).coerceAtLeast(1)
                val v = y.toFloat()/(view.height-1).coerceAtLeast(1)
                var tw = 0f; var a=0f; var r=0f; var g=0f; var b=0f
                for(i in colors.indices){
                    val dx=u-positions[i][0]; val dy=v-positions[i][1]
                    val d=sqrt((dx*dx+dy*dy).toDouble()).toFloat()
                    val w=(1.0/(d+0.1)).pow(2.0).toFloat()
                    tw+=w; a+=Color.alpha(colors[i])*w
                    r+=Color.red(colors[i])*w; g+=Color.green(colors[i])*w; b+=Color.blue(colors[i])*w
                }
                pixels[y*view.width+x]=Color.argb((a/tw).toInt(),(r/tw).toInt(),(g/tw).toInt(),(b/tw).toInt())
            }
        }
        bmp.setPixels(pixels,0,view.width,0,0,view.width,view.height)
        return BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    private fun startBorderAnimation() {
        stopBorderAnimation()
        val dir = if (borderAnimationDirection == BorderAnimationDirection.CLOCKWISE) 1 else -1
        animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = (1000L * 360 / borderAnimationSpeed).coerceAtLeast(1L)
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                rotationAngle = (it.animatedValue as Float) * dir
                view.invalidate()
            }
            start()
        }
    }

    private fun stopBorderAnimation() {
        animator?.cancel()
        animator = null
    }
}