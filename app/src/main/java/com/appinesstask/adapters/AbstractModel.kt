package com.appinesstask.adapters

import kotlinx.parcelize.RawValue

abstract class AbstractModel {

    @Transient
    var vpPosition: Int = -1

    @Transient
    var onItemClick: @RawValue RecyclerAdapter.OnItemClick? = null

}
