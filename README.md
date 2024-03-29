# Dish app
## Details and Description
### Functionality
Dish app allows you:
* manually add new dishes to the "all dishes" tab
* add/edit details about each dish: photo(from gallery or camera), title, type, category, ingredients, cooking time, directions to cook
* filter dishes by category
* add dishes to favorite
* get random dish from spoonacular API with the ability to add it to favorites

### App is using
* MVVM Architecture:

      - Model consists of 3 packages:
            - database: room database with dao and repository to manage dishes data
            - entities: data classes to hold information about dish and random dish
            - network: interface to make API query to get random dish information

      - View
            - 3 activities:
                  - Splash activity: starting activity with splash animation
                  - Main Activity: activity with main navigation and functionality of the app. Activity holds 3 fragments:
                        - AllDishesFragment: recycler view with all the dishes
                        - FavoriteDishesFragment: dishes list that were marked as favorite
                        - RandomDishFragment: one dish is presented with the details and information from the spoonacular API
                        - DishDetailFragment: screen with details about one selected dish
                  - AddUpdateDishActivity: separate activity to add new dish or edit the existing one

      - ViewModel
            - ViewModel is represented by DishViewModel class. View model communicates with database via dao and make get, insert, update, delete calls to dish database of the app. It is also use to keep and pass data between all dishes fragment and dish detail fragment when dish is selected.
            - RandomDishViewModel is used to get random dish from the spoonacular API call using SingleObserver

* JSON To Kotlin Class ​(JsonToKotlinClass)​ - Plugin for Kotlin to convert Json String into Kotlin data class code quickly. Used to generate random dish data classes
* Dexter - Requests permissions:
      - to read and write to external storage in order to add/get image from a device
      - to access camera to make a photo for new dish
      - to use internet to make API calls to get random dish
* Glide is an image loading framework to add images in UI from the url
* Room persistence library to allow for more robust database access
* Pallette library to extract the dominant color from images on the dish details screen
* RecyclerView for list of dishes with item click listener interface
* Retrofit + RxJava - HTTP client to prepare API call query and asynchronous fetch data from spoonacular API using observable sequences
* Share feature for dish details screen
* Work manager to schedule periodic app notifications

### Challenges

* RxJava library integration

### Screenshots

All dishes screen, edit dish, filter, add to favorite, get random dish recipe

https://user-images.githubusercontent.com/42688915/127745908-0ef212b9-9f25-4e69-a8cd-cf3cd75bd5da.mov

Add new dish:

https://user-images.githubusercontent.com/42688915/127745914-b7c9341e-c16f-4577-a6a9-6c0662719958.mov
