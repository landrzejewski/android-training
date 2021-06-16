package pl.training.goodweather.profile.adapter.view

import androidx.lifecycle.ViewModel

class ProfileViewModel: ViewModel() {

    fun isEmailValid(text: String) = text.contains("@")

}