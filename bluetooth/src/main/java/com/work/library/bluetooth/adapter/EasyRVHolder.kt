package com.work.library.bluetooth.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Checkable
import android.widget.ImageView
import android.widget.TextView

/**
 * <p>作者：01140782</p>
 * <p>日期：2018/1/23 10:08</p>
 * <p>描述: </p>
 */
class EasyRVHolder(protected var mContext: Context, val layoutId: Int,
                   itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mViews = SparseArray<View>()

    init {
        this.itemView.tag = this
    }

    fun <V : View> getView(viewId: Int): V {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = this.itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as V
    }

    fun setOnItemViewClickListener(listener: View.OnClickListener): EasyRVHolder {
        this.itemView.setOnClickListener(listener)
        return this
    }

    fun setText(viewId: Int, value: String): EasyRVHolder {
        val view = getView<TextView>(viewId)
        view.text = value
        return this
    }

    fun setTextColor(viewId: Int, color: Int): EasyRVHolder {
        val view = getView<TextView>(viewId)
        view.setTextColor(color)
        return this
    }

    fun setTextColorRes(viewId: Int, colorRes: Int): EasyRVHolder {
        val view = getView<TextView>(viewId)
        view.setTextColor(ContextCompat.getColor(mContext, colorRes))
        return this
    }

    fun setImageResource(viewId: Int, imgResId: Int): EasyRVHolder {
        val view = getView<ImageView>(viewId)
        view.setImageResource(imgResId)
        return this
    }

    fun setBackgroundColor(viewId: Int, color: Int): EasyRVHolder {
        val view = getView<View>(viewId)
        view.setBackgroundColor(color)
        return this
    }

    fun setBackgroundColorRes(viewId: Int, colorRes: Int): EasyRVHolder {
        val view = getView<View>(viewId)
        view.setBackgroundResource(colorRes)
        return this
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable?): EasyRVHolder {
        val view = getView<ImageView>(viewId)
        view.setImageDrawable(drawable)
        return this
    }

    fun setImageDrawableRes(viewId: Int, drawableRes: Int): EasyRVHolder {
        val drawable = ContextCompat.getDrawable(mContext, drawableRes)
        return setImageDrawable(viewId, drawable)
    }

    fun setImageBitmap(viewId: Int, imgBitmap: Bitmap): EasyRVHolder {
        val view = getView<ImageView>(viewId)
        view.setImageBitmap(imgBitmap)
        return this
    }

    fun setVisible(viewId: Int, visible: Boolean): EasyRVHolder {
        val view = getView<View>(viewId)
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun setVisible(viewId: Int, visible: Int): EasyRVHolder {
        val view = getView<View>(viewId)
        view.visibility = visible
        return this
    }

    fun setTag(viewId: Int, tag: Any): EasyRVHolder {
        val view = getView<View>(viewId)
        view.tag = tag
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any): EasyRVHolder {
        val view = getView<View>(viewId)
        view.setTag(key, tag)
        return this
    }

    fun setChecked(viewId: Int, checked: Boolean): EasyRVHolder {
        val view = getView<View>(viewId)
        (view as Checkable).setChecked(checked)
        return this
    }

    fun setAlpha(viewId: Int, value: Float): EasyRVHolder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView<View>(viewId).alpha = value
        } else {
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            getView<View>(viewId).startAnimation(alpha)
        }
        return this
    }

    fun setTypeface(viewId: Int, typeface: Typeface): EasyRVHolder {
        val view = getView<TextView>(viewId)
        view.typeface = typeface
        view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        return this
    }

    fun setTypeface(typeface: Typeface, vararg viewIds: Int): EasyRVHolder {
        for (viewId in viewIds) {
            val view = getView<TextView>(viewId)
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setOnClickListener(viewId: Int, listener: View.OnClickListener): EasyRVHolder {
        val view = getView<View>(viewId)
        view.setOnClickListener(listener)
        return this
    }
}
