package com.joule.tokobarang.dialog

import com.joule.tokobarang.data.ProductItem

interface CallbackDialogListener {
    fun addProduct(item: ProductItem)
    fun updateProduct(item: ProductItem)
}