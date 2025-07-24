package com.example.elearningplatform.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.elearningplatform.data.model.Course
import com.example.elearningplatform.databinding.ItemCourseBinding

class CourseAdapter(
    private val onClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.VH>() {

    private val items = mutableListOf<Course>()

    fun submit(list: List<Course>) {
        items.clear(); items.addAll(list); notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class VH(private val b: ItemCourseBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(c: Course) {
            b.title.text = c.title
            b.instructor.text = c.instructor
            Glide.with(b.thumbnail).load(c.thumbnail).into(b.thumbnail)
            b.root.setOnClickListener { onClick(c) }
        }
    }
}
