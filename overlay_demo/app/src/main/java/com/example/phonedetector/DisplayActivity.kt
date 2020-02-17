package com.example.phonedetector


import android.app.Activity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_sample.*

class DisplayActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MY_APP", "Create activity")

        setContentView(R.layout.activity_sample)

        val extras = intent.extras

        var incomingNumber: String? = ""
        var incomingName: String? = ""
        var displayString = ""

        if (extras!!.containsKey("number")) {
            incomingNumber = intent.getStringExtra("number")
            displayString = displayString + "INCOMING NUMBER : " + incomingNumber + "\n"
        }
        if (extras.containsKey("name")) {
            incomingName = intent.getStringExtra("name")
            displayString = "$displayString NAME : $incomingName\n"
        }
        textView.setText(displayString)
        button.setOnClickListener { finish() }
    }
}