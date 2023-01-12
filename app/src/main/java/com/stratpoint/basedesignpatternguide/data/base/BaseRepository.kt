package com.stratpoint.basedesignpatternguide.data.base

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.stratpoint.basedesignpatternguide.BuildConfig
import retrofit2.HttpException
import java.net.UnknownHostException

abstract class BaseRepository {

    inline fun <T> serviceCall(callFunction: () -> T): Resource<T> = try {
        Resource.success(callFunction())
    } catch (e: Exception) {

        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }

        val exception = ServiceException(
            when (e) {
                is UnknownHostException, is RetrofitBuilder.NoInternetInterceptor.NoInternetException -> {
                    ErrorResponse(
                        exceptionMessage = e.message
                    )
                }
                is HttpException -> {
                    ErrorResponse(
                        e.code(),
                        extractHttpErrorMessage(e)
                    )
                }
                else -> {
                    ErrorResponse(
                        exceptionMessage = e.message
                    )
                }
            }
        )

        Resource.error(
            null,
            exception.error.exceptionMessage ?: ERROR_SOMETHING_WENT_WRONG,
            exception
        )

    }

    fun extractHttpErrorMessage(exception: HttpException) = try {

        val rawErrorBody = exception.response()!!.errorBody()!!.string()

        val type = object : TypeToken<GenericErrorResponse>() {}.type

        val errorResponse: GenericErrorResponse = Gson().fromJson(rawErrorBody, type)

        errorResponse.errors[0]

    } catch (e: Exception) {
        ERROR_SOMETHING_WENT_WRONG
    }

    class ServiceException(val error: ErrorResponse) :
        RuntimeException(error.exceptionMessage)

    data class ErrorResponse(
        var errorCode: Int = ERROR_CODE_GENERIC,
        var exceptionMessage: String? = ERROR_SOMETHING_WENT_WRONG
    )

    //Change structure depending on agreed Error Message Format with BackEnd Team
    data class GenericErrorResponse(
        @SerializedName("errors") var errors: List<String>
    )

    companion object {
        const val ERROR_SOMETHING_WENT_WRONG = "Something went wrong. Please try again"
        const val ERROR_CODE_GENERIC = 417
    }

}