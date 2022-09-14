package com.shinedev.digitalent.appwidget

import android.app.job.JobParameters
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.graphics.Bitmap
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList


class UpdateWidgetService : JobService() {
    private val mWidgetItems: List<Bitmap> = ArrayList()

    override fun onStartJob(params: JobParameters): Boolean {
        val widgetManager = AppWidgetManager.getInstance(this)
        val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
            ComponentName(
                application,
                CollectionWidgetProvider::class.java
            )
        )
        val myWidget = CollectionWidgetProvider()
        myWidget.onUpdate(applicationContext, widgetManager, ids)
        Log.d("Services Running ", "" + Calendar.getInstance().time.toString())
        jobFinished(params, false)
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return true
    }
}