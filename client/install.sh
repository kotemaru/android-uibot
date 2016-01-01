#dx.bat --dex --output=client.dex build/libs/client.jar

cd build
dx.bat --dex --output=classes.dex classes/main/
zip uibot-client.zip classes.dex
adb push uibot-client.zip /sdcard/
adb push ../uibot-client.sh /sdcard/
