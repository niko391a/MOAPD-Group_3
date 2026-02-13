package dk.itu.moapd.x9.nalsmnla

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dk.itu.moapd.x9.nalsmnla.databinding.ActivityMainBinding
import dk.itu.moapd.x9.nalsmnla.R
import android.util.Log
import com.google.android.material.bottomappbar.BottomAppBar
import kotlin.math.log

/**
 * An activity class with methods to manage the main activity of X9 application.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Migrate from Kotlin synthetics to Jetpack view binding.
        // https://developer.android.com/topic/libraries/view-binding/migration
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle window insets to support edge-to-edge content.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Set up the UI components.
        setupUI()
    }

    private fun setupUI() = with(binding) {
        // 1. Use the correct Selection Listener
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.create_report -> {
                    val intent = Intent(this,CreateReportActivity::class)
                    true // Returning true highlights the item as selected
                }
                R.id.home -> {

                    true
                }
                else -> false
            }
        }
    }

}
