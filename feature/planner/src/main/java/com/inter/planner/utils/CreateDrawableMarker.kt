package com.inter.planner.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import com.inter.planner.databinding.CustomMarkerBinding

object CreateDrawableMarker {
    fun CreateDrawableFromLayout(context: Context, markerOrder:Int): Drawable {
        val drawableView =CustomMarkerBinding.inflate(LayoutInflater.from(context),null,false)
        drawableView.tvMarkerOrder.text =""+markerOrder
        drawableView.root.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        drawableView.root.layout(0, 0, drawableView.root.measuredWidth, drawableView.root.measuredHeight)
        val bitmap =
            Bitmap.createBitmap(drawableView.root.width, drawableView.root.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        drawableView.root.draw(canvas)

        return BitmapDrawable(context.resources, bitmap)

    }
}