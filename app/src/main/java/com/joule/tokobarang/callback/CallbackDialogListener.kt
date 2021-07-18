package com.joule.tokobarang.callback

import android.app.Dialog
import com.joule.tokobarang.data.ProductItem

interface CallbackDialogListener {
    fun addProduct(dialog: Dialog, item: ProductItem)
    fun updateProduct(dialog: Dialog, item: ProductItem)
}