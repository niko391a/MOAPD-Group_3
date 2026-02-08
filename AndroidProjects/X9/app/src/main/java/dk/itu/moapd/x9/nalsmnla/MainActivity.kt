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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up the UI components.
        setupUI()
    }

    private fun setupUI() =
        with(binding.contentMain) {
            // Create the adapter using your string array
            val adapter = ArrayAdapter.createFromResource(
                root.context, // Use 'root.context' or 'this@MainActivity'
                R.array.report_types,
                android.R.layout.simple_spinner_item
            )

            // Specify the layout for the dropdown choices
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Apply it to the spinner (using the ID from your XML)
            spinnerReportTypes.adapter = adapter

            // Serverity buttons
            var severity : String = "Low"

            buttonLow.setOnClickListener {
                severity = "Low"
            }
            buttonMid.setOnClickListener {
                severity = "Mid"
            }
            buttonHigh.setOnClickListener {
                severity = "High"
            }

            // Submit button
            buttonSubmit.setOnClickListener {
                val reportTitle : String = textReportTitle.text.toString()
                val reportType : String = spinnerReportTypes.selectedItem.toString()
                val reportDescription : String = textReportDescription.text.toString()
                submitReport(reportTitle, reportType, reportDescription, severity)
            }
        }
}
private fun submitReport(reportTitle : String,
                         reportType : String,
                         reportDescription : String,
                         severity : String) =
    if (!reportTitle.isEmpty() || !reportDescription.isEmpty())
        Log.d("Submit", """
            User report has been submitted with the following information
            Report Title: ${reportTitle}
            Report Type: ${reportType}
            Report Description: ${reportDescription}
            Severity: ${severity}
        """.trimIndent())
    else
        Log.d("Submit", """
            User report has been submitted with invalid information:
            Report Title: ${reportTitle}
            Report Type: ${reportType}
            Report Description: ${reportDescription}
            Severity: ${severity}
        """.trimIndent())

