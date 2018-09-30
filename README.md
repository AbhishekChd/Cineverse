# Cineverse

A bit overview, it uses [The Movie Database(TMDb)](https://www.themoviedb.org/) API to get movies data in JSON format, which is parsed using google's gson library and the requests are sent by Retrofit. It follows a [Repository Pattern](https://developer.android.com/jetpack/docs/guide) to fetch, store and display data.

#### Note: To run the app you have to add [The Movie DB](https://developers.themoviedb.org/3/getting-started/introduction) API key. Getting a key would take just few seconds :)
**Run the app**
1. Open `/home/.gradle/gradle.properties` and add the following line *(Create the file if not exists)*
```
TheMovieDbApi_Api_Key=<YOUR API KEY>
``` 
2. Clone the app with `.git` url
3. Run the app by <kbd>Shift + F10</kbd> or clicking <kbd>Run</kbd>

<img src="gif/cineverse.gif" width="40%">

#### Libraries
- [Google gson](https://github.com/google/gson) For parsing JSON data
- [Retrofit](http://square.github.io/retrofit/) For sending network requests
- [Picasso](http://square.github.io/picasso/) Loading and caching images
- [Butterknife](http://jakewharton.github.io/butterknife/) View Binding

#### Credits
- [Udacity](https://udacity.com/) For teaching me Android
- [Uplabs](https://www.uplabs.com/) Design inspiration
- [Freepik](https://www.freepik.com/) Free image for app logo

#### License
GNU General Public License v3.0
