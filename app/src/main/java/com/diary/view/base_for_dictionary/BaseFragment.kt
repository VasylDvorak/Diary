package com.diary.view.base_for_dictionary

import android.animation.Animator
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.diary.R
import com.diary.model.lessons_home_works.Lesson
import com.diary.navigation.IScreens
import com.diary.utils.delegates.viewById
import com.diary.view.AnimatorDictionary
import com.github.terrakok.cicerone.Router
import org.koin.java.KoinJavaComponent

const val countDownInterval: Long = 60 * 1000
const val millisInFuture:Long = countDownInterval*1000
abstract class BaseFragment<B : ViewBinding>(
    private val inflateBinding: (
        inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean
    ) -> B,
    private var lessonTableId: Int = 0
) : Fragment() {
    val lessonTable by viewById<RecyclerView>(lessonTableId)
    var lessons: List<Lesson> = listOf()
    lateinit protected var timer: CountDownTimer
    private var _binding: B? = null
    protected val binding: B
        get() = _binding!!
    val router: Router by KoinJavaComponent.inject(Router::class.java)
    val screen = KoinJavaComponent.getKoin().get<IScreens>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = inflateBinding.invoke(inflater, container, false)
        return binding.root
    }

    fun onItemClick(lesson: Lesson) {
        val skype = Intent("android.intent.action.VIEW")
        skype.data = Uri.parse("skype:" + "");
        context?.startActivity(skype)
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

                        query?.let { searchString ->
                            if (!lessons.isNullOrEmpty()) {
                                val moveTo =
                                    lessons.find { lesson -> lesson.subject == searchString }
                                        ?: null
                                if (moveTo != null) {
                                    scrollToPosition(lessons.indexOf(moveTo))
                                    Toast.makeText(
                                        context,
                                        resources.getString(R.string.looking_for) + " " + query
                                            ?: "",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        getString(R.string.can_not_find_lesson),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                        it.clearFocus()
                        it.setQuery("", false)
                        collapseActionView()

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_app_bar -> {
                Toast.makeText(context, getString(R.string.settings), Toast.LENGTH_SHORT)
                    .show()
                return true
            }

            R.id.app -> {
                Toast.makeText(
                    context,
                    getString(R.string.app_button),
                    Toast.LENGTH_SHORT
                ).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun scrollToPosition(scrollToPosition: Int) {
        val layoutManager = lessonTable.layoutManager as LinearLayoutManager
        val firstPosition = layoutManager.findFirstVisibleItemPosition()
        val lastPosition = layoutManager.findLastVisibleItemPosition()
        val visibleItems = lastPosition - firstPosition + 1

        if (firstPosition < scrollToPosition) {
            lessonTable.smoothScrollToPosition(scrollToPosition + (visibleItems / 2))
        } else {
            lessonTable.smoothScrollToPosition(scrollToPosition - (visibleItems / 2))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return AnimatorDictionary().setAnimator(transit, enter)
    }

}