package com.appinesstask.views.models

data class ContactRes(
    val dataObject: List<DataObject>? = null,
    val message: String? = null,
    val status: Boolean? = null,
    val statusCode: Int? = null
)