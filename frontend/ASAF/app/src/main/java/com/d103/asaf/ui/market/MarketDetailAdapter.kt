package com.d103.asaf.ui.market

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.util.Log
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
        Log.d("사진 주소", "${item.imageUri} ")
        val imageSplit = item.imageUri.split("/")
        val path =  "http://i9d103.p.ssafy.io" + "/" + imageSplit[4] + "/" + imageSplit[5] + "/" + imageSplit[6]
        Log.d("사진 주소 2", "onBindViewHolder: $path")
        try{
            Glide.with(context).load(path)
                .into(holder.image)
        }
        catch (e : Exception){

            Glide.with(context).load("https://cdn-icons-png.flaticon.com/512/75/75519.png")
                .into(holder.image)


        }
        holder.cancelBtn.visibility = View.GONE


        holder.image.setOnClickListener {
            showImageDialog(item.imageUri)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun showImageDialog(image: String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_market_register_image)
        val imageView = dialog.findViewById<ImageView>(R.id.imageView)
        val imageSplit = image.split("/")
        val path =  "http://i9d103.p.ssafy.io" + "/" + imageSplit[4] + "/" + imageSplit[5] + "/" + imageSplit[6]
        Glide.with(context)
            .load(path)
            .into(imageView)

        dialog.show()
    }

}
class DetailViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private var view: View = v
    var image = v.findViewById<ImageView>(R.id.marketRegisterImageView)
    var cancelBtn = v.findViewById<ImageView>(R.id.imageRegisterCancelBtn)
    fun bind(listener: View.OnClickListener, item:String) {
        view.setOnClickListener(listener)
    }
}