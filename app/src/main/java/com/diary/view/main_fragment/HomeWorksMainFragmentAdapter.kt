package com.diary.view.main_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diary.databinding.HomeWorkItemBinding
import com.diary.model.home_work.HomeWork

class HomeWorksMainFragmentAdapter :
    RecyclerView.Adapter<HomeWorksMainFragmentAdapter.HomeWorkItemViewHolder>() {

    private var data: List<HomeWork> = listOf()
    fun setData(data: List<HomeWork>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeWorkItemViewHolder {
        val binding =
            HomeWorkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HomeWorkItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeWorkItemViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class HomeWorkItemViewHolder(val binding: HomeWorkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HomeWork) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.apply {
                    binding.apply {
                        lesson.text = data.subject
                        timeLeft.text = data.dataCalendarTimePass.day.toString()
                        task.text = data.task
                    }
                }
            }
        }

    }

}


