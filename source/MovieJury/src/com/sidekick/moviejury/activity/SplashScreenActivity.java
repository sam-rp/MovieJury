package com.sidekick.moviejury.activity;

import com.sidekick.moviejury.activity.LoadingTask;
import com.sidekick.moviejury.activity.LoadingTask.LoadingTaskFinishedListener;
import com.sidekick.moviejury.activity.MainActivity;
//import com.sidekick.moviejury.activity.SplashScreenActivity;
import com.sidekick.moviejury.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.os.Handler;
import android.widget.ProgressBar;

//The below piece of splash screen code is used when there is a Http calls to get the data

public class SplashScreenActivity extends Activity implements LoadingTaskFinishedListener{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the splash screen
		setContentView(R.layout.activity_splash);
		// Find the progress bar
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		// Start your loading		
		new LoadingTask(progressBar, this).execute("http://www.pgatour.com");
		
	}

	// This is the callback for when your async task has finished
	@Override
	public void onTaskFinished() {
		completeSplash();
	}

	private void completeSplash() {
		startApp();
		finish(); // Don't forget to finish this Splash Activity so the user
					// can't return to it!
	}

	private void startApp() {
		Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
		startActivity(intent);
	}
}
