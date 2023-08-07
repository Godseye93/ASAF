package com.d103.asaf.ui.market

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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
import com.d103.asaf.databinding.ItemMarketBinding
import com.d103.asaf.databinding.ItemMarketPhotoBinding
import com.d103.asaf.databinding.ItemStudentAttendanceBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "MarketPhotoRegisterAdap ASAF"
class MarketPhotoRegisterAdapter(var context : Context)  : ListAdapter<MarketImage, MarketPhotoRegisterAdapter.ItemViewHolder> (
    AdapterUtil.diffUtilMarketRegister){


    inner class ItemViewHolder(var binding : ItemMarketPhotoBinding) :  RecyclerView.ViewHolder(binding.root) {

        fun bind(data : MarketImage) {

            Glide.with(binding.marketRegisterImageView)
                .load(data.imageUri)
                .into(binding.marketRegisterImageView)

            binding.marketRegisterImageView.setOnClickListener {
                showImageDialog(data.imageUri)

            }
        }
        
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemMarketPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    private fun showImageDialog(imageUri: Uri) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_market_register_image)
        val imageView = dialog.findViewById<ImageView>(R.id.imageView)

        Glide.with(context)
            .load(imageUri)
            .into(imageView)

        dialog.show()
    }
}