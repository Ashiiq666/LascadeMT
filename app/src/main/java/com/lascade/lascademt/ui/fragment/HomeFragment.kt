package com.lascade.lascademt.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.lascade.lascademt.R
import com.lascade.lascademt.databinding.FragmentHomeBinding
import com.lascade.lascademt.ui.adapter.SliderViewPagerAdapter


class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    val handlers = Handler(Looper.getMainLooper())
    lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        handleEvents()
    }

    private fun init() {
        val imageList =
            listOf(R.drawable.banner, R.drawable.banner, R.drawable.banner, R.drawable.banner)
        val sliderAdapter = SliderViewPagerAdapter(requireContext(), imageList)
        binding?.vpBanner?.adapter = sliderAdapter
        binding?.vpBanner?.autoScroll(1800)
        binding?.dotsIndicator?.createIndicators(imageList.size, 0)
    }

    private fun handleEvents() {
        binding?.cslSearch?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
        binding?.vpBanner?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> { // to pause scroll when user touches the images
                        handlers.removeCallbacks(runnable)
                        return true
                    }
                    MotionEvent.ACTION_UP -> { // to resume scroll
                        handlers.post(runnable)
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun ViewPager.autoScroll(delayInMillis: Long) {
        var scrollPosition = 0

        runnable = object : Runnable {
            override fun run() {
                val count = adapter?.count ?: 0
                setCurrentItem(scrollPosition++ % count, true)
                handlers.postDelayed(this, delayInMillis)
            }
        }

        addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                scrollPosition =
                    position + 1 // Updating "scroll position" when user scrolls manually
                binding?.dotsIndicator?.animatePageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                // No need to implement
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // No need to implement
            }
        })

    }

    override fun onResume() {
        handlers.post(runnable) //to start scroll when user resume the screen
        super.onResume()
    }

    override fun onPause() { // to stop scroll when user lefts the screen
        handlers.removeCallbacks(runnable)
        super.onPause()
    }

}