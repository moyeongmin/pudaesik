package com.example.bbudaesik.presentation.ui.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.bbudaesik.R

class BDSAppWidgetConfigureActivity : Activity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_CANCELED)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContentView(R.layout.bds_app_widget_configure)

        val spinner = findViewById<Spinner>(R.id.restaurant_spinner)
        val confirmButton = findViewById<Button>(R.id.confirm_button)

        val restaurants = listOf("금정회관 교직원", "금정회관 학생", "샛벌회관", "편의동 2층", "학생회관 학생", "학생회관(밀양) 교직원", "학생회관(밀양) 학생", "진리관", "웅비관", "자유관", "비마관", "행림관")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, restaurants)

        confirmButton.setOnClickListener {
            val selectedRestaurant = spinner.selectedItem.toString()

            saveRestaurantPref(this, appWidgetId, selectedRestaurant)

            val appWidgetManager = AppWidgetManager.getInstance(this)
            updateAppWidget(this, appWidgetManager, appWidgetId)

            val resultValue = Intent().apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            setResult(RESULT_OK, resultValue)
            finish()
        }
    }

    private fun saveRestaurantPref(context: Context, appWidgetId: Int, restaurant: String) {
        val prefs = context.getSharedPreferences("BDSAppWidgetPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("restaurant_$appWidgetId", restaurant).apply()
    }
}
