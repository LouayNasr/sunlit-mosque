import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sunlitmosques.Screen
import com.example.sunlitmosques.models.Mosque
import com.example.sunlitmosques.viewmodels.MosqueViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MosqueListScreen(navController: NavController, mosqueViewModel: MosqueViewModel = viewModel()) {
    val mosqueList by mosqueViewModel.mosqueList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sunlit Mosque") },
            )
        },
        content = { paddingValues ->
            if (mosqueList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp)
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = mosque.accountNumber, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Total Outstanding: ${mosque.totalOutstanding}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
