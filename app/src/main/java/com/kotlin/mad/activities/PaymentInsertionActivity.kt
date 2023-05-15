package com.kotlin.mad.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.kotlin.mad.models.PaymentModel
import com.kotlin.mad.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PaymentInsertionActivity : AppCompatActivity() {

    //initializing variables

    private lateinit var etCName: EditText
    private lateinit var etCNumber: EditText
    private lateinit var etCCvv: EditText
    private lateinit var etCDate: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etCName = findViewById(R.id.etCName)
        etCNumber = findViewById(R.id.etCNumber)
        etCCvv = findViewById(R.id.etCCvv)
        etCDate = findViewById(R.id.etCDate)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("PaymentDB")

        btnSaveData.setOnClickListener {
            savePaymentData()
        }

    }

    private fun savePaymentData() {

        //Geting Values
        val cName = etCName.text.toString()
        val cNumber = etCNumber.text.toString()
        val cCvv = etCCvv.text.toString()
        val cDate = etCDate.text.toString()

        //validation
        if (cName.isEmpty() || cNumber.isEmpty() || cCvv.isEmpty() || cDate.isEmpty()) {

            if (cName.isEmpty()) {
                etCName.error = "Please enter Card Holders Name"
            }
            if (cNumber.isEmpty()) {
                etCNumber.error = "Please Card Number"
            }
            if (cCvv.isEmpty()) {
                etCCvv.error = "Please Enter CVV"
            }
            if (cDate.isEmpty()) {
                etCDate.error = "Please Enter Expire year month"
            }
            Toast.makeText(this, "please check Some areas are not filled", Toast.LENGTH_LONG).show()
        } else {

            //genrate unique ID
            val cId = dbRef.push().key!!

            val payment = PaymentModel(cId, cName, cNumber, cCvv, cDate)

            dbRef.child(cId).setValue(payment)
                .addOnCompleteListener {
                    Toast.makeText(this, "All card details insert successfully", Toast.LENGTH_SHORT).show()

                    //clear data after insert
                    etCName.text.clear()
                    etCNumber.text.clear()
                    etCCvv.text.clear()
                    etCDate.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}