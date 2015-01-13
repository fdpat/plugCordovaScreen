package com.uniclau.alarmplugin;

import java.text.SimpleDateFormat;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.Date;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.os.Vibrator;


public class AlarmPlugin extends CordovaPlugin {
	
	   @Override
	    public void onPause(boolean multitasking) {
	        Log.d("AlarmPlugin", "onPause");
	        super.onPause(multitasking);
	    }

	    @Override
	    public void onResume(boolean multitasking) {
	    	//Called when an alarm already 
	        Log.d("AlarmPlugin", "onResume " );
	        super.onResume(multitasking);
	        
	        PowerManager pm = (PowerManager)this.cordova.getActivity().getSystemService(Context.POWER_SERVICE);
	        WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
	        wakeLock.acquire();
	 
	        KeyguardManager keyguardManager = (KeyguardManager) this.cordova.getActivity().getSystemService(Context.KEYGUARD_SERVICE); 
	        KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
	        keyguardLock.disableKeyguard();

	        //Vibrator v = (Vibrator) this.cordova.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        	//v.vibrate(1000);
	    }
	    
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		try {
			if ("programAlarm".equals(action)) {
				JSONObject arg_object = args.getJSONObject(0);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				Date aDate = sdf.parse(arg_object.getString("ringDate").replace("Z", "+0000"));
				int id = arg_object.getInt("ringId");
				
				Date n = new Date();
				if(aDate.before(n)) {
					callbackContext.error("The date is in the past");
					return true;
				}
				
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.cordova.getActivity());
				SharedPreferences.Editor editor = settings.edit();
	            editor.putLong("AlarmPlugin.AlarmDate", aDate.getTime()); //$NON-NLS-1$
	            editor.commit();
				
				AlarmManager alarmMgr = (AlarmManager)(this.cordova.getActivity().getSystemService(Context.ALARM_SERVICE));
				
				PendingIntent alarmIntent;     
				Intent intent = new Intent(this.cordova.getActivity(), AlarmReceiver.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("IDs", id); 
				alarmIntent = PendingIntent.getBroadcast(this.cordova.getActivity(), id, intent, 0);
				
				alarmMgr.cancel(alarmIntent);
				alarmMgr.set(AlarmManager.RTC_WAKEUP,  aDate.getTime(), alarmIntent);

				callbackContext.success("Alarm set at: " +sdf.format(aDate));
			    return true; 		
			}else if("unsetAlarm".equals(action)){
				JSONObject arg_object = args.getJSONObject(0);
				int id = arg_object.getInt("ringId");
		
				AlarmManager alarmMgr = (AlarmManager)(this.cordova.getActivity().getSystemService(Context.ALARM_SERVICE));
				
				PendingIntent alarmIntent;     
				Intent intent = new Intent(this.cordova.getActivity(), AlarmReceiver.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				alarmIntent = PendingIntent.getBroadcast(this.cordova.getActivity(), id, intent, 0);
				
				alarmMgr.cancel(alarmIntent);

				callbackContext.success("Alarm unset, id: " +id);
				return true;
			}
			return false;
		} catch(Exception e) {
		    System.err.println("Exception: " + e.getMessage());
		    callbackContext.error(e.getMessage());
		    return false;
		} 
	}
}
