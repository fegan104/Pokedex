package com.frankegan.pokedex.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.frankegan.pokedex.Greeting
import android.widget.TextView
import androidx.fragment.app.commit
import com.frankegan.pokedex.android.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            replace(android.R.id.content, HomeFragment())
        }
    }
}
