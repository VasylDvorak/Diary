package com.diary.view.main_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.diary.R
import com.diary.databinding.LessonItemBinding
import com.diary.model.lessons_home_works.Lesson

class LessonsMainFragmentAdapter(
    private var onListItemClickListener: (Lesson) -> Unit,
) : RecyclerView.Adapter<LessonsMainFragmentAdapter.RecyclerItemViewHolder>() {

    private var data: List<Lesson> = listOf()
    fun setData(data: List<Lesson>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        val binding =
            LessonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecyclerItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(val binding: LessonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Lesson) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.apply {
                    binding.apply {
                        lesson.text = data.subject
                        timeSpan.text = data.dataCalendarStartTime?.hour.toString() + ":" +
                                data.dataCalendarStartTime?.minute.toString() +
                                " - " + data.dataCalendarEndTime?.hour.toString() + ":" +
                                data.dataCalendarEndTime?.minute.toString()

                        callSkype.setOnClickListener {
                            openInNewWindow(data)
                        }
                        if (data.typeofLesson == "Дополнительное занятие") {
                            binding.lessonCard.background =
                                getDrawable(context, R.drawable.rounded_card)
                            callSkype.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun openInNewWindow(listItemData: Lesson) {
        onListItemClickListener(listItemData)
    }
}
