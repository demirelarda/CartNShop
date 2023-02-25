# CartNShop

## Description
* This is an e-commerce(shopping) app made with Kotlin and Firebase.
* You can buy and sell products easily.
* Note: You can see the [build information and release apk file](https://github.com/demirelarda/CartNShop/blob/master/README.md#release-v100download-apk-file) at the bottom of this file.

## Key Features
* App includes all the necessary features that a modern shopping app needs to has.
* Buy products, add them into your cart.
* Sell products.
* Product sellers can see the details when some customer bought their product.(Order details like, address, cost etc.)
* Product sellers can determine the stock quantity.
* Users can't buy more than stock quantity.
* Users can add multiple addresses under some categories(Home,Office,Other)
* Users can update their profiles anytime they want.
* Product Owners can't buy their own products.

## Technical Details
* App has a clean design. UIs and Database are completely separated by each other.
* There is a progress bar system so internet proccesses never block UI.
* Used Firebase Authentication for login/register systems.
* Used Firestore for database system.
* Used Firebase Storage for storage.

## Database Structure
### Users
![dbs_users](https://user-images.githubusercontent.com/93993257/189739553-a7155485-811c-4390-a85d-9ba5e7dce7ec.PNG)

### Products
![dbs_products](https://user-images.githubusercontent.com/93993257/189739956-08e76308-cfa9-498d-beb2-391486a28bae.PNG)

### In-Cart Items
![dbs_cartItems](https://user-images.githubusercontent.com/93993257/189740119-3b544434-ba6e-4856-9365-a2bdce749254.PNG)

### User Addresses
![dbs_addresses](https://user-images.githubusercontent.com/93993257/189740265-9976f1a1-608e-4a39-8b5b-6e189204ccc5.PNG)

### Sold Products
![dbs_soldProducts](https://user-images.githubusercontent.com/93993257/189740407-b606a000-92cf-4443-b237-80089b6668e1.PNG)

### Orders
![dbs_orders](https://user-images.githubusercontent.com/93993257/189740793-18101102-f3ed-4ff2-93b4-2b538832e611.PNG)

## Build
* Clone this repository.
* Get your own `google-services.json` file from Firebase.
* Put it into Project/app. And run the app.

## Release v1.0.0(Download APK File)
* [https://github.com/demirelarda/CartNShop/releases/tag/v1.0.0](https://github.com/demirelarda/CartNShop/releases/download/v1.0.0/CartNShopV1.0.apk)

