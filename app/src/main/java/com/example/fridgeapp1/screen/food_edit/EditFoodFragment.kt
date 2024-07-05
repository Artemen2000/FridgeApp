package com.example.fridgeapp1.screen.food_edit

import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fridgeapp1.FridgeApplication
import com.example.fridgeapp1.R
import com.example.fridgeapp1.databinding.FragmentEditFoodBinding
import com.example.fridgeapp1.di.EditFoodComponent
import com.example.fridgeapp1.di.EditFoodModule
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialContainerTransform
import javax.inject.Inject

class EditFoodFragment : Fragment() {
    //private val viewModel1: EditFoodViewModel by viewModels {
    //    EditFoodViewModelFactory(
    //        (activity?.application as FridgeApplication).database.getFoodDao(),
    //        navigationArgs.itemId
    //    )
    //}

    private lateinit var editFoodComponent : EditFoodComponent

    @Inject
    lateinit var viewModel: EditFoodViewModel

    private val navigationArgs: EditFoodFragmentArgs by navArgs()

    private var _binding: FragmentEditFoodBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        editFoodComponent = (requireActivity().application as FridgeApplication).appComponent.editFoodComponent().create(
            EditFoodModule(navigationArgs.itemId)
        )
        editFoodComponent.inject(this)
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform(requireContext(), true)
            .apply {
                scrimColor = Color.TRANSPARENT
                fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
            }
        
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        binding.inputDataExpireDate.isEnabled = false
        setOnTextChangedListeners()
        binding.inputDataFoodName.setText(viewModel.currentName.value)
        setDeleteButtonVisibility()
        setObservers()
    }

    private fun setOnTextChangedListeners() {
        binding.inputDataFoodName.doOnTextChanged { text, _, _, _ ->
            viewModel.nameChanged(text.toString())
        }

        binding.inputDataExpireDays.doOnTextChanged { text, _, _, _ ->
            viewModel.dateTextChanged(text.toString())
        }
    }

    private fun setObservers() {
        viewModel.currentDate.observe(viewLifecycleOwner) {
            binding.inputDataExpireDate.setText(it.date)
            binding.inputDataExpireDays.apply {
                if (text.toString() != it.days.toString()) {
                    setText(it.days.toString())
                    setSelection(it.days.toString().length)
                }
            }
        }

        viewModel.currentName.observe(viewLifecycleOwner) {
            Log.d("FridgeApp", "Current name: $it")
            Log.d("FridgeApp", "VM data: ${viewModel.currentDate.value?.days }; ${viewModel.currentName.value}")
            if (binding.inputDataFoodName.text.toString() != it)
                binding.inputDataFoodName.setText(it)
        }

        viewModel.navDest.observe(viewLifecycleOwner) { dest ->
            dest?.let {
                when (dest) {
                    EditFoodDestination.Up -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.inputDataExpireDateLayout.setEndIconOnClickListener {
            viewModel.currentDate.value?.millis?.let {
                launchDatePicker(it)
            }

        }

        binding.btnAccept.setOnClickListener {
            viewModel.acceptClicked()
        }
        binding.btnDelete.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.confirm_deleting_label)
                .setNegativeButton(R.string.confirm_deleting_cancel) { _, _ -> }
                .setPositiveButton(R.string.confirm_deleting_accept) { _, _ ->
                    viewModel.deleteItem()
                }
                .show()
        }
    }

    private fun setDeleteButtonVisibility() {
        if (viewModel.isEditing) {
            binding.btnDelete.visibility = Button.VISIBLE
        } else {
            binding.btnDelete.visibility = Button.GONE
        }
    }

    private fun launchDatePicker(date: Long) {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.datepicker_title)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setSelection(date)
            .build()
            .apply {
                addOnPositiveButtonClickListener { selection: Long? ->
                    selection?.let { viewModel.datePicked(it) }
                }
            }
            .show(this.parentFragmentManager, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE)
                as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            requireActivity().currentFocus?.windowToken,
            0
        )
        _binding = null
    }
}