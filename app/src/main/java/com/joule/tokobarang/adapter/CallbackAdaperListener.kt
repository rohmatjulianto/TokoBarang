package com.joule.tokobarang.adapter

import com.joule.tokobarang.data.ProductItem

interface CallbackAdaperListener {
    fun onClickDelete(productItem: ProductItem)
    fun onClickEdit(productItem: ProductItem)
}