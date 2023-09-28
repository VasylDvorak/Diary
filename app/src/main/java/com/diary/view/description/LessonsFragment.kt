package com.diary.view.description

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diary.R
import com.diary.databinding.FragmentLessonsBinding
import com.diary.di.ConnectKoinModules
import com.diary.model.lessons_home_works.Lesson
import com.diary.utils.delegates.viewById
import com.diary.view.base_for_dictionary.BaseFragmentSettingsMenu


class LessonsFragment :
    BaseFragmentSettingsMenu<FragmentLessonsBinding>(FragmentLessonsBinding::inflate) {


    lateinit var viewModel: LessonsViewModel
    private val observer = Observer<List<Lesson>> { renderData(it) }
    private val lessonTable by viewById<RecyclerView>(R.id.lesson_table_for_lessons)


    private val lessonTableAdapter: LessonsFragmentAdapter by lazy {
        LessonsFragmentAdapter(::onItemClick)
    }


    private fun onItemClick(lesson: Lesson) {
        val skype = Intent("android.intent.action.VIEW")
        skype.data = Uri.parse("skype:" + "");
        context?.startActivity(skype)
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

        timer = object : CountDownTimer(
            1000 * 1000 * 60, 60 * 1000
        ) {
            override fun onTick(millisUntilFinished: Long) {
                this@LessonsFragment.viewModel.getData()
            }

            override fun onFinish() {
                timer.start()
            }
        }
        timer.start()
    }

    private fun initViews() {
        lessonTable.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        lessonTable.adapter = lessonTableAdapter
    }


    fun renderData(lessons: List<Lesson>) {
        viewModel.setQuery(lessons)
        setDataToAdapter(lessons)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val manager = requireActivity().getSystemService(Context.SEARCH_SERVICE)
                as SearchManager
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView

        searchItem.apply {

            searchView.also {

                it.setSearchableInfo(manager.getSearchableInfo(requireActivity().componentName))
                it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String?): Boolean {

                        if (isNetworkAvailable) {

                            query?.let { searchString ->
                                viewModel.getData()
                            }
                            it.clearFocus()
                            it.setQuery("", false)
                            collapseActionView()
                            Toast.makeText(
                                context,
                                resources.getString(R.string.looking_for) + " " + query ?: "",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Ошибка",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        return true
                    }

                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }
        }

    }

    fun setDataToAdapter(lessons: List<Lesson>) {
        lessonTableAdapter?.setData(lessons)
    }


    companion object {
        fun newInstance() = LessonsFragment()
    }
}

