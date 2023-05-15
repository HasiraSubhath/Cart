package com.kotlin.mad.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.mad.adapters.CartAdapter
import com.kotlin.mad.models.CartModel
import com.kotlin.mad.R
import com.google.firebase.database.*

class CartFetchingActivity : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var CartList: ArrayList<CartModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_fetching)

        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        CartList = arrayListOf<CartModel>()

        getCartData()


    }

    private fun getCartData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("CartDB")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               CartList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val CartData = empSnap.getValue(CartModel::class.java)
                        CartList.add(CartData!!)
                    }
                    val mAdapter = CartAdapter(CartList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : CartAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@CartFetchingActivity, CartDetailsActivity::class.java)

                            //put extra(passing data to another activity)
                            intent.putExtra("pId", CartList[position].pId)
                            intent.putExtra("pName", CartList[position].pName)
                            intent.putExtra("pType", CartList[position].pType)
                            intent.putExtra("pPrice", CartList[position].pPrice)
                            intent.putExtra("pQty", CartList[position].pQty)
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