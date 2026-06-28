# This is a configuration file for R8, the new code shrinker from Google.
# https://r8.googlesource.com/r8/+/master/compatibility-faq.md

-dontwarn **

-keep class androidx.** { *; }
-keep class com.google.android.material.** { *; }

-keepclassmembers class com.manas.fusionlauncher.** {
    public *;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
