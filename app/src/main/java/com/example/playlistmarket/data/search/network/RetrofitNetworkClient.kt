package com.example.playlistmarket.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmarket.data.search.dto.ITunesSearchRequest
import com.example.playlistmarket.data.search.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val BAD_REQUEST_STATUS_CODE = 400
const val NO_INTERNET = -1
const val OK_REQUEST = 200
const val INTERNAL_SERVER_ERROR = 500
class RetrofitNetworkClient(private val context: Context, private val iTunesSearchApi: ITunesSearchApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()){
            return Response().apply { result = NO_INTERNET }
        }
        return withContext(Dispatchers.IO) {
            if (dto is ITunesSearchRequest) {
                try {
                    val resp = iTunesSearchApi.getTracks(dto.expression)
                    resp.apply { result = OK_REQUEST }
                } catch (e: Throwable){
                    Response().apply { result = INTERNAL_SERVER_ERROR }
                }

            } else {
                Response().apply { result = BAD_REQUEST_STATUS_CODE }
            }
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