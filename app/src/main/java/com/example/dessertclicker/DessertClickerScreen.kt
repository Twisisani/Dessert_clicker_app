package com.example.dessertclicker

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DessertClickerScreen(
    revenue: Int,
    dessertsSold: Int,
    dessertImageId: Int,
    onDessertClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Dessert Image
        Image(
            painter = painterResource(id = dessertImageId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { onDessertClicked() }
                .padding(16.dp)
        )

        // Stats
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Desserts Sold: $dessertsSold",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Total Revenue: $$revenue",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
