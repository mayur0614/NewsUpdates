package com.example.newsupdates


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class NewsViewModel : ViewModel() {
    private val _newsState = MutableStateFlow<List<Article>>(emptyList())
    val newsState: StateFlow<List<Article>> = _newsState

    fun fetchNews(apiKey: String) {
        viewModelScope.launch {
            try {
                //val response =  RetrofitInstance.api.getEverythingPopular("popularity",apiKey)
                val response = RetrofitInstance.api.getTopHeadlines("","general","us", apiKey)
                _newsState.value = response.articles
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun fetchNewsByCategory(apiKey: String, category: String) {

        viewModelScope.launch {
            try {

                val response = RetrofitInstance.api.getTopHeadlines("",category,"us", Constant.apiKey)
                _newsState.value = response.articles // Update the state with the fetched articles
            } catch (e: Exception) {
                e.printStackTrace() // Handle exceptions, such as network errors
            }
        }
    }
    val isLoading = mutableStateOf(false)

    fun fetchEverythingWithQuery(searchQuery :String){
       isLoading.value=true
        viewModelScope.launch {
            try{
                val response = RetrofitInstance.api.getEverything("popularity",searchQuery,Constant.apiKey)
              // val response1 = RetrofitInstance.api.getTopHeadlines(searchQuery,"general","us",Constant.apiKey)
                _newsState.value = response.articles
            }catch(e:Exception){
                e.printStackTrace()
            }finally {
                isLoading.value=false
            }
        }
    }
}
