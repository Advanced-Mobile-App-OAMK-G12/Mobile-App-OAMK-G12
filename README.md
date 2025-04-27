# EasyFinn Mobile Application

## Project Overview
EasyFinn is a mobile application built to assist foreigners living in Finland by offering a community-driven platform for sharing practical life tips.  
Whether it’s explaining bureaucratic processes, sharing money-saving hacks, or recommending useful products from Finnish supermarkets, EasyFinn connects expats to real experiences.

Users can post, save, search, and comment on tips — making daily life in Finland more accessible and enjoyable.

## Features
- **User Authentication:** Email/password registration and Google Sign-In via Firebase Authentication.
- **Post Tips:** Users can create posts with text and upload images (stored in Firebase Storage).
- **Save Tips:** Favorite any tip to revisit later.
- **Search Tips:** Easily search tips by keyword or category.
- **Comment on Tips:** Engage by commenting on posts.
- **Notifications:** Receive updates on comments and saved tips.
- **Google Maps Integration:** Location-based tips with map support.
- **Responsive Design:** Mobile-first UI built with Jetpack Compose.
- **User Profile:** View, edit, and manage personal profiles and saved posts.

## Technologies and Tools

### Frontend (Mobile App)
- Kotlin with Jetpack Compose
- Firebase Authentication (Email/Password, Google)
- Firebase Firestore (NoSQL cloud database)
- Firebase Storage (for images)
- Google Maps API
- Hilt (Dependency Injection)
- Coroutine & LiveData (Async programming)

### Development Tools
- Gradle (Build tool)
- `libs.versions.toml` (Centralized dependency management)
- Firebase BoM (Safe management of Firebase SDK versions)
- Android Studio (IDE)
- Google Services JSON (Firebase connection)

## Architecture Overview
- **MVVM (Model-View-ViewModel)** Pattern
  - Clean separation of concerns for scalable code.
- **Hilt Dependency Injection**
  - Injects Firebase services and repositories.
- **Repository Layer**
  - Handles Firebase Authentication, Firestore, Storage.
- **Coroutine-based Async Handling**
  - Suspend functions and await() for clean async operations.
- **Lifecycle-Aware Components**
  - ViewModels use `viewModelScope.launch` for safe background work.

## Key Implementations

### User Registration and Login
- Email/password sign-up with password strength rules (must include a capital letter and a number).
- Google Sign-In via Play Services.
- User profile saved in Firestore with name, email, bio, and timestamps.

### Posting a Tip
- Images uploaded to Firebase Storage.
- Posts contain title, description, optional image, and category.

### Saving and Commenting on Tips
- Favorite tips to a personal Saved Tips list.
- Add comments to posts, and view comments in real-time.

### Notifications
- Push notifications (local) for new comments or updates.

### Google Maps Integration
- Show location-based tips (e.g., supermarket product availability) using Google Maps API.

## Installation and Usage

### Prerequisites
- Android Studio installed
- Firebase project setup
- Google Maps API key configured

### Setup Steps
1. Clone the repository:

   ```bash
   git clone <https://github.com/Advanced-Mobile-App-OAMK-G12>
2. Open the project in Android Studio.

3. Add your google-services.json file to the /app directory.

4. Configure API keys (Maps, Firebase) in your local.properties or secure config files.

5. Sync Gradle and build the project.

6. Run the app on an emulator or physical device.
