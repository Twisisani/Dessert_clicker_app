package com.example.dessertclicker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.dessertclicker.ui.theme.DessertClickerTheme
import com.example.dessertclicker.DessertViewModel
import com.example.dessertclicker.DessertUiState

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    private val viewModel: DessertViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate Called")
        setContent {
            DessertClickerTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                ) {
                    DessertClickerApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun DessertClickerApp(viewModel: DessertViewModel) {
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        topBar = {
            DessertClickerAppBar(
                onShareButtonClicked = {
                    shareSoldDessertsInformation(
                        intentContext = context,
               dessertsSold = uiState.dessertsSold,
                        revenue = uiState.revenue
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateStartPadding(layoutDirection),
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(layoutDirection),
                    )
                    .background(MaterialTheme.colorScheme.primary)
            )
        },
        content = { contentPadding: PaddingValues ->
            DessertClickerScreen(
                revenue = uiState.revenue,
                dessertsSold = uiState.dessertsSold,
                dessertImageId = uiState.currentDessertImageId,
                onDessertClicked = { viewModel.onDessertClicked() },
                modifier = Modifier.padding(contentPadding)
            )
        }
    )
}

@Composable
private fun DessertClickerAppBar(
    onShareButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium)),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
        )
        IconButton(
            onClick = onShareButtonClicked,
            modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_medium)),
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.share),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

private fun shareSoldDessertsInformation(intentContext: android.content.Context, dessertsSold: Int, revenue: Int) {
    val sendIntent = android.content.Intent().apply {
        action = android.content.Intent.ACTION_SEND
        putExtra(
            android.content.Intent.EXTRA_TEXT,
            intentContext.getString(R.string.share_text, dessertsSold, revenue)
        )
        type = "text/plain"
    }

    val shareIntent = android.content.Intent.createChooser(sendIntent, null)

    try {
        androidx.core.content.ContextCompat.startActivity(intentContext, shareIntent, null)
    } catch (e: android.content.ActivityNotFoundException) {
        android.widget.Toast.makeText(
            intentContext,
            intentContext.getString(R.string.sharing_not_available),
            android.widget.Toast.LENGTH_LONG
        ).show()
    }
}

@Preview
@Composable
fun MyDessertClickerAppPreview() {
    DessertClickerTheme {
        val dummyState = DessertUiState(
            revenue = 0,
            dessertsSold = 0,
            currentDessertImageId = R.drawable.cupcake,
            currentDessertPrice = 5
        )
        DessertClickerScreen(
            revenue = dummyState.revenue,
            dessertsSold = dummyState.dessertsSold,
            dessertImageId = dummyState.currentDessertImageId,
            onDessertClicked = {},
            modifier = Modifier
        )
    }
}
