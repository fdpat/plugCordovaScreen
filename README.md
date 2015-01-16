AlarmPlugin
===========

Alarm plugin for Cordova/PhoneGap.

To install the plugin:

    cordova plugins add https://github.com/fdpat/plugCordovaScreen.git

To invoke the plugin: 

    navigator.plugins.alarm.set(alarmDate, 
    function(){
      // SUCCESS
    }, 
    function(){
      // ERROR
    })

```alarmDate``` is the date that the application will wake up. This will happen even if the device has been rebooted.

The second and third parameters are the callbacks to handle the success or failure of the call, respectively. 
