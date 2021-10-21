package com.nithesh.androidtask

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nithesh.androidtask.data.Employee
import com.nithesh.androidtask.databinding.ActivityDetailBinding
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_detail
        )
        val intent = intent
        val bundle = intent.extras
        val employee = bundle?.getParcelable<Employee>("employee_key")!!
        employee.let { e ->
            binding.apply {
                realNameTextView.text = e.name
                realIdTextView.text = e.id.toString()
                realEmailTextView.text = e.email?.lowercase(Locale.ROOT)
                realAddressTextView.text = resources.getString(
                    R.string.real_address,
                    e.address.suite!!,
                    e.address.street!!,
                    e.address.city!!
                )
                realPhoneTextView.text = e.phone
                realCompanyTextView.text = resources.getString(
                    R.string.real_company,
                    e.company!!.name,
                    e.website!!.toString()
                )
            }
        }
        binding.realEmailTextView.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply{
                type = "*/*"
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(employee.email))
                putExtra(Intent.EXTRA_SUBJECT, "Email subject")
                putExtra(Intent.EXTRA_TEXT, "Email message text")
            }
            sendIntent(emailIntent)
        }
        binding.realPhoneTextView.setOnClickListener {
            val callIntent = Uri.parse("tel:"+employee.phone).let{
                Intent(Intent.ACTION_DIAL,it)
            }
            sendIntent(callIntent)
        }
    }
    private fun sendIntent(appIntent: Intent){
        try{
            startActivity(appIntent)
        }catch(e: Exception){
            Toast.makeText(this, "App Not found", Toast.LENGTH_SHORT).show()
        }
    }
}