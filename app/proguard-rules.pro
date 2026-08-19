# Gson Rules - Keep field names in data models
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }

# Keep data models used in Requests.kt
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Retrofit Rules
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn rx.**