package com.diary.view.base_for_dictionary

import android.animation.Animator
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.diary.navigation.IScreens
import com.diary.view.AnimatorDictionary
import com.github.terrakok.cicerone.Router
import org.koin.java.KoinJavaComponent

abstract class BaseFragmentSettingsMenu<B : ViewBinding>(
    private val inflateBinding: (
        inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean
    ) -> B
) : Fragment() {
    lateinit protected var timer: CountDownTimer
    private var _binding: B? = null
    protected val binding: B
        get() = _binding!!
    val router: Router by KoinJavaComponent.inject(Router::class.java)
    val screen = KoinJavaComponent.getKoin().get<IScreens>()
    protected var isNetworkAvailable: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return AnimatorDictionary().setAnimator(transit, enter)
    }

}