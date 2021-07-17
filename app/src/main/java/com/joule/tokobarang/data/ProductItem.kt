package com.joule.tokobarang.data

data class ProductItem(
    val id: Int,
    val kode_barang: String,
    val nama_barang: String,
    val jumlah_barang: Int,
    val harga_barang: Long,
    val satuan_barang: String,
    val image: String? = null,
    val status_barang: Int,
    val created_at: String,
    val updated_at: String
)
