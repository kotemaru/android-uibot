#!/bin/sh

#adb shell am instrument -w org.kotemaru.android.uibot.tests/android.support.test.runner.AndroidJUnitRunner

export ANDROID_DATA=/sdcard

mkdir /sdcard/dalvik-cache
exec dalvikvm  -cp /sdcard/uibot-client.zip org.kotemaru.android.uibot.Client $*
#exec dvz -classpath /sdcard/uibot-client.zip org.kotemaru.android.uibot.Client $*

