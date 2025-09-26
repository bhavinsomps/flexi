package com.sample.flexi

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.bs.flexi.BorderType
import com.bs.flexi.FillType
import com.sample.flexi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val offsetRange = 100

    private val sectionStates = mutableMapOf(
        "border" to false,
        "corner" to false,
        "shadow" to false,
        "fill" to false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val box = binding.flexiBox

        setupToggleButtons()

        box.flexiShadowColor = Color.BLACK
        box.flexiShadowOffsetX = 0f
        box.flexiShadowOffsetY = 0f

        binding.sliderBorderWidth.progress = box.borderWidth.toInt()
        binding.tvBorderWidth.text = buildString {
            append("Border Width:")
            append(box.borderWidth)
            append(" dp")
        }
        binding.sliderBorderWidth.setOnSeekBarChangeListener(onProgress { progress ->
            box.borderWidth = progress.toFloat()
            binding.tvBorderWidth.text = buildString {
                append("Border Width:")
                append(box.borderWidth)
                append(" dp")
            }
        })

        updateBorderTypeButtonText(box.borderType)
        binding.btnBorderType.setOnClickListener {
            box.borderType = when (box.borderType) {
                BorderType.NONE -> BorderType.FILL
                BorderType.FILL -> BorderType.GRADIENT
                BorderType.GRADIENT -> BorderType.NONE
            }
            updateBorderTypeButtonText(box.borderType)
        }

        updateBorderAnimButtonText(box.borderAnimation)
        binding.btnBorderAnim.setOnClickListener {
            box.borderAnimation = !box.borderAnimation
            updateBorderAnimButtonText(box.borderAnimation)
        }

        binding.btnBorderBlack.setOnClickListener { box.borderColor = Color.BLACK }
        binding.btnBorderRed.setOnClickListener { box.borderColor = Color.RED }
        binding.btnBorderGreen.setOnClickListener { box.borderColor = Color.GREEN }
        binding.btnBorderBlue.setOnClickListener { box.borderColor = Color.BLUE }

        binding.btnShadowBlack.setOnClickListener { box.flexiShadowColor = Color.BLACK }
        binding.btnShadowRed.setOnClickListener { box.flexiShadowColor = Color.RED }
        binding.btnShadowBlue.setOnClickListener { box.flexiShadowColor = Color.BLUE }
        binding.btnShadowGreen.setOnClickListener { box.flexiShadowColor = Color.GREEN }

        binding.sliderCorner.initWith(box.cornerRadius.toInt()) { progress ->
            box.cornerRadius = progress.toFloat()
        }
        binding.sliderTopLeft.initWith(box.cornerRadiusTopLeft.toInt()) { progress ->
            box.cornerRadiusTopLeft = progress.toFloat()
        }
        binding.sliderTopRight.initWith(box.cornerRadiusTopRight.toInt()) { progress ->
            box.cornerRadiusTopRight = progress.toFloat()
        }
        binding.sliderBottomLeft.initWith(box.cornerRadiusBottomLeft.toInt()) { progress ->
            box.cornerRadiusBottomLeft = progress.toFloat()
        }
        binding.sliderBottomRight.initWith(box.cornerRadiusBottomRight.toInt()) { progress ->
            box.cornerRadiusBottomRight = progress.toFloat()
        }

        binding.sliderShadowBlur.progress = box.flexiShadowBlur.toInt()
        binding.tvShadowBlur.text = buildString {
            append("Shadow Blur: ")
            append(box.flexiShadowBlur)
            append(" dp")
        }
        binding.sliderShadowBlur.setOnSeekBarChangeListener(onProgress { progress ->
            box.flexiShadowBlur = progress.toFloat()
            binding.tvShadowBlur.text = buildString {
                append("Shadow Blur: ")
                append(box.flexiShadowBlur)
                append(" dp")
            }
        })

        binding.sliderShadowSpread.progress = box.flexiShadowSpread.toInt()
        binding.tvShadowSpread.text = buildString {
            append("Shadow Spread: ")
            append(box.flexiShadowSpread)
            append(" dp")
        }
        binding.sliderShadowSpread.setOnSeekBarChangeListener(onProgress { progress ->
            box.flexiShadowSpread = progress.toFloat()
            binding.tvShadowSpread.text = buildString {
                append("Shadow Spread: ")
                append(box.flexiShadowSpread)
                append(" dp")
            }
        })

        binding.sliderShadowOffsetX.apply {
            max = offsetRange * 2
            progress = offsetRange // represents 0
            setOnSeekBarChangeListener(onProgress {
                box.flexiShadowOffsetX = (it - offsetRange).toFloat()
                binding.tvShadowOffsetX.text = buildString {
                    append("Shadow Offset X: ")
                    append(box.flexiShadowOffsetX)
                    append(" dp")
                }
            })
        }
        binding.sliderShadowOffsetY.apply {
            max = offsetRange * 2
            progress = offsetRange
            setOnSeekBarChangeListener(onProgress {
                box.flexiShadowOffsetY = (it - offsetRange).toFloat()
                binding.tvShadowOffsetY.text = buildString {
                    append("Shadow Offset Y: ")
                    append(box.flexiShadowOffsetY)
                    append(" dp")
                }
            })
        }

        binding.btnFillSolid.setOnClickListener {
            box.fillType = FillType.SOLID
            box.fillColor = Color.CYAN
        }
        binding.btnFillLinear.setOnClickListener {
            box.setFillGradient(Color.YELLOW, Color.MAGENTA, angle = 45f)
        }
        binding.btnFillRadial.setOnClickListener {
            box.fillType = FillType.RADIAL
            box.fillGradientStart = Color.GREEN
            box.fillGradientEnd = Color.BLUE
        }
    }

    private fun setupToggleButtons() {
        binding.btnToggleBorder.setOnClickListener {
            toggleSection("border", binding.borderControlsContainer, binding.btnToggleBorder)
        }

        binding.btnToggleCorner.setOnClickListener {
            toggleSection("corner", binding.cornerControlsContainer, binding.btnToggleCorner)
        }

        binding.btnToggleShadow.setOnClickListener {
            toggleSection("shadow", binding.shadowControlsContainer, binding.btnToggleShadow)
        }

        binding.btnToggleFill.setOnClickListener {
            toggleSection("fill", binding.fillControlsContainer, binding.btnToggleFill)
        }
    }

    private fun toggleSection(
        sectionKey: String,
        container: android.view.ViewGroup,
        button: android.widget.Button,
    ) {
        val isExpanded = sectionStates[sectionKey] ?: true

        TransitionManager.beginDelayedTransition(binding.root, AutoTransition().apply {
            duration = 300 // milliseconds
        })

        if (isExpanded) {
            container.visibility = android.view.View.GONE
            button.text = "+"
            sectionStates[sectionKey] = false
        } else {
            container.visibility = android.view.View.VISIBLE
            button.text = "−"
            sectionStates[sectionKey] = true
        }
    }

    private fun updateBorderTypeButtonText(type: BorderType) {
        binding.btnBorderType.text = when (type) {
            BorderType.NONE -> "Border Type: NONE"
            BorderType.FILL -> "Border Type: FILL"
            BorderType.GRADIENT -> "Border Type: GRADIENT"
        }
    }

    private fun updateBorderAnimButtonText(isAnimating: Boolean) {
        binding.btnBorderAnim.text =
            if (isAnimating) "Border Animation: ON" else "Border Animation: OFF"
    }

    private fun onProgress(change: (Int) -> Unit) =
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                change(progress)
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        }

    private fun SeekBar.initWith(initial: Int, onChange: (Int) -> Unit) {
        progress = initial
        setOnSeekBarChangeListener(onProgress(onChange))
    }
}