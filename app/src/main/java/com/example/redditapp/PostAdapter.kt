package com.example.redditapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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
            val context = binding.image.context
            val url = post.data.url
            val author = post.data.author
            val title = post.data.title
            val selfText = post.data.selftext
            binding.author.text = author
            binding.title.text = title
            binding.comments.text = "${post.data.num_comments.toString()} Comments"
            binding.selftext?.text = selfText

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



            binding.image.setOnClickListener {
                if (!url.endsWith(".jpg") && !url.endsWith(".png")) {
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
    fun setPosts(newPosts: List<Children>) {
        posts = newPosts as MutableList<Children>
        notifyDataSetChanged()
    }
    fun getPosts(): List<Children> {
        return posts
    }
}