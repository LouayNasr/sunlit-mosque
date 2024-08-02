package com.example.sunlitmosques.viewmodels

import JsonParser
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sunlitmosques.models.Mosque
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kotlin.math.roundToInt

class MosqueViewModel(application: Application) : AndroidViewModel(application) {

    private val _mosqueList = MutableStateFlow<List<Mosque>>(emptyList())
    val mosqueList: StateFlow<List<Mosque>> get() = _mosqueList

    private val _selectedMosque = MutableStateFlow<Mosque?>(null)
    val selectedMosque: StateFlow<Mosque?> get() = _selectedMosque

    private val _peakMonth = MutableStateFlow<String>("")
    val peakMonth: StateFlow<String> get() = _peakMonth

    private val _numberOfPanels = MutableStateFlow(0)
    val numberOfPanels: StateFlow<Int> get() = _numberOfPanels

    private val _numberOfBatteries = MutableStateFlow(0)
    val numberOfBatteries: StateFlow<Int> get() = _numberOfBatteries

    private val _inverterCapacity = MutableStateFlow(0)
    val inverterCapacity: StateFlow<Int> get() = _inverterCapacity

    private val _showResultSection = MutableStateFlow(false)
    val showResultSection: StateFlow<Boolean> = _showResultSection.asStateFlow()

    private val _percentageOfDailyCoverage = MutableStateFlow(0.5f)
    val percentageOfDailyCoverage: StateFlow<Float> = _percentageOfDailyCoverage.asStateFlow()

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

    fun performCalculation() {
        val highestMonth = _selectedMosque.value?.monthlyConsumption?.maxBy { it.value }
        _peakMonth.value = (highestMonth?.key ?: "")
        val dailyPowerConsumption = (highestMonth?.value)?.times(1000)
            ?.div(30) // FIXME: hard coded number of days a month is incorrect, it should calculated base on the selected month, also consumption may vary from day to day: for that should be safe_factor took into consideration
        val dailyPowerCoverage = dailyPowerConsumption?.times(_percentageOfDailyCoverage.value)
        val systemEfficiency = 0.75
        val solarInsolation = 5
        val requiredCapacity = dailyPowerCoverage?.div((systemEfficiency * solarInsolation))


        //calculate number of required panels
        val solarPanelWattage = 300
        _numberOfPanels.value =
            if (requiredCapacity == 0.0) 0 else requiredCapacity?.div(solarPanelWattage)
                ?.roundToInt()?.coerceAtLeast(1) ?: 1

        //calculate number of batteries
        val dayOfAutonomy = 2
        val batteryEfficiency = 0.9
        val requiredBatteryCapacity =
            (dailyPowerCoverage?.times(dayOfAutonomy))?.div(batteryEfficiency)
        val batteryVoltage = 48
        val requiredCapacityInAh = requiredBatteryCapacity?.div(batteryVoltage)
        val singleBatteryAmps = 200
        _numberOfBatteries.value =
            if (requiredCapacityInAh == 0.0) 0 else requiredCapacityInAh?.div(singleBatteryAmps)
                ?.roundToInt()?.coerceAtLeast(1) ?: 1


        //calculate inverter capacity
        val peakPowerDemand = dailyPowerCoverage?.times(0.10)
        _inverterCapacity.value = peakPowerDemand?.times(1.25)?.roundToInt() ?: 0

        _showResultSection.value = true;
    }

    fun resetCalculatedValues() {
        _selectedMosque.value = null
        _peakMonth.value = ""
        _numberOfPanels.value = 0
        _numberOfBatteries.value = 0
        _inverterCapacity.value = 0
        _showResultSection.value = false
        _percentageOfDailyCoverage.value = 0.5f
    }

    fun updatePercentageOfDailyCoverage(percentage: Float) {
        _percentageOfDailyCoverage.value = percentage
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