import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sunlitmosques.Screen
import com.example.sunlitmosques.models.Mosque
import com.example.sunlitmosques.ui.theme.ChangaFontFamily
import com.example.sunlitmosques.viewmodels.MosqueViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MosqueListScreen(navController: NavController, mosqueViewModel: MosqueViewModel = viewModel()) {
    val mosqueList by mosqueViewModel.mosqueList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Sunlit Mosque",
                        fontFamily = ChangaFontFamily
                    )
                },
            )
        },
        content = { paddingValues ->
            if (mosqueList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(mosqueList) { mosque ->
                        MosqueRow(mosque) {
                            mosqueViewModel.selectMosque(mosque)
                            navController.navigate(Screen.MosqueDetails.route)
                        }
                    }
                }
            } else {
                Text(
                    "No data available",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    )
}

@Composable
fun MosqueRow(mosque: Mosque, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mosque.accountNumber,
                    fontFamily = ChangaFontFamily,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Total Outstanding: ${mosque.totalOutstanding}",
                    fontFamily = ChangaFontFamily,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSystemInDarkTheme()) Color.Green else Color.DarkGray
                )
            }
        }
    }

}


val sampleMosque = Mosque(
    accountNumber = "R21477G",
    minName = "MINISTRY OF AWQAF (MOSQUE)(ELEC)",
    totalOutstanding = 446.48,
    monthlyConsumption = mapOf(
        "22-Nov" to 446.477,
        "22-Oct" to 0.0,
        "22-Sep" to 0.0,
        "22-Aug" to 0.0,
        "22-Jul" to 0.0,
        "22-Jun" to 0.0,
        "22-May" to 0.0,
        "22-Apr" to 0.0,
        "22-Mar" to 0.0,
        "22-Feb" to 0.0,
        "22-Jan" to 0.0
    )
)

@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PreviewMosqueItem() {
    MosqueRow(mosque = sampleMosque, onClick = {})
}
