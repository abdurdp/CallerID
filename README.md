# CallerID

## How to Run the App
1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Sync the Gradle files.
4. Just **Run** the app on an emulator or a physical Android device. 🚀  

---

## Solution and Features

### Solution
The Caller App is designed to manage and display a list of contacts with features like searching, sorting, blocking, and viewing blocked contacts. It provides a smooth and user-friendly interface with animations and optimized functionality for handling contact data.

### Implemented Features
- **Shared Preferences Storage:** The contact list is saved locally using SharedPreferences, ensuring data persistence even after app restarts.  
- **Incoming Call Detection:** Detects incoming calls and matches the number with the saved contact list to show a push notification with the caller's name and number.  
- **Search Contacts:** Quickly find contacts by name or number using a search bar integrated into the toolbar.  
- **Block/Unblock Contacts:** Toggle blocking status with a button and view blocked contacts in a separate screen.  
- **Alphabetical Sorting:** Automatically sorts the contact list alphabetically for easy navigation.  
- **Duplicate Removal:** Ensures there are no duplicate entries in the contact list.  
- **Custom Animations:** Adds visual appeal with animations for adding/removing items.  
- **Activity Transition Animations:** Smooth transitions when navigating between activities.  
- **View Binding Enabled:** Simplifies view management with safe and efficient `ViewBinding`.
- **Optimized for Large Contact Lists:**  Implements paging and efficient querying to handle large contact lists without performance degradation.

