package com.kotlin.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kotlin.mad.R
import com.kotlin.mad.models.PaymentModel
import com.google.firebase.database.FirebaseDatabase

class PaymentDetailsActivity : AppCompatActivity() {

    private lateinit var tvCId: TextView
    private lateinit var tvCName: TextView
    private lateinit var tvCNumber: TextView
    private lateinit var tvCCvv: TextView
    private lateinit var tvCDate: TextView

    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("cId").toString(),
                intent.getStringExtra("cName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("cId").toString()
            )
        }

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("PaymentDB").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, " data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, PaymentFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }





    private fun initView() {
        tvCId = findViewById(R.id.tvCId)
        tvCName = findViewById(R.id.tvCName)
        tvCNumber = findViewById(R.id.tvCNumber)
        tvCCvv = findViewById(R.id.tvCCvv)
        tvCDate = findViewById(R.id.tvCDate)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        //passing data
        tvCId.text = intent.getStringExtra("cId")
        tvCName.text = intent.getStringExtra("cName")
        tvCNumber.text = intent.getStringExtra("cNumber")
        tvCCvv.text = intent.getStringExtra("cCvv")
        tvCDate.text = intent.getStringExtra("cDate")

    }

    private fun openUpdateDialog(
        cId: String,
        cName: String

    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etCName = mDialogView.findViewById<EditText>(R.id.etCName)
        val etCNumber = mDialogView.findViewById<EditText>(R.id.etCNumber)
        val etCCvv = mDialogView.findViewById<EditText>(R.id.etCCvv)
        val etCDate = mDialogView.findViewById<EditText>(R.id.etCDate)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        //update
        etCName.setText(intent.getStringExtra("cName").toString())
        etCNumber.setText(intent.getStringExtra("cNumber").toString())
        etCCvv.setText(intent.getStringExtra("cCvv").toString())
        etCDate.setText(intent.getStringExtra("cDate").toString())

        mDialog.setTitle("Updating $cName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updatePaymentData(
                cId,
                etCName.text.toString(),
                etCNumber.text.toString(),
                etCCvv.text.toString(),
                etCDate.text.toString()

            )

            Toast.makeText(applicationContext, " Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our text views
            tvCName.text = etCName.text.toString()
            tvCNumber.text = etCNumber.text.toString()
            tvCCvv.text = etCCvv.text.toString()
            tvCDate.text = etCDate.text.toString()

            alertDialog.dismiss()

        }

    }

    private fun updatePaymentData(
        id: String,
        name: String,
        number: String,
        cvv: String,
        date: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("PaymentDB").child(id)
        val paymentIno = PaymentModel(id, name, number, cvv, date)
        dbRef.setValue(paymentIno)
    }
}