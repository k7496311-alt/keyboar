package com.example.keyboard.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.keyboard.settings.PreferencesManager

class BanglaKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    interface KeyboardActionListener {
        fun onKeyText(text: String)
        fun onBackspace()
        fun onEnter()
        fun onSpace()
        fun onLanguageSwitch()
        fun onShiftToggle()
        fun onSymbolToggle()
        fun onEmojiToggle()
        fun onClipboardToggle()
        fun onCursorMove(offset: Int)
        fun onCandidateSelected(candidate: String)
    }

    var listener: KeyboardActionListener? = null

    private var currentLang = PreferencesManager.LANG_BANGLA
    private var isShifted = false
    private var isSymbolMode = false

    // Views
    private val toolbarContainer = LinearLayout(context)
    private val candidateContainer = HorizontalScrollView(context)
    private val candidateLayout = LinearLayout(context)
    private val keysContainer = LinearLayout(context)

    // Touch tracking for spacebar cursor swipe
    private var spaceTouchStartX = 0f
    private var isSpaceSwiping = false

    init {
        orientation = VERTICAL
        setBackgroundColor(Color.parseColor("#1C1B1F")) // Elegant Dark Background
        setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(8))

        setupToolbar()
        setupCandidateStrip()
        setupKeysContainer()

        buildKeyboardLayout()
    }

    private fun setupToolbar() {
        toolbarContainer.orientation = HORIZONTAL
        toolbarContainer.gravity = Gravity.CENTER_VERTICAL
        toolbarContainer.setPadding(dpToPx(6), dpToPx(2), dpToPx(6), dpToPx(2))

        val langBtn = createToolbarButton(if (currentLang == PreferencesManager.LANG_BANGLA) "বাংলা" else "ENG") {
            listener?.onLanguageSwitch()
        }
        val emojiBtn = createToolbarButton("😀") {
            listener?.onEmojiToggle()
        }
        val clipBtn = createToolbarButton("📋") {
            listener?.onClipboardToggle()
        }

        toolbarContainer.addView(langBtn)
        toolbarContainer.addView(emojiBtn)
        toolbarContainer.addView(clipBtn)

        addView(toolbarContainer, LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(38)))
    }

    private fun setupCandidateStrip() {
        candidateContainer.isHorizontalScrollBarEnabled = false
        candidateLayout.orientation = HORIZONTAL
        candidateLayout.gravity = Gravity.CENTER_VERTICAL
        candidateLayout.setPadding(dpToPx(8), 0, dpToPx(8), 0)

        candidateContainer.addView(
            candidateLayout,
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        )
        addView(candidateContainer, LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(36)))

        // Initial candidates sample
        updateCandidates(listOf("আমি", "আমার", "আমাদের", "বাংলাদেশ"))
    }

    fun updateCandidates(candidates: List<String>) {
        candidateLayout.removeAllViews()
        for (cand in candidates) {
            val tv = TextView(context).apply {
                text = cand
                setTextColor(Color.parseColor("#E6E1E5"))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                typeface = Typeface.DEFAULT_BOLD
                setPadding(dpToPx(12), dpToPx(4), dpToPx(12), dpToPx(4))
                background = createDrawable("#4A4458", dpToPx(12))
                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                params.setMargins(dpToPx(4), 0, dpToPx(4), 0)
                layoutParams = params
                setOnClickListener {
                    listener?.onCandidateSelected(cand)
                }
            }
            candidateLayout.addView(tv)
        }
    }

    private fun setupKeysContainer() {
        keysContainer.orientation = VERTICAL
        addView(keysContainer, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }

    fun setLanguage(lang: String) {
        if (currentLang != lang) {
            currentLang = lang
            buildKeyboardLayout()
            (toolbarContainer.getChildAt(0) as? TextView)?.text =
                if (currentLang == PreferencesManager.LANG_BANGLA) "বাংলা" else "ENG"
        }
    }

    fun setSymbolMode(symbol: Boolean) {
        if (isSymbolMode != symbol) {
            isSymbolMode = symbol
            buildKeyboardLayout()
        }
    }

    fun setShifted(shifted: Boolean) {
        if (isShifted != shifted) {
            isShifted = shifted
            buildKeyboardLayout()
        }
    }

    private fun buildKeyboardLayout() {
        keysContainer.removeAllViews()

        if (isSymbolMode) {
            buildSymbolLayout()
            return
        }

        if (currentLang == PreferencesManager.LANG_BANGLA) {
            buildBanglaPhoneticLayout()
        } else {
            buildEnglishLayout()
        }
    }

    private fun buildEnglishLayout() {
        val row1 = if (isShifted) listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P")
        else listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")

        val row2 = if (isShifted) listOf("A", "S", "D", "F", "G", "H", "J", "K", "L")
        else listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")

        val row3Keys = if (isShifted) listOf("Z", "X", "C", "V", "B", "N", "M")
        else listOf("z", "x", "c", "v", "b", "n", "m")

        addKeyRow(row1)
        addKeyRow(row2, sideMarginDp = 12)

        // Row 3 with Shift & Backspace
        val row3Layout = LinearLayout(context).apply { orientation = HORIZONTAL }
        val shiftBtn = createSpecialKey(if (isShifted) "⇪" else "⇧", weight = 1.3f) {
            listener?.onShiftToggle()
        }
        row3Layout.addView(shiftBtn)

        for (k in row3Keys) {
            row3Layout.addView(createCharacterKey(k, weight = 1f))
        }

        val backspaceBtn = createSpecialKey("⌫", weight = 1.3f) {
            listener?.onBackspace()
        }
        row3Layout.addView(backspaceBtn)
        keysContainer.addView(row3Layout)

        // Row 4 (Bottom Bar)
        buildBottomRow()
    }

    private fun buildBanglaPhoneticLayout() {
        // Phonetic row structure for Bangla typing
        val row1 = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
        val row2 = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")
        val row3Keys = listOf("z", "x", "c", "v", "b", "n", "m")

        addKeyRow(row1)
        addKeyRow(row2, sideMarginDp = 12)

        val row3Layout = LinearLayout(context).apply { orientation = HORIZONTAL }
        val shiftBtn = createSpecialKey("কখগ", weight = 1.3f) {
            listener?.onShiftToggle()
        }
        row3Layout.addView(shiftBtn)

        for (k in row3Keys) {
            row3Layout.addView(createCharacterKey(k, weight = 1f))
        }

        val backspaceBtn = createSpecialKey("⌫", weight = 1.3f) {
            listener?.onBackspace()
        }
        row3Layout.addView(backspaceBtn)
        keysContainer.addView(row3Layout)

        buildBottomRow()
    }

    private fun buildSymbolLayout() {
        val row1 = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
        val row2 = listOf("@", "#", "$", "%", "&", "-", "+", "(", ")")
        val row3Keys = listOf("*", "\"", "'", ":", ";", "!", "?")

        addKeyRow(row1)
        addKeyRow(row2, sideMarginDp = 12)

        val row3Layout = LinearLayout(context).apply { orientation = HORIZONTAL }
        val symAltBtn = createSpecialKey("1/2", weight = 1.3f) {
            // Toggle additional symbols
        }
        row3Layout.addView(symAltBtn)

        for (k in row3Keys) {
            row3Layout.addView(createCharacterKey(k, weight = 1f))
        }

        val backspaceBtn = createSpecialKey("⌫", weight = 1.3f) {
            listener?.onBackspace()
        }
        row3Layout.addView(backspaceBtn)
        keysContainer.addView(row3Layout)

        buildBottomRow()
    }

    private fun buildBottomRow() {
        val row = LinearLayout(context).apply { orientation = HORIZONTAL }

        val symBtn = createSpecialKey(if (isSymbolMode) "ABC" else "?123", weight = 1.3f) {
            listener?.onSymbolToggle()
        }

        val langSwitchBtn = createSpecialKey("🌐", weight = 1f) {
            listener?.onLanguageSwitch()
        }

        val spaceBtn = createSpacebarKey(weight = 4f)

        val periodBtn = createCharacterKey(".", weight = 1f)

        val enterBtn = createSpecialKey("⏎", weight = 1.5f, bgColor = "#D0BCFF", textColorHex = "#381E72") {
            listener?.onEnter()
        }

        row.addView(symBtn)
        row.addView(langSwitchBtn)
        row.addView(spaceBtn)
        row.addView(periodBtn)
        row.addView(enterBtn)

        keysContainer.addView(row)
    }

    private fun addKeyRow(keys: List<String>, sideMarginDp: Int = 0) {
        val row = LinearLayout(context).apply {
            orientation = HORIZONTAL
            if (sideMarginDp > 0) {
                setPadding(dpToPx(sideMarginDp), 0, dpToPx(sideMarginDp), 0)
            }
        }
        for (key in keys) {
            row.addView(createCharacterKey(key, weight = 1f))
        }
        keysContainer.addView(row)
    }

    private fun createCharacterKey(label: String, weight: Float): View {
        val frame = FrameLayout(context)
        val tv = TextView(context).apply {
            text = label
            setTextColor(Color.parseColor("#E6E1E5"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            gravity = Gravity.CENTER
            background = createDrawable("#2B2930", dpToPx(8))
        }

        val params = LayoutParams(0, dpToPx(48), weight)
        params.setMargins(dpToPx(2), dpToPx(3), dpToPx(2), dpToPx(3))
        frame.layoutParams = params

        frame.addView(tv, FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        frame.setOnClickListener {
            listener?.onKeyText(label)
        }

        return frame
    }

    private fun createSpecialKey(
        label: String,
        weight: Float,
        bgColor: String = "#4A4458",
        textColorHex: String = "#E6E1E5",
        onClick: () -> Unit
    ): View {
        val frame = FrameLayout(context)
        val tv = TextView(context).apply {
            text = label
            setTextColor(Color.parseColor(textColorHex))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            background = createDrawable(bgColor, dpToPx(8))
        }

        val params = LayoutParams(0, dpToPx(48), weight)
        params.setMargins(dpToPx(2), dpToPx(3), dpToPx(2), dpToPx(3))
        frame.layoutParams = params

        frame.addView(tv, FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        frame.setOnClickListener {
            onClick()
        }

        return frame
    }

    private fun createSpacebarKey(weight: Float): View {
        val frame = FrameLayout(context)
        val tv = TextView(context).apply {
            text = if (currentLang == PreferencesManager.LANG_BANGLA) "বাংলা" else "English"
            setTextColor(Color.parseColor("#938F99"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            gravity = Gravity.CENTER
            background = createDrawable("#2B2930", dpToPx(8))
        }

        val params = LayoutParams(0, dpToPx(48), weight)
        params.setMargins(dpToPx(2), dpToPx(3), dpToPx(2), dpToPx(3))
        frame.layoutParams = params

        frame.addView(tv, FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        // Spacebar touch & swipe gesture for cursor control
        frame.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    spaceTouchStartX = event.x
                    isSpaceSwiping = false
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.x - spaceTouchStartX
                    if (Math.abs(deltaX) > dpToPx(24)) {
                        isSpaceSwiping = true
                        if (deltaX > 0) {
                            listener?.onCursorMove(1)
                        } else {
                            listener?.onCursorMove(-1)
                        }
                        spaceTouchStartX = event.x
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (!isSpaceSwiping) {
                        listener?.onSpace()
                    }
                }
            }
            true
        }

        return frame
    }

    private fun createToolbarButton(label: String, onClick: () -> Unit): TextView {
        return TextView(context).apply {
            text = label
            setTextColor(Color.parseColor("#E6E1E5"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            gravity = Gravity.CENTER
            setPadding(dpToPx(10), dpToPx(4), dpToPx(10), dpToPx(4))
            background = createDrawable("#4A4458", dpToPx(16))
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.setMargins(dpToPx(4), 0, dpToPx(4), 0)
            layoutParams = params
            setOnClickListener { onClick() }
        }
    }

    private fun createDrawable(colorHex: String, radiusPx: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.parseColor(colorHex))
            cornerRadius = radiusPx.toFloat()
        }
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}
