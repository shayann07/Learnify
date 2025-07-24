package com.example.elearningplatform.ui.course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elearningplatform.data.model.Lesson
import com.example.elearningplatform.databinding.ItemLessonBinding

class LessonAdapter(
    private val onClick: (Lesson) -> Unit
) : RecyclerView.Adapter<LessonAdapter.VH>() {

    private val items = mutableListOf<Lesson>()
    fun submit(list: List<Lesson>) {
        items.apply { clear(); addAll(list) }; notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size

    inner class VH(private val b: ItemLessonBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(l: Lesson) {
            b.title.text = l.title
            b.root.setOnClickListener { onClick(l) }
        }
    }
}
