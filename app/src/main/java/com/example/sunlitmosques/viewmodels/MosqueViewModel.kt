package com.example.sunlitmosques.viewmodels

import JsonParser
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sunlitmosques.models.Mosque
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MosqueViewModel(application: Application) : AndroidViewModel(application) {

    private val _mosqueList = MutableStateFlow<List<Mosque>>(emptyList())
    val mosqueList: StateFlow<List<Mosque>> get() = _mosqueList

    private val _selectedMosque = MutableStateFlow<Mosque?>(null)
    val selectedMosque: StateFlow<Mosque?> get() = _selectedMosque

    fun loadMosquesFromAssets(fileName: String) {
        val jsonString = readJsonFromAssets(fileName)
        jsonString?.let {
            val mosqueList = JsonParser().parseJsonToMosqueList(it)
            _mosqueList.value = mosqueList
        }
    }

    private fun readJsonFromAssets(fileName: String): String? {
        return try {
            val inputStream = getApplication<Application>().assets.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            bufferedReader.use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun selectMosque(mosque: Mosque) {
        _selectedMosque.value = mosque
    }
}

class MosqueViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MosqueViewModel::class.java)) {
            return MosqueViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}