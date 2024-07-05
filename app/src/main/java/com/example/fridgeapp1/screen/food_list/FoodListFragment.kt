package com.example.fridgeapp1.screen.food_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fridgeapp1.FridgeApplication
import com.example.fridgeapp1.databinding.FragmentFoodListBinding
import com.example.fridgeapp1.di.ViewModelFactory
import com.google.android.material.transition.MaterialElevationScale
import javax.inject.Inject

class FoodListFragment: Fragment() {
    //@Inject
    //lateinit var viewModelFactory : ViewModelFactory<FoodListViewModel>

    //private val viewModel1: FoodListViewModel by activityViewModels<FoodListViewModel> {
    //    viewModelFactory
    //}

    @Inject
    lateinit var viewModel: FoodListViewModel

    private var _binding: FragmentFoodListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
    }
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
        (requireActivity().application as FridgeApplication).appComponent.inject(this)
        (requireActivity().application as FridgeApplication).appComponent.inject(viewModel)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
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
        //binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.layoutManager = GridLayoutManager(this.context, 2)
        binding.recyclerView.adapter = adapter

        val a = viewModel
        viewModel.allFood.observe(this.viewLifecycleOwner) { items -> items.let { adapter.submitList(it) } }

        binding.floatingActionButton.setOnClickListener {
            val action = FoodListFragmentDirections
                .actionFoodListFragmentToEditFoodFragment("New entry")
            val extras = FragmentNavigatorExtras(it to "shared_element_container")
            this.findNavController().navigate(action, extras)
        }
    }
}