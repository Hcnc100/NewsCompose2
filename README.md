# NewsCompose
Simple app that shows a series of news, obtained from an api and saved in an internal database


### Instruccions

1. SigUp in https://newsapi.org/register
2. Get yout api key in https://newsapi.org/account
3. Put yout Api key in local.properties

  apiKeyNews="your_api_key_xxx_Xxx"

4.Set this code in your *build.gradle* in defaultConfig 

```kotlin
 val newsApiKey: String = gradleLocalProperties(rootDir).getProperty("newsKey")
 buildConfigField("String", "NEWS_API_KEY", newsApiKey)

```
where "API_KEY_NEWS" is the name with your reference in inner code, and *apiKeyNews* is the name in the step 3
your can use this api key in yout code as 

```kotlin
BuildConfig.NEWS_API_KEY
```

### Change source

You can change country code for request in **NewsConstants**

```kotlin

const val newsPageSize = 10
const val countryNews = "us" // * change this
const val urlInvalidNews = "https://removed.com"

```


You cant see another code contry in https://newsapi.org/docs/endpoints/top-headlines in *country*

## Screenshots
### Splash
<p>
  <img src="https://i.imgur.com/9Ozjgr7.png" alt="splash" width="200"/>
</p>

### MainScreen
<p>
  <img src="https://i.imgur.com/ygIvEqL.png" alt="main" width="200"/>
  <img src="https://i.imgur.com/Ys6Iy4Z.png" alt="webview" width="200"/>
</p>
