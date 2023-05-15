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
import com.kotlin.mad.models.CartModel
import com.google.firebase.database.FirebaseDatabase

class CartDetailsActivity : AppCompatActivity() {

    private lateinit var tvPId: TextView
    private lateinit var tvPName: TextView
    private lateinit var tvPType: TextView
    private lateinit var tvPPrice: TextView
    private lateinit var tvPQty: TextView

    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("pId").toString(),
                intent.getStringExtra("pName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("pId").toString()
            )
        }

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("CartDB").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, " data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, CartFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }





    private fun initView() {
        tvPId = findViewById(R.id.tvPId)
        tvPName = findViewById(R.id.tvPName)
        tvPType = findViewById(R.id.tvPType)
        tvPPrice = findViewById(R.id.tvPPrice)
        tvPQty = findViewById(R.id.tvPQty)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        //passing data
        tvPId.text = intent.getStringExtra("pId")
        tvPName.text = intent.getStringExtra("pName")
        tvPType.text = intent.getStringExtra("pType")
        tvPPrice.text = intent.getStringExtra("pPrice")
        tvPQty.text = intent.getStringExtra("pQty")

    }

    private fun openUpdateDialog(
        pId: String,
        pName: String

    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etPName = mDialogView.findViewById<EditText>(R.id.etPName)
        val etPType = mDialogView.findViewById<EditText>(R.id.etPType)
        val etPPrice = mDialogView.findViewById<EditText>(R.id.etPPrice)
        val etPQty = mDialogView.findViewById<EditText>(R.id.etPQty)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        //update
        etPName.setText(intent.getStringExtra("pName").toString())
        etPType.setText(intent.getStringExtra("pType").toString())
        etPPrice.setText(intent.getStringExtra("pPrice").toString())
        etPQty.setText(intent.getStringExtra("pQty").toString())

        mDialog.setTitle("Updating $pName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateCartData(
                pId,
                etPName.text.toString(),
                etPType.text.toString(),
                etPPrice.text.toString(),
                etPQty.text.toString()

            )

            Toast.makeText(applicationContext, " Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our text views
            tvPName.text = etPName.text.toString()
            tvPType.text = etPType.text.toString()
            tvPPrice.text = etPPrice.text.toString()
            tvPQty.text = etPQty.text.toString()

            alertDialog.dismiss()

        }

    }

    private fun updateCartData(
        id: String,
        name: String,
        type: String,
        price: String,
        qty: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("CartDB").child(id)
        val CartIno = CartModel(id, name, type, price, qty)
        dbRef.setValue(CartIno)
    }
}