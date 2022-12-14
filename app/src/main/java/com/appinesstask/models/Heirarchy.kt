package com.appinesstask.models

import com.appinesstask.adapters.AbstractModel
import com.appinesstask.extensions.makeCap

data class Heirarchy(
    val contactName: String? = null,
    val contactNumber: String? = null,
    val designationName: String? = null
) : AbstractModel() {

    fun initContactName() = contactName?.makeCap()
    fun initDesignationName() = designationName?.makeCap()

}