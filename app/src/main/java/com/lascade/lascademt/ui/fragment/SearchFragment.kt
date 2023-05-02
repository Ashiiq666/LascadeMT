package com.lascade.lascademt.ui.fragment

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.lascade.lascademt.R
import com.lascade.lascademt.data.network.NetworkStateManager
import com.lascade.lascademt.databinding.FragmentSearchBinding
import com.lascade.lascademt.ui.adapter.SearchAdapter
import com.lascade.lascademt.ui.viewmodel.SearchFragmentViewModel
import java.net.UnknownHostException


class SearchFragment : Fragment() {
    private var binding: FragmentSearchBinding? = null
    private val searchAdapter by lazy { SearchAdapter() }
    private val viewModel by viewModels<SearchFragmentViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initView()
        getGalaxies()
        observeGalaxies()
        handleEvents()
    }

    private fun initView() {
        setUpTabLayout()
    }

    private fun setUpTabLayout() {
        val tabList = listOf("Largest", "Brightest", "Closest", "Smallest")
        binding?.tabLayout?.apply {
            for (title in tabList) {
                val tab: TabLayout.Tab = newTab()
                tab.setCustomView(R.layout.custom_tab_layout)

                val tabText: TextView? = tab.customView?.findViewById(R.id.tabText)
                tabText?.text = title

                addTab(tab)
            }
            val root: View = getChildAt(0)
            if (root is LinearLayout) {
                root.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
                val drawable = GradientDrawable()
                drawable.setColor(resources.getColor(R.color.tabLayoutSeparator))
                drawable.setSize(2, 1)
                root.dividerPadding = 10
                root.dividerDrawable = drawable
            }
        }
    }

    private fun init() {
        binding?.rvData?.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getGalaxies() {
        showLoader(true)
        viewModel.getGalaxies()
    }

    private fun observeGalaxies() {
        viewModel.observeGalaxiesLiveData().observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkStateManager.Success -> {
                    response.data?.let {
                        if (it.status == true) {
                            it.result?.let { dataList ->
                                searchAdapter.submitList(dataList)
                            } ?: showToast("Galaxy list is empty!")
                        } else showToast(it.message ?: "Something went wrong!")
                    } ?: showToast("Failed to get data!")
                }

                is NetworkStateManager.Error -> {
                    if (response.throwable is UnknownHostException) showToast("Please check your internet connection!")
                    else showToast(response.message ?: "Something went wrong!")
                }
            }

            showLoader(false)
        }
    }

    private fun handleEvents() {
        binding?.ivBack?.setOnClickListener {
            findNavController().navigateUp()
        }
        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                handleTabSelection(tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                handleTabSelection(tab, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                handleTabSelection(tab, true)
            }
        })
        binding?.tabLayout?.apply {
            selectTab(getTabAt(0), true)
        } // Selects first tab by default with color
    }

    private fun handleTabSelection(tab: TabLayout.Tab?, isTabSelected: Boolean) {
        val tabView: View? = tab?.customView
        val tabText: TextView? = tabView?.findViewById(R.id.tabText)
        tabText?.apply {
            alpha = if (isTabSelected) {
                setTextColor(Color.parseColor("#000000"))
                1f
            } else {
                setTextColor(Color.parseColor("#677279"))
                0.7f
            }
        }
    }

    private fun showLoader(b: Boolean) {
        binding?.progressBar?.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}