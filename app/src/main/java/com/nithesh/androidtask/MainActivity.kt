package com.nithesh.androidtask

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.nithesh.androidtask.data.Employee
import com.nithesh.androidtask.data.employeeData
import com.nithesh.androidtask.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val data = MutableLiveData<List<Employee>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        if (isInternetAvailable()) {
            getEmployeeList()
            val adapter = RecyclerViewAdapter(EmployeeClickListener {
                Log.i("knk", "onCreate: onclick is clicked")
                onClick(it)
            }, EmailClickListener {
                val emailIntent = Intent(
                    Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", it, null
                    )
                )
                startActivity(Intent.createChooser(emailIntent, null))
            })
            binding.employeeRecyclerView.adapter = adapter
            data.observe(this) {
                adapter.submitList(it)
            }
            Log.i("knk", "onCreate: list is submitted ${getEmployeeList()}")


        }
    }

    private fun getEmployeeList() {

        lifecycleScope.launch {
            try {
                data.value = employeeData.getEmployeeList()
                Log.i("knk", "getEmployeeList: ${data.value?.size}")

            } catch (e: Exception) {
                Log.i("MainActivity KNK", "getWordList: ${e.message}")
            }

        }
    }

    private fun onClick(employee: Employee) {
        val intent = Intent(this, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("employee_key", employee)
        intent.putExtras(bundle);
        startActivity(intent)
    }

    private fun isInternetAvailable(): Boolean {
        //this method is to check the internet connection availability
        return true
    }

}