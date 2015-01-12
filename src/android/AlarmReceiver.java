package com.uniclau.alarmplugin;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.util.Log;
import android.os.Vibrator;

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

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.init(); // Calling this is necessary to make this work
		appView.addJavascriptInterface(this, "MainActivity");

		/* "this" points the to the object of the current activity. "MainActivity" is used to refer "this" object in JavaScript as in Step 3. */

		//super.loadUrl("file:///android_asset/www/index.html");
	}
	
	public int customFunctionCalled() {
		return 2;
	}
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmPlugin", "AlarmReceived");
        
/*        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        pm.wakeUp(SystemClock.uptimeMillis());
*/
        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE); 
        KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
		
		int vibetime = intent.getIntExtra("KEY_ROWID",2000);
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        v.vibrate(vibetime);
		
		myCbkId = cbkCtx.getCallbackId(); 
		JSONObject data = arr.getJSONObject(0); 
		String ack = data.getString("data"); // You can acknowledge to the callback for instance and keep it alive 
		Log.d(TAG, "ack".equals(ack) ? "ack !" : "not ack !");
	
		PluginResult result = new PluginResult(PluginResult.Status.OK, ack);
		result.setKeepCallback(true);//This is the important part that allows executing the callback more than once, change to false if you want the callbacks to stop firing  
		this.webView.sendPluginResult(result, this.myCbkId); 
		
        intent = new Intent();
        intent.setAction("com.uniclau.alarmplugin.ALARM");
        intent.setPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", "file:///android_asset/www/wakeScreen.html");
        context.startActivity(intent);
    }
}
