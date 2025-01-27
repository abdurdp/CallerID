package com.test.caller.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import androidx.core.app.NotificationCompat
import com.test.caller.repository.ContactRepository

class MyCallScreeningService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        Log.d("MyCallScreeningService", "onScreenCall: ${callDetails.handle}")

        val number = callDetails.handle.schemeSpecificPart
        val repository = ContactRepository(applicationContext)
        val callerInfo = repository.getContactByNumber(number)

        val response = CallResponse.Builder()

        if (callerInfo != null && callerInfo.isBlocked) {

            response.setDisallowCall(true)
            Log.d("MyCallScreeningService", "Blocked call from: $number")
        } else {
            val name = callerInfo?.name ?: "Unknown"
            showIncomingCallNotification(applicationContext, name, number)
            response.setDisallowCall(false)
            Log.d("MyCallScreeningService", "Allowed call from: $number")
        }

        respondToCall(callDetails, response.build())
    }

    private fun showIncomingCallNotification(context: Context, name: String, number: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "incoming_call",
                "Incoming Call",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "incoming_call")
            .setContentTitle("Incoming Call")
            .setContentText("Name: $name, Number: $number")
            .setSmallIcon(android.R.drawable.sym_call_incoming)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }

}
