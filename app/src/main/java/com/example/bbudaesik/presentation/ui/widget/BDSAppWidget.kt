package com.example.bbudaesik.presentation.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.example.bbudaesik.R
import com.example.bbudaesik.data.model.DormitoryResponse
import com.example.bbudaesik.data.model.RestaurantResponse
import com.example.bbudaesik.di.WidgetHelper
import com.example.bbudaesik.utils.BuildingInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Implementation of App Widget functionality.
 */
class BDSAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        // There may be multiple widgets active, so update   all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
) {
    CoroutineScope(Dispatchers.IO).launch {
        val prefs = context.getSharedPreferences("BDSAppWidgetPrefs", Context.MODE_PRIVATE)
        val selectedRestaurant =
            prefs.getString("restaurant_$appWidgetId", "금정회관 학생") ?: "등록된 정보 없음"

        val selectedCode = BuildingInfo.AppWidgetResMap[selectedRestaurant] ?: ""

        val isDormitory = BuildingInfo.regionDormitories.containsKey(selectedRestaurant)
        val dbKey = if (isDormitory) "DORMITORY" else "RESTAURANT"

        val repository = WidgetHelper.provideNotionRepository()
        val result = repository.getMeals(getFormattedDate(), dbKey, listOf(selectedCode))

        val views = RemoteViews(context.packageName, R.layout.b_d_s_app_widget)

        when (result) {
            is RestaurantResponse -> {
                val item = result.results.firstOrNull()

                if (item != null) {
                    val menuCode = item.properties.menuType.rich_text?.firstOrNull()?.plain_text
                    val menuType = BuildingInfo.menuTimeCodes[menuCode] ?: "식사 시간 정보 없음"
                    val menuCost =
                        item.properties.menuTitle.rich_text?.firstOrNull()?.plain_text ?: ""
                    val menuDetail =
                        item.properties.menuContent.rich_text?.firstOrNull()?.plain_text
                            ?: "메뉴 정보 없음"

                    views.setTextViewText(R.id.appwidget_title, selectedRestaurant)
                    views.setTextViewText(R.id.appwidget_menu_type, menuType)
                    if (menuCost.isEmpty()) {
                        views.setViewVisibility(R.id.appwidget_menu_cost, View.GONE)
                    } else {
                        views.setViewVisibility(R.id.appwidget_menu_cost, View.VISIBLE)
                        views.setTextViewText(R.id.appwidget_menu_cost, menuCost)
                    }
                    views.setTextViewText(R.id.appwidget_menu_detail, menuDetail)
                } else {
                    views.setTextViewText(R.id.appwidget_title, selectedRestaurant)
                    views.setTextViewText(R.id.appwidget_menu_type, "")
                    views.setTextViewText(R.id.appwidget_menu_detail, "식단 정보가 없습니다.")
                }
            }

            is DormitoryResponse -> {
                val item = result.results.firstOrNull()

                if (item != null) {
                    val mealType =
                        item.properties.codeNm.rich_text?.firstOrNull()?.plain_text ?: "식사 시간 정보 없음"
                    val menuDetail =
                        item.properties.mealNm.rich_text?.firstOrNull()?.plain_text ?: "메뉴 정보 없음"

                    views.setTextViewText(R.id.appwidget_title, selectedRestaurant)
                    views.setTextViewText(R.id.appwidget_menu_type, mealType)
                    views.setTextViewText(R.id.appwidget_menu_detail, menuDetail)
                } else {
                    views.setTextViewText(R.id.appwidget_title, selectedRestaurant)
                    views.setTextViewText(R.id.appwidget_menu_type, "메뉴 없음")
                    views.setTextViewText(R.id.appwidget_menu_detail, "오늘의 식단 정보가 없습니다.")
                }
            }

            else -> {
                views.setTextViewText(R.id.appwidget_title, selectedRestaurant)
                views.setTextViewText(R.id.appwidget_menu_type, "오류")
                views.setTextViewText(R.id.appwidget_menu_detail, "데이터를 불러올 수 없습니다.")
            }
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}


private fun getFormattedDate(): String {
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormatter.format(calendar.time)
}