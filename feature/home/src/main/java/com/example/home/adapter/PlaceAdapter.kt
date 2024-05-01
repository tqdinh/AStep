package com.example.home.adapter

import android.content.Context

import android.graphics.drawable.Drawable

import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.home.R
import com.example.home.databinding.CustomImageBinding
import com.example.home.databinding.CustomPlaceBinding
import com.inter.entity.planner.PlaceEntity
import java.text.SimpleDateFormat
import java.util.Date


class PlaceAdapter(val onItemSelectOptionListener: OnItemSelectOptionListener) : BaseAdapter() {

    var listPlaces: MutableList<PlaceEntity> = mutableListOf()

    fun submitNewList(newlist: MutableList<PlaceEntity>) {
        listPlaces = newlist
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return listPlaces.size
    }

    override fun getItem(position: Int): Any {
        return listPlaces.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            CustomPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val windowManager = parent.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val widthPixels = displayMetrics.widthPixels
        val heightPixels = displayMetrics.heightPixels


        val layoutParams = ViewGroup.LayoutParams(widthPixels, (heightPixels * 0.4).toInt())
        view.root.layoutParams = layoutParams


        val customImageBinding: CustomImageBinding =
            CustomImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

//        val imageView = ImageView(parent.context)
        val imageView = customImageBinding.ivPhoto
        imageView.layoutParams = ConstraintLayout.LayoutParams(
            (heightPixels * 0.3).toInt(), (heightPixels * 0.3).toInt()
        )


        listPlaces?.get(position)?.listImage?.forEach {

            val requestOptions: RequestOptions = RequestOptions()
                //.placeholder(android.R.drawable.progress_indeterminate_horizontal) // Set a placeholder image
                .placeholder(R.drawable.img_holder_bg)
                .error(R.drawable.ic_error).centerCrop()
            // Set an error image if the loading fails


            Glide.with(customImageBinding.root.context)

                .load(it.path)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {


//                        view.tvPlaceTime.text = e.toString()

                        Log.d("GLIDE_ERROR", e.toString())

                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {


                        return false
                    }

                })
                .into(imageView)
            view.llImages.addView(customImageBinding.root)
        }



        view.tvPlaceOrder.text = "" + position

        listPlaces?.get(position)?.apply {

            val date = Date(this.timestamp)


            val sdfDate = SimpleDateFormat("dd/MM/yyyy")
            val sdfTime = SimpleDateFormat("HH:mm")

            val formattedDate: String = sdfDate.format(date)
            val formattedTime: String = sdfTime.format(date)



            if (this.listImage.isNotEmpty()) {
//                view.tvPlaceTime.text = this.listImage.first().path

            }
            view.tvPlaceDate.text = formattedDate
            //view.tvPlaceTime.text = formattedTime

        }
        view.llDeletePlace.setOnClickListener {
            onItemSelectOptionListener.onDeletePlace(position)
        }

        view.llDirection.setOnClickListener {
            onItemSelectOptionListener.onNavigatePlace(position)
        }


        return view.root
    }


    interface OnItemSelectOptionListener {
        fun onDeletePlace(position: Int)
        fun onNavigatePlace(position: Int)
        fun onSharePlace(position: Int)

        fun onSelectPlace(position: Int)
    }

}