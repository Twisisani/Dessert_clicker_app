package com.example.dessertclicker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert

class DessertViewModel : ViewModel() {

    private val desserts: List<Dessert> = Datasource.dessertList

    private val _uiState = MutableStateFlow(
        DessertUiState(
            currentDessertImageId = desserts[0].imageId,
            currentDessertPrice = desserts[0].price
        )
    )
    val uiState: StateFlow<DessertUiState> = _uiState

    fun onDessertClicked() {
        val newDessertsSold = _uiState.value.dessertsSold + 1
        val newRevenue = _uiState.value.revenue + _uiState.value.currentDessertPrice

        val dessertToShow = determineDessertToShow(desserts, newDessertsSold)

        _uiState.value = DessertUiState(
            revenue = newRevenue,
            dessertsSold = newDessertsSold,
            currentDessertImageId = dessertToShow.imageId,
            currentDessertPrice = dessertToShow.price
        )
    }

    private fun determineDessertToShow(
        desserts: List<Dessert>,
        dessertsSold: Int
    ): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                break
            }
        }
        return dessertToShow
    }
}
