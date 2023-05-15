package com.kotlin.mad.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.kotlin.mad.models.CartModel
import com.kotlin.mad.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CartInsertionActivity : AppCompatActivity() {

    //initializing variables

    private lateinit var etPName: EditText
    private lateinit var etPType: EditText
    private lateinit var etPPrice: EditText
    private lateinit var etPQty: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etPName = findViewById(R.id.etPName)
        etPType = findViewById(R.id.etPType)
        etPPrice = findViewById(R.id.etPPrice)
        etPQty = findViewById(R.id.etPQty)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("CartDB")

        btnSaveData.setOnClickListener {
            saveCartData()
        }

    }

    private fun saveCartData() {

        //Geting Values
        val pName = etPName.text.toString()
        val pType = etPType.text.toString()
        val pPrice = etPPrice.text.toString()
        val pQty = etPQty.text.toString()

        //validation
        if (pName.isEmpty() || pType.isEmpty() || pPrice.isEmpty() || pQty.isEmpty()) {

            if (pName.isEmpty()) {
                etPName.error = "Please Product Name"
            }
            if (pType.isEmpty()) {
                etPType.error = "Please Product Type"
            }
            if (pPrice.isEmpty()) {
                etPPrice.error = "Please Product Price"
            }
            if (pQty.isEmpty()) {
                etPQty.error = "Please Enter Product QTY"
            }
            Toast.makeText(this, "please check Some areas are not filled", Toast.LENGTH_LONG).show()
        } else {

            //genrate unique ID
            val pId = dbRef.push().key!!

            val Cart = CartModel(pId, pName, pType, pPrice, pQty)

            dbRef.child(pId).setValue(Cart)
                .addOnCompleteListener {
                    Toast.makeText(this, "All  details insert successfully", Toast.LENGTH_SHORT).show()

                    //clear data after insert
                    etPName.text.clear()
                    etPType.text.clear()
                    etPPrice.text.clear()
                    etPQty.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}