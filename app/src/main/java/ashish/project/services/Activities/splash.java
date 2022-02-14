package ashish.project.services.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import ashish.project.services.R;

public class splash extends AppCompatActivity {

    private Boolean alreadyHasDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final SharedPreferences userDetails = getSharedPreferences("user_details", MODE_PRIVATE);

        alreadyHasDetails = userDetails.getBoolean("hasDetails", false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent dashboardIntent;
                if(alreadyHasDetails){
                    dashboardIntent = new Intent(splash.this, dashboard.class);
                }else{
                    dashboardIntent = new Intent(splash.this, ProfileForm.class);
                    dashboardIntent.putExtra("intentFromSplash", true);
                }
                startActivity(dashboardIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

            }
        }, 3000);

    }
}