package com.example.fridgeapp1

import android.content.Context.INPUT_METHOD_SERVICE
import android.icu.text.DateFormat.DAY
import android.icu.text.DateFormat.getDateInstance
import android.icu.util.Calendar
import android.icu.util.TimeUnit
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
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

    //FoodItem that is currently observed
    lateinit var observingFood: LiveData<FoodItem>

    //This FoodItem is used for filling fields via LiveData
    lateinit var currentFood: MutableLiveData<FoodItem>

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
            .setTitleText("Select expirement date")
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()

        binding.inputDataExpireDate.isEnabled = false
        binding.inputDataExpireDateLayout.setEndIconOnClickListener {
            datePicker.show(this.parentFragmentManager, "Kek")
        }

        datePicker.addOnPositiveButtonClickListener {
                selection: Long? ->
            Log.d("goida", "DatePicker on positive Listener")
            val date = Calendar.getInstance()
            date.timeInMillis = selection!!
            binding.inputDataExpireDate.text =
                    SpannableStringBuilder(getDateInstance().format(date.time))
        }

        return binding.root
    }

    private fun addNewFoodItem() {
        Log.d("goida", "Hello, Viewmodel")
        viewModel.hello()
        Log.d("goida", "Collecting data from widgets")
        val date = Calendar.getInstance()
        date.timeInMillis = datePicker.selection!!
        Log.d("goida", "Calling addNewFood from viemModel...")
        viewModel.addNewFood(
            binding.inputDataFoodName.text.toString(),
            date.time)
    }

    private fun bind(){
        val item = currentFood.value ?: return
        val cal = Calendar.getInstance()
        cal.timeInMillis = item?.expiresAt!!
        val now = Calendar.getInstance().timeInMillis
        val days = cal.timeInMillis - now
        val days1 = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(days)
        binding.inputDataFoodName.setText(item.name)
        binding.inputDataFoodName.doOnTextChanged { text, start, before, count ->
            currentFood.value?.name = text.toString()
        }
        binding.inputDataExpireDays.setText(days1.toString())
        binding.inputDataExpireDays.doOnTextChanged { text, start, before, count ->
            val cal_new = Calendar.getInstance()
            cal_new.add(Calendar.DAY_OF_MONTH, binding.inputDataExpireDays.text.toString().toInt())
            currentFood.value?.expiresAt = cal_new.timeInMillis
        }

        binding.inputDataExpireDate.text = SpannableStringBuilder(getDateInstance().format(cal.time))
        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select expirement date")
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setSelection(cal.timeInMillis)
            .build()

        datePicker.addOnPositiveButtonClickListener {
                selection: Long? ->
            Log.d("goida", "DatePicker on positive Listener")
            val date = Calendar.getInstance()
            date.timeInMillis = selection!!
            binding.inputDataExpireDate.text =
                SpannableStringBuilder(getDateInstance().format(date.time))
        }

        binding.btnAccept.setOnClickListener {
            updateFood()
            this.findNavController().navigateUp()
        }
    }

   private fun updateFood() {
       val date = Calendar.getInstance()
       date.timeInMillis = datePicker.selection!!

       viewModel.updateFood(this.navigationArgs.itemId,
           this.binding.inputDataFoodName.text.toString(),
           date.time)
       val action = EditFoodFragmentDirections.actionEditFoodFragmentToFoodListFragment()
       findNavController().navigate(action)
   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        currentFood = MutableLiveData()


        val id = navigationArgs.itemId

        if (id > 0){
            val item = viewModel.retrieveItem(id)
            currentFood.value = viewModel.retrieveItem(id).value
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                //observingFood.value = selectedItem
                currentFood.value = selectedItem
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
            }
        }
        else {
            currentFood.value = FoodItem(name = "", expiresAt = Calendar.getInstance().timeInMillis)
            binding.btnDelete.visibility = Button.GONE
            binding.btnAccept.setOnClickListener {
                addNewFoodItem()
                this.findNavController().navigateUp()
            }
        }

        //bind()
        currentFood.observe(this.viewLifecycleOwner){
            //При изменении currentFood
            if (it == null) return@observe
            bind()
            val cal = Calendar.getInstance()
            cal.timeInMillis = it.expiresAt
            val now = Calendar.getInstance().timeInMillis
            val days = cal.timeInMillis - now
            val days1 = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(days)
            binding.inputDataExpireDate.text = SpannableStringBuilder(getDateInstance().format(cal.time))
            binding.inputDataExpireDays.setText(days1.toString())
            binding.inputDataFoodName.setText(it.name)
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