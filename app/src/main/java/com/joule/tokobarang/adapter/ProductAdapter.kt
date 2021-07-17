package com.joule.tokobarang.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joule.tokobarang.R
import com.joule.tokobarang.Utils.IOUtils
import com.joule.tokobarang.data.ProductItem
import com.joule.tokobarang.databinding.ItemProductBinding
import java.util.*
import kotlin.collections.ArrayList

class ProductAdapter(val listener: CallbackAdaperListener, val item : ArrayList<ProductItem>) : RecyclerView.Adapter<ProductAdapter.viewHolder>() {
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemProductBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBind(item: ProductItem) {
            binding.tvKodeBarang.text = item.kode_barang
            binding.tvName.text = item.nama_barang
            binding.tvPrice.text =  "${IOUtils.getCurrency(item.harga_barang)}/${item.satuan_barang}"
            binding.tvStock.text = "Stock : ${item.jumlah_barang}"
            binding.tvCreated.text = "Created at : ${IOUtils.getTime(item.created_at)}"
            binding.tvLastUpdate.text = "Last Update : ${IOUtils.getTime(item.updated_at)}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.onBind(item.get(position))
    }

    override fun getItemCount(): Int {
        return item.size
    }
}