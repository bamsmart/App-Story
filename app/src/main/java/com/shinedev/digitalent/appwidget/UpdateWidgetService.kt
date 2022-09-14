package com.shinedev.digitalent.appwidget

import android.app.job.JobParameters
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.ComponentName


class UpdateWidgetService : JobService() {
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
        jobFinished(params, false)
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return true
    }
}