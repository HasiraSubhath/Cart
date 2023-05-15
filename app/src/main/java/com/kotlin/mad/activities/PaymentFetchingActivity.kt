package com.kotlin.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.mad.adapters.PaymentAdapter
import com.kotlin.mad.models.PaymentModel
import com.kotlin.mad.R
import com.google.firebase.database.*

class PaymentFetchingActivity : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var paymentList: ArrayList<PaymentModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_fetching)

        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        paymentList = arrayListOf<PaymentModel>()

        getPaymentData()


    }

    private fun getPaymentData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("PaymentDB")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               paymentList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val paymentData = empSnap.getValue(PaymentModel::class.java)
                        paymentList.add(paymentData!!)
                    }
                    val mAdapter = PaymentAdapter(paymentList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : PaymentAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@PaymentFetchingActivity, PaymentDetailsActivity::class.java)

                            //put extra(passing data to another activity)
                            intent.putExtra("cId", paymentList[position].cId)
                            intent.putExtra("cName", paymentList[position].cName)
                            intent.putExtra("cNumber", paymentList[position].cNumber)
                            intent.putExtra("cCvv", paymentList[position].cCvv)
                            intent.putExtra("cDate", paymentList[position].cDate)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}