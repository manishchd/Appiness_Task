package com.appinesstask.views.networkcalls

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import com.appinesstask.R
import com.appinesstask.views.extensions.showToast
import com.appinesstask.views.utilities.AlertLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val retrofitApi: RetrofitApi
) {
    fun <T> makeCall(
        apiKey: ApiEnums,
        loader: Boolean,
        view: View,
        requestProcessor: ApiProcessor<Response<T>>,
    ) {
        getResponseFromCall(loader, view, requestProcessor)
    }

    private fun <T> getResponseFromCall(
        loader: Boolean,
        view: View,
        requestProcessor: ApiProcessor<Response<T>>
    ) {
        try {

            view.context?.let {
                if (!view.context.isNetworkAvailable()) {
                    view.context.showToast(view.context.getString(R.string.your_device_offline))
                    return
                }
                if (loader) {
                    AlertLoader.showProgress(view.context)
                }
            }

            val dataResponse: Flow<Response<Any>> = flow {
                val response =
                    requestProcessor.sendRequest(retrofitApi) as Response<Any>
                emit(response)
            }.flowOn(Dispatchers.IO)

            CoroutineScope(Dispatchers.Main).launch {
                dataResponse.catch { exception ->
                    exception.printStackTrace()
                    AlertLoader.hideProgress()
                    view.context.showToast(exception.message ?: "")
                }.collect { response ->
                    Log.d("resCodeIs", "====${response.code()}")
                    AlertLoader.hideProgress()
                    when {
                        response.code() in 100..199 -> {
                            /**Informational*/
                            requestProcessor.onError(
                                view.context.resources?.getString(R.string.some_error_occured)
                                    ?: "",
                                response.code()
                            )
                            view.context.showToast(
                                view.context.resources?.getString(R.string.some_error_occured) ?: ""
                            )
                        }
                        response.isSuccessful -> {
                            /**Success*/
                            Log.d("successBody", "====${response.body()}")
                            requestProcessor.onResponse(response as Response<T>)
                        }
                        response.code() in 300..399 -> {
                            /**Redirection*/
                            requestProcessor.onError(
                                view.context.resources?.getString(R.string.some_error_occured)
                                    ?: "",
                                response.code()
                            )
                            view.context.showToast(
                                view.context.resources?.getString(R.string.some_error_occured) ?: ""
                            )
                        }
                        response.code() == 401 -> {
                            /**UnAuthorized*/
                            requestProcessor.onError("unAuthorized", response.code())

                        }
                        response.code() == 404 -> {
                            /**Page Not Found*/
                            requestProcessor.onError(
                                view.context.resources?.getString(R.string.some_error_occured)
                                    ?: "",
                                response.code()
                            )

                        }
                        response.code() in 500..599 -> {
                            /**ServerErrors*/
                            requestProcessor.onError(
                                view.context.resources?.getString(R.string.some_error_occured)
                                    ?: "",
                                response.code()
                            )
                            view.context.showToast(
                                view.context.resources?.getString(R.string.some_error_occured) ?: ""
                            )
                        }
                        else -> {
                            /**ClientErrors*/
                            val res = response.errorBody()!!.string()
                            val jsonObject = JSONObject(res)
                            when {
                                jsonObject.has("message") -> {
                                    Log.e(
                                        "Repository",
                                        "makeCall: ${jsonObject.getString("message")}"
                                    )
                                    requestProcessor.onError(
                                        jsonObject.getString("message"),
                                        response.code()
                                    )

                                    if (!jsonObject.getString("message")
                                            .equals("Data not found", true)
                                    )
                                        view.context.showToast(jsonObject.getString("message"))
                                }
                                jsonObject.has("error") -> {
                                    val message =
                                        jsonObject.getJSONObject("error").getString("text") ?: ""
                                    Log.e("Repository", "makeCall: ${message}")
                                    requestProcessor.onError(
                                        message,
                                        response.code()
                                    )

                                    if (!message.equals("Data not found", true))
                                        view.context.showToast(message)
                                }
                                else -> {
                                    requestProcessor.onError(
                                        view.context.resources?.getString(R.string.some_error_occured)
                                            ?: "", response.code()
                                    )
                                    view.context.showToast(
                                        view.resources?.getString(R.string.some_error_occured)
                                            ?: ""
                                    )
                                }
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Internet Connection Detector */
    private fun Context.isNetworkAvailable(): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network != null)
    }


}
