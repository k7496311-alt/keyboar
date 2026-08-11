package com.example.keyboard

import android.inputmethodservice.InputMethodService
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import com.example.keyboard.settings.PreferencesManager
import com.example.keyboard.ui.BanglaKeyboardView

class BanglaKeyboardService : InputMethodService(), BanglaKeyboardView.KeyboardActionListener {

    private var keyboardView: BanglaKeyboardView? = null
    private lateinit var prefsManager: PreferencesManager

    private var currentLanguage = PreferencesManager.LANG_BANGLA
    private var isShifted = false
    private var isSymbolMode = false

    override fun onCreate() {
        super.onCreate()
        prefsManager = PreferencesManager(this)
        currentLanguage = prefsManager.currentLanguage
        Log.d(TAG, "BanglaKeyboardService onCreate")
    }

    override fun onCreateInputView(): View {
        Log.d(TAG, "BanglaKeyboardService onCreateInputView")
        return try {
            val view = BanglaKeyboardView(this)
            view.layoutParams = android.view.ViewGroup.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            )
            view.listener = this
            view.setLanguage(currentLanguage)
            keyboardView = view
            Log.d(TAG, "Keyboard view created and set successfully")
            view
        } catch (e: Exception) {
            Log.e(TAG, "Error creating keyboard view", e)
            val fallback = BanglaKeyboardView(this)
            fallback.layoutParams = android.view.ViewGroup.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            )
            fallback
        }
    }

    override fun onEvaluateInputViewShown(): Boolean {
        super.onEvaluateInputViewShown()
        Log.d(TAG, "onEvaluateInputViewShown forced to true")
        return true
    }

    override fun onShowInputRequested(flags: Int, configChange: Boolean): Boolean {
        Log.d(TAG, "onShowInputRequested called")
        return true
    }

    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        Log.d(TAG, "onStartInput called")
        attribute?.let { info ->
            val inputType = info.inputType
            val variation = inputType and InputType.TYPE_MASK_VARIATION
            val isPassword = variation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                    variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
                    variation == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD

            if (isPassword) {
                keyboardView?.updateCandidates(emptyList())
            }
        }
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        Log.d(TAG, "onStartInputView called")
        keyboardView?.setLanguage(currentLanguage)
        keyboardView?.setShifted(isShifted)
        keyboardView?.setSymbolMode(isSymbolMode)
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        Log.d(TAG, "onFinishInputView called")
    }

    override fun onFinishInput() {
        super.onFinishInput()
        Log.d(TAG, "onFinishInput called")
    }

    override fun onDestroy() {
        Log.d(TAG, "BanglaKeyboardService onDestroy")
        keyboardView = null
        super.onDestroy()
    }

    // --- KeyboardActionListener implementation ---

    override fun onKeyText(text: String) {
        try {
            val ic = currentInputConnection ?: return
            ic.commitText(text, 1)

            // Auto-reset shift state if single shift was active
            if (isShifted) {
                isShifted = false
                keyboardView?.setShifted(false)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to commit text: $text", e)
        }
    }

    override fun onBackspace() {
        try {
            val ic = currentInputConnection ?: return
            val selectedText = ic.getSelectedText(0)
            if (selectedText != null && selectedText.isNotEmpty()) {
                ic.commitText("", 1)
            } else {
                ic.deleteSurroundingText(1, 0)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to execute backspace", e)
        }
    }

    override fun onEnter() {
        try {
            val ic = currentInputConnection ?: return
            val editorInfo = currentInputEditorInfo
            if (editorInfo != null && editorInfo.actionId != 0) {
                ic.performEditorAction(editorInfo.actionId)
            } else {
                ic.commitText("\n", 1)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to execute enter", e)
        }
    }

    override fun onSpace() {
        try {
            val ic = currentInputConnection ?: return
            ic.commitText(" ", 1)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to commit space", e)
        }
    }

    override fun onLanguageSwitch() {
        currentLanguage = if (currentLanguage == PreferencesManager.LANG_BANGLA) {
            PreferencesManager.LANG_ENGLISH
        } else {
            PreferencesManager.LANG_BANGLA
        }
        prefsManager.currentLanguage = currentLanguage
        keyboardView?.setLanguage(currentLanguage)
    }

    override fun onShiftToggle() {
        isShifted = !isShifted
        keyboardView?.setShifted(isShifted)
    }

    override fun onSymbolToggle() {
        isSymbolMode = !isSymbolMode
        keyboardView?.setSymbolMode(isSymbolMode)
    }

    override fun onEmojiToggle() {
        // Emoji panel toggle (will be fully populated in Phase 5)
        onKeyText("😊")
    }

    override fun onClipboardToggle() {
        // Clipboard panel toggle (will be fully populated in Phase 6)
    }

    override fun onCursorMove(offset: Int) {
        try {
            val ic = currentInputConnection ?: return
            if (offset > 0) {
                ic.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_DPAD_RIGHT))
                ic.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_DPAD_RIGHT))
            } else if (offset < 0) {
                ic.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_DPAD_LEFT))
                ic.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_DPAD_LEFT))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed cursor move", e)
        }
    }

    override fun onCandidateSelected(candidate: String) {
        try {
            val ic = currentInputConnection ?: return
            ic.commitText("$candidate ", 1)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to commit candidate", e)
        }
    }

    companion object {
        private const val TAG = "BanglaKeyboardService"
    }
}
