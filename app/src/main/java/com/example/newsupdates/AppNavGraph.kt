package com.example.newsupdates


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsupdates.roomdb.User
import kotlinx.serialization.json.Json


@Composable
fun AppNavGraph(navController: NavHostController,userList: List<User>) {

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onTimeout = {
               // navController.navigate("main") {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true } // Remove splash screen from back stack
                }
            })
        }


        composable(
            route = "detail/{article}",
            arguments = listOf(navArgument("article") { type = NavType.StringType })
        ) { backStackEntry ->
            // Deserialize the JSON string back to an Article object
            val articleJson = backStackEntry.arguments?.getString("article")
            val article = Json.decodeFromString<Article>(articleJson!!)


            DetailScreen(article = article,navController)
        }
        composable("main") {
            //MainScreen() // Your main screen composable
            val viewModel: NewsViewModel = viewModel()
            val apiKey = Constant.apiKey
            NewsScreen(viewModel = viewModel, apiKey = apiKey,navController)
        }

        composable("login",
            ) {
            LoginScreen(navController,userList)
        }

        composable("saved") {
         //   LoginScreen(navController,userList)
            val viewModel: NewsViewModel = viewModel()
            SavedArticles(navController)
        }
        composable(
            route = "entireArticle/{article}",
            arguments = listOf(navArgument("article") { type = NavType.StringType })
        ) { backStackEntry ->
            // Deserialize the JSON string back to an Article object
            val articleJson = backStackEntry.arguments?.getString("article")
            val article = Json.decodeFromString<Article>(articleJson!!)
            //DetailScreen(article = article,navController)
            NewsArticlePage(article=article,navController)
        }
    }
}



//@Composable
//@Preview
//fun PreviewApp() {
//    val navController = rememberNavController()
//    AppNavGraph(navController = navController, userList = )
//}
