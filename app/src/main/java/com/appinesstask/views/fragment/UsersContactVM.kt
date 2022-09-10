package com.appinesstask.views.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appinesstask.R
import com.appinesstask.views.adapters.RecyclerAdapter
import com.appinesstask.views.extensions.showToast
import com.appinesstask.views.models.ContactRes
import com.appinesstask.views.models.Heirarchy
import com.appinesstask.views.networkcalls.ApiEnums
import com.appinesstask.views.networkcalls.ApiProcessor
import com.appinesstask.views.networkcalls.Repository
import com.appinesstask.views.networkcalls.RetrofitApi
import com.appinesstask.views.utilities.openDialPad
import com.appinesstask.views.utilities.sendMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UsersContactVM @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val contactAdapter by lazy { RecyclerAdapter<Heirarchy>(R.layout.item_user_contact) }
    val originalList = ArrayList<Heirarchy>()

    // API
    fun getMyList(view: View?) = viewModelScope.launch {
        kotlin.runCatching {
            repository.makeCall(
                apiKey = ApiEnums.GET_MY_LIST,
                loader = true,
                view = view!!,
                requestProcessor = object : ApiProcessor<Response<ContactRes>> {
                    override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<ContactRes> {
                        return retrofitApi.getMyListData()
                    }

                    override fun onResponse(res: Response<ContactRes>) {
                        res.body()?.let { body ->
                            view.context?.showToast(
                                message = body.message
                                    ?: view.context.getString(R.string.data_fetched_sucessfully)
                            )
                            try {
                                val finalList = body.dataObject?.flatMap {
                                    it.myHierarchy?.flatMap {
                                        it.heirarchyList ?: emptyList()
                                    } ?: emptyList()
                                }
                                contactAdapter.addItems(finalList ?: emptyList())
                                originalList.clear()
                                originalList.addAll(finalList ?: emptyList())
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            )
        }
    }

    val textChangeListeners = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (query?.isDigitsOnly() == true) {
                contactAdapter.addItems(originalList.filter {
                    it.contactNumber?.startsWith(
                        query, ignoreCase = true
                    ) == true
                })
            } else {
                contactAdapter.addItems(originalList.filter {
                    it.contactName?.startsWith(
                        query ?: "", ignoreCase = true
                    ) == true
                })
            }
        }

        override fun afterTextChanged(p0: Editable?) = Unit

    }

    private val onRecyclerItemClick = RecyclerAdapter.OnItemClick { view, position ->
        val hierarchy = contactAdapter.getItemAt(position)
        when (view.id) {
            R.id.ivContact -> {
                hierarchy.contactNumber?.let { view.context.openDialPad(it) }
            }
            R.id.ivMessage -> {
                hierarchy.contactNumber?.let { view.context.sendMessage(it) }
            }
        }
    }

    init {
        contactAdapter.setOnItemClick(onRecyclerItemClick)
    }

}