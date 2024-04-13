package com.example.fridgeapp1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fridgeapp1.databinding.FragmentFoodListBinding

class FoodListFragment: Fragment() {
    private val viewModel: FoodViewModel by activityViewModels {
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
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        viewModel.allFood.observe(this.viewLifecycleOwner) { items -> items.let { adapter.submitList(it) } }

        binding.floatingActionButton.setOnClickListener {
            val action = FoodListFragmentDirections
                .actionFoodListFragmentToEditFoodFragment("New entry")

            this.findNavController().navigate(action)
        }
    }
}