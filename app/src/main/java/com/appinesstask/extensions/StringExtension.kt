package com.appinesstask.extensions

import java.util.*

fun String.makeCap() =
    this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }