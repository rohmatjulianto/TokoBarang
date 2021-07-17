package com.joule.tokobarang.auth.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.joule.tokobarang.R
import com.joule.tokobarang.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    lateinit var viewModel: RegisterViewModel
    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        val email = binding.email
        val pass = binding.password

        binding.login.setOnClickListener{
            if (email.text!!.isNotEmpty() && pass.text!!.isNotEmpty()){
                viewModel.register(email.text.toString(), pass.text.toString())
            }else{
                if (email.text!!.isEmpty()){
                    email.error = "email can't be null"
                }
                if (pass.text!!.isEmpty()){
                    pass.error = "password can't be null"
                }
            }
        }

        viewModel.msg.observe(this, {
            it?.let {
                if (it == "success"){
                    showStatus("Success")
                    finish()
                }else{
                    showStatus(it)
                }
            }
        })

    }

    private fun showStatus(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}