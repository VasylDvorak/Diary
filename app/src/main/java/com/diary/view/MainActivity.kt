package com.diary.view

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.diary.R
import com.diary.databinding.ActivityMainBinding
import com.diary.navigation.IScreens
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

}
