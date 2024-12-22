package com.example.newsupdates
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

@Serializable
data class Article(
    val title: String,
    val description: String?,
    val content: String?,
    val author: String?,
    val publishedAt: String?,
    //val source: Source,
    val url: String,
    val urlToImage: String?
)
data class Source(
    val id: String?,
    val name: String
)

data class NewsResponse(val articles: List<Article>)

interface NewsApiService1 {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("q") searchQuery :String,
        @Query("category") category :String ,
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}

interface NewsApiService {

    @GET("v2/everything")
    suspend fun getEverything(
        @Query("sortBy") sortBy:String,
        @Query("q") searchQuery :String,
        @Query("apiKey") apiKey: String
    ): NewsResponse



    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("q") searchQuery :String,
        @Query("category") category :String ,
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}

object RetrofitInstance {
    private const val BASE_URL = "https://newsapi.org/"

    val api: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
}
