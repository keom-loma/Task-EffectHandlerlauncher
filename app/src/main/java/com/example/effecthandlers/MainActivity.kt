package com.example.effecthandlers
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.effecthandlers.model.UserModelItem
import com.example.effecthandlers.ui.theme.EffectHandlersTheme
import com.example.effecthandlers.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		setContent {
			val viewModel: UserViewModel = hiltViewModel()

			EffectHandlersTheme {
				Scaffold(
					modifier = Modifier
						.fillMaxSize()
						.padding(top = 10.dp)
						.background(color = Color.Cyan), topBar = {
						TopAppBar(title = {
							Text(text = "Effect Handlers")
						})
					}) {
					LaunchedEffectExample(viewModel = viewModel)
				}
			}
		}
	}
}

// Make a fetch data function
suspend fun fetchData(): String {
	delay(1000) // Simulate network delay
	return "Data fetched from network"
}

// Example of LaunchedEffect
@Composable
fun LaunchedEffectExample(viewModel: UserViewModel) {
	LaunchedEffect(key1 = Unit) {
		viewModel.getUserPost()
	}
   val postState = viewModel.resData.collectAsState()
	if(postState.value.isEmpty()) {
		Column(modifier = Modifier.fillMaxSize()) {
			CircularProgressIndicator()
		}
	}else{
		PostList(posts = postState.value)
	}
}

// Example of SideEffect
@Composable
fun MySideEffect(count: Int) {
	SideEffect {
		Log.d("CounterExample", "Counter value: $count")
	}
	Text("Number: $count")
}

// Example of rememberCoroutineScope
@Composable
fun MyRememberCoroutineScope() {
	var text by remember { mutableStateOf("Click the button to start coroutine") }
	val scope = rememberCoroutineScope()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(text = text, style = MaterialTheme.typography.bodyMedium)
		Spacer(modifier = Modifier.height(16.dp))
		Button(onClick = {
			scope.launch {
				text = "Coroutine started..."
				delay(2000) // Simulate a long-running task
				text = "Coroutine completed!"
			}
		}) {
			Text(text = "Start Coroutine")
		}
	}
}

// Example of DisposableEffect
@Composable
fun MyDisposableEffect() {
	var timerValue by remember { mutableIntStateOf(0) }
	val coroutineScope = rememberCoroutineScope()
	DisposableEffect(Unit) {
		val job = coroutineScope.launch {
			while (isActive) {
				delay(1000)
				timerValue++
			}
		}
		onDispose {
			job.cancel()
		}
	}
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(text = "Timer: $timerValue", style = MaterialTheme.typography.bodyMedium)
	}
}

// Example of ProduceState
@Composable
fun MyProduceState(id: Int) {
	val data = produceState(initialValue = "Loading...", id) {
		// Producer coroutine
		value = "fetched data"
	}
	Text("Data: ${data.value}")
}

@Composable
fun CounterSideEffect() {
	var counter by remember { mutableIntStateOf(0) }
	MySideEffect(count = counter)

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(text = "Counter: $counter", style = MaterialTheme.typography.bodyMedium)
		Spacer(modifier = Modifier.height(16.dp))
		Row {
			Button(onClick = { counter++ }) {
				Text(text = "Increment")
			}
			Spacer(modifier = Modifier.width(8.dp))
			Button(onClick = { counter-- }) {
				Text(text = "Decrement")
			}
		}
	}
}
@Composable
fun PostList(posts: List<UserModelItem>) {
	LazyColumn(modifier = Modifier.padding(top = 100.dp)) {
		items(posts) {
			Card(modifier = Modifier
				.height(50.dp)
				.fillMaxWidth()
				.padding(top = 10.dp, start = 10.dp, end = 10.dp)) {
				Text(text = it.title, modifier = Modifier.padding(10.dp))
			}
		}
	}
}

@Composable
fun MyAppHandlerLauncher() {
	var data by remember { mutableStateOf("First initial") }
	var shouldFetchData by remember { mutableStateOf(false) }

	var showToast by remember { mutableStateOf(false) }

	val context = LocalContext.current

	if(showToast){
		LaunchedEffect(key1 = Unit) {
			Toast.makeText(context, "Button clicked!", Toast.LENGTH_SHORT).show()
			showToast = false
		}
	}
	if(shouldFetchData){
		LaunchedEffect(key1 = Unit) {
			data = fetchData()
			shouldFetchData = false
		}
	}


	Column(modifier = Modifier.padding(100.dp)) {
		Text(text = data)
		Spacer(modifier = Modifier.height(16.dp))
		Button(onClick = { data = "New data" }) {
			Text("Fetch Data")
			//LaunchedEffectExample(viewModel = viewModel[0])
		}
	}
}

@Composable
fun ErrorDisplay(exception: Exception) {
	Text(text = "Error: ${exception.message}", color = Color.Red)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier.padding(top = 150.dp)) {
	Text(
		text = "Hello $name!",
		modifier = modifier
	)
	MyApp()
}

@Preview(showBackground = false)
@Composable
fun GreetingPreview() {
	EffectHandlersTheme {
		Greeting("Android")
	}
}