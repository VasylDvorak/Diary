package com.diary.view.main_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diary.R
import com.diary.databinding.ExaminationItemBinding
import com.diary.model.lessons_home_works.Lesson

class ExaminationMainFragmentAdapter :
    RecyclerView.Adapter<ExaminationMainFragmentAdapter.ExaminationItemViewHolder>() {

    private var data: List<Lesson> = listOf()
    fun setData(data: List<Lesson>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExaminationItemViewHolder {
        val binding =
            ExaminationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExaminationItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExaminationItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ExaminationItemViewHolder(val binding: ExaminationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Lesson) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.apply {
                    binding.apply {
                        subjectExamination.text = data.description
                        if (data.duration <= 0L) {
                            toExamination.text = context.getString(R.string.now_exem)
                            timerUnit.visibility = View.GONE
                        } else {
                            timerUnit.visibility = View.VISIBLE
                            timer.text =
                                (if (data.elapsedTime.day < 10) "0" else "").toString() +
                                        data.elapsedTime.day.toString() + ":" +
                                        (if (data.elapsedTime.hour < 10) "0" else "").toString() +
                                        data.elapsedTime.hour.toString() + ":" +
                                        (if (data.elapsedTime.minute < 10) "0" else "").toString() +
                                        data.elapsedTime.minute.toString()
                        }
                    }
                }
            }
        }
    }
}


