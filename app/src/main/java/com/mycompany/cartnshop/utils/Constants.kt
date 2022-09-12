package com.mycompany.cartnshop.utils

import android.app.Activity
import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap

object Constants {

    //SP values
    const val SHOP_PREFERENCES: String = "ShopPreferences"
    const val CURRENT_NAME: String = "CurrentName"
    const val CURRENT_SURNAME: String = "CurrentSurname"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val DEFAULT_CART_QUANTITY: String = "1"
    const val ADD_ADDRESS_REQUEST_CODE: Int = 121


    //get file extension
    fun getFileExt(activity: Activity,uri: Uri?):String?{
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }


}