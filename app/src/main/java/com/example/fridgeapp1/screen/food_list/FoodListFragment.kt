package com.example.fridgeapp1.screen.food_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fridgeapp1.FridgeApplication
import com.example.fridgeapp1.R
import com.example.fridgeapp1.databinding.FragmentFoodListBinding

class FoodListFragment: Fragment() {
    private val viewModel: FoodListViewModel by activityViewModels {
        FoodViewModelFactory(
            (activity?.application as FridgeApplication).database.foodDao())
    }

    private var _binding: FragmentFoodListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentFoodListBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val adapter = FoodListAdapter{
            val action =
                FoodListFragmentDirections.actionFoodListFragmentToEditFoodFragment(it.name, it.id)
            this.findNavController().navigate(
                action,
                NavOptions.Builder()
                    .setEnterAnim(com.google.android.material.R.anim.m3_side_sheet_enter_from_right)
                    .setExitAnim(com.google.android.material.R.anim.m3_side_sheet_exit_to_left)
                    .setPopExitAnim(com.google.android.material.R.anim.m3_side_sheet_exit_to_right)
                    .setPopEnterAnim(com.google.android.material.R.anim.m3_side_sheet_enter_from_left)
                    .build())
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        viewModel.allFood.observe(this.viewLifecycleOwner) { items -> items.let { adapter.submitList(it) } }

        binding.floatingActionButton.setOnClickListener {
            val action = FoodListFragmentDirections
                .actionFoodListFragmentToEditFoodFragment("New entry")
            it.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.circle_explosion_anim).apply {
                    duration = 700
                    interpolator = AccelerateDecelerateInterpolator()
                }
            )
            this.findNavController().navigate(action)
        }
    }
}