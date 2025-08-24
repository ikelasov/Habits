# Habits

A Firebase-Powered Android App for Building and Tracking Habits.

This is an Android application built to help users create, track, and manage their habits effectively. It's developed using modern Android technologies like Jetpack Compose for the UI, Firebase for backend services, and follows a clean architecture pattern.

## Features

* **User Authentication:** Secure user sign-up and login functionality using Firebase Authentication.
* **Habit Creation and Management:** Users can create new habits, specifying details like name, category, frequency, and priority.
* **Habit Tracking:** A simple and intuitive interface to track daily progress for each habit.
* **Categorization:** Organize habits into custom categories with unique colors for better visualization.
* **Motivational Quotes:** Displays a random motivational quote to inspire users.
* **Data Synchronization:** Seamlessly syncs user data across devices using Firebase Firestore.
* **Offline Support:** The app works offline, and data is synced automatically when a connection is available.
* **Reminders:** Set reminders for habits to stay on track.

## Tech Stack

* **Android:** The primary platform for the application.
* **Kotlin:** The programming language used for development.
* **Jetpack Compose:** For building the user interface.
* **Firebase:**
    * **Authentication:** For user management.
    * **Firestore:** As the real-time NoSQL database.
* **Hilt:** For dependency injection.
* **Room:** For local data persistence.
* **Coroutines & Flow:** For asynchronous programming.
* **WorkManager:** For scheduling background tasks like reminders.

## Getting Started

### Prerequisites

* Android Studio
* A Firebase project

### Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/ikelasov/habits.git](https://github.com/ikelasov/habits.git)
    ```
2.  **Open in Android Studio:** Open the cloned project in Android Studio.
3.  **Connect to Firebase:**
    * Go to **Tools > Firebase** in Android Studio.
    * Connect to your Firebase project and follow the on-screen instructions to add the `google-services.json` file to the `app` module.
4.  **Build and run the app:** Build and run the app on an emulator or a physical device.

## Contributing

Contributions are welcome! Please feel free to submit a pull request or open an issue for any bugs or feature requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
