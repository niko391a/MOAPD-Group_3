package dk.itu.moapd.x9.nalsmnla

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dk.itu.moapd.x9.nalsmnla.databinding.ActivityMainBinding
import dk.itu.moapd.x9.nalsmnla.R
import android.util.Log

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up the UI components.
        setupUI()
    }

    private fun setupUI() =
        with(binding) {
            with(contentMain) {
                // Create the adapter using your string array
                val adapter = ArrayAdapter.createFromResource(
                    root.context, // Use 'root.context' or 'this@MainActivity'
                    R.array.report_types,
                    android.R.layout.simple_spinner_item
                )

                // Specify the layout for the dropdown choices
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Apply it to the spinner (using the ID from your XML)
                reportTypesSpinner.adapter = adapter


                // Serverity buttons
                buttonLow.setOnClickListener {
                    textViewMessage.text = getString(R.string.true_text)
                }
                buttonMid.setOnClickListener {
                    textViewMessage.text =
                        getString(R.string.false_text) // send back a value of severity
                }
                buttonHigh.setOnClickListener {
                    textViewMessage.text = getString(R.string.false_text)
                }

                // Submit button
                buttonSubmit.setOnClickListener {
                    textViewMessage.text = getString(R.string.false_text)
                }

                checkBoxSelect.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (!buttonView.isPressed) return@setOnCheckedChangeListener
                    val status = if (isChecked) "checked" else "unchecked"
                    textViewMessage.text = resources.getString(R.string.selected_text, status)
                }
            }
        }
}