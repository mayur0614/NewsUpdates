package com.example.newsupdates

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable

fun NewsScreen(viewModel: NewsViewModel, apiKey: String, navController: NavController) {
    val articles = viewModel.newsState.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)


    val scrollState = rememberLazyListState()

    val isLoading by viewModel.isLoading
    val categories = listOf(
        "GENERAL", "BUSINESS", "ENTERTAINMENT", "HEALTH", "SCIENCE", "SPORTS"
    )
    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(title = {
            androidx.compose.material3.Text("News Updates ")
        },
            actions ={
                Row(){
                    IconButton(
                        onClick = {
                            navController.navigate("login")
                        }
                    ) {
                        Icon(Icons.Default.ExitToApp,"Logout")
                    }
                    IconButton(
                            onClick = {
                                navController.navigate("saved")
                            }
                            ) {
                        Icon(Icons.Default.Favorite,"Logout")
                    }
                }


            }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.light_blue) // Set your R.color value here
        ), navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
        }

        )
    }, drawerContent = {
        DrawerContent(categories, scope, scaffoldState, viewModel) { selectedCategory ->



                viewModel.fetchNewsByCategory(Constant.apiKey,selectedCategory)


        }
    }, content = {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            SearchBarFun(viewModel)
            if(isLoading){
                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CircularProgressIndicator(color = Color.Black)
                }

            }else{
                Box(modifier =  Modifier.fillMaxSize()) {


                    LazyColumn(
                        state = scrollState,
                        modifier = Modifier.fillMaxSize()) {
                        items(articles.value.size) { index ->
                            NewsItem(article = articles.value[index], navController)
                        }
                    }
                    val coroutineScope = rememberCoroutineScope()
                    InteractiveVerticalScrollbar(
                        scrollState = scrollState,
                        itemCount = articles.value.size,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp),
                        onScrollToItem = { targetIndex ->
                            coroutineScope.launch {
                                scrollState.animateScrollToItem(targetIndex)
                            }
                        }
                    )

                }
            }
        }
    }

    )
    // Trigger data fetch
   viewModel.fetchNews(Constant.apiKey)
   // viewModel.fetchEverythingWithQuery("india")
}


@Composable
fun InteractiveVerticalScrollbar(
    scrollState: LazyListState,
    itemCount: Int,
    modifier: Modifier = Modifier,
    width: Dp = 6.dp,
    color: Color = Color.Gray,
    onScrollToItem: (Int) -> Unit
) {
    // Calculate scrollbar height and offset dynamically
    val totalItems = scrollState.layoutInfo.totalItemsCount
    val visibleItems = scrollState.layoutInfo.visibleItemsInfo.size
    val proportion = visibleItems.toFloat() / totalItems

    val scrollbarHeight = proportion * 300.dp // Adjust height scaling
    val scrollbarOffset = (scrollState.firstVisibleItemIndex.toFloat() / totalItems * 300).dp // Adjust offset scaling

    Box(
        modifier = modifier
            .width(width)
            .fillMaxHeight()
            .background(Color.LightGray.copy(alpha = 0.5f))
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    change.consume() // Consume the touch event
                    val newIndex = (scrollState.firstVisibleItemIndex + dragAmount / 300 * totalItems).toInt()
                        .coerceIn(0, itemCount - 1) // Keep the index within bounds
                    onScrollToItem(newIndex)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(scrollbarHeight)
                .offset(y = scrollbarOffset)
                .background(color)
        )
    }
}


@Composable
fun DrawerContent(
    categories: List<String>,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    viewModel: NewsViewModel,
    onCategorySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_blue))
            .padding(16.dp)
    ) {
        DrawerHeader()
        categories.forEach { category ->
            DrawerItem(title = category, onClick = {
                viewModel.fetchNewsByCategory(Constant.apiKey, category)
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            })
        }
        Spacer(
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "Developed By mayur.it22028@sstcollege.edu.in",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DrawerHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.app_logo), contentDescription = null
            )
//            Text(
//                text = "N",
//                color = Color.White,
//                fontSize = 32.sp,
//                fontWeight = FontWeight.Bold
//            )
            // Replace this with a logo if available
        }
Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "News App",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Your daily news hub",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth() // Width of the spacer
            .height(2.dp) // Height of the spacer
            .background(Color.White) // Background color
    )
    }
@Composable
fun DrawerItem(
    title: String, onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(onClick = onClick)
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title, color = Color.White, fontSize = 16.sp
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsItem(article: Article, navController: NavController) {
    //val context = LocalContext.current
    if (article.description != "[Removed]") {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                //navController.navigate("detail")
                val articleJson = Uri.encode(Json.encodeToString(article))
                navController.navigate("detail/$articleJson")
                // Toast.makeText(context,"Clicked ${article.author} ",Toast.LENGTH_SHORT).show()
            }, elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

                if (!article.urlToImage.isNullOrEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(model = article.urlToImage),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(end = 8.dp)
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.notfound),
                        contentDescription = "image not found",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(end = 8.dp)
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp)
                ) {
                    article.description?.let { Log.i("removed ", it) }

                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.h6,
                        color = Color.Black,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = article.author ?: "",
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarFun(newsViewModel: NewsViewModel) {
    val searchedList = remember { mutableStateListOf<String>() }
    var searched by remember { mutableStateOf("") }
    Column (
        modifier = Modifier.fillMaxWidth()
    ){

        var showHistory by remember { mutableStateOf(false) }
        var act by remember { mutableStateOf(false) }
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = searched,
            placeholder = {
                Text("Search here ")
            },
            onQueryChange = {
                searched = it
                showHistory = true
            },
            onSearch = {
                searched = it
            },
            active = false,
            onActiveChange = {
                act = !act
            },
            leadingIcon = {
                IconButton(
                    onClick = {
                        if(searched.isNotEmpty()){
                            if(!searchedList.contains(searched)){
                                searchedList.add(searched)
                            }
                            newsViewModel.fetchEverythingWithQuery(searched)
                            searched = ""
                            showHistory=false
                        }

                    }
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "search icon ")
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    searched=""
                    showHistory = false
                }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear icon ")
                }
            },
        ) {

        }
        Spacer(modifier = Modifier.height(16.dp))
        if(showHistory) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(searchedList.reversed()) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "history"
                        )
                        androidx.compose.material3.Text(
                            text = item,
                            fontSize = 20.sp,
                            modifier = Modifier.weight(1f).padding(8.dp).clickable {
                                searched = item
                                newsViewModel.fetchEverythingWithQuery(searched)
                                searched = ""
                                showHistory=false
                            },
                        )
                        // Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                searchedList.remove(item)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "clear"
                            )

                        }
                    }

                }
            }
        }
    }

}