package com.example.redditapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.redditapp.databinding.ItemPostBinding


class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private var posts = mutableListOf<Children>()

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Children) {
            binding.author.text = post.data.author
            binding.title.text = post.data.title
            binding.comments.text = "${post.data.num_comments.toString()} Comments"
            binding.selftext.text = post.data.selftext

            val currentTimestamp = System.currentTimeMillis() / 1000
            val timeDifference = currentTimestamp - post.data.created

            when {
                timeDifference < 60 -> binding.date.text = "$timeDifference seconds ago"
                timeDifference < 3600 -> {
                    val timeAgo = timeDifference / 60
                    binding.date.text = "$timeAgo minutes ago"
                }

                timeDifference < 86400 -> {
                    val timeAgo = timeDifference / 3600
                    binding.date.text = "$timeAgo hours ago"
                }

                else -> {
                    val timeAgo = timeDifference / 86400
                    binding.date.text = "$timeAgo days ago"
                }
            }

            Glide.with(binding.root)
                .load(post.data.thumbnail)
                .into(binding.image)

            binding.image.setOnClickListener {
                val context = binding.image.context
                val intent = Intent(context, FullScreenImageActivity::class.java)
                intent.putExtra("imgUrl", post.data.url)
                intent.putExtra("imgTitle", post.data.title)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun addPosts(newPosts: List<Children>) {
        posts.addAll(newPosts)
        notifyDataSetChanged()
    }
}
