package com.joule.tokobarang.ui.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.joule.tokobarang.R
import com.joule.tokobarang.Utils.IOUtils
import com.joule.tokobarang.callback.CallbackAdaperListener
import com.joule.tokobarang.adapter.ProductAdapter
import com.joule.tokobarang.auth.ui.login.LoginActivity
import com.joule.tokobarang.auth.ui.login.LoginViewModel
import com.joule.tokobarang.data.ProductItem
import com.joule.tokobarang.databinding.ActivityMainBinding
import com.joule.tokobarang.callback.CallbackDialogListener
import com.joule.tokobarang.ui.dialog.FormFragment

class MainActivity : AppCompatActivity(), CallbackDialogListener, CallbackAdaperListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferences: SharedPreferences
    var token: String? = null
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        sharedPreferences = getSharedPreferences(IOUtils.PREFERENCE_NAME, Context.MODE_PRIVATE)

        val rvProduct = binding.rvProduct
        rvProduct.layoutManager = LinearLayoutManager(this)

        viewModel.product.observe(this, {
            it?.let {
                binding.pbProduct.visibility = View.GONE
                rvProduct.adapter = ProductAdapter(this, it)
            }
        })

        binding.btnAddProduct.setOnClickListener {
            val dialog = FormFragment(this, null)
            dialog.show(supportFragmentManager, "add")
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(it.context, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                token?.let {
                    if (s?.isNotEmpty() == true) {
                        viewModel.getListByKode(token!!, s.toString())
                    } else {
                        initList()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        relogin()

        viewModel.delete.observe(this, {
            it?.let {
                showStatus(it)
                initList()
            }
        })

        viewModel.edit.observe(this, {
            it?.let {
                dialog?.dismiss()
                showStatus("Edit ${it.nama_barang} Success")
                initList()
            }
        })

        viewModel.add.observe(this, {
            it?.let {
                dialog?.dismiss()
                showStatus("Add ${it.nama_barang} Success")
                initList()
            }
        })

        viewModel.addProductFailure.observe(this, {
            it?.let {
                showStatus(it)
            }
        })

        viewModel.msgNoData.observe(this, {
            it?.let {
                if (it) {
                    rvProduct.adapter = null
                    showStatus("No result for ${binding.etSearch.text.toString()}")
                } else {
                    rvProduct.visibility = View.VISIBLE
                }
            }
        })

    }


    private fun showStatus(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun relogin() {
        val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        val email = sharedPreferences.getString("email", "").toString()
        val pass = sharedPreferences.getString("pass", "").toString()

        viewModel.msg.observe(this, {
            it?.let {
                show(false)
                loginViewModel.login(email, pass)
            }
        })

        loginViewModel.token.observe(this, {
            it?.let {
                sharedPreferences.edit().putString("token", it)
                    .apply()
                initList()
            }
        })
    }

    fun initList() {
        val login = sharedPreferences.getBoolean("login", false)
        show(login)
        if (login) {
            token = "Bearer ${sharedPreferences.getString("token", "").toString()}"
            viewModel.getListBarang(token!!)
            binding.tvEmail.text =
                String.format(getString(R.string.say_hi), sharedPreferences.getString("email", ""))
        }
    }


    fun show(boolean: Boolean) {
        if (boolean) {
            binding.cardToLogin.visibility = View.GONE
            binding.btnAddProduct.visibility = View.VISIBLE
            binding.btnLogout.visibility = View.VISIBLE
            binding.tvEmail.visibility = View.VISIBLE
            binding.rvProduct.visibility = View.VISIBLE
            binding.pbProduct.visibility = View.VISIBLE
            binding.etSearch.isEnabled = true
        } else {
            binding.cardToLogin.visibility = View.VISIBLE
            binding.btnAddProduct.visibility = View.GONE
            binding.btnLogout.visibility = View.GONE
            binding.tvEmail.visibility = View.GONE
            binding.rvProduct.visibility = View.GONE
            binding.pbProduct.visibility = View.GONE
            binding.etSearch.isEnabled = false
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun logout() {
        val dialogAlert = AlertDialog.Builder(this)
            .setIcon(getDrawable(android.R.drawable.ic_dialog_alert))
            .setTitle("Sign Out")
            .setMessage("Sure to Sign out?")
            .setPositiveButton(
                "yes"
            ) { dialog, which ->
                sharedPreferences.edit().clear()
                    .apply()
                initList()
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, which -> dialog.dismiss() }
            .create()
        dialogAlert.show()
    }

    override fun onResume() {
        super.onResume()
        initList()
    }

    override fun addProduct(dialog: Dialog, item: ProductItem) {
        this.dialog = dialog
        viewModel.addProduct(token!!, item)
    }

    override fun updateProduct(dialog: Dialog, item: ProductItem) {
        this.dialog = dialog
        viewModel.editProduct(token!!, item)
    }

    override fun onClickDelete(kodeBarang: String) {
        val dialogAlert = AlertDialog.Builder(this)
            .setIcon(getDrawable(android.R.drawable.ic_dialog_alert))
            .setTitle("Delete")
            .setMessage("Sure to delete $kodeBarang?")
            .setPositiveButton(
                "yes"
            ) { dialog, which ->
                viewModel.deleteProduct(token!!, kodeBarang)
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, which -> dialog.dismiss() }
            .create()
        dialogAlert.show()

    }

    override fun onClickEdit(productItem: ProductItem) {
        val dialog = FormFragment(this, productItem)
        dialog.show(supportFragmentManager, "edit")
    }
}
