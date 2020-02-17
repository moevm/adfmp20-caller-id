package com.example.phonedetector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast


class PhoneStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MY_APP", "Received")
        Toast.makeText(context,"Receiver Started",Toast.LENGTH_SHORT).show();
        Log.d("MY_APP", "After toast")

        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        val contactName = getContactName(incomingNumber, context)

        val mIntent = Intent(context, DisplayActivity::class.java)
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        mIntent.putExtra("number", incomingNumber)
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        mIntent.action = Intent.ACTION_MAIN
        mIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        Log.d("MY_APP", "Created Intent")

        if (contactName != null && contactName.isNotEmpty()) {
            mIntent.putExtra("name", contactName)
        }


        if (incomingNumber != null) {
            Log.d("MY_APP", "Phone $incomingNumber")

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                Toast.makeText(context,"Ringing State. Number : $incomingNumber",Toast.LENGTH_SHORT).show()
                Log.d("MY_APP", "After toast with number")

                Handler().postDelayed({ context.startActivity(mIntent) }, 200)
            }
            if (state == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                Toast.makeText(context, "Offhook State", Toast.LENGTH_SHORT).show()
            }
            if (state == TelephonyManager.EXTRA_STATE_IDLE) {
                //Toast.makeText(context,"Idle State",Toast.LENGTH_SHORT).show();
            }
        }


    }

    fun getContactName(phoneNumber: String?, context: Context): String? {
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
        var contactName = ""
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0)
            }
            cursor.close()
        }
        return contactName
    }
}
