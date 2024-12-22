package com.example.newsupdates

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailScreen(article: Article,navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("News Updates ")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.light_blue) // Set your R.color value here
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.back), // Replace with your back icon
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            article.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp) // Add some space between title and image
        )

        // Image
        article.urlToImage?.let {
            Image(
                painter = rememberAsyncImagePainter(model = it),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth() // Make the image fill the width of its container
                    .height(200.dp) // Set a fixed height to make it rectangular
                    .clip(RoundedCornerShape(20.dp)) // Optional: Add rounded corners
                    .padding(8.dp), // Optional: Add padding around the image
                contentScale = ContentScale.Crop
            )
        }

        // Author and Published Date
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp) // Add space between the image and content
        ) {
            article.author?.let { Text(text = "By $it") }
            article.publishedAt?.let { Text(text = "Published on $it") }
        }

        article.description?.let { Text(
            text= it,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold) }
        // Content Section: Making sure it's scrollable and displays all text
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            article.content?.let {
                Text(
                    text = it, // Display full content
                    maxLines = Int.MAX_VALUE, // Allow unlimited lines
                    overflow = TextOverflow.Visible, // Prevent truncation
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            } ?: run {
                // In case the content is null, display a placeholder text
                Text(text = "No content available", modifier = Modifier.padding(bottom = 8.dp))
            }
            Box (
                modifier = Modifier.clickable {
                   // navController.navigate("entireArticle")
                    val articleJson = Uri.encode(Json.encodeToString(article))
                    navController.navigate("entireArticle/$articleJson")
                }
            ){
                Text(
                    text="To read entire article click here ..!",
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
    }
}

