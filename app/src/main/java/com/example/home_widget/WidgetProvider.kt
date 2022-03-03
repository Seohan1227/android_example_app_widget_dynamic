package com.example.home_widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class WidgetProvider : AppWidgetProvider() {

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action == ACTION_UPDATE) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(context
                ?.let { ComponentName(it,this::class.java) })

            onUpdate(context,appWidgetManager,appWidgetIds)
        }
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        appWidgetIds?.forEach { appWidgetId ->
            val remoteViews = RemoteViews(context?.packageName, R.layout.widget_main)
            remoteViews.setOnClickPendingIntent(R.id.button1,updateWidget(context))
            updateRandomView(context,remoteViews)
            appWidgetManager?.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    private fun updateWidget(context: Context?): PendingIntent {
        val intent = Intent(context,WidgetProvider::class.java)
        intent.action = ACTION_UPDATE
        return PendingIntent.getBroadcast(context,0,intent,flags)
    }

    private fun updateRandomView(context: Context?, parentRemoteViews: RemoteViews) {
        context?.let {
            val range = (1..7)
            val packageName = context.packageName
            for (i in 1..3) {
                val id = it.resources.getIdentifier("linearLayoutWidget$i", "id", packageName)
                parentRemoteViews.removeAllViews(id)
                for (i2 in 1..range.random()) {
                    val childRemoteViews = RemoteViews(packageName,R.layout.widget_item)
                    childRemoteViews.setTextViewText(R.id.textView1,i2.toString())
                    parentRemoteViews.addView(id,childRemoteViews)
                }
            }
        }
    }

    companion object {
        const val ACTION_UPDATE = "actionUpdate"
        const val flags = PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
    }
}