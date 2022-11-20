package com.fermimn.gamewishlist.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseFragment<VDB : ViewDataBinding> : Fragment() {

    protected val consoleTag: String = javaClass.simpleName

    protected lateinit var binding: VDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getFragmentView(), container, false)
        return binding.root
    }

    abstract fun getFragmentView(): Int

}