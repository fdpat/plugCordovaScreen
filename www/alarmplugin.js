var alarm = {
    set: function(alarmDate, id, successCallback, errorCallback) {
        if(alarmDate < new Date())
    		return;
    	
        cordova.exec(
            successCallback,
            errorCallback,
            "AlarmPlugin",
            "programAlarm",
            [{"ringDate":alarmDate,
			"ringId" : id}]
        );
    },
    unset: function(id, successCallback, errorCallback){
        cordova.exec(
            successCallback,
            errorCallback,
            "AlarmPlugin",
            "unsetAlarm",
            [{"ringId": id}]
        );
    },
	unsetAll: function(id, successCallback, errorCallback){
        cordova.exec(
            successCallback,
            errorCallback,
            "AlarmPlugin",
            "unsetAlarmAll",
            [{"arrayId": id}]
        );
    }
};
module.exports = alarm;
