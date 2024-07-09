package com.kei037.pay_breeze_mvc.ui.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kei037.pay_breeze_mvc.databinding.FragmentAnalysisBinding

/**
 * AnalysisFragment
 *
 * This fragment is responsible for displaying the analysis section of the application.
 * It sets up a ViewPager2 with a TabLayout to show different transaction analysis views.
 *
 * The fragment uses View Binding for efficient view access and implements a ViewPager2
 * with a TabLayout to provide a tabbed interface for different transaction analyses.
 */
class AnalysisFragment : Fragment() {

    // View binding object for the fragment
    private var _binding: FragmentAnalysisBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    /**
     * Inflates the fragment layout and initializes the view binding.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     * @return The root View of the inflated layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Sets up the ViewPager2 and TabLayout after the view is created.
     *
     * This method initializes the ViewPager2 with a custom adapter and sets up the TabLayout
     * to work in conjunction with the ViewPager2. It defines the tab titles for different
     * analysis views.
     *
     * @param view The View returned by onCreateView(...)
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout

        // Set up the ViewPager2 with a custom adapter
        val adapter = AnalysisPagerAdapter(this)
        viewPager.adapter = adapter

        // Configure the TabLayout with the ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Expense"
                1 -> "Income"
                else -> null
            }
        }.attach()
    }

    /**
     * Cleans up the binding when the view is destroyed.
     *
     * This is important to avoid memory leaks and ensure proper garbage collection.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}