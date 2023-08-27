package com.param.exercise.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.coroutineScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.param.kohinoor.R
import kotlinx.coroutines.*


fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}


fun <T> androidLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun Int.asColor(app: Context) = ContextCompat.getColor(app, this)
fun Int.asDimen(app: Resources) = app.getDimension(this)
fun Int.asDrawable(app: Context) = ContextCompat.getDrawable(app, this)
fun TextView.setHtml(text: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        this.text = Html.fromHtml(text)
    }
}

fun View.delayOnLifecycle(
    durationInMills: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit
): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
        delay(durationInMills)
        block()
    }
}

fun CoroutineScope.launchPeriodicAsync(repeatMillis: Long, action: () -> Unit) = this.async {
    while (isActive) {
        action()
        delay(repeatMillis)
    }
}

fun EditText.toTextview() {
    this.isFocusable = false
    this.isFocusableInTouchMode = false
    this.isClickable = false
}

fun EditText.setEditable() {
    this.isFocusable = true
    this.isFocusableInTouchMode = true
    this.isClickable = true
}

fun EditText.addSuffix(suffix: String) {
    val editText = this
    val formattedSuffix = " $suffix"
    var text = ""
    var tempText = ""
    var isSuffixModified = false

    val setCursorPosition: () -> Unit =
        { Selection.setSelection(editableText, editableText.length - formattedSuffix.length) }

    val setEditText: () -> Unit = {
        editText.setText(text)
        setCursorPosition()
    }

    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            val newText = editable.toString()

            if (isSuffixModified) {
                // user tried to modify suffix
                isSuffixModified = false
                setEditText()
            } else if (text.isNotEmpty() && newText.length < text.length && !newText.contains(
                    formattedSuffix
                )
            ) {
                // user tried to delete suffix
                setEditText()
            } else if (!newText.contains(formattedSuffix)) {
                // new input, add suffix
                text = "$newText$formattedSuffix"
                setEditText()
            } else {
                if (newText.contains(formattedSuffix)) {
                    val value = newText.split(" %")
                    if (value[0] != "" && value[0].toInt() > 100) {
                        this@addSuffix.error = "Value should not be greater than 100"
                        text = tempText
                        setEditText()
                        return
                    }
                }
                tempText = newText
                text = newText
            }
        }

        override fun beforeTextChanged(
            charSequence: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
            charSequence?.let {
                val textLengthWithoutSuffix = it.length - formattedSuffix.length
                if (it.isNotEmpty() && start > textLengthWithoutSuffix) {
                    isSuffixModified = true
                }
            }
        }

        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
        }
    })
}
//fun DisplayMetrics.setDisplayMetrics(activity: Activity?) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        activity?.display?.getRealMetrics(this)
//    } else {
//        activity?.windowManager?.defaultDisplay?.getMetrics(this)
//    }
//}
//
//fun Window.fullScreen() {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        this.insetsController?.hide(WindowInsets.Type.statusBars())
//    } else {
//        this.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//    }
//}
//fun Window.hideFullScreen() {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        this.insetsController?.show(WindowInsets.Type.statusBars())
//    } else {
//        this.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//    }
//}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                // use this to change the link color
                textPaint.color = textPaint.linkColor
                // toggle below value to enable/disable
                // the underline shown below the clickable text
                textPaint.isUnderlineText = true
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
//      if(startIndexOfLink == -1) continue // todo if you want to verify your texts contains links text
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ImageView.loadImg(url: String?, error: Int, activity: Context?) {
    if (url != null) {
        if (activity != null) {
            Glide.with(activity).load(url).apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_default_placeholder)
                    .error(error)
                    .centerCrop().fitCenter()
            ).into(this)
        }
    }

}
fun ImageView.loadImg(url: String?) {
    if (url != null) {
        if (this.context != null) {
            Glide.with(context).load(url).apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_default_placeholder)
                    .centerCrop().fitCenter()
            ).into(this)
        }
    }

}

fun Int.gender(): String {
    return if (this == 1) {
        "Male"
    } else {
        "Female"
    }
}

fun View.backgroundColor(colorBackground: String) {
    val colorArray = colorBackground.split(",")
    if (colorArray.size == 3) {
        try {
            this.setBackgroundColor(
                Color.rgb(
                    colorArray[0].toInt(), colorArray[1].toInt(), colorArray[2].toInt()
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


//fun ImageView.loadSave(url: String?, ctx: Activity) {
//    if (url != null) {
//        Glide.with(ctx).load(url).apply {
//            placeholder(R.drawable.anim_progress)
//            error(R.mipmap.ic_launcher)
//            diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//            override(300, 240)
//        }.into(this)
//    }
//}

fun ViewPager2.findFragmentAtPosition(
    fragmentManager: FragmentManager,
    position: Int
): Fragment? {
    return fragmentManager.findFragmentByTag("f$position")
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun Activity.isHasFreeSpace(): Boolean {
    val freeSpace = applicationContext.filesDir.freeSpace
    val mb = 1024L * 1024L
    val spaceMB = freeSpace / mb
    if (spaceMB > 700) {
        return true
    }
    return false
}

fun String.toSpanned(): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        return Html.fromHtml(this)
    }
}

val Context.appContext: Application
    get() = applicationContext as Application

fun TextView.setStatus(statusString:String?,status:CardView){
    when (statusString) {
        "completed" -> {
            this.text = "Completed"
            status.setCardBackgroundColor(
                ContextCompat.getColor(
                    this.context,
                    R.color.text_color_green
                )
            )
        }

        "cancelled" -> {
            this.text = "Cancelled"
            status.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.text_color_orange
                )
            )
        }
        "on-hold" -> {
            text = "On hold"
            status.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.text_color_grey
                )
            )
        }
        "refunded" -> {
            text = "Refunded"
            status.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.text_color_blue
                )
            )
        }

        "pending" -> {
            text = "Pending payment"
            status.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.text_color_mustard
                )
            )
        }
    }
}
fun AlertDialog.Builder.positiveButton(
    name: String = "Okay",
    clickListener: (which: Int) -> Any = {}
) {
    this.setPositiveButton(
        name
    ) { _, which -> clickListener(which) }
}

fun AlertDialog.Builder.negativeButton(
    name: String = "Cancel",
    clickListener: (which: Int) -> Any = {}
) {
    this.setNegativeButton(
        name
    ) { _, which -> clickListener(which) }
}


fun Any.error(message: String) {
    Log.e(this::class.java.simpleName, message)
}

val colors = arrayOf(
    Color.parseColor("#FFC001"),
    Color.parseColor("#0DDAB3")
)

fun CardView.doRandomColor() {
    val color: Int = colors.random()
    setCardBackgroundColor(color)
}

fun Group.addOnClickListener(listener: (view: View) -> Unit) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(listener)
    }
}


/**
 * Custom snackbar extension function
 */
//fun Snackbar.config(context: Context) {
//    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
//    params.setMargins(12, 12, 12, 12)
//    this.view.layoutParams = params
//    this.view.background = (R.drawable.bg_snackbar).asDrawable(context)
//    ViewCompat.setElevation(this.view, 6f)
//}


val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }







