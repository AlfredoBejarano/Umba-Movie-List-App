# MovieList App
<img width="160" height="160" alt="app_icon" src="./app/src/main/ic_launcher-playstore.png">

Demo app that uses [TheMovideDB V3 API](https://developers.themoviedb.org/3/getting-started/introduction) to fetch some lists describing the upcoming, popular and top rated movies in their database. The user can also search for a movie and choose a movie (from any of the lists or a search result) to see its details, such as its runtime, the overview, the amount of votes and a trailer video (if available).

- This app follows the recommended modern Android development guidelines and good practices described by Google in Android Jet pack with a twist, separating each layer in a **module**. More info about this architecture can be found within the [Android Jetpack documentation](https://developer.android.com/jetpack/docs/guide).

- This app harness the power of the [Android Architecture components](https://developer.android.com/topic/libraries/architecture) to create a lean, fast and reliable app.

- This app offers a clean, layered architecture while keeping it simple, the `model` classes are transparent across the architecture, the `repository` classes provide a clean API and a single source of truth from all the app `data sources` for the rest of the layers in the app. The `Presenter` classes serves as intermediaries between the `view` (UI Controllers) and the `repository` layers, provides clean and immutable observation via `LiveData` and reacts accordingly to the app's lifecycle.

- It also uses the good practices accepted from Google, such as _Dependency Injection_ using **Dagger** and using the _Observation_ pattern. The app architecture is **M**odel**V**iew**V**iew**M**odel (_MVVM from now on_).

- This project is written 100% natively using **Kotlin**, Kotlin is a concise and safe language interoperable with Java. This makes Kotlin a powerful tool for android development. It is also the oficial language for Android.

# How the app is built?

All the layers dependencies are satisfied using *Dependency Injection* (_DI from now on_) using **Dagger**.

The idea of separate layers and module is to maintain the **single responsibility principle**.

As said before, **MovieList app** is using the MVP architecture using some of the Architecture Components from Google (like Lifecycle) to better attach the presenter classes to the View's lifecyles.
Each layer being separated into a module, it looks as follows:

# Modules
- App
  - Navigation Component
  - Safe Args
  - Activity
  - Fragments
  - Presenters
- Domain
  - Use Cases
- Repository
  - Repositories
- Remote
  - Retrofit
  - GSON
- Local
  - Room
- Core
  - model

# Modules

This section will explain the idea behind each module:

## App
The app module contains the UI elements of the app itself. Such as the NavHostActivity and the Fragment destinations (and their respective Presenters) it also contains the drawable resources and strings.

## Domain
This layer defines use case classes. Classes that performs computation using data from the repository layers and report the data for the UI classes (in other words, business logic).

## Repository
The repository classes serve as the single source of truth for the app. This classes manage all the data sources from where the app can fetch data and provides a unified stream of data with unified classes that are used across the rest of the layers.

## Remote
Module that should contain the data sources that comes from a  _remote_ source, such as an REST API or web service.

## Local
This module contains the local data sources for the app such as a Room database or SharedPreferences.

## Core
This module contains common things that all modules should need, more explicitly, model classes. Following the Google's
[Guide to architecture](https://developer.android.com/jetpack/docs/guide#drive-ui-from-model), The UI should be model
driven. Sometimes the remote JSON responses don't follow what the UI needs, so this models serve as the single source of
truth for the app. Also, UI-driven models should be used as the entities as well, following this guide to architecture,
the local data source should be the one providing the true data using caches, so the models in this module use **Room
annotations**, To prevent Room classes from leaking to other layers, this module is a pure **Java** module, so only
the Room annotations are set as a dependency and the unnecessary classes such as DAO or Database are not added / leaked.

# Wait, are those commits FROM 2 YEARS AGO?

Yes. And I'll explain why.

This repository is a technical test for applying to Umba, a Fintech startup from Africa. I made this technical challenge before a year and a half ago (in August 2019) using a different architecture pattern (MVVM) and using some old code techniques I had.
Technical challenges for applying to jobs are fun and highlight what a developer can do, yet, I think some other points like how good a developer code will escalate or how maintainable said developers code will be. I want to highlight those skills as well
and I saw the old repo it as an opportunity to demostrate how scalable and maintainable my code is.

## Reasons I chose to change an old repo:

- The deadline. I had four days to complete the challenge. I already had a codebase that did almost what the challenge was requiring, If I started from scratch, I would deliver something similar as this but in well, 4 days. Applying tweaks and changes to my code took me around 4 hours.

- How much I've grown. Learning in the software development industry never ends, I also want to highlight how much I've improved in a year and a half. How much my coding skills had matured. I'm an android nerd, I devour all the google talks and Android Developers channel youtube videos and I love learning.

- The scalability and maintainability of my code. The code I've wrote was far from perfect, but yet, something as deep as migrating from an architecture pattern (MVVM) to another (MVP) or updating outdated (like, very outdated) dependencies into newer versions was a breeze, a few bugs were also in the original code base and those were easily debuggable and fixed, testing newly added classes was a breeze as well. What I want to show is that the code I write matures quite well, When I write software, my priorities are performance and maintainability. Those skills are hard to show in a technical challenge as those code base usually end up forgotten by the developer.


# The single responsibility principle

Layers, modules, separation... to what end?. To achieve the single responsibility principle.

The single responsibility principle describes that each class should do and just do one thing. It means, the UI classes should just display stuff and interact with the user, the repository classes should manage all the various data sources available to provide a clean API to upper layers, the domain layer should manage said data and just compute stuff with it, the data sources should just retrieve data and thats it. Is basically prevent classes from having too much stuff and let them do just one thing. This helps the codebase being understandable, scalable and maintainable.

# So, what clean code should be?

In my opinion, a clean codebase or "clean code" should be code that is easily understandable, that goes "to the point" and is easily maintainable, it also should help the development of future features to be more productive. This means variables should have a meaningful name and functions should be small and compact, usually doing just a single action or two (how many lines your function or class have is a good indicative of this).

So no. multiple model classes or single implementation interfaces are not a good indicative of clean code, at least in my opinion. This is because if you have to write 16 or more clases or 4 different model classes or an activity with a single fragment to implement a feature that is not productive or fast, is just a chore. More code, means more things to maintain.

This is, for me at least, what a clean code should look like.

# Running the project

In order to run the project, you are going to need a **gradle.properties** file. This file is not within this repository for security reasons (as much security a demo project needs).

    # Project-wide Gradle settings.  
    # IDE (e.g. Android Studio) users:  
    # Gradle settings configured through the IDE *will override*  
    # any settings specified in this file.  
    # For more details on how to configure your build environment visit  
    # http://www.gradle.org/docs/current/userguide/build_environment.html  
    # Specifies the JVM arguments used for the daemon process.  
    # The setting is particularly useful for tweaking memory settings.  
    org.gradle.jvmargs=-Xmx4096m  
    # When configured, Gradle will run in incubating parallel mode.  
    # This option should only be used with decoupled projects. More details, visit  
    # http://www.gradle.org/docs/current/userguide/multi_project_builds.html#sec:decoupled_projects  
    # org.gradle.parallel=true  
    # AndroidX package structure to make it clearer which packages are bundled with the  
    # Android operating system, and which are packaged with your app's APK  
    # https://developer.android.com/topic/libraries/support-library/androidx-rn  
    android.useAndroidX=true  
    # Automatically convert third-party libraries to use AndroidX  
    android.enableJetifier=true  
    # Kotlin code style for this project: "official" or "obsolete":  
    kotlin.code.style=official  
    # TheMovieDB API key  
    theMovieDBApiKey="841fe808855945f7b06ccb3d5a20e7ea"
    # TheMovieDB Base URL  
    theMovieDBBaseURL="https://api.themoviedb.org/3/"  
    # TheMovieDB Images base URLs  
    theMovieDBImageBaseURl="http://image.tmdb.org/t/p/w500"  
    # YouTube API Key  
    youtubeAPIKey="AIzaSyDNzu8UkpQnf_LhoPZbmgTrtLuRM3iDe2g"
