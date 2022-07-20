package com.lucas.businesscard.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lucas.businesscard.App
import com.lucas.businesscard.R
import com.lucas.businesscard.databinding.ActivityMainBinding
import com.lucas.businesscard.util.Image


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }
    private val adapter by lazy { BusinessCardAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.rvCards.adapter = adapter
        getAllBusinessCards()
        insertListeners()
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddBusinessCardActivity::class.java)
            startActivity(intent)
        }
        adapter.listenerShare = { card ->

            val dialog = MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.label_share_business_card))
                .setNegativeButton(resources.getString(R.string.label_cancel)) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.label_share)) { dialog, which ->
                    Image.share(this@MainActivity, card)
                }

            dialog.show()
        }
    }

    private fun getAllBusinessCards() {
        mainViewModel.getAll().observe(this) { businessCards ->
            adapter.submitList(businessCards)
        }
    }
}