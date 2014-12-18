var alarm = {
    set: function(alarmDate, successCallback, errorCallback) {
        if(alarmDate < new Date())
    		return;
    									 cordova.getActivity().runOnUiThread(new Runnable() {
					public void run() {
						        cordova.exec(
            successCallback,
            errorCallback,
            "AlarmPlugin",
            "programAlarm",
            [alarmDate]
        );
					}
				});

    },
    setNew: function(alarmDate, successCallback, errorCallback){
        if(alarmDate < new Date())
            return;

        cordova.exec(
            successCallback,
            errorCallback,
            "AlarmPlugin",
            "programAlarmNew",
            [alarmDate]
        );
    }
};
module.exports = alarm;
