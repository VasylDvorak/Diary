package com.diary.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.diary.R
import com.diary.databinding.ActivityMainBinding
import com.diary.model.home_work.CalendarTime
import com.diary.model.lessons_home_works.Lesson
import com.diary.navigation.IScreens
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent


class MainActivity : AppCompatActivity() {
    private val router: Router by KoinJavaComponent.inject(Router::class.java)
    private val screen = KoinJavaComponent.getKoin().get<IScreens>()
    private val navigatorHolder: NavigatorHolder by inject()
    val navigator = AppNavigator(this, R.id.flFragment)

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        toJson()
        binding?.bottomNavigationView?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.to_home -> {
                    router.replaceScreen(screen.startMainFragment())
                }

                R.id.lessons -> {
                    router.replaceScreen(screen.startLessonsFragment())
                }

                R.id.settings -> {}
                R.id.favorites -> {}
            }
            true
        }
        if (savedInstanceState == null) {
            binding?.bottomNavigationView?.selectedItemId = R.id.to_home
        }
    }


    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.search_word),
                    Toast.LENGTH_SHORT
                ).show()
                return true
            }

            R.id.settings_app_bar -> {
                Toast.makeText(applicationContext, getString(R.string.settings), Toast.LENGTH_SHORT)
                    .show()
                return true
            }

            R.id.app -> {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.app_button),
                    Toast.LENGTH_SHORT
                ).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun toJson() {
        val output = listOf(
            Lesson(
                id = 1,
                subject = "Литература",
                typeofLesson = "Экзамен",
                description = "Зкзамен по литературе",
                icons = listOf(),
                dataCalendarStartTime = CalendarTime(
                    year = 2024,
                    month = 2,
                    day = 20,
                    hour = 13,
                    minute = 30
                ),
                dataCalendarEndTime = CalendarTime(
                    year = 2024,
                    month = 2,
                    day = 20,
                    hour = 14,
                    minute = 30
                ),
                elapsedTime = CalendarTime(
                    year = 0,
                    month = 0,
                    day = 0,
                    hour = 0,
                    minute = 0
                ),
            ),
            Lesson(
                id = 2,
                subject = "Русский язык",
                typeofLesson = "Урок",
                description = "Чистописание",
                icons = listOf(),
                dataCalendarStartTime = CalendarTime(
                    year = 2024,
                    month = 3,
                    day = 20,
                    hour = 13,
                    minute = 30
                ),
                dataCalendarEndTime = CalendarTime(
                    year = 2024,
                    month = 3,
                    day = 20,
                    hour = 14,
                    minute = 30
                ),
                elapsedTime = CalendarTime(
                    year = 0,
                    month = 0,
                    day = 0,
                    hour = 0,
                    minute = 0
                ),
            ),
            Lesson(
                id = 3,
                subject = "Физика",
                typeofLesson = "Домашнее задание",
                description = "Решить задачу",
                icons = listOf(),
                dataCalendarStartTime = CalendarTime(
                    year = 2024,
                    month = 4,
                    day = 20,
                    hour = 13,
                    minute = 30
                ),
                dataCalendarEndTime = CalendarTime(
                    year = 2024,
                    month = 4,
                    day = 20,
                    hour = 14,
                    minute = 30
                ),
                elapsedTime = CalendarTime(
                    year = 0,
                    month = 0,
                    day = 0,
                    hour = 0,
                    minute = 0
                )
            ),
            Lesson(
                id = 1,
                subject = "Математика",
                typeofLesson = "Экзамен",
                description = "Экзамен по математике",
                icons = listOf(),
                dataCalendarStartTime = CalendarTime(
                    year = 2023,
                    month = 11,
                    day = 15,
                    hour = 12,
                    minute = 20
                ),
                dataCalendarEndTime = CalendarTime(
                    year = 2023,
                    month = 11,
                    day = 15,
                    hour = 13,
                    minute = 20
                ),
                elapsedTime = CalendarTime(
                    year = 0,
                    month = 0,
                    day = 0,
                    hour = 0,
                    minute = 0
                )
            ),
            Lesson(
                id = 3,
                subject = "География",
                typeofLesson = "Домашнее задание",
                description = "Как отжать аляску у Пиндосов",
                icons = listOf(),
                dataCalendarStartTime = CalendarTime(
                    year = 2024,
                    month = 4,
                    day = 20,
                    hour = 13,
                    minute = 30
                ),
                dataCalendarEndTime = CalendarTime(
                    year = 2024,
                    month = 4,
                    day = 20,
                    hour = 14,
                    minute = 30
                ),
                elapsedTime = CalendarTime(
                    year = 0,
                    month = 0,
                    day = 0,
                    hour = 0,
                    minute = 0
                )
            )
        )
        Gson().toJson(output)
    }

}
