package com.diary.view.lessons

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.diary.R
import com.diary.databinding.FragmentLessonsBinding
import com.diary.di.ConnectKoinModules
import com.diary.model.lessons_home_works.Lesson
import com.diary.view.base_for_dictionary.BaseFragment


class LessonsFragment :
    BaseFragment<FragmentLessonsBinding>(
        FragmentLessonsBinding::inflate,
        R.id.lesson_table_for_lessons
    ) {


    lateinit var viewModel: LessonsViewModel
    private val observer = Observer<List<Lesson>> { renderData(it) }

    private val lessonTableAdapter: LessonsFragmentAdapter by lazy {
        LessonsFragmentAdapter(::onItemClick)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initViews()
        viewModel.getRestoredData()?.let { renderData(it) }
    }


    private fun initViewModel() {
        if (lessonTable.adapter != null) {
            throw IllegalStateException(getString(R.string.exception_error))
        }
        val viewModel: LessonsViewModel by lazy { ConnectKoinModules.lessonsScreenScope.get() }
        this.viewModel = viewModel
        this.viewModel.subscribe().observe(viewLifecycleOwner, observer)
        this.viewModel.getData()
    }

    private fun initViews() {
        lessonTable.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        lessonTable.adapter = lessonTableAdapter
    }


    fun renderData(lessons: List<Lesson>) {
        viewModel.setQuery(lessons)
        super.lessons=lessons
        setDataToAdapter(lessons)

    }


    fun setDataToAdapter(lessons: List<Lesson>) {
        lessonTableAdapter?.setData(lessons)
    }


    companion object {
        fun newInstance() = LessonsFragment()
    }
}

