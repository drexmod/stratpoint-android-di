package com.stratpoint.basedesignpatternguide.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding>(
    private val setupBinding: (LayoutInflater) -> VB
) : Fragment() {

    lateinit var viewBinding: VB
    private var isNewlyCreated = true

    open fun <T> Fragment.observe(flow: LiveData<T>, observer: Observer<T>) =
        flow.observe(viewLifecycleOwner, observer)

    open fun <T> Fragment.collect(flow: Flow<T>, collect: suspend (T) -> Unit) =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(collect)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::viewBinding.isInitialized) {
            viewBinding = setupBinding(inflater)
            isNewlyCreated = true
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isNewlyCreated) {
            setUpView()
        }

        observeViewModel()
        collectViewModel()

        if (isNewlyCreated) {
            loadContent()
            isNewlyCreated = false
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (this@BaseFragment.handleBackPress().not()) {
                    defaultBackPress()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    open fun setUpView() {}
    open fun collectViewModel() {}
    open fun observeViewModel() {}
    open fun loadContent() {}

    /**
     * return true to override default back behavior
     */
    open fun handleBackPress(): Boolean {
        return false
    }

    fun defaultBackPress() {
        if (findNavController().popBackStack().not()) {
            requireActivity().finish()
        }
    }

    protected inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
        }

}