package com.joule.tokobarang.dialog

import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.joule.tokobarang.data.ProductItem
import com.joule.tokobarang.databinding.FragmentFormBinding

class FormFragment(val listener: CallbackDialogListener, val productItem: ProductItem?) : DialogFragment() {

    lateinit var binding: FragmentFormBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(false)

        binding.btnCancel.setOnClickListener{
            dialog?.dismiss()
        }

        if (productItem != null){
            binding.tvTitle.text = "Edit Product"

            binding.etKodeBarang.text = SpannableStringBuilder(productItem.kode_barang)
            binding.etNameBarang.text = SpannableStringBuilder(productItem.nama_barang)
            binding.etStockBarang.text = SpannableStringBuilder(productItem.jumlah_barang.toString())
            binding.etPriceBarang.text = SpannableStringBuilder(productItem.harga_barang.toString())
            binding.etSatuanBarang.text = SpannableStringBuilder(productItem.satuan_barang)
            binding.etStatusBarang.text = SpannableStringBuilder(productItem.status_barang.toString())
        }
    }
}