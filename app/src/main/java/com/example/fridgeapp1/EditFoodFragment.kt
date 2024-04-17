package com.example.fridgeapp1

import android.content.Context.INPUT_METHOD_SERVICE
import android.icu.text.DateFormat.getDateInstance
import android.icu.util.Calendar
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fridgeapp1.data.FoodItem
import com.example.fridgeapp1.databinding.FragmentEditFoodBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditFoodFragment : Fragment() {
    private val viewModel: FoodViewModel by activityViewModels {
        FoodViewModelFactory((activity?.application as FridgeApplication).database.foodDao())
    }
    private val navigationArgs : EditFoodFragmentArgs by navArgs()

    lateinit var observingFood: LiveData<FoodItem>

    val currentName: MutableLiveData<String> = MutableLiveData()
    val currentDate: MutableLiveData<Long> = MutableLiveData()

    var editingDays = false
    var editingDate = false

    private var _binding: FragmentEditFoodBinding? = null
    private val binding get() = _binding!!

    lateinit var datePicker: MaterialDatePicker<Long>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditFoodBinding.inflate(inflater, container, false)
        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select expiration date")
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()

        binding.inputDataExpireDate.isEnabled = false
        binding.inputDataExpireDateLayout.setEndIconOnClickListener {
            editingDays = false
            datePicker.show(this.parentFragmentManager, "Kek")
        }

        datePicker.addOnPositiveButtonClickListener {
                selection: Long? ->
            val date = Calendar.getInstance()
            date.timeInMillis = selection!!
            binding.inputDataExpireDate.text =
                    SpannableStringBuilder(getDateInstance().format(date.time))
        }

        return binding.root
    }

    private fun addNewFoodItem() {
        viewModel.hello()
        val date = Calendar.getInstance()
        date.timeInMillis = currentDate.value!!
        viewModel.addNewFood(
            currentName.value!!,
            date.time)
    }

    private fun bind(){
        if (currentDate.value != null && currentName.value != null) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = currentDate.value!!
            val now = Calendar.getInstance().timeInMillis
            val days = cal.timeInMillis - now
            val days1 = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(days)
            binding.inputDataFoodName.setText(currentName.value)

            binding.inputDataExpireDays.setText(days1.toString())

            binding.inputDataExpireDate.text =
                SpannableStringBuilder(getDateInstance().format(cal.time))
            datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select expiration date")
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setSelection(cal.timeInMillis)
                .build()
        }

        binding.inputDataFoodName.doOnTextChanged { text, start, before, count ->
            currentName.value = text.toString()
        }

        binding.inputDataExpireDays.setOnFocusChangeListener { v, hasFocus ->
            editingDays = hasFocus }

        binding.inputDataExpireDays.setOnClickListener {
            editingDays = true
        }

        binding.inputDataExpireDays.doOnTextChanged { text, start, before, count ->
            if (editingDays) {
                val cal_new = Calendar.getInstance()
                val days = if (text.toString().equals("")) 0 else text.toString().toInt()
                cal_new.add(Calendar.DAY_OF_MONTH, days)
                currentDate.value = cal_new.timeInMillis
            }
        }

        datePicker.addOnPositiveButtonClickListener { selection: Long? ->
            val date = Calendar.getInstance()
            date.timeInMillis = selection!!
            currentDate.value = date.timeInMillis
        }


    }

   private fun updateFood() {
       val date = Calendar.getInstance()
       //date.timeInMillis = datePicker.selection!!
       date.timeInMillis = currentDate.value!!

       viewModel.updateFood(this.navigationArgs.itemId,
           currentName.value!!,
           date.time)
       val action = EditFoodFragmentDirections.actionEditFoodFragmentToFoodListFragment()
       findNavController().navigate(action)
   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)


        val id = navigationArgs.itemId

        if (id > 0){
            val item = viewModel.retrieveItem(id)
            if (item.value != null) {
                currentName.value = item.value?.name
                currentDate.value = item.value?.expiresAt
            }
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                currentName.value = selectedItem.name
                currentDate.value = selectedItem.expiresAt
                binding.btnDelete.visibility = Button.VISIBLE
                binding.btnDelete.setOnClickListener {
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage("Delete food entry?")
                        .setNegativeButton("Cancel") { _, _ -> }
                        .setPositiveButton("Accept") { dialog, which ->
                            viewModel.deleteItem(observingFood.value!!)
                            this.findNavController().navigateUp()
                        }
                        .show()
                }

                binding.btnAccept.setOnClickListener {
                    updateFood()
                    this.findNavController().navigateUp()
                }
            }
        }
        else {
            currentName.value = ""
            currentDate.value = Calendar.getInstance().timeInMillis
            binding.btnDelete.visibility = Button.GONE
            binding.btnAccept.setOnClickListener {
                addNewFoodItem()
                this.findNavController().navigateUp()
            }
        }

        //bind()
        bind()

        currentDate.observe(this.viewLifecycleOwner){
            val cal = Calendar.getInstance()
            cal.timeInMillis = it
            val now = Calendar.getInstance().timeInMillis
            val days = cal.timeInMillis - now
            val days1 = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(days)
            if (!editingDate)
                binding.inputDataExpireDate.text = SpannableStringBuilder(getDateInstance().format(cal.time))
            if (!editingDays)
                binding.inputDataExpireDays.setText(days1.toString())
            binding.inputDataFoodName.setText(currentName.value)


            datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select expiration date")
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setSelection(it)
                .build()
            datePicker.addOnPositiveButtonClickListener {
                    selection: Long? ->
                val date = Calendar.getInstance()
                date.timeInMillis = selection!!
                currentDate.value = date.timeInMillis
            }

        }

        binding.inputDataExpireDate.setOnClickListener {
            datePicker.show(this.parentFragmentManager, "Kek")
        }
    }

    override fun onDestroyView(){
        super.onDestroyView()

        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE)
                as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}