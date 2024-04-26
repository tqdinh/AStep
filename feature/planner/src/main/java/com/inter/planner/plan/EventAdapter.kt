package com.inter.planner.plan

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.inter.planner.databinding.JourneyCustomeViewBinding
import com.inter.entity.planner.JourneyEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EventAdapter(
    private val onItemClickListener: OnItemClickListener,
) : ListAdapter<com.inter.entity.planner.JourneyEntity, EventAdapter.EventViewHolder>(object :
    DiffUtil.ItemCallback<com.inter.entity.planner.JourneyEntity>() {
    override fun areItemsTheSame(oldItem: com.inter.entity.planner.JourneyEntity, newItem: com.inter.entity.planner.JourneyEntity): Boolean {
        return oldItem.listPlaces.size == newItem.listPlaces.size
    }

    override fun areContentsTheSame(oldItem: com.inter.entity.planner.JourneyEntity, newItem: com.inter.entity.planner.JourneyEntity): Boolean {
        return oldItem.listPlaces.size == newItem.listPlaces.size

    }
}) {

    interface OnItemClickListener {
        fun onItemClick(item: com.inter.entity.planner.JourneyEntity)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            JourneyCustomeViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        val windowManager = parent.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val widthPixels = displayMetrics.widthPixels


        val layoutParams = ViewGroup.LayoutParams(widthPixels, (widthPixels * 0.8).toInt())
        binding.root.layoutParams = layoutParams

        return ActiveEventViewHolder(binding, parent.context, widthPixels)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == currentList.size - 1) {
            return 1
        } else if (position == 0) {
            return 0
        }
        return 2
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position), position)

    }

    inner class ActiveEventViewHolder(
        binding: JourneyCustomeViewBinding,
        context: Context,
        width: Int
    ) :
        EventViewHolder(binding, onItemClickListener, context, width) {

        override fun bind(item: com.inter.entity.planner.JourneyEntity, position: Int) {
            super.bind(item, position)
        }


    }

    open class EventViewHolder(

        val binding: JourneyCustomeViewBinding,
        private val onItemClickListener: OnItemClickListener,
        val context: Context,
        val width: Int
    ) : RecyclerView.ViewHolder(binding.root) {

        open fun bind(item: com.inter.entity.planner.JourneyEntity, position: Int) {
            val totalImage = item.listPlaces.flatMap {
                it.listImage
            }.count()

            binding.tvStartDate.text =
                SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date(item.timestamp))
            binding.tvJourneyName.text = item.title
            item.listPlaces?.let {
                binding.tvPlaceNumber.text = item.listPlaces.size.toString()
            }

            var tmp = 0
            for (place in item.listPlaces) {
                if (tmp >= 7)
                    break

                if (place.listImage.isNotEmpty()) {

                    val firstPlace = item.listPlaces.first()
                    val lastPlace = item.listPlaces.last()
                    val dayInterVal =
                        Math.abs(lastPlace.timestamp - firstPlace.timestamp) / (1000 * 60 * 60)
                    binding.tvDays.text = dayInterVal.toString() + "hours"

                    place.listImage.first().let {
                        val imgPath = it.path
                        val imageView = ImageView(context)
                        val layoutParams = ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,  // Width
                            0  // Height
                        )
                        layoutParams.width = width // Set height to 200 pixels
                        layoutParams.height = width
                        imageView.layoutParams = layoutParams



                        bindPhoto(
//                    "https://media.geeksforgeeks.org/wp-content/cdn-uploads/gfg_200x200-min.png",
                            imgPath,
                            imageView
                        )

                        // bindPhoto(imgPath, binding.ivTmp)
                        binding.llPlaces.addView(imageView)
                    }
                }


                // Apply the LayoutParams to the ImageView

                tmp += 1
            }

            binding.root.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
            binding.ivTrackJourney.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
            binding.llPlaces.setOnClickListener{
                onItemClickListener.onItemClick(item)
            }
        }


        private fun bindPhoto(url: String, imageView: ImageView) {
            Glide.with(itemView.context)
                .load(url)
//                .placeholder(circularProgressDrawable)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .apply(RequestOptions().centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
    }

}