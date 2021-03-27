package com.example.yaplacaklarlistesi.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaplacaklarlistesi.Model.Type
import com.example.yaplacaklarlistesi.Room.Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TypeDialogViewModel @Inject constructor(val dao: Dao) : ViewModel() {

    fun saveType(type: Type) {
        viewModelScope.launch {
            dao.saveType(type)
        }
    }

}