-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory

-keep class org.koin.** { *; }
-dontwarn org.koin.**