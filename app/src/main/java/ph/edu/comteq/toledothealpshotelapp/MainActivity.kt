package ph.edu.comteq.toledothealpshotelapp

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import ph.edu.comteq.toledothealpshotelapp.ui.theme.ToledoTheAlpsHotelAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ToledoTheAlpsHotelAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Homepage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Homepage(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val loadedInitialHotels = remember {
        try {
            val json = context.assets
                .open("hotels.json")
                .bufferedReader()
                .use { it.readText() }

            val gson = Gson()
            val hotelsArray = gson.fromJson(
                json,
                Array<Hotel>::class.java
            )
            hotelsArray?.toList() ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    // Stores all hotels loaded from hotels.json
    var hotels by remember {
        mutableStateOf(loadedInitialHotels)
    }

    // Stores the text entered into the search bar
    var searchText by remember {
        mutableStateOf("")
    }

    // Load hotels.json
    LaunchedEffect(Unit) {

        val json = context.assets
            .open("hotels.json")
            .bufferedReader()
            .use { it.readText() }

        val gson = Gson()

        val hotelsArray = gson.fromJson(
            json,
            Array<Hotel>::class.java
        )

        hotels = hotelsArray.toList()
    }

    // Filter hotels using the search text
    val filteredHotels = hotels.filter { hotel ->

        hotel.hotel_name.contains(
            searchText,
            ignoreCase = true
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Header

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // The Alps Hotels
            Text(
                text = "The Alps Hotels",
                fontSize = 24.sp
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            // France Flag
            Image(
                painter = painterResource(
                    id = R.drawable.france_national_flag
                ),
                contentDescription = "France Flag",
                modifier = Modifier.size(32.dp)
            )

            // Push profile icon to the right
            Spacer(
                modifier = Modifier.weight(1f)
            )

            // Profile Icon
            Image(
                painter = painterResource(
                    id = R.drawable.person
                ),
                contentDescription = "Profile",
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // Search Bar

        OutlinedTextField(
            value = searchText,

            onValueChange = {
                searchText = it
            },

            modifier = Modifier.fillMaxWidth(),

            placeholder = {
                Text(
                    text = "Search hotels..."
                )
            },

            shape = RoundedCornerShape(12.dp),

            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // Hotel List

        LazyColumn(
            modifier = Modifier.fillMaxSize(),

            verticalArrangement = Arrangement.spacedBy(
                12.dp
            )
        ) {

            items(filteredHotels) { hotel ->

                HotelItem(
                    hotel = hotel
                )
            }
        }
    }
}

@Composable
fun HotelItem(
    hotel: Hotel
) {

    val context = LocalContext.current

    // Load the hotel image from assets

    val imageBitmap = remember(
        hotel.hotel_cover_image
    ) {

        try {

            BitmapFactory
                .decodeStream(
                    context.assets.open(
                        hotel.hotel_cover_image
                    )
                )

        } catch (_: Exception) {

            null
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(
            12.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            // Hotel Image

            if (imageBitmap != null) {

                Image(
                    bitmap = imageBitmap.asImageBitmap(),

                    contentDescription = hotel.hotel_name,

                    modifier = Modifier
                        .size(120.dp)
                        .clip(
                            RoundedCornerShape(10.dp)
                        ),

                    contentScale = ContentScale.Crop
                )
            }

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            // Hotel Information

            Column(
                modifier = Modifier.weight(1f)
            ) {

                // Hotel Name
                Text(
                    text = hotel.hotel_name,
                    fontSize = 18.sp
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                // Hotel Rating
                Text(
                    text = "Rating: ${hotel.hotel_rating}"
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                // Distance to ski area
                Text(
                    text = "Ski distance: " +
                            "${hotel.hotel_to_ski_distance} km"
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GreetingPreview() {

    ToledoTheAlpsHotelAppTheme {

        Homepage()
    }
}
