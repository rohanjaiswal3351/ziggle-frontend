package com.rohan.datingapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rohan.datingapp.R
import com.rohan.datingapp.databinding.ShowInterestLayoutBinding

class ShowInterestAdapter(val context: Context)
    : RecyclerView.Adapter<ShowInterestAdapter.ShowInterestViewHolder>()  {

    private val list = ArrayList<String>()

    inner class ShowInterestViewHolder(val binding: ShowInterestLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowInterestViewHolder {
        return ShowInterestViewHolder(ShowInterestLayoutBinding.inflate(LayoutInflater.from(context) , parent , false))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ShowInterestViewHolder, position: Int) {
        val currentItem = list[position]

        holder.binding.text.text = currentItem

        val music = arrayOf(
            "Pop","Rock","Country","Folk","Jazz",
            "Classical","Punk", "Disco","World","Soul","Acoustic"
        )

        val movies = arrayOf(
            "Drama","Comedy","Documentary","Action","Horror",
            "Sci-fi","Romance","Adventure", "Anime","Fantasy","Thriller"
        )

        val hobbies = arrayOf(
            "Cooking","Video games","Yoga","Fashion","Acting","Writing",
            "Singing","Pets","Magic","Camping","Running"
        )

        val sports = arrayOf(
            "Football","Basketball","Soccer","Baseball","Tennis","Swimming","Weight lifting",
            "Cycling","Esports","Dance","Racing"
        )

        val communities = arrayOf(
            "Vegan","LGBTQIA+","Partying","Greek life","University","High school","Foodie"
        )

        if(music.contains(currentItem)){
            holder.binding.image.setImageDrawable(context.getDrawable(R.drawable.music))
        }else if(movies.contains(currentItem)){
            holder.binding.image.setImageDrawable(context.getDrawable(R.drawable.movies))
        }else if(hobbies.contains(currentItem)){
            holder.binding.image.setImageDrawable(context.getDrawable(R.drawable.hobbies))
        }else if(sports.contains(currentItem)){
            holder.binding.image.setImageDrawable(context.getDrawable(R.drawable.sports))
        }else if(communities.contains(currentItem)){
            holder.binding.image.setImageDrawable(context.getDrawable(R.drawable.communities))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList:ArrayList<String>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}