package com.rohan.datingapp.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.R
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.databinding.ItemFindLayoutBinding
import com.rohan.datingapp.databinding.ItemUserLayoutBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.utils.Config

class DatingAdapter(val context: Context, val listener: DatingAdapterInterface)
    : RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {

    private val list = ArrayList<UserModel>()
    private val music = arrayOf(
        "Pop","Rock","Country","Folk","Jazz",
        "Classical","Punk", "Disco","World","Soul","Acoustic"
    )

    private val movies = arrayOf(
        "Drama","Comedy","Documentary","Action","Horror",
        "Sci-fi","Romance","Adventure", "Anime","Fantasy","Thriller"
    )

    private val hobbies = arrayOf(
        "Cooking","Video games","Yoga","Fashion","Acting","Writing",
        "Singing","Pets","Magic","Camping","Running"
    )

    private val sports = arrayOf(
        "Football","Basketball","Soccer","Baseball","Tennis","Swimming","Weight lifting",
        "Cycling","Esports","Dance","Racing"
    )

    private val communities = arrayOf(
        "Vegan","LGBTQIA+","Partying","Greek life","University","High school","Foodie"
    )


    inner class DatingViewHolder(val binding: ItemUserLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {
        return DatingViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context) , parent , false))
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val currentItem = list[position]

        holder.binding.name.text = "${currentItem.name.toString().trim()}, ${currentItem.age.toString().trim()}"

        if(currentItem.city?.isNotEmpty() == true){
            holder.binding.country.text = "${currentItem.city}"
        }else{
            holder.binding.country.text = "United States"
        }

        if(currentItem.bio?.isNotEmpty() == true){
            holder.binding.bioShow.visibility = View.VISIBLE
            holder.binding.bio.visibility = View.VISIBLE
            holder.binding.bio.text = currentItem.bio
        }

        if(currentItem.height?.isNotEmpty() == true){
            holder.binding.height.visibility = View.VISIBLE
            holder.binding.heightTxt.text = "${currentItem.height}cm"
        }

        if(currentItem.exercise?.isNotEmpty() == true){
            holder.binding.exercise.visibility = View.VISIBLE
            holder.binding.exerciseTxt.text = currentItem.exercise
        }

        if(currentItem.education?.isNotEmpty() == true){
            holder.binding.education.visibility = View.VISIBLE
            holder.binding.educationTxt.text = currentItem.education
        }

        if(currentItem.gender?.isNotEmpty() == true){
            holder.binding.gender.visibility = View.VISIBLE
            holder.binding.genderTxt.text = currentItem.gender
        }

        if(currentItem.star?.isNotEmpty() == true){
            holder.binding.star.visibility = View.VISIBLE
            holder.binding.starTxt.text = currentItem.star
        }

        if(currentItem.image1?.isNotEmpty() == true){
            //holder.binding.imgCard1.visibility = View.VISIBLE
            holder.binding.image1.visibility = View.VISIBLE
            Glide.with(context).load(currentItem.image1).into(holder.binding.image1)
        }

        if(currentItem.image2?.isNotEmpty() == true){
            //holder.binding.imgCard2.visibility = View.VISIBLE
            holder.binding.image2.visibility = View.VISIBLE
            Glide.with(context).load(currentItem.image2).into(holder.binding.image2)
        }

        if(currentItem.image3?.isNotEmpty() == true){
            //holder.binding.imgCard3.visibility = View.VISIBLE
            holder.binding.image3.visibility = View.VISIBLE
            Glide.with(context).load(currentItem.image3).into(holder.binding.image3)
        }


        if (currentItem.interests != null){
            holder.binding.interestText.visibility = View.VISIBLE
            if(currentItem.interests!!.size >= 1){
                val item = currentItem.interests!![0]
                holder.binding.interest1.visibility = View.VISIBLE
                holder.binding.interest1Txt.text = item

                if(music.contains(item)){
                    holder.binding.interest1Image.setImageDrawable(context.getDrawable(R.drawable.music))
                }else if(movies.contains(item)){
                    holder.binding.interest1Image.setImageDrawable(context.getDrawable(R.drawable.movies))
                }else if(hobbies.contains(item)){
                    holder.binding.interest1Image.setImageDrawable(context.getDrawable(R.drawable.hobbies))
                }else if(sports.contains(item)){
                    holder.binding.interest1Image.setImageDrawable(context.getDrawable(R.drawable.sports))
                }else if(communities.contains(item)){
                    holder.binding.interest1Image.setImageDrawable(context.getDrawable(R.drawable.communities))
                }
            }
            if(currentItem.interests!!.size >= 2){
                val item = currentItem.interests!![1]
                holder.binding.interest2.visibility = View.VISIBLE
                holder.binding.interest2Txt.text = item

                if(music.contains(item)){
                    holder.binding.interest2Image.setImageDrawable(context.getDrawable(R.drawable.music))
                }else if(movies.contains(item)){
                    holder.binding.interest2Image.setImageDrawable(context.getDrawable(R.drawable.movies))
                }else if(hobbies.contains(item)){
                    holder.binding.interest2Image.setImageDrawable(context.getDrawable(R.drawable.hobbies))
                }else if(sports.contains(item)){
                    holder.binding.interest2Image.setImageDrawable(context.getDrawable(R.drawable.sports))
                }else if(communities.contains(item)){
                    holder.binding.interest2Image.setImageDrawable(context.getDrawable(R.drawable.communities))
                }
            }
            if(currentItem.interests!!.size >= 3){
                val item = currentItem.interests!![2]
                holder.binding.interest3.visibility = View.VISIBLE
                holder.binding.interest3Txt.text = item

                if(music.contains(item)){
                    holder.binding.interest3Image.setImageDrawable(context.getDrawable(R.drawable.music))
                }else if(movies.contains(item)){
                    holder.binding.interest3Image.setImageDrawable(context.getDrawable(R.drawable.movies))
                }else if(hobbies.contains(item)){
                    holder.binding.interest3Image.setImageDrawable(context.getDrawable(R.drawable.hobbies))
                }else if(sports.contains(item)){
                    holder.binding.interest3Image.setImageDrawable(context.getDrawable(R.drawable.sports))
                }else if(communities.contains(item)){
                    holder.binding.interest3Image.setImageDrawable(context.getDrawable(R.drawable.communities))
                }
            }
            if(currentItem.interests!!.size >= 4){
                val item = currentItem.interests!![3]
                holder.binding.interest4.visibility = View.VISIBLE
                holder.binding.interest4Txt.text = item

                if(music.contains(item)){
                    holder.binding.interest4Image.setImageDrawable(context.getDrawable(R.drawable.music))
                }else if(movies.contains(item)){
                    holder.binding.interest4Image.setImageDrawable(context.getDrawable(R.drawable.movies))
                }else if(hobbies.contains(item)){
                    holder.binding.interest4Image.setImageDrawable(context.getDrawable(R.drawable.hobbies))
                }else if(sports.contains(item)){
                    holder.binding.interest4Image.setImageDrawable(context.getDrawable(R.drawable.sports))
                }else if(communities.contains(item)){
                    holder.binding.interest4Image.setImageDrawable(context.getDrawable(R.drawable.communities))
                }
            }
            if(currentItem.interests!!.size >= 5){
                val item = currentItem.interests!![4]
                holder.binding.interest5.visibility = View.VISIBLE
                holder.binding.interest5Txt.text = item

                if(music.contains(item)){
                    holder.binding.interest5Image.setImageDrawable(context.getDrawable(R.drawable.music))
                }else if(movies.contains(item)){
                    holder.binding.interest5Image.setImageDrawable(context.getDrawable(R.drawable.movies))
                }else if(hobbies.contains(item)){
                    holder.binding.interest5Image.setImageDrawable(context.getDrawable(R.drawable.hobbies))
                }else if(sports.contains(item)){
                    holder.binding.interest5Image.setImageDrawable(context.getDrawable(R.drawable.sports))
                }else if(communities.contains(item)){
                    holder.binding.interest5Image.setImageDrawable(context.getDrawable(R.drawable.communities))
                }
            }
            if(currentItem.interests!!.size >= 6){
                val item = currentItem.interests!![5]
                holder.binding.interest6.visibility = View.VISIBLE
                holder.binding.interest6Txt.text = item

                if(music.contains(item)){
                    holder.binding.interest6Image.setImageDrawable(context.getDrawable(R.drawable.music))
                }else if(movies.contains(item)){
                    holder.binding.interest6Image.setImageDrawable(context.getDrawable(R.drawable.movies))
                }else if(hobbies.contains(item)){
                    holder.binding.interest6Image.setImageDrawable(context.getDrawable(R.drawable.hobbies))
                }else if(sports.contains(item)){
                    holder.binding.interest6Image.setImageDrawable(context.getDrawable(R.drawable.sports))
                }else if(communities.contains(item)){
                    holder.binding.interest6Image.setImageDrawable(context.getDrawable(R.drawable.communities))
                }
            }
        }

        Glide.with(context).load(currentItem.image).into(holder.binding.userImage)

        holder.binding.reportUser.setOnClickListener {
            showDialog(currentItem , position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList:ArrayList<UserModel>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showDialog(user: UserModel, position: Int) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_layout)

        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val reason1 = dialog.findViewById<CardView>(R.id.reason1)
        val reason2 = dialog.findViewById<CardView>(R.id.reason2)
        val reason3 = dialog.findViewById<CardView>(R.id.reason3)
        val reason4 = dialog.findViewById<CardView>(R.id.reason4)
        val reason5 = dialog.findViewById<CardView>(R.id.reason5)
        val reportBtn = dialog.findViewById<CardView>(R.id.reportBtn)

        val userDao = UserDao()
        var check = 0

        reason1.setOnClickListener {
            check = 1
            reportBtn.setCardBackgroundColor(context.resources.getColor(R.color.pinkish))
            reason1.setCardBackgroundColor(context.resources.getColor(R.color.colorPrimary))
            reason2.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason3.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason4.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason5.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
        }

        reason2.setOnClickListener {
            check = 1
            reportBtn.setCardBackgroundColor(context.resources.getColor(R.color.pinkish))
            reason2.setCardBackgroundColor(context.resources.getColor(R.color.colorPrimary))
            reason1.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason3.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason4.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason5.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
        }

        reason3.setOnClickListener {
            check = 1
            reportBtn.setCardBackgroundColor(context.resources.getColor(R.color.pinkish))
            reason3.setCardBackgroundColor(context.resources.getColor(R.color.colorPrimary))
            reason2.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason1.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason4.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason5.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
        }

        reason4.setOnClickListener {
            check = 1
            reportBtn.setCardBackgroundColor(context.resources.getColor(R.color.pinkish))
            reason4.setCardBackgroundColor(context.resources.getColor(R.color.colorPrimary))
            reason2.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason3.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason1.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason5.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
        }

        reason5.setOnClickListener {
            check = 1
            reportBtn.setCardBackgroundColor(context.resources.getColor(R.color.pinkish))
            reason5.setCardBackgroundColor(context.resources.getColor(R.color.colorPrimary))
            reason2.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason3.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason4.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
            reason1.setCardBackgroundColor(context.resources.getColor(R.color.light_blue_2))
        }

        reportBtn.setOnClickListener {
            if(check == 0){
                Toast.makeText(context , "Please select one reason", Toast.LENGTH_SHORT).show()
            }
            else{
                Config.showDialog(context)
                userDao.updateBlockedUsers(FirebaseAuth.getInstance().currentUser!!.uid ,
                    user.uid.toString()
                )
                dialog.hide()
                list.remove(user)
                notifyItemRemoved(position)
                Config.hideDialog()
            }
        }

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }


}

interface DatingAdapterInterface{
    fun add()
    fun remove()
}