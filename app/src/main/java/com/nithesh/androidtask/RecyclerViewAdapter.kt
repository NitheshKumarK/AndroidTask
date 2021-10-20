package com.nithesh.androidtask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nithesh.androidtask.data.Employee
import com.nithesh.androidtask.databinding.EmployeeListItemBinding

class RecyclerViewAdapter(
    private val clickListener: EmployeeClickListener,
    private val emailClickListener: EmailClickListener
) : ListAdapter<Employee,
        RecyclerViewAdapter.ViewHolder>(EmployeeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener,emailClickListener)
    }

    class ViewHolder(private val binding: EmployeeListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            employee: Employee,
            clickListener: EmployeeClickListener,
            emailClickListener: EmailClickListener
        ) {
            binding.employee = employee
            binding.clickListener = clickListener
            binding.emailClickListener = emailClickListener
            binding.email = employee.email
            binding.nameTextView.text = employee.name ?: "No Name"
            binding.emailTextView.text = employee.email ?: "no Email"
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val listItemBinding = EmployeeListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolder(listItemBinding)
            }
        }
    }

}

class EmployeeDiffCallback : DiffUtil.ItemCallback<Employee>() {
    override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
        return oldItem == newItem
    }
}

class EmployeeClickListener(val clickListener: (employee: Employee) -> Unit) {
    fun onClick(employee: Employee) = clickListener(employee)
}

class EmailClickListener(val clickListener: (email: String) -> Unit) {
    fun onClick(email: String) = clickListener(email)
}

