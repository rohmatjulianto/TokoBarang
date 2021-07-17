package com.joule.tokobarang.auth.ui.login


import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.joule.tokobarang.Utils.IOUtils
import com.joule.tokobarang.auth.ui.register.RegisterActivity
import com.joule.tokobarang.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.tvRegister.setOnClickListener{
            val intent = Intent(it.context, RegisterActivity::class.java)
            startActivity(intent)
        }

        val email = binding.email
        val pass = binding.password

        binding.login.setOnClickListener{
            if (email.text!!.isNotEmpty() && pass.text!!.isNotEmpty()){
                loginViewModel.login(email.text.toString(), pass.text.toString())
                binding.loading.visibility = View.VISIBLE
            }else{
                if (email.text!!.isEmpty()){
                    email.error = "email can't be null"
                }
                if (pass.text!!.isEmpty()){
                    pass.error = "password can't be null"
                }
            }
        }

        loginViewModel.token.observe(this, {
            it?.let {
                binding.loading.visibility = View.GONE
                val sharedPreferences = getSharedPreferences(IOUtils.PREFERENCE_NAME, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("token", it)
                editor.putString("email", email.text.toString())
                editor.putString("pass", pass.text.toString())
                editor.putBoolean("login", true)
                editor.apply()

                showLoginStatus("login success")
                finish()
            }
        })

        loginViewModel.error.observe(this, {
            it?.let {
                binding.loading.visibility = View.GONE
                showLoginStatus(it)
            }
        })
    }

    private fun showLoginStatus(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


}