package com.example.vacantionapp.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import com.example.vacantionapp.R


class CustomLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_input_layout, this, true)
    }

    val clearImage: Drawable =
        ResourcesCompat.getDrawable(context.resources, R.drawable.baseline_clear_24, null)!!
    val field = ResourcesCompat.getDrawable(
        context.resources, R.drawable.edit_text_background_primary_border, null
    )
    val errorImage: Drawable =
        ResourcesCompat.getDrawable(context.resources, R.drawable.baseline_warning_amber_24, null)!!
    val errorField =
        ResourcesCompat.getDrawable(context.resources, R.drawable.edit_text_background_error, null)
    val editText = findViewById<EditText>(R.id.userEditText)
    val hintTextView = findViewById<TextView>(R.id.hintTextView)
    val inputButton = findViewById<ImageView>(R.id.inputButton)


    fun getUserInputText(): String {


        editText.setOnFocusChangeListener { _, hasFocus ->
            editText.background = field
            inputButton.visibility = View.GONE
            hintTextView.visibility = if (hasFocus) VISIBLE else GONE
//                if (hasFocus && editText.text.isNullOrEmpty()) View.VISIBLE else View.GONE
        }

        editText.doOnTextChanged { _, _, _, _ ->
            inputButton.background = clearImage
            inputButton.visibility = if (editText.text.isNotEmpty()) View.VISIBLE else View.GONE
        }

        inputButton.setOnClickListener {
            editText.text?.clear()
            inputButton.visibility = View.GONE
        }

        return editText.text.toString()
    }


    fun errorFields() {

        editText.background = errorField
        inputButton.background = errorImage
        inputButton.visibility = View.VISIBLE

        editText.doOnTextChanged { text, start, before, count -> getUserInputText() }


        inputButton.setOnClickListener {


            val popupMessage = "This field cannot be blank"
            showCustomErrorPopup(popupMessage)

        }


    }

    private fun showCustomErrorPopup(message: String) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.custom_error_popup, null)
        val popupMessageView = popupView.findViewById<TextView>(R.id.popupMessage)
        popupMessageView.text = message

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Show the popup near the input button
        popupWindow.showAsDropDown(inputButton, 0, -inputButton.height)
    }
    // You can add more functions here as needed
}
