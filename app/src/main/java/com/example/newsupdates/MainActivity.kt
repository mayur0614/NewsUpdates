package com.example.newsupdates

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.example.newsupdates.roomdb.User
import com.example.newsupdates.roomdb.UserDatabase
import com.example.newsupdates.viewmodel.Repository
import com.example.newsupdates.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

//    private val db by lazy{
//        Room.databaseBuilder(
//            applicationContext,
//            UserDatabase::class.java,
//            name = "user.db"
//        ).build()
//    }
//    private val viewModel by viewModels<UserViewModel> (
//        factoryProducer = {
//            object :ViewModelProvider.Factory{
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    return UserViewModel(Repository(db))as T
//                }
//            }
//        }
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Constant.initialize(this)
        setContent {
            val navController = rememberNavController()
            //val viewModel: NewsViewModel = viewModel()
          //  val apiKey = "0887aa19c1244194900c5aa4f5a7f846"
            MaterialTheme {
                val context = LocalContext.current
                val isConnected = isInternetAvailable(context)


                var userList by remember { mutableStateOf(listOf<User>()) }
                Constant.viewModel.getUser().observe(this){
                    userList = it
                }

                var showDialog by remember { mutableStateOf(false) }

                // If no internet, show the dialog
                if (!isConnected) {
                    showDialog = true
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = {
                            Text(text = "No Internet Connection")
                        },
                        text = {
                            Text(text = "Please check your internet connection and try again.")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = { showDialog = false }
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }
                //SavedArticles(navController)
                AppNavGraph(navController = navController,userList)
                //NewsScreen(viewModel = viewModel, apiKey = apiKey)
            }
        }
    }
}


fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // For Android 9 (API level 28) and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}


