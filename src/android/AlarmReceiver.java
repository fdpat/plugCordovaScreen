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

public class AlarmReceiver extends BroadcastReceiver {
	
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
		WebView AppView = new WebView();
		appView.addJavascriptInterface(this, "MainActivity");
		
        intent = new Intent();
        intent.setAction("com.uniclau.alarmplugin.ALARM");
        intent.setPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", "file:///android_asset/www/wakeScreen.html");
        context.startActivity(intent);
    }
}
