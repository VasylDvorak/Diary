package com.diary.view.main_fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diary.R
import com.diary.databinding.FragmentMainBinding
import com.diary.di.ConnectKoinModules.mainScreenScope
import com.diary.model.lessons_home_works.CommonDataModel
import com.diary.utils.delegates.viewById
import com.diary.view.base_for_dictionary.BaseFragment
import com.diary.view.base_for_dictionary.countDownInterval
import com.diary.view.base_for_dictionary.millisInFuture


class MainFragment : BaseFragment<FragmentMainBinding>(
    FragmentMainBinding::inflate,
    R.id.lesson_table
) {

    lateinit var viewModel: MainViewModel
    private val observer = Observer<CommonDataModel> { renderData(it) }
    private val homeWorkTable by viewById<RecyclerView>(R.id.home_work_table)
    private val examinationTable by viewById<RecyclerView>(R.id.examination_table)

    private val lessonTableAdapter: LessonsMainFragmentAdapter by lazy {
        LessonsMainFragmentAdapter(::onItemClick)
    }
    private val homeWorkTableAdapter: HomeWorksMainFragmentAdapter by lazy {
        HomeWorksMainFragmentAdapter()
    }
    private val examinationTableAdapter: ExaminationMainFragmentAdapter by lazy {
        ExaminationMainFragmentAdapter()
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
        val viewModel: MainViewModel by lazy { mainScreenScope.get() }
        this.viewModel = viewModel
        this.viewModel.subscribe().observe(viewLifecycleOwner, observer)

        timer = object : CountDownTimer(
            millisInFuture, countDownInterval
        ) {
            override fun onTick(millisUntilFinished: Long) {

                this@MainFragment.viewModel.getData()
            }

            override fun onFinish() {
                timer.start()
            }
        }
        timer.start()
    }

    private fun initViews() {
        examinationTable.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        examinationTable.adapter = examinationTableAdapter

        lessonTable.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        lessonTable.adapter = lessonTableAdapter

        homeWorkTable.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        homeWorkTable.adapter = homeWorkTableAdapter


    }

    fun renderData(commonDataModel: CommonDataModel) {
        viewModel.setQuery(commonDataModel)
        setDataToAdapter(commonDataModel)

    }

    fun setDataToAdapter(commonDataModel: CommonDataModel) {
        examinationTableAdapter?.setData(commonDataModel.examinations)
        lessonTableAdapter?.setData(commonDataModel.lessons)
        lessons = commonDataModel.lessons
        homeWorkTableAdapter?.setData(commonDataModel.homeWorkList)
    }


    companion object {
        fun newInstance() = MainFragment()

    }
}




