package com.sidekick.moviejury.util;

import com.sidekick.moviejury.BuildConfig;

import android.util.Log;

public final class LoggingUtil {
	
	@SuppressWarnings("rawtypes")
	public static void debug(final Class clazz, final String message){
		if(BuildConfig.DEBUG){
			Log.d(clazz.getCanonicalName(), message);
		}
	}
	
	
	
	@SuppressWarnings("rawtypes")
	public static void error(final Class clazz, final String message, final Throwable e){
		Log.e(clazz.getCanonicalName(),message, e);
	}

}
