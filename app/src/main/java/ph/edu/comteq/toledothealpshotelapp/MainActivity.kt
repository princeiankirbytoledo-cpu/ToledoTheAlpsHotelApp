package ph.edu.comteq.toledothealpshotelapp

import android.R
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import ph.edu.comteq.toledothealpshotelapp.ui.theme.ToledoTheAlpsHotelAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToledoTheAlpsHotelAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Homepage(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Homepage(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var hotels by remember { mutableStateOf(emptyList<hotel>()) }

    //load json data
    LaunchedEffect(unit) {
        val json = context.assets.open("hotels.json")
            .bufferedReader()
            .use { it.readText() }
        val gson = Gson()
        val hotelsArray = gson.fromJson(json, Array<Hotel>::class.java)
        hotels = hotelsArray.toList()
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        //Top Section

        Row(
            modifier = modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //title
            Text(
                text = "The Alps Hotel",
                fontWeight = FontWeight.Bold,
                modifier = modifier
            )

            spacer(modifier = modifier.width(9.dp))
        }
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToledoTheAlpsHotelAppTheme {
        Homepage("Android")
    }
}