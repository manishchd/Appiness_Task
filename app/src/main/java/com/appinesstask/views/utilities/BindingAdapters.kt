package com.appinesstask.views.utilities

import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appinesstask.views.adapters.RecyclerAdapter

@BindingAdapter("setRecyclerAdapter", requireAll = false)
fun RecyclerView.setRecyclerAdapter(adapter: RecyclerAdapter<*>){
    kotlin.runCatching {
        this.adapter = adapter
    }
}

@BindingAdapter("addTextChangeListener", requireAll = false)
fun EditText.addTextChangeListener(textWatcher: TextWatcher){
    kotlin.runCatching {
        this.addTextChangedListener(textWatcher)
    }
}