package com.appinesstask.views.models

import com.appinesstask.views.adapters.AbstractModel
import com.appinesstask.views.extensions.makeCap

data class Heirarchy(
    val contactName: String? = null,
    val contactNumber: String? = null,
    val designationName: String? = null
) : AbstractModel() {

    fun initContactName() = contactName?.makeCap()
    fun initDesignationName() = designationName?.makeCap()

}