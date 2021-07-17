package com.joule.tokobarang

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.joule.tokobarang.Utils.IOUtils
import com.joule.tokobarang.adapter.CallbackAdaperListener
import com.joule.tokobarang.adapter.ProductAdapter
import com.joule.tokobarang.auth.ui.login.LoginActivity
import com.joule.tokobarang.auth.ui.login.LoginViewModel
import com.joule.tokobarang.data.ProductItem
import com.joule.tokobarang.databinding.ActivityMainBinding
import com.joule.tokobarang.dialog.CallbackDialogListener
import com.joule.tokobarang.dialog.FormFragment

class MainActivity : AppCompatActivity(), CallbackDialogListener, CallbackAdaperListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferences: SharedPreferences
    var token: String? = null

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

        binding.btnLogin.setOnClickListener {
            val intent = Intent(it.context, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }
        binding.btnAddProduct.setOnClickListener {
            val dialog = FormFragment(this, null)
            dialog.show(supportFragmentManager, "add")
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        })
        relogin()


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
        } else {
            binding.cardToLogin.visibility = View.VISIBLE
            binding.btnAddProduct.visibility = View.GONE
            binding.btnLogout.visibility = View.GONE
            binding.tvEmail.visibility = View.GONE
            binding.rvProduct.visibility = View.GONE
            binding.pbProduct.visibility = View.GONE
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun logout() {
        val dialogAlert = AlertDialog.Builder(this)
            .setIcon(getDrawable(android.R.drawable.ic_dialog_alert))
            .setTitle("Sign Out")
            .setMessage("Sure to Sign out?")
            .setPositiveButton("yes"
            ) { dialog, which ->
                sharedPreferences.edit().clear()
                    .apply()
                initList()
            }
            .setNegativeButton("Cancel"
            ) { dialog, which -> dialog.dismiss() }
            .create()
        dialogAlert.show()
    }

    override fun onResume() {
        super.onResume()
        initList()
    }

    override fun addProduct(item: ProductItem) {
        viewModel.addProduct(token!!, item)
    }

    override fun updateProduct(item: ProductItem) {
        viewModel.editProduct(token!!, item)
    }

    override fun onClickDelete(kodeBarang: String) {
        viewModel.deleteProduct(token!!, kodeBarang)
    }

    override fun onClickEdit(productItem: ProductItem) {
        val dialog = FormFragment(this, productItem)
        dialog.show(supportFragmentManager, "edit")
    }
}
