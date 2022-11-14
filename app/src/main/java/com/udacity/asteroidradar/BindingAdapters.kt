package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidAdapter
import com.udacity.asteroidradar.main.ResponseStatues

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription =
            imageView.context.getString(R.string.potentially_hazardous_asteroid_image)

    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription =
            imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, asteroidList: List<Asteroid>?) {
    if (!asteroidList.isNullOrEmpty()) {
        val adapter = recyclerView.adapter as AsteroidAdapter
        adapter.submitList(asteroidList)
    }
}

@BindingAdapter("imgUrl")
fun bindPicOfDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let {
        if (it.mediaType == "image") {
            var imgUri = it.url.toUri().buildUpon().scheme("https").build()
            Picasso.get()
                .load(imgUri)
                .into(imageView)
            imageView.contentDescription = imageView.context.getString(R.string.image_of_the_day)
        } else {
            imageView.setImageResource(R.drawable.ic_broken_image)
            imageView.contentDescription = imageView.context.getString(R.string.broken_image)
        }
    }
}

@BindingAdapter("responseStatus")
fun bindProgressBar(progressBar: ProgressBar, responseStatues: ResponseStatues) {
    when (responseStatues) {
        ResponseStatues.LOADING -> progressBar.visibility = View.VISIBLE
        ResponseStatues.SUCCESS -> progressBar.visibility = View.GONE
    }
}

@BindingAdapter("asteroidStatus")
fun bindAsteroidStatus(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.ic_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = imageView.context.getString(R.string.ic_not_hazardous)
    }
}