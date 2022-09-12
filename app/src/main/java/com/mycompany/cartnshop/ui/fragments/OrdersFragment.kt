package com.mycompany.cartnshop.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.adapter.OrderAdapter
import com.mycompany.cartnshop.database.Database
import com.mycompany.cartnshop.model.Order
import kotlinx.android.synthetic.main.fragment_orders.*


class OrdersFragment() : UiComponentsFragment() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onResume() {
        super.onResume()
        getOrderList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_orders, container, false)

    }

    fun showOrderList(orderList: ArrayList<Order>){
        hideProgressBar()
        if(orderList.size>0){
            rv_my_order_items.visibility = View.VISIBLE
            tv_no_orders_found.visibility = View.GONE

            rv_my_order_items.layoutManager = LinearLayoutManager(activity)
            rv_my_order_items.setHasFixedSize(true)

            val orderListAdapter = OrderAdapter(requireActivity(),orderList)
            rv_my_order_items.adapter = orderListAdapter
        }
        else{
            rv_my_order_items.visibility = View.GONE
            tv_no_orders_found.visibility = View.VISIBLE
        }
    }

    private fun getOrderList(){
        showProgressBar(getString(R.string.please_wait))
        Database().getOrderList(this)
    }






}