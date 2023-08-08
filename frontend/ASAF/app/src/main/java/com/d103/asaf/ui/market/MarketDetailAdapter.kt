package com.d103.asaf.ui.market

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d103.asaf.R
import com.d103.asaf.common.model.dto.MarketImage

class MarketDetailAdapter (private val items: List<MarketImage>, val context: Context) :
    RecyclerView.Adapter<DetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val inflatedVIEW =
            LayoutInflater.from(parent.context).inflate(R.layout.item_market_photo, parent, false)
        return DetailViewHolder(inflatedVIEW)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {

        val item = items[position]

        Glide.with(context).load(item.imageUri)
            .into(holder.image)

        holder.image.setOnClickListener {
            showImageDialog(item.imageUri)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun showImageDialog(image: Uri) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_market_register_image)
        val imageView = dialog.findViewById<ImageView>(R.id.imageView)

        Glide.with(context)
            .load(image)
            .into(imageView)

        dialog.show()
    }

}
class DetailViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private var view: View = v
    var image = v.findViewById<ImageView>(R.id.marketRegisterImageView)

    fun bind(listener: View.OnClickListener, item:String) {
        view.setOnClickListener(listener)
    }
}