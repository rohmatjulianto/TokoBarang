package com.joule.tokobarang.ui.dialog

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.joule.tokobarang.data.ProductItem
import com.joule.tokobarang.databinding.FragmentFormBinding
import com.joule.tokobarang.callback.CallbackDialogListener

class FormFragment(val listener: CallbackDialogListener, val productItem: ProductItem?) :
    DialogFragment() {

    lateinit var binding: FragmentFormBinding
    val inputs: ArrayList<TextInputEditText> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFormBinding.inflate(layoutInflater)

        inputs.add(binding.etKodeBarang)
        inputs.add(binding.etNameBarang)
        inputs.add(binding.etStockBarang)
        inputs.add(binding.etPriceBarang)
        inputs.add(binding.etSatuanBarang)
        inputs.add(binding.etStatusBarang)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCancelable(false)

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
        binding.btnSubmit.setOnClickListener {
            validation()
        }

        if (productItem != null) {
            binding.tvTitle.text = "Edit Product"
            binding.etKodeBarang.isEnabled = false
            binding.etKodeBarang.text = SpannableStringBuilder(productItem.kode_barang)
            binding.etNameBarang.text = SpannableStringBuilder(productItem.nama_barang)
            binding.etStockBarang.text =
                SpannableStringBuilder(productItem.jumlah_barang.toString())
            binding.etPriceBarang.text = SpannableStringBuilder(productItem.harga_barang.toString())
            binding.etSatuanBarang.text = SpannableStringBuilder(productItem.satuan_barang)
            binding.etStatusBarang.text =
                SpannableStringBuilder(productItem.status_barang.toString())
        }
    }

    fun validation() {
        for (i in 0 until inputs.size) {
            if (inputs[i].text?.length == 0) {
                inputs[i].error = "${inputs[i].hint} can't be empty"
            }
        }

        if (
            binding.etKodeBarang.text?.length != 0 &&
            binding.etNameBarang.text?.length != 0 &&
            binding.etStockBarang.text?.length != 0 &&
            binding.etPriceBarang.text?.length != 0 &&
            binding.etSatuanBarang.text?.length != 0 &&
            binding.etStatusBarang.text?.length != 0
        ) {
            val item = ProductItem(
                0,
                binding.etKodeBarang.text.toString(),
                binding.etNameBarang.text.toString(),
                binding.etStockBarang.text.toString().toInt(),
                binding.etPriceBarang.text.toString().toLong(),
                binding.etSatuanBarang.text.toString(),
                "",
                binding.etStatusBarang.text.toString().toInt(),
                "",
                ""
            )

            if (productItem != null) {
                listener.updateProduct(dialog!!, item)
            }else{
                listener.addProduct(dialog!!, item)
            }
        }
    }
}