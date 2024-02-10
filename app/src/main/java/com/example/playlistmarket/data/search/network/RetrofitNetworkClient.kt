package com.example.playlistmarket.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmarket.data.search.dto.ITunesSearchRequest
import com.example.playlistmarket.data.search.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

    private val iTunesSearchService = retrofit.create(ITunesSearchApi::class.java)
    override fun doRequest(dto: Any): Response {
        if (!isConnected()){
            return Response().apply { result = -1 }
        }
        return if (dto is ITunesSearchRequest) {
            val resp = iTunesSearchService.getTracks(dto.expression).execute()
            val body = resp.body() ?: Response()
            body.apply { result = resp.code() }
        } else {
            Response().apply { result = 400 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}