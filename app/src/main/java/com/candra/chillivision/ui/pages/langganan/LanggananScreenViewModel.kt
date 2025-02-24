package com.candra.chillivision.ui.pages.langganan

import androidx.lifecycle.ViewModel
import com.candra.chillivision.data.repository.ChilliVisionRepository

class LanggananScreenViewModel (private val repository : ChilliVisionRepository) : ViewModel(){
    fun getSubscriptions() = repository.getAllSubscriptions()
}