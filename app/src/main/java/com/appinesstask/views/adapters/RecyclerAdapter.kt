package com.appinesstask.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.appinesstask.BR

class RecyclerAdapter<T : AbstractModel>(
    @LayoutRes val layoutId: Int,
) : RecyclerView.Adapter<RecyclerAdapter.VH<T>>() {

    private val items = ArrayList<T>()
    private var inflater: LayoutInflater? = null
    private var onItemClick: OnItemClick? = null

    fun getItemAt(position: Int) = items[position]
    fun getAllItems() = items

    fun addItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClick(onItemClickBasic: OnItemClick?) {
        this.onItemClick = onItemClickBasic
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<T> {
        val layoutInflater = inflater ?: LayoutInflater.from(parent.context)

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater,
            layoutId,
            parent,
            false
        )
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH<T>, position: Int) {
        holder.setIsRecyclable(false)
        val model = items[position]
        model.vpPosition = position
        onItemClick?.let { model.onItemClick = it }
        holder.bind(model)
    }

    fun isEmpty(): Boolean = this.items.isEmpty()

    class VH<T : AbstractModel>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: T) {
            binding.setVariable(BR.model, model)
            binding.executePendingBindings()
        }
    }

    fun interface OnItemClick {
        fun onClick(view: View, position: Int)
    }
}
