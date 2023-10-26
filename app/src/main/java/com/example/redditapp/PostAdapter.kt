package com.example.redditapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
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
            if (!post.data.thumbnail.endsWith(".jpg")) {
                binding.image.visibility = View.GONE
            } else {
                binding.image.visibility = View.VISIBLE
                Glide.with(binding.root)
                    .load(post.data.thumbnail)
                    .into(binding.image)
            }

            val url = post.data.url
            val context = binding.image.context
            binding.image.setOnClickListener {
                if (!url.endsWith(".jpg")) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                } else {
                    val intent = Intent(context, FullScreenImageActivity::class.java)
                    intent.putExtra("imgUrl", post.data.url)
                    intent.putExtra("imgTitle", post.data.title)
                    context.startActivity(intent)
                }
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
