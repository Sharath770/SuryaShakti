package com.example.suryashakti2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.suryashakti2.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

enum class WeatherCondition(val displayName: String, val multiplier: Double) {
    SUNNY("Sunny", 1.05),
    CLOUDY("Cloudy", 0.90)
}

data class EnergyUiState(
    val solarGenerationInput: String = "",
    val startReadingInput: String = "",
    val endReadingInput: String = "",
    val isRollover: Boolean = false,

    val selectedWeather: WeatherCondition = WeatherCondition.SUNNY,

    val adjustedGeneration: Double = 0.0,
    val consumption: Double = 0.0,
    val netGridUsage: Double = 0.0,
    val netSavings: Double = 0.0,
    val independenceScore: Double = 0.0,

    val thirtyDaySavings: Double = 0.0,
    val saveStatusMessage: String = ""
)

class EnergyViewModel(
    private val repository: EnergyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EnergyUiState())
    val uiState: StateFlow<EnergyUiState> = _uiState

    val userProfile: StateFlow<UserProfile?> =
        repository.getProfile()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allLogs: StateFlow<List<EnergyLog>> =
        repository.getAllLogs()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        observeThirtyDayReport()
    }

    // 🔹 INPUTS
    fun onSolarGenerationChanged(value: String) {
        _uiState.update { it.copy(solarGenerationInput = value) }
        recalculate()
    }

    fun onStartReadingChanged(value: String) {
        _uiState.update { it.copy(startReadingInput = value) }
        recalculate()
    }

    fun onEndReadingChanged(value: String) {
        _uiState.update { it.copy(endReadingInput = value) }
        recalculate()
    }

    fun onRolloverToggle(value: Boolean) {
        _uiState.update { it.copy(isRollover = value) }
        recalculate()
    }

    fun onWeatherSelected(weather: WeatherCondition) {
        _uiState.update { it.copy(selectedWeather = weather) }
        recalculate()
    }

    // 🔹 SAVE DAILY LOG
    fun saveLog() {
        val current = _uiState.value
        viewModelScope.launch {
            repository.saveDailyLog(
                generation = current.adjustedGeneration,
                consumption = current.consumption,
                savings = current.netSavings
            )
            _uiState.update { it.copy(saveStatusMessage = "Saved ✅") }
        }
    }

    // 🔥 ✅ THIS WAS MISSING (IMPORTANT FIX)
    fun saveUserProfile(capacity: Double, rate: Double, tariff: Double) {
        viewModelScope.launch {
            repository.saveUserProfile(capacity, rate, tariff)
        }
    }

    private fun observeThirtyDayReport() {
        viewModelScope.launch {
            repository.getThirtyDaySavingsReport().collectLatest {
                _uiState.update { s -> s.copy(thirtyDaySavings = it) }
            }
        }
    }

    // 🔥 CORE LOGIC
    private fun recalculate() {
        val current = _uiState.value
        val profile = userProfile.value

        val gen = current.solarGenerationInput.toDoubleOrNull() ?: 0.0
        val start = current.startReadingInput.toDoubleOrNull() ?: 0.0
        val end = current.endReadingInput.toDoubleOrNull() ?: 0.0

        // ✅ FR-04: cap generation
        val maxGen = (profile?.panelCapacity ?: 0.0) * 8
        val cappedGen = min(gen, maxGen)

        val adjustedGen = cappedGen * current.selectedWeather.multiplier

        // ✅ FR-08 + FR-06
        val consumption = if (current.isRollover) {
            (end + 10000) - start
        } else {
            end - start
        }.coerceAtLeast(0.0)

        val self = min(adjustedGen, consumption)
        val export = max(adjustedGen - consumption, 0.0)

        val rate = profile?.gridRate ?: 0.0
        val tariff = profile?.feedInTariff ?: 0.0

        val savings = (self * rate) + (export * tariff)

        val independence =
            if (consumption > 0)
                (adjustedGen / consumption * 100).coerceAtMost(100.0)
            else 0.0

        _uiState.update {
            it.copy(
                adjustedGeneration = adjustedGen,
                consumption = consumption,
                netGridUsage = consumption - adjustedGen,
                netSavings = savings,
                independenceScore = independence
            )
        }
    }
}

// 🔹 FACTORY
class EnergyViewModelFactory(
    private val repository: EnergyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EnergyViewModel(repository) as T
    }
}