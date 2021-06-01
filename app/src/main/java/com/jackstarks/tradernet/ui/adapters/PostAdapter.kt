package com.jackstarks.tradernet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jackstarks.tradernet.R
import com.jackstarks.tradernet.databinding.ItemPostBinding
import com.jackstarks.tradernet.model.Ticker
import com.jackstarks.tradernet.ui.fragments.SplashViewModel


class PostAdapter(
    private val postArrayList: MutableSet<Ticker>,
    private val viewModel: SplashViewModel
) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val binding: ItemPostBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_post, parent, false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        position: Int
    ) {
        val post = postArrayList.elementAt(position)
        viewHolder.binding.viewModel = viewModel
        viewHolder.binding.post = post

        viewHolder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return postArrayList.size
    }

    inner class ViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root)


}

/*@BindingAdapter("app:customSize")
fun setLayoutHeight(view: View, height: Float) {
    view.layoutParams = view.layoutParams.apply {
        this.height = height.toInt()
        this.width = height.toInt()
    }
}*/