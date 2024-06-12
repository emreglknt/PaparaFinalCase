package com.example.recipeapppaparaproject.utils


import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

val gson = Gson()
fun <T> apiFlow(
    call: suspend () -> Response<T>?
): Flow<ApiResult<T>> = flow {
    emit(ApiResult.Loading)
    try {
        val c = call()
        c?.let {
            if (c.isSuccessful) {

                val body = c.body()
                if (body != null) {
                    emit(ApiResult.Success(body))
                } else {
                    emit(ApiResult.Error("An error occurred"))
                }

            } else {
                val errorBody = c.errorBody()?.string()
                val errorResponse = gson.fromJson(errorBody, ApiErrorResponse::class.java)
                Log.d("ApiFlow", "Error: ${errorResponse.error}")
                emit(ApiResult.Error(errorResponse.error))
            }
        } ?: emit(ApiResult.Error("Response is null"))
    } catch(e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        val errorMessage = when (e.code()) {
            429 -> "API request limit reached. Please try again later."
            else -> errorBody ?: "An unknown HTTP error occurred"
        }
        Log.e("ApiFlow", "HTTP Error: ${e.message} - $errorBody", e)
        emit(ApiResult.Error(errorMessage))
    } catch (e: IOException) {
        Log.d("ApiFlow", "Error-IOException: ${e.message}")
        emit(ApiResult.Error(e.message ?: "An error occurred"))
    }  catch (e: Exception) {
        Log.d("ApiFlow", "Error-Exception: ${e.message}")
        emit(ApiResult.Error(e.message ?: "An error occurred"))
    }
}.flowOn(Dispatchers.IO)

sealed class ApiResult<out T> {
    data class Success<out R>(val data: R?) : ApiResult<R>()
    data class Error(val message: String?) : ApiResult<Nothing>()
    data object Loading : ApiResult<Nothing>()
}

data class ApiErrorResponse(
    val error: String
)