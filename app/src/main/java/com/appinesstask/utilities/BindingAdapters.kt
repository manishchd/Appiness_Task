package com.appinesstask.utilities

import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appinesstask.adapters.RecyclerAdapter

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