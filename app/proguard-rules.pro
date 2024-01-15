# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Specify the fully qualified class name to the JavaScript interface
# class:


# Default ProGuard Rules
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keepattributes Signature
-keepattributes SetJavaScriptEnabled

-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }
-keep class com.pusher.** { *; }
-keep class com.google.firebase.iid.** { *; }

-dontwarn com.google.firebase.iid.FirebaseInstanceId*
-dontwarn com.google.firebase.iid.InstanceIdResult*

# Keep the FirebaseMessagingService
-keep class com.google.firebase.messaging.FirebaseMessagingService { *; }

-keepclassmembers class dev.fs.mad.game11.JScript {
   public *;
}

-keep class com.google.android.gms.common.ConnectionResult {
   int SUCCESS;
}

-keep class com.appsflyer.** { *; }
-keep class com.android.installreferrer.** { *; }

-keep class com.google.android.gms.measurement.** { *; }
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
   com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
}

-keep public class * extends android.app.Application

-keep class com.appsflyer.**$InstallReceiver { *; }
-keep class com.appsflyer.**$referrer { *; }
-keep class com.appsflyer.**$FirebaseMessagingService { *; }
-keep class com.appsflyer.**AppsFlyerLib

-keep class com.appsflyer.** { *; }

-keepclassmembers class * {
    @com.appsflyer.*
    <methods>;
}



