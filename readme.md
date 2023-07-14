# ADB Wifi connector utility

A small utility app to connect to wifi by calling an intent with ADB.
On some devices wpa_supplicant, wpa_cli and/or the wifi utility is not available or non-functional. This utility helps as a bridge for using adb to connect the device to a network.

This app solves that by using WifiManager to connect to a wifi with the provided SSID and Password from an intent.

The app automatically closes after 3 seconds when wifi has successfully connected.

NOTE: This is written for API levels below 29. Newer devices should work fine with wifi utility or wpa_cli utility.

## Usage

### Install the app using adb

adb install -r adb_wifi_connector.apk

Replace myssid and mypassword with actual ssid and password

### connect

connects to the network and saves the network with the provided ssid and password. (It is saved even if it's invalid)

Using ADB:
`adb shell am start -n "no.normedia.adbwificonnector/.WifiConnectActivity/connect" -e "ssid" "myssid" -e "password" "mypassword" --ez "connect" true`

On an interactive shell:
`am start -n "no.normedia.adbwificonnector/.WifiConnectActivity/connect" -e ssid "myssid" -e password "mypassword"`

### disconnect

disconnects and removes the network from the device

Using ADB:
`adb shell am start -n "no.normedia.adbwificonnector/.WifiConnectActivity/disconnect" -e "ssid" "myssid"`

On an interactive shell:
`am start -n "no.normedia.adbwificonnector/.WifiConnectActivity/disconnect" -e ssid "myssid"`
