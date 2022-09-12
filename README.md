# CartNShop

## Description
* This is an e-commerce(shopping) app made with Kotlin and Firebase.
* You can buy and sell products easily.

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

### Cart Items(Products that user added them to him/her cart)
![dbs_cartItems](https://user-images.githubusercontent.com/93993257/189740119-3b544434-ba6e-4856-9365-a2bdce749254.PNG)

### User Addresses
![dbs_addresses](https://user-images.githubusercontent.com/93993257/189740265-9976f1a1-608e-4a39-8b5b-6e189204ccc5.PNG)

### Sold Products
![dbs_soldProducts](https://user-images.githubusercontent.com/93993257/189740407-b606a000-92cf-4443-b237-80089b6668e1.PNG)

### Orders
![dbs_orders](https://user-images.githubusercontent.com/93993257/189740793-18101102-f3ed-4ff2-93b4-2b538832e611.PNG)

## Some Screenshots of app(not the whole app)
![LoginSS](https://user-images.githubusercontent.com/93993257/189743324-0f6dc4e8-de64-4fe0-b270-2b837f5e2127.png)

![RegisterSS](https://user-images.githubusercontent.com/93993257/189743369-f27f283f-a168-44c0-a30a-438b8d253edc.png)

![buySS](https://user-images.githubusercontent.com/93993257/189743460-03720f45-8451-4c3d-a07d-2c01d05ac762.png)

![detailsSS](https://user-images.githubusercontent.com/93993257/189743615-9cef0a88-ebbc-47d8-999f-9d406f41e4b5.png)

![cartSS](https://user-images.githubusercontent.com/93993257/189743573-8ea78d6d-ee32-4fd5-acb2-18ec12b6a3b2.png)

![orderDetailsSS](https://user-images.githubusercontent.com/93993257/189743736-bb2dd5ab-4bb8-4082-9deb-1f7bd5e0ae41.png)

![sellSS](https://user-images.githubusercontent.com/93993257/189743954-f8907bc0-1238-4b5b-9e41-0e1120d34e3f.png)

![addProductSS](https://user-images.githubusercontent.com/93993257/189743985-3de6e7ef-669c-46fe-983c-7eb45e6adeb0.png)

![soldProductsSS](https://user-images.githubusercontent.com/93993257/189744223-69701eeb-fd86-46c0-bc36-d95897010bdf.png)


## Build
* Clone this repository.
* Get you own `google-services.json` file from Firebase.
* Put it into Project/app. And run the app.

