package com.example.bbudaesik.presentation.ui.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.bbudaesik.R
import com.example.bbudaesik.databinding.BdsAppWidgetConfigureBinding

class BDSAppWidgetConfigureActivity : Activity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var binding: BdsAppWidgetConfigureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_CANCELED)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId =
                extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID,
                )
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
        binding = BdsAppWidgetConfigureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items =
            listOf(
                "금정회관 학생",
                "금정회관 교직원",
                "샛벌회관",
                "편의동 2층",
                "학생회관 학생",
                "학생회관(밀양) 교직원",
                "학생회관(밀양) 학생",
                "진리관",
                "웅비관",
                "자유관",
                "비마관",
                "행림관",
            )

        val adapter =
            object : ArrayAdapter<String>(
                this,
                R.layout.spinner_item,
                R.id.selected_item,
                items,
            ) {
                override fun getView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup,
                ): View {
                    val view = super.getView(position, convertView, parent)
                    val selectedItem = view.findViewById<TextView>(R.id.selected_item)
                    selectedItem.text = items[position]
                    return view
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup,
                ): View {
                    val inflater = LayoutInflater.from(context)
                    val view = inflater.inflate(R.layout.spinner_dropdown_item, parent, false)
                    (view as TextView).text = items[position]
                    return view
                }
            }
        binding.restaurantSpinner.adapter = adapter

        binding.confirmButton.setOnClickListener {
            val selectedRestaurant = binding.restaurantSpinner.selectedItem.toString()

            saveRestaurantPref(this, appWidgetId, selectedRestaurant)

            val appWidgetManager = AppWidgetManager.getInstance(this)
            updateAppWidget(this, appWidgetManager, appWidgetId)

            val resultValue =
                Intent().apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                }
            setResult(RESULT_OK, resultValue)
            finish()
        }
    }

    private fun saveRestaurantPref(
        context: Context,
        appWidgetId: Int,
        restaurant: String,
    ) {
        val prefs = context.getSharedPreferences("BDSAppWidgetPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("restaurant_$appWidgetId", restaurant).apply()
    }
}
