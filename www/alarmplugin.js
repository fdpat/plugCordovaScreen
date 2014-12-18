var alarm = {
    set: function(alarmDate, id, successCallback, errorCallback) {
        if(alarmDate < new Date())
    		return;
    	
        cordova.exec(
            successCallback,
            errorCallback,
            "AlarmPlugin",
            "programAlarm",
            [{alarmDate,id}]
        );
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
