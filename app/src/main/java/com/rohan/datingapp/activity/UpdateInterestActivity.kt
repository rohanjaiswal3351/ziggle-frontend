package com.rohan.datingapp.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.rohan.datingapp.R
import com.rohan.datingapp.daos.UserDao
import com.rohan.datingapp.databinding.ActivityUpdateInterestBinding
import com.rohan.datingapp.model.UserModel
import com.rohan.datingapp.utils.Config
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class UpdateInterestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateInterestBinding

    private var count: Int = 0
    private lateinit var list: ArrayList<String>

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.colorPrimary)

        loadContent()

        binding.back.setOnClickListener {
            finish()
        }

        binding.pop.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Pop")) {
                binding.pop.setCardBackgroundColor(Color.WHITE)
                binding.popText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Pop")
                setUpCount(0, "Pop")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.pop.setCardBackgroundColor(Color.BLACK)
                    binding.popText.setTextColor(Color.WHITE)

                    val userDao = UserDao()
                    userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 0, "Pop")
                    setUpCount(1,"Pop")
                    Config.hideDialog()
                }
            }
        }
        binding.rock.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Rock")) {
                binding.rock.setCardBackgroundColor(Color.WHITE)
                binding.rockText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Rock")
                setUpCount(0, "Rock")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.rock.setCardBackgroundColor(Color.BLACK)
                    binding.rockText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 0, "Rock")
                    setUpCount(1, "Rock")
                    Config.hideDialog()
                }
            }
        }
        binding.country.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Country")) {
                binding.country.setCardBackgroundColor(Color.WHITE)
                binding.countryText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Country")
                setUpCount(0, "Country")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.country.setCardBackgroundColor(Color.BLACK)
                    binding.countryText.setTextColor(Color.WHITE)

                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Country"
                    )
                    setUpCount(1, "Country")
                    Config.hideDialog()
                }
            }
        }
        binding.folk.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Folk")) {
                binding.folk.setCardBackgroundColor(Color.WHITE)
                binding.folkText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Folk")
                setUpCount(0, "Folk")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.folk.setCardBackgroundColor(Color.BLACK)
                    binding.folkText.setTextColor(Color.WHITE)

                    val userDao = UserDao()
                    userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 0, "Folk")
                    setUpCount(1, "Folk")
                    Config.hideDialog()
                }
            }
        }
        binding.classical.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Classical")) {
                binding.classical.setCardBackgroundColor(Color.WHITE)
                binding.classicalText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    1,
                    "Classical"
                )
                setUpCount(0, "Classical")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.classical.setCardBackgroundColor(Color.BLACK)
                    binding.classicalText.setTextColor(Color.WHITE)

                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Classical"
                    )
                    setUpCount(1, "Classical")
                    Config.hideDialog()
                }
            }
        }
        binding.disco.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Disco")) {
                binding.disco.setCardBackgroundColor(Color.WHITE)
                binding.discoText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Disco")
                setUpCount(0, "Disco")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                    Config.hideDialog()
                }else {
                    binding.disco.setCardBackgroundColor(Color.BLACK)
                    binding.discoText.setTextColor(Color.WHITE)

                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Disco"
                    )
                    setUpCount(1, "Disco")
                    Config.hideDialog()
                }
            }
        }
        binding.acoustic.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Acoustic")) {
                binding.acoustic.setCardBackgroundColor(Color.WHITE)
                binding.acousticText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Acoustic")
                setUpCount(0, "Acoustic")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.acoustic.setCardBackgroundColor(Color.BLACK)
                    binding.acousticText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Acoustic"
                    )
                    setUpCount(1, "Acoustic")
                    Config.hideDialog()
                }
            }
        }
        binding.soul.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Soul")) {
                binding.soul.setCardBackgroundColor(Color.WHITE)
                binding.soulText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Soul")
                setUpCount(0, "Soul")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.soul.setCardBackgroundColor(Color.BLACK)
                    binding.soulText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 0, "Soul")
                    setUpCount(1, "Soul")
                    Config.hideDialog()
                }
            }
        }
        binding.jazz.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Jazz")) {
                binding.jazz.setCardBackgroundColor(Color.WHITE)
                binding.jazzText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Jazz")
                setUpCount(0, "Jazz")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.jazz.setCardBackgroundColor(Color.BLACK)
                    binding.jazzText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 0, "Jazz")
                    setUpCount(1, "Jazz")
                    Config.hideDialog()
                }
            }
        }
        binding.punk.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Punk")) {
                binding.punk.setCardBackgroundColor(Color.WHITE)
                binding.punkText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Punk")
                setUpCount(0, "Punk")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.punk.setCardBackgroundColor(Color.BLACK)
                    binding.punkText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 0, "Punk")
                    setUpCount(1, "Punk")
                    Config.hideDialog()
                }
            }
        }
        binding.world.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("World")) {
                binding.world.setCardBackgroundColor(Color.WHITE)
                binding.worldText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "World")
                setUpCount(0, "World")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.world.setCardBackgroundColor(Color.BLACK)
                    binding.worldText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "World"
                    )
                    setUpCount(1, "World")
                    Config.hideDialog()
                }
            }
        }
        binding.romance.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Romance")) {
                binding.romance.setCardBackgroundColor(Color.WHITE)
                binding.romanceText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Romance")
                setUpCount(0, "Romance")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.romance.setCardBackgroundColor(Color.BLACK)
                    binding.romanceText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Romance"
                    )
                    setUpCount(1, "Romance")
                    Config.hideDialog()
                }
            }
        }
        binding.comedy.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Comedy")) {
                binding.comedy.setCardBackgroundColor(Color.WHITE)
                binding.comedyText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Comedy")
                setUpCount(0, "Comedy")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.comedy.setCardBackgroundColor(Color.BLACK)
                    binding.comedyText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Comedy"
                    )
                    setUpCount(1, "Comedy")
                    Config.hideDialog()
                }
            }
        }
        binding.horror.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Horror")) {
                binding.horror.setCardBackgroundColor(Color.WHITE)
                binding.horrorText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Horror")
                setUpCount(0, "Horror")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.horror.setCardBackgroundColor(Color.BLACK)
                    binding.horrorText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Horror"
                    )
                    setUpCount(1, "Horror")
                    Config.hideDialog()
                }
            }
        }
        binding.thriller.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Thriller")) {
                binding.thriller.setCardBackgroundColor(Color.WHITE)
                binding.thrillerText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Thriller")
                setUpCount(0, "Thriller")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.thriller.setCardBackgroundColor(Color.BLACK)
                    binding.thrillerText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Thriller"
                    )
                    setUpCount(1, "Thriller")
                    Config.hideDialog()
                }
            }
        }
        binding.fantasy.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Fantasy")) {
                binding.fantasy.setCardBackgroundColor(Color.WHITE)
                binding.fantasyText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Fantasy")
                setUpCount(0, "Fantasy")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.fantasy.setCardBackgroundColor(Color.BLACK)
                    binding.fantasyText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Fantasy"
                    )
                    setUpCount(1, "Fantasy")
                    Config.hideDialog()
                }
            }
        }
        binding.scifi.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Sci-fi")) {
                binding.scifi.setCardBackgroundColor(Color.WHITE)
                binding.scifiText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Sci-fi")
                setUpCount(0, "Sci-fi")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.scifi.setCardBackgroundColor(Color.BLACK)
                    binding.scifiText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Sci-fi"
                    )
                    setUpCount(1, "Sci-fi")
                    Config.hideDialog()
                }
            }
        }
        binding.anime.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Anime")) {
                binding.anime.setCardBackgroundColor(Color.WHITE)
                binding.animeText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Anime")
                setUpCount(0, "Anime")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.anime.setCardBackgroundColor(Color.BLACK)
                    binding.animeText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Anime"
                    )
                    setUpCount(1,"Anime")
                    Config.hideDialog()
                }
            }
        }
        binding.action.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Action")) {
                binding.action.setCardBackgroundColor(Color.WHITE)
                binding.actionText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Action")
                setUpCount(0, "Action")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.action.setCardBackgroundColor(Color.BLACK)
                    binding.actionText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Action"
                    )
                    setUpCount(1, "Action")
                    Config.hideDialog()
                }
            }
        }
        binding.adventure.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Adventure")) {
                binding.adventure.setCardBackgroundColor(Color.WHITE)
                binding.adventureText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    1,
                    "Adventure"
                )
                setUpCount(0, "Adventure")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.adventure.setCardBackgroundColor(Color.BLACK)
                    binding.adventureText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Adventure"
                    )
                    setUpCount(1, "Adventure")
                    Config.hideDialog()
                }
            }
        }
        binding.documentary.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Documentary")) {
                binding.documentary.setCardBackgroundColor(Color.WHITE)
                binding.documentaryText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    1,
                    "Documentary"
                )
                setUpCount(0, "Documentary")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.documentary.setCardBackgroundColor(Color.BLACK)
                    binding.documentaryText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Documentary"
                    )
                    setUpCount(1,"Documentary")
                    Config.hideDialog()
                }
            }
        }
        binding.drama.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Drama")) {
                binding.drama.setCardBackgroundColor(Color.WHITE)
                binding.dramaText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Drama")
                setUpCount(0, "Drama")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.drama.setCardBackgroundColor(Color.BLACK)
                    binding.dramaText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Drama"
                    )
                    setUpCount(1, "Drama")
                    Config.hideDialog()
                }
            }
        }
        binding.cooking.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Cooking")) {
                binding.cooking.setCardBackgroundColor(Color.WHITE)
                binding.cookingText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Cooking")
                setUpCount(0, "Cooking")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.cooking.setCardBackgroundColor(Color.BLACK)
                    binding.cookingText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Cooking"
                    )
                    setUpCount(1, "Cooking")
                    Config.hideDialog()
                }
            }
        }
        binding.videoGames.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Video games")) {
                binding.videoGames.setCardBackgroundColor(Color.WHITE)
                binding.videoGamesText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    1,
                    "Video games"
                )
                setUpCount(0, "Video games")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.videoGames.setCardBackgroundColor(Color.BLACK)
                    binding.videoGamesText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Video games"
                    )
                    setUpCount(1, "Video games")
                    Config.hideDialog()
                }
            }
        }
        binding.fashion.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Fashion")) {
                binding.fashion.setCardBackgroundColor(Color.WHITE)
                binding.fashionText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Fashion")
                setUpCount(0, "Fashion")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.fashion.setCardBackgroundColor(Color.BLACK)
                    binding.fashionText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Fashion"
                    )
                    setUpCount(1, "Fashion")
                    Config.hideDialog()
                }
            }
        }
        binding.singing.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Singing")) {
                binding.singing.setCardBackgroundColor(Color.WHITE)
                binding.singingText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Singing")
                setUpCount(0, "Singing")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.singing.setCardBackgroundColor(Color.BLACK)
                    binding.singingText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Singing"
                    )
                    setUpCount(1, "Singing")
                    Config.hideDialog()
                }
            }
        }
        binding.pets.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Pets")) {
                binding.pets.setCardBackgroundColor(Color.WHITE)
                binding.petsText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Pets")
                setUpCount(0, "Pets")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.pets.setCardBackgroundColor(Color.BLACK)
                    binding.petsText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 0, "Pets")
                    setUpCount(1, "Pets")
                    Config.hideDialog()
                }
            }
        }
        binding.running.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Running")) {
                binding.running.setCardBackgroundColor(Color.WHITE)
                binding.runningText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Running")
                setUpCount(0, "Running")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.running.setCardBackgroundColor(Color.BLACK)
                    binding.runningText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Running"
                    )
                    setUpCount(1, "Running")
                    Config.hideDialog()
                }
            }
        }
        binding.writing.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Writing")) {
                binding.writing.setCardBackgroundColor(Color.WHITE)
                binding.writingText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Writing")
                setUpCount(0, "Writing")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.writing.setCardBackgroundColor(Color.BLACK)
                    binding.writingText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Writing"
                    )
                    setUpCount(1, "Writing")
                    Config.hideDialog()
                }
            }
        }
        binding.acting.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Acting")) {
                binding.acting.setCardBackgroundColor(Color.WHITE)
                binding.actingText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Acting")
                setUpCount(0, "Acting")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.acting.setCardBackgroundColor(Color.BLACK)
                    binding.actingText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Acting"
                    )
                    setUpCount(1, "Acting")
                    Config.hideDialog()
                }
            }
        }
        binding.camping.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Camping")) {
                binding.camping.setCardBackgroundColor(Color.WHITE)
                binding.campingText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Camping")
                setUpCount(0, "Camping")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.camping.setCardBackgroundColor(Color.BLACK)
                    binding.campingText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Camping"
                    )
                    setUpCount(1, "Camping")
                    Config.hideDialog()
                }
            }
        }
        binding.magic.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Magic")) {
                binding.magic.setCardBackgroundColor(Color.WHITE)
                binding.magicText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Magic")
                setUpCount(0, "Magic")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.magic.setCardBackgroundColor(Color.BLACK)
                    binding.magicText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Magic"
                    )
                    setUpCount(1, "Magic")
                    Config.hideDialog()
                }
            }
        }
        binding.yoga.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Yoga")) {
                binding.yoga.setCardBackgroundColor(Color.WHITE)
                binding.yogaText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Yoga")
                setUpCount(0, "Yoga")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.yoga.setCardBackgroundColor(Color.BLACK)
                    binding.yogaText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 0, "Yoga")
                    setUpCount(1, "Yoga")
                    Config.hideDialog()
                }
            }
        }
        binding.football.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Football")) {
                binding.football.setCardBackgroundColor(Color.WHITE)
                binding.footballText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Football")
                setUpCount(0, "Football")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.football.setCardBackgroundColor(Color.BLACK)
                    binding.footballText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Football"
                    )
                    setUpCount(1, "Football")
                    Config.hideDialog()
                }
            }
        }
        binding.basketball.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Basketball")) {
                binding.basketball.setCardBackgroundColor(Color.WHITE)
                binding.basketballText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    1,
                    "Basketball"
                )
                setUpCount(0, "Basketball")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.basketball.setCardBackgroundColor(Color.BLACK)
                    binding.basketballText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Basketball"
                    )
                    setUpCount(1, "Basketball")
                    Config.hideDialog()
                }
            }
        }
        binding.tennis.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Tennis")) {
                binding.tennis.setCardBackgroundColor(Color.WHITE)
                binding.tennisText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Tennis")
                setUpCount(0, "Tennis")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.tennis.setCardBackgroundColor(Color.BLACK)
                    binding.tennisText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Tennis"
                    )
                    setUpCount(1, "Tennis")
                    Config.hideDialog()
                }
            }
        }
        binding.swimming.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Swimming")) {
                binding.swimming.setCardBackgroundColor(Color.WHITE)
                binding.swimmingText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Swimming")
                setUpCount(0, "Swimming")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.swimming.setCardBackgroundColor(Color.BLACK)
                    binding.swimmingText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Swimming"
                    )
                    setUpCount(1, "Swimming")
                    Config.hideDialog()
                }
            }
        }
        binding.esports.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Esports")) {
                binding.esports.setCardBackgroundColor(Color.WHITE)
                binding.esportsText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Esports")
                setUpCount(0, "Esports")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.esports.setCardBackgroundColor(Color.BLACK)
                    binding.esportsText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Esports"
                    )
                    setUpCount(1, "Esports")
                    Config.hideDialog()
                }
            }
        }
        binding.racing.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Racing")) {
                binding.racing.setCardBackgroundColor(Color.WHITE)
                binding.racingText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Racing")
                setUpCount(0, "Racing")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.racing.setCardBackgroundColor(Color.BLACK)
                    binding.racingText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Racing"
                    )
                    setUpCount(1, "Racing")
                    Config.hideDialog()
                }
            }
        }
        binding.dance.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Dance")) {
                binding.dance.setCardBackgroundColor(Color.WHITE)
                binding.danceText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Dance")
                setUpCount(0, "Dance")
                Config.hideDialog()
            } else {
                if(count >= 6){
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT).show()
                }else {
                    binding.dance.setCardBackgroundColor(Color.BLACK)
                    binding.danceText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Dance"
                    )
                    setUpCount(1, "Dance")
                    Config.hideDialog()
                }
            }
        }
        binding.weightLifting.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Weight lifting")) {
                binding.weightLifting.setCardBackgroundColor(Color.WHITE)
                binding.weightLiftingText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    1,
                    "Weight lifting"
                )
                setUpCount(0, "Weight lifting")
                Config.hideDialog()
            } else {
                if (count >= 6) {
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.weightLifting.setCardBackgroundColor(Color.BLACK)
                    binding.weightLiftingText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Weight lifting"
                    )
                    setUpCount(1, "Weight lifting")
                    Config.hideDialog()
                }
            }
        }
        binding.soccer.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Soccer")) {
                binding.soccer.setCardBackgroundColor(Color.WHITE)
                binding.soccerText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Soccer")
                setUpCount(0, "Soccer")
                Config.hideDialog()
            } else {
                if (count >= 6) {
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.soccer.setCardBackgroundColor(Color.BLACK)
                    binding.soccerText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Soccer"
                    )
                    setUpCount(1, "Soccer")
                    Config.hideDialog()
                }

            }
        }
        binding.baseball.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Baseball")) {
                binding.baseball.setCardBackgroundColor(Color.WHITE)
                binding.baseballText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Baseball")
                setUpCount(0, "Baseball")
                Config.hideDialog()
            } else {
                if (count >= 6) {
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.baseball.setCardBackgroundColor(Color.BLACK)
                    binding.baseballText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Baseball"
                    )
                    setUpCount(1, "Baseball")
                    Config.hideDialog()
                }
            }
        }
        binding.cycling.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Cycling")) {
                binding.cycling.setCardBackgroundColor(Color.WHITE)
                binding.cyclingText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Cycling")
                setUpCount(0, "Cycling")
                Config.hideDialog()
            } else {
                if (count >= 6) {
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.cycling.setCardBackgroundColor(Color.BLACK)
                    binding.cyclingText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Cycling"
                    )
                    setUpCount(1, "Cycling")
                    Config.hideDialog()
                }
            }
        }
        binding.vegan.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Vegan")) {
                binding.vegan.setCardBackgroundColor(Color.WHITE)
                binding.veganText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Vegan")
                setUpCount(0, "Vegan")
                Config.hideDialog()
            } else {
                if (count >= 6) {
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.vegan.setCardBackgroundColor(Color.BLACK)
                    binding.veganText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Vegan"
                    )
                    setUpCount(1, "Vegan")
                    Config.hideDialog()
                }
            }
        }
        binding.lgbtqia.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("LGBTQIA+")) {
                binding.lgbtqia.setCardBackgroundColor(Color.WHITE)
                binding.lgbtqiaText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "LGBTQIA+")
                setUpCount(0, "LGBTQIA+")
                Config.hideDialog()
            } else {
                if (count >= 6) {
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.lgbtqia.setCardBackgroundColor(Color.BLACK)
                    binding.lgbtqiaText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "LGBTQIA+"
                    )
                    setUpCount(1, "LGBTQIA+")
                    Config.hideDialog()
                }
            }
        }
        binding.greekLife.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Greek life")) {
                binding.greekLife.setCardBackgroundColor(Color.WHITE)
                binding.greekLifeText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    1,
                    "Greek life"
                )
                setUpCount(0, "Greek life")
                Config.hideDialog()
            } else {
                if (count >= 6) {
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.greekLife.setCardBackgroundColor(Color.BLACK)
                    binding.greekLifeText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Greek life"
                    )
                    setUpCount(1, "Greek life")
                    Config.hideDialog()
                }
            }
        }
        binding.foodie.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Foodie")) {
                binding.foodie.setCardBackgroundColor(Color.WHITE)
                binding.foodieText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Foodie")
                setUpCount(0, "Foodie")
                Config.hideDialog()
            } else {
                if (count >= 6) {
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.foodie.setCardBackgroundColor(Color.BLACK)
                    binding.foodieText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Foodie"
                    )
                    setUpCount(1, "Foodie")
                    Config.hideDialog()
                }
            }
        }
        binding.highSchool.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("High school")) {
                binding.highSchool.setCardBackgroundColor(Color.WHITE)
                binding.highSchoolText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    1,
                    "High school"
                )
                setUpCount(0, "High school")
                Config.hideDialog()
            } else {
                if (count >= 6) {
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.highSchool.setCardBackgroundColor(Color.BLACK)
                    binding.highSchoolText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "High school"
                    )
                    setUpCount(1, "High school")
                    Config.hideDialog()
                }

            }
        }
        binding.partying.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("Partying")) {
                binding.partying.setCardBackgroundColor(Color.WHITE)
                binding.partyingText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(FirebaseAuth.getInstance().currentUser!!.uid, 1, "Partying")
                setUpCount(0, "Partying")
                Config.hideDialog()
            } else {
                if (count >= 6) {
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.partying.setCardBackgroundColor(Color.BLACK)
                    binding.partyingText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "Partying"
                    )
                    setUpCount(1, "Partying")
                    Config.hideDialog()
                }
            }
        }
        binding.university.setOnClickListener {
            Config.showDialog(this)
            if (list.contains("University")) {
                binding.university.setCardBackgroundColor(Color.WHITE)
                binding.universityText.setTextColor(Color.BLACK)

                val userDao = UserDao()
                userDao.updateInterests(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    1,
                    "University"
                )
                setUpCount(0, "University")
                Config.hideDialog()
            } else {
                if (count >= 6) {
                    Config.hideDialog()
                    Toast.makeText(this, "You can only select 6 interests", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    binding.university.setCardBackgroundColor(Color.BLACK)
                    binding.universityText.setTextColor(Color.WHITE)
                    val userDao = UserDao()
                    userDao.updateInterests(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        0,
                        "University"
                    )
                    setUpCount(1, "University")
                    Config.hideDialog()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpCount(i: Int, s: String) {
        if (i == 0) {
            count -= 1
            list.remove(s)
        } else {
            count += 1
            list.add(s)
        }
        binding.count.text = "${count}/6"
    }

    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.M)
    private fun loadContent() {
        Config.showDialog(this)
        list = ArrayList()

        GlobalScope.launch {
            val userDao = UserDao()
            val currData: UserModel =
                userDao.getUserById(FirebaseAuth.getInstance().currentUser!!.uid).await()
                    .getValue(UserModel::class.java)!!

            withContext(Dispatchers.Main) {
                currData.interests?.let {
                    list.addAll(it)
                    count = list.size
                }
                binding.count.text = "${count}/6"

                if (list.contains("Pop")) {
                    binding.pop.setCardBackgroundColor(Color.BLACK)
                    binding.popText.setTextColor(Color.WHITE)
                }
                if (list.contains("Rock")) {
                    binding.rock.setCardBackgroundColor(Color.BLACK)
                    binding.rockText.setTextColor(Color.WHITE)
                }
                if (list.contains("Country")) {
                    binding.country.setCardBackgroundColor(Color.BLACK)
                    binding.countryText.setTextColor(Color.WHITE)
                }
                if (list.contains("Folk")) {
                    binding.folk.setCardBackgroundColor(Color.BLACK)
                    binding.folkText.setTextColor(Color.WHITE)
                }
                if (list.contains("Classical")) {
                    binding.classical.setCardBackgroundColor(Color.BLACK)
                    binding.classicalText.setTextColor(Color.WHITE)
                }
                if (list.contains("Disco")) {
                    binding.disco.setCardBackgroundColor(Color.BLACK)
                    binding.discoText.setTextColor(Color.WHITE)
                }
                if (list.contains("Acoustic")) {
                    binding.acoustic.setCardBackgroundColor(Color.BLACK)
                    binding.acousticText.setTextColor(Color.WHITE)
                }
                if (list.contains("Soul")) {
                    binding.soul.setCardBackgroundColor(Color.BLACK)
                    binding.soulText.setTextColor(Color.WHITE)
                }
                if (list.contains("Jazz")) {
                    binding.jazz.setCardBackgroundColor(Color.BLACK)
                    binding.jazzText.setTextColor(Color.WHITE)
                }
                if (list.contains("Punk")) {
                    binding.punk.setCardBackgroundColor(Color.BLACK)
                    binding.punkText.setTextColor(Color.WHITE)
                }
                if (list.contains("World")) {
                    binding.world.setCardBackgroundColor(Color.BLACK)
                    binding.worldText.setTextColor(Color.WHITE)
                }
                if (list.contains("Romance")) {
                    binding.romance.setCardBackgroundColor(Color.BLACK)
                    binding.romanceText.setTextColor(Color.WHITE)
                }
                if (list.contains("Comedy")) {
                    binding.comedy.setCardBackgroundColor(Color.BLACK)
                    binding.comedyText.setTextColor(Color.WHITE)
                }
                if (list.contains("Horror")) {
                    binding.horror.setCardBackgroundColor(Color.BLACK)
                    binding.horrorText.setTextColor(Color.WHITE)
                }
                if (list.contains("Thriller")) {
                    binding.thriller.setCardBackgroundColor(Color.BLACK)
                    binding.thrillerText.setTextColor(Color.WHITE)
                }
                if (list.contains("Fantasy")) {
                    binding.fantasy.setCardBackgroundColor(Color.BLACK)
                    binding.fantasyText.setTextColor(Color.WHITE)
                }
                if (list.contains("Sci-fi")) {
                    binding.scifi.setCardBackgroundColor(Color.BLACK)
                    binding.scifiText.setTextColor(Color.WHITE)
                }
                if (list.contains("Anime")) {
                    binding.anime.setCardBackgroundColor(Color.BLACK)
                    binding.animeText.setTextColor(Color.WHITE)
                }
                if (list.contains("Action")) {
                    binding.action.setCardBackgroundColor(Color.BLACK)
                    binding.actionText.setTextColor(Color.WHITE)
                }
                if (list.contains("Adventure")) {
                    binding.adventure.setCardBackgroundColor(Color.BLACK)
                    binding.adventureText.setTextColor(Color.WHITE)
                }
                if (list.contains("Documentary")) {
                    binding.documentary.setCardBackgroundColor(Color.BLACK)
                    binding.documentaryText.setTextColor(Color.WHITE)
                }
                if (list.contains("Drama")) {
                    binding.drama.setCardBackgroundColor(Color.BLACK)
                    binding.dramaText.setTextColor(Color.WHITE)
                }
                if (list.contains("Cooking")) {
                    binding.cooking.setCardBackgroundColor(Color.BLACK)
                    binding.cookingText.setTextColor(Color.WHITE)
                }
                if (list.contains("Video games")) {
                    binding.videoGames.setCardBackgroundColor(Color.BLACK)
                    binding.videoGamesText.setTextColor(Color.WHITE)
                }
                if (list.contains("Fashion")) {
                    binding.fashion.setCardBackgroundColor(Color.BLACK)
                    binding.fashionText.setTextColor(Color.WHITE)
                }
                if (list.contains("Singing")) {
                    binding.singing.setCardBackgroundColor(Color.BLACK)
                    binding.singingText.setTextColor(Color.WHITE)
                }
                if (list.contains("Pets")) {
                    binding.pets.setCardBackgroundColor(Color.BLACK)
                    binding.petsText.setTextColor(Color.WHITE)
                }
                if (list.contains("Running")) {
                    binding.running.setCardBackgroundColor(Color.BLACK)
                    binding.runningText.setTextColor(Color.WHITE)
                }
                if (list.contains("Writing")) {
                    binding.writing.setCardBackgroundColor(Color.BLACK)
                    binding.writingText.setTextColor(Color.WHITE)
                }
                if (list.contains("Acting")) {
                    binding.acting.setCardBackgroundColor(Color.BLACK)
                    binding.actingText.setTextColor(Color.WHITE)
                }
                if (list.contains("Camping")) {
                    binding.camping.setCardBackgroundColor(Color.BLACK)
                    binding.campingText.setTextColor(Color.WHITE)
                }
                if (list.contains("Magic")) {
                    binding.magic.setCardBackgroundColor(Color.BLACK)
                    binding.magicText.setTextColor(Color.WHITE)
                }
                if (list.contains("Yoga")) {
                    binding.yoga.setCardBackgroundColor(Color.BLACK)
                    binding.yogaText.setTextColor(Color.WHITE)
                }
                if (list.contains("Football")) {
                    binding.football.setCardBackgroundColor(Color.BLACK)
                    binding.footballText.setTextColor(Color.WHITE)
                }
                if (list.contains("Basketball")) {
                    binding.basketball.setCardBackgroundColor(Color.BLACK)
                    binding.basketballText.setTextColor(Color.WHITE)
                }
                if (list.contains("Tennis")) {
                    binding.tennis.setCardBackgroundColor(Color.BLACK)
                    binding.tennisText.setTextColor(Color.WHITE)
                }
                if (list.contains("Swimming")) {
                    binding.swimming.setCardBackgroundColor(Color.BLACK)
                    binding.swimmingText.setTextColor(Color.WHITE)
                }
                if (list.contains("Esports")) {
                    binding.esports.setCardBackgroundColor(Color.BLACK)
                    binding.esportsText.setTextColor(Color.WHITE)
                }
                if (list.contains("Racing")) {
                    binding.racing.setCardBackgroundColor(Color.BLACK)
                    binding.racingText.setTextColor(Color.WHITE)
                }
                if (list.contains("Dance")) {
                    binding.dance.setCardBackgroundColor(Color.BLACK)
                    binding.danceText.setTextColor(Color.WHITE)
                }
                if (list.contains("Weight lifting")) {
                    binding.weightLifting.setCardBackgroundColor(Color.BLACK)
                    binding.weightLiftingText.setTextColor(Color.WHITE)
                }
                if (list.contains("Soccer")) {
                    binding.soccer.setCardBackgroundColor(Color.BLACK)
                    binding.soccerText.setTextColor(Color.WHITE)
                }
                if (list.contains("Baseball")) {
                    binding.baseball.setCardBackgroundColor(Color.BLACK)
                    binding.baseballText.setTextColor(Color.WHITE)
                }
                if (list.contains("Cycling")) {
                    binding.cycling.setCardBackgroundColor(Color.BLACK)
                    binding.cyclingText.setTextColor(Color.WHITE)
                }
                if (list.contains("Vegan")) {
                    binding.vegan.setCardBackgroundColor(Color.BLACK)
                    binding.veganText.setTextColor(Color.WHITE)
                }
                if (list.contains("LGBTQIA+")) {
                    binding.lgbtqia.setCardBackgroundColor(Color.BLACK)
                    binding.lgbtqiaText.setTextColor(Color.WHITE)
                }
                if (list.contains("Greek life")) {
                    binding.greekLife.setCardBackgroundColor(Color.BLACK)
                    binding.greekLifeText.setTextColor(Color.WHITE)
                }
                if (list.contains("Foodie")) {
                    binding.foodie.setCardBackgroundColor(Color.BLACK)
                    binding.foodieText.setTextColor(Color.WHITE)
                }
                if (list.contains("High school")) {
                    binding.highSchool.setCardBackgroundColor(Color.BLACK)
                    binding.highSchoolText.setTextColor(Color.WHITE)
                }
                if (list.contains("Partying")) {
                    binding.partying.setCardBackgroundColor(Color.BLACK)
                    binding.partyingText.setTextColor(Color.WHITE)
                }
                if (list.contains("University")) {
                    binding.university.setCardBackgroundColor(Color.BLACK)
                    binding.universityText.setTextColor(Color.WHITE)
                }

                Config.hideDialog()
            }

        }

    }
}