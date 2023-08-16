package com.d103.asaf.ui.market

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d103.asaf.R
import com.d103.asaf.common.model.dto.Market
import com.d103.asaf.common.model.dto.MarketImage
import com.d103.asaf.common.util.AdapterUtil
import com.d103.asaf.databinding.ItemMarketPhotoBinding

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "MarketPhotoRegisterAdap ASAF"
class MarketPhotoRegisterAdapter(private val items: List<MarketImage>, val context: Context,  private val clickListener: OnImageClickListener) :

    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedVIEW =
            LayoutInflater.from(parent.context).inflate(R.layout.item_market_photo, parent, false)
        return ViewHolder(inflatedVIEW)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        Glide.with(context).load(item.imageUri)
            .into(holder.image)

        holder.image.setOnClickListener {
            showImageDialog(item.imageUri)
        }
        holder.cancelBtn.setOnClickListener {
            clickListener.onCancelClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun showImageDialog(image: String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_market_register_image)
        val imageView = dialog.findViewById<ImageView>(R.id.imageView)

        Glide.with(context)
            .load(image)
            .into(imageView)

        dialog.show()
    }

}
class ViewHolder(v:View) :RecyclerView.ViewHolder(v) {
    private var view:View = v
    var image = v.findViewById<ImageView>(R.id.marketRegisterImageView)
    var cancelBtn = v.findViewById<ImageView>(R.id.imageRegisterCancelBtn)
    fun bind(listener:View.OnClickListener,item:String) {
        view.setOnClickListener(listener)
    }
}


