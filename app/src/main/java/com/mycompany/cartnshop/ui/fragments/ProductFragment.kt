package com.mycompany.cartnshop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.adapter.ProductsListAdapter
import com.mycompany.cartnshop.database.Database
import com.mycompany.cartnshop.model.Product
import com.mycompany.cartnshop.ui.activities.AddProductActivity
import com.mycompany.cartnshop.ui.activities.SoldProductsActivity
import kotlinx.android.synthetic.main.fragment_products.*


class ProductFragment : UiComponentsFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onResume() {
        super.onResume()
        getProductList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_top_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
            R.id.sold_products ->{
                startActivity(Intent(activity, SoldProductsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successProductListFS(productList: ArrayList<Product>){
        hideProgressBar()
        if(productList.size>0){
            rv_my_product_items.visibility = View.VISIBLE
            tv_no_products_found.visibility = View.GONE

            rv_my_product_items.layoutManager = LinearLayoutManager(activity)
            rv_my_product_items.setHasFixedSize(true)
            val adapterProducts = ProductsListAdapter(requireActivity(),productList,this)
            rv_my_product_items.adapter = adapterProducts
        }else{
            rv_my_product_items.visibility = View.GONE
            tv_no_products_found.visibility = View.VISIBLE
        }



    }

    fun getProductList(){
        showProgressBar(resources.getString(R.string.please_wait))
        Database().getProductList(this)


    }



    fun deleteProduct(productId: String) {

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.delete_product_title))
        builder.setMessage(getString(R.string.are_you_sure_want_to_delete_product))
        builder.setIcon(R.drawable.ic_baseline_warning_24)
        builder.setPositiveButton(getString(R.string.yes)){ d, _ ->
            showProgressBar(getString(R.string.please_wait))
            Database().deleteProduct(this,productId)
            d.dismiss()
        }
        builder.setNegativeButton(getString(R.string.no)){ d, _ ->
            d.dismiss()

        }
        val alert : AlertDialog = builder.create()
        alert.setCancelable(false)
        alert.show()


    }


    fun productDeleteSuccess(){
        hideProgressBar()
        getProductList()

    }



}
