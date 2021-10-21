package com.nithesh.androidtask

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply{
                    type = "*/*"
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(it))
                    putExtra(Intent.EXTRA_SUBJECT, "Email subject")
                    putExtra(Intent.EXTRA_TEXT, "Email message text")
                }
                try{
                    startActivity(emailIntent)
                }catch(e: Exception){
                    Toast.makeText(this, "App not found", Toast.LENGTH_SHORT).show()
                }
            })
            binding.employeeRecyclerView.adapter = adapter
            data.observe(this) {
                adapter.submitList(it)
            }
            Log.i("knk", "onCreate: list is submitted ${getEmployeeList()}")


        }
        else{
            Toast.makeText(this,
                "Please check your internet connection",
                Toast.LENGTH_SHORT).show()
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
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(
            ConnectivityManager::class.java) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when{
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }

    }

}