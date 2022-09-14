package com.shinedev.digitalent.appwidget

import android.content.Intent
import android.widget.RemoteViewsService

class WidgetRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
            WidgetRemoteViewsFactory(this.applicationContext)
}