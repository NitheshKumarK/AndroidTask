package com.nithesh.androidtask

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", employee.email, null
                )
            )
            startActivity(Intent.createChooser(emailIntent, null))
        }
        binding.realPhoneTextView.setOnClickListener {
            val phoneIntent = Intent()
                .setAction(Intent.ACTION_DIAL)
                .setData(Uri.parse("tel: ${employee.phone}"))
            startActivity(phoneIntent)

        }
    }
}