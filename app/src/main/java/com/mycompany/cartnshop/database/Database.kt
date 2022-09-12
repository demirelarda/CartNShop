package com.mycompany.cartnshop.database

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mycompany.cartnshop.model.*
import com.mycompany.cartnshop.ui.activities.*
import com.mycompany.cartnshop.ui.fragments.DashboardFragment
import com.mycompany.cartnshop.ui.fragments.OrdersFragment
import com.mycompany.cartnshop.ui.fragments.ProductFragment
import com.mycompany.cartnshop.utils.Constants
import kotlinx.android.synthetic.main.activity_user_profile.*

class Database {

    private val db = FirebaseFirestore.getInstance()


    fun registerUser(activity: RegisterActivity, userInfo: User) {

        db.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressBar()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getUserID() : String{

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if(currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID

    }

    fun getCurrentUserDetails(activity:Activity){

        db.collection("users")
            .document(getUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(javaClass.simpleName, document.toString())
                val user = document.toObject(User::class.java)

                //Save Name and Surname to SP.
                val sharedPreferences = activity.getSharedPreferences(Constants.SHOP_PREFERENCES,Context.MODE_PRIVATE)
                sharedPreferences.edit().putString(Constants.CURRENT_NAME, user!!.firstName).apply()
                sharedPreferences.edit().putString(Constants.CURRENT_SURNAME,user.lastName).apply()


                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                }
                when(activity){
                    is UserProfileActivity -> {
                        activity.et_email.setText(user.email)
                        activity.et_first_name.setText(user.firstName)
                        activity.et_last_name.setText(user.lastName)
                    }

                    is SettingsActivity ->{
                        activity.userDetailsSuccess(user)
                    }

                }
            }


    }

    fun updateProfileDetails(activity: Activity,userDetailsHashmap : HashMap<String,Any>){

        db.collection("users").document(getUserID())
            .update(userDetailsHashmap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity ->{
                        activity.userDetailsUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener {
                when(activity){
                    is UserProfileActivity ->{
                        activity.hideProgressBar()
                    }

                    is SettingsActivity ->{
                        activity.hideProgressBar()
                    }

                }
                println("Error while updating datas.")
            }





    }

    fun uploadImageToStorage(activity: Activity, imageUri : Uri, imageType: String){
        val storage : StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis()+"."+
                    Constants.getFileExt(
                        activity, imageUri
                    )
        )
        storage.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())

                        when (activity) {
                            is UserProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                            is AddProductActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }

                    }
            }
            .addOnFailureListener { exception ->

                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressBar()
                    }
                    is AddProductActivity -> {
                        activity.hideProgressBar()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }


    }


    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product){

        db.collection("products")
            .document()
            .set(productInfo,SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressBar()
                Toast.makeText(activity,"Error!",Toast.LENGTH_LONG).show()
            }


    }

    fun getProductList(fragment: Fragment){
        db.collection("products")
            .whereEqualTo("user_id",getUserID())
            .get()
            .addOnSuccessListener { document->
                val productList : ArrayList<Product> = ArrayList()
                for(p in document.documents){
                    val product = p.toObject(Product::class.java)
                    product!!.product_id = p.id
                    productList.add(product)
                }

                when(fragment){
                    is ProductFragment ->{
                        fragment.successProductListFS(productList)
                    }
                }
            }

    }

    fun getItemsForDashboard(fragment: DashboardFragment){
        db.collection("products")
            .get()
            .addOnSuccessListener { document ->
                println(document.documents.toString())
                val productList : ArrayList<Product> = ArrayList()
                for(p in document.documents){
                    val product = p.toObject(Product::class.java)
                    product!!.product_id = p.id
                    productList.add(product)
                }
                fragment.successDashboardList(productList)
            }
            .addOnFailureListener {
                fragment.hideProgressBar()
                println("Error with dashboard items.")
            }
    }

    fun deleteProduct(fragment: ProductFragment, productId: String){
        db.collection("products")
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.productDeleteSuccess()


            }.addOnFailureListener {
                println("Error while deleting product")
                fragment.hideProgressBar()
            }


    }

    fun getProductDetails(activity: ProductDetailsActivity, productId: String){
        db.collection("products")
            .document(productId)
            .get()
            .addOnSuccessListener {document->
                val product = document.toObject(Product::class.java)
                activity.productDetailsSuccess(product!!)

            }
            .addOnFailureListener {
                println("Error while getting product details!")
                activity.hideProgressBar()
            }


    }

    fun updateProduct(activity: AddProductActivity, productList: HashMap<String,Any>, product: Product){
        db.collection("products").document(product.product_id)
            .update(productList)
            .addOnSuccessListener {
                activity.productUpdateSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressBar()
                println("Failed to update product."+it.toString())
            }

    }


    fun addItemToCart(activity: ProductDetailsActivity, addToCart: Cart){

        db.collection("cart_items")
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSuccess()

            }.addOnFailureListener {
                println("Error while creating the cart doc.")
            }


    }

    fun checkIfItemExistsInCart(activity: ProductDetailsActivity, productId: String){

        db.collection("cart_items")
            .whereEqualTo("user_id",getUserID())
            .whereEqualTo("product_id",productId)
            .get()
            .addOnSuccessListener { document->
                if(document.documents.size>0){
                    activity.productExistsInCard()
                }
                else{
                    activity.hideProgressBar()
                }

            }
            .addOnFailureListener {
                activity.hideProgressBar()
            }



    }

    fun getAllProductsList(activity: Activity){
        db.collection("products")
            .get()
            .addOnSuccessListener { document ->

                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }

                when (activity) {
                    is CartListActivity -> {
                        activity.successProductListFromFS(productsList)
                    }



                    is CheckoutActivity -> {
                        activity.successProductListFromFS(productsList)
                    }

                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is CartListActivity -> {
                        activity.hideProgressBar()
                    }


                    is CheckoutActivity -> {
                        activity.hideProgressBar()
                    }
                }

            }
    }



    fun getCartList(activity: Activity){
        db.collection("cart_items")
            .whereEqualTo("user_id",getUserID())
            .get()
            .addOnSuccessListener {document->
                val list: ArrayList<Cart> = ArrayList()
                for (cart in document.documents){
                    val cartItem = cart.toObject(Cart::class.java)
                    cartItem!!.id = cart.id
                    list.add(cartItem)
                }

                when(activity){
                    is CartListActivity ->{
                        activity.successCartItemList(list)
                    }
                    is CheckoutActivity ->{
                        activity.successCartItemList(list)
                    }
                }
            }.addOnFailureListener {
                when(activity){
                    is CartListActivity ->{
                        activity.hideProgressBar()
                    }
                    is CheckoutActivity ->{
                        activity.hideProgressBar()
                    }
                }
            }


    }


    fun updateCartList(context: Context,cartId: String,itemHashMap: HashMap<String,Any>){
        db.collection("cart_items")
            .document(cartId)
            .update(itemHashMap)
            .addOnSuccessListener {
                when(context){
                    is CartListActivity ->{
                        context.itemUpdateSuccess()
                    }
                }


            }
            .addOnFailureListener { when(context){
                is CartListActivity ->{
                    context.hideProgressBar()
                }
            } }

    }

   fun removeItemInCart(context: Context,cartId: String){
       db.collection("cart_items")
           .document(cartId)
           .delete()
           .addOnSuccessListener {
               when(context){
                   is CartListActivity ->{
                       context.itemRemovedSuccess()
                   }
               }


           }.addOnFailureListener {
               when(context){
                   is CartListActivity ->{
                       context.hideProgressBar()
                       println("Error while removing cart Item.")
                   }
               }
           }
   }


    fun addAddress(activity: EditAddAddressActivity, addressInfo: Address){

        db.collection("addresses")
            .document()
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.editAddAddressToDBSuccess()
            }.addOnFailureListener {
                activity.hideProgressBar()
            }
    }

    fun getAddresses(activity: AddressActivity){
        db.collection("addresses")
            .whereEqualTo("userId",getUserID())
            .get()
            .addOnSuccessListener {document->
                val addressList : ArrayList<Address> = ArrayList()
                for(i in document.documents){
                    val address = i.toObject(Address::class.java)
                    address!!.id = i.id //burada her adress diye bir adres objesi oluşturup bu objeye id veriyoruz. Bu id (i.id) belgemizin ismi. Yani random bir id.
                    addressList.add(address)
                }
                activity.addressListFromDBSuccess(addressList)

            }
            .addOnFailureListener {
                activity.hideProgressBar()
            }
    }


    fun updateAddress(activity: EditAddAddressActivity, addressInfo: Address, addressId: String) {

        db.collection("addresses")
            .document(addressId)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.editAddAddressToDBSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressBar()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the Address.",
                    e
                )
            }
    }

    fun deleteAddress(activity: AddressActivity, addressId: String){
        db.collection("addresses")
            .document(addressId)
            .delete()
            .addOnSuccessListener {
                activity.deleteAddressSuccessful()
            }
            .addOnFailureListener {
                activity.hideProgressBar()
            }
    }


    fun createOrder(activity: CheckoutActivity, order: Order){
        db.collection("orders")
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.orderCreatedSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressBar()
            }
    }

    fun updateProductCartDetails(activity: CheckoutActivity, cartList: ArrayList<Cart>, order: Order){
        val write = db.batch()

        for (cart in cartList) {

            val soldProduct = SoldProduct(
                cart.product_owner_id,
                cart.title,
                cart.price,
                cart.cart_quantity,
                cart.image,
                order.title,
                order.order_date,
                order.sub_total_amount,
                order.shipping_charge,
                order.total_amount,
                order.address
            )

            val documentReference = db.collection("sold_products")
                .document(cart.product_id)

            write.set(documentReference, soldProduct)
        }

        for (cart in cartList) {

            val documentReference = db.collection("cart_items")
                .document(cart.id)
            write.delete(documentReference)
        }

        write.commit().addOnSuccessListener {

            activity.cartDetailsUpdatedSuccessfully()

        }.addOnFailureListener {
            activity.hideProgressBar()
        }

    }

    fun getOrderList(fragment: OrdersFragment){
        db.collection("orders")
            .whereEqualTo("user_id",getUserID())
            .get()
            .addOnSuccessListener { document->
                val list: ArrayList<Order> = ArrayList()
                for(i in document.documents){
                    val orderItem = i.toObject(Order::class.java)
                    orderItem!!.id = i.id
                    list.add(orderItem)
                }
                fragment.showOrderList(list)

            }
            .addOnFailureListener {
                fragment.hideProgressBar()
            }

    }

    fun getSoldProductsList(activity: SoldProductsActivity){
        db.collection("sold_products")
            .whereEqualTo("user_id",getUserID())
            .get()
            .addOnSuccessListener {document->
                val list: ArrayList<SoldProduct> = ArrayList()
                for(i in document.documents){
                    val soldProduct = i.toObject(SoldProduct::class.java)!!
                    soldProduct.id = i.id
                    list.add(soldProduct)
                }
                activity.successSoldProductList(list)

            }
            .addOnFailureListener {
                println("error while getting sold products")
                it.printStackTrace()
                activity.hideProgressBar()
            }
    }








}