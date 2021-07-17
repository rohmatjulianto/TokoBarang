package com.joule.tokobarang.adapter

import com.joule.tokobarang.data.ProductItem

interface CallbackAdaperListener {
    fun onClickDelete(kodeBarang : String)
    fun onClickEdit(productItem: ProductItem)
}