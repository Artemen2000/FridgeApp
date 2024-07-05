package com.example.fridgeapp1.di

import com.example.fridgeapp1.screen.food_edit.EditFoodFragment
import com.example.fridgeapp1.screen.food_edit.EditFoodViewModel
import dagger.Subcomponent

@Subcomponent(modules = [EditFoodModule::class])
interface EditFoodComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(editFoodModule: EditFoodModule): EditFoodComponent
    }

    fun inject(fragment: EditFoodFragment)
    fun inject(viewModel: EditFoodViewModel)
}