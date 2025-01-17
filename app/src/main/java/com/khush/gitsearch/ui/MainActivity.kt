package com.khush.gitsearch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.khush.gitsearch.R
import com.khush.gitsearch.ui.fragments.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(), MainFragment.TAG)
                .addToBackStack(MainFragment.TAG)
                .commit()
        }

    }
}


