package com.mo.bbudaesik.presentation.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.mo.bbudaesik.R
import com.mo.bbudaesik.data.model.DormitoryResponse
import com.mo.bbudaesik.data.model.RestaurantResponse
import com.mo.bbudaesik.di.WidgetHelper
import com.mo.bbudaesik.utils.BuildingInfo
import com.mo.bbudaesik.utils.getCurrentMealCode
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
        val currentMealCode = getCurrentMealCode()
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
                val resMealCode =
                    when (currentMealCode) {
                        "중식" -> "L"
                        "석식" -> "D"
                        else -> "B"
                    }
                when (selectedRestaurant) {
                    "학생회관(밀양) 학생" -> views.setTextViewText(R.id.appwidget_title, "학생회관\n(밀양) 학생")
                    "학생회관(밀양) 교직원" -> views.setTextViewText(R.id.appwidget_title, "학생회관\n(밀양) 교직원")
                    else -> views.setTextViewText(R.id.appwidget_title, selectedRestaurant)
                }
                val item =
                    result.results.firstOrNull {
                        it.properties.menuType.rich_text
                            ?.firstOrNull()
                            ?.plain_text == resMealCode
                    }

                if (item != null) {
                    val menuCode =
                        item.properties.menuType.rich_text
                            ?.firstOrNull()
                            ?.plain_text
                    val menuType = BuildingInfo.menuTimeCodes[menuCode] ?: "식사 시간 정보 없음"
                    val menuCost =
                        item.properties.menuTitle.rich_text
                            ?.firstOrNull()
                            ?.plain_text ?: ""
                    val menuDetailRaw =
                        item.properties.menuContent.rich_text
                            ?.firstOrNull()
                            ?.plain_text
                            ?: "메뉴 정보 없음"
                    views.setTextViewText(R.id.appwidget_menu_type, menuType)
                    if (menuCost.isEmpty()) {
                        views.setViewVisibility(R.id.appwidget_menu_cost, View.GONE)
                    } else {
                        views.setViewVisibility(R.id.appwidget_menu_cost, View.VISIBLE)
                        views.setTextViewText(R.id.appwidget_menu_cost, menuCost)
                    }
                    val menuItems =
                        if (selectedRestaurant == "샛벌회관") {
                            menuDetailRaw.split("/").map { it.trim() }
                        } else {
                            menuDetailRaw.split("\n").map { it.trim() }
                        }
                    val trimItems =
                        if (menuItems.last().isBlank()) {
                            menuItems.dropLast(1)
                        } else {
                            menuItems
                        }
                    val formattedMenuItem = trimItems.joinToString("\n") { "\u2022 $it" }
                    views.setTextViewText(R.id.appwidget_menu_detail, formattedMenuItem)
                } else {
                    views.setTextViewText(R.id.appwidget_menu_type, currentMealCode)
                    views.setTextViewText(R.id.appwidget_menu_detail, "식단 정보가 없습니다.")
                }
            }

            is DormitoryResponse -> {
                val item =
                    result.results.firstOrNull {
                        it.properties.codeNm.rich_text
                            ?.firstOrNull()
                            ?.plain_text == currentMealCode
                    }
                views.setViewVisibility(R.id.appwidget_menu_cost, View.GONE)
                if (item != null) {
                    val mealType =
                        item.properties.codeNm.rich_text
                            ?.firstOrNull()
                            ?.plain_text ?: "식사 시간 정보 없음"
                    val menuDetailRaw =
                        item.properties.mealNm.rich_text
                            ?.firstOrNull()
                            ?.plain_text ?: "메뉴 정보 없음"
                    val menuItems =
                        if (selectedRestaurant in listOf("비마관", "진리관")) {
                            val splitList = menuDetailRaw.split("/").map { it.trim() }
                            if (splitList.size > 2) {
                                val (mainItems, calorieItems) =
                                    splitList.dropLast(2) to
                                        splitList.takeLast(
                                            2,
                                        )
                                (mainItems + listOf(calorieItems.joinToString(" / ")))
                            } else {
                                splitList
                            }
                        } else {
                            menuDetailRaw.split("\n").map { it.trim() }
                        }
                    val formattedMenuItem = menuItems.joinToString("\n") { "\u2022 $it" }
                    views.setTextViewText(R.id.appwidget_title, selectedRestaurant)
                    views.setTextViewText(R.id.appwidget_menu_type, mealType)
                    views.setTextViewText(R.id.appwidget_menu_detail, formattedMenuItem)
                } else {
                    views.setTextViewText(R.id.appwidget_title, selectedRestaurant)
                    views.setTextViewText(R.id.appwidget_menu_type, currentMealCode)
                    views.setTextViewText(R.id.appwidget_menu_detail, "식단 정보가 없습니다.")
                }
            }

            else -> {
                views.setTextViewText(R.id.appwidget_title, selectedRestaurant)
                views.setTextViewText(R.id.appwidget_menu_type, "오류")
                views.setTextViewText(R.id.appwidget_menu_detail, "데이터를 불러올 수 없습니다.")
            }
        }
        val intent =
            Intent(context, BDSAppWidgetConfigureActivity::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}

private fun getFormattedDate(): String {
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormatter.format(calendar.time)
}
