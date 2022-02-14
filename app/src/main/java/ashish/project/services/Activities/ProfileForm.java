package ashish.project.services.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Locale;

import ashish.project.services.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileForm extends AppCompatActivity {

    private EditText uFName, uLName, uPhone, uAddress, uPincode, uLandmark;
    private TextView saveDetailsBtn, getAddressBtn;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1002;
    private double latitude;
    private double longitude;
    private Boolean alreadyHasDetails;
    private ProgressDialog progressDialog;
    private Boolean intentFromSplash;
    private TextView txtHeader;
    private ImageView backBtn;
    private static final int PICK_IMAGE = 1;
    private CircleImageView userImage;
    private Uri imageURI;

    @Override
    protected void onStart() {
        super.onStart();

        locationStatusCheck(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);

        final SharedPreferences userDetails = getSharedPreferences("user_details", MODE_PRIVATE);

        alreadyHasDetails = userDetails.getBoolean("hasDetails", false);

        txtHeader = findViewById(R.id.txtHeader);
        backBtn = findViewById(R.id.backBtn);

        userImage = findViewById(R.id.userImage);
        uFName = findViewById(R.id.customerFirstName);
        uLName = findViewById(R.id.customerLastName);
        uPhone = findViewById(R.id.customerNumber);
        uAddress = findViewById(R.id.customerAddress);
        uPincode = findViewById(R.id.Pincode);
        uLandmark = findViewById(R.id.Landmark);

        getAddressBtn = findViewById(R.id.getAddressBtn);
        saveDetailsBtn = findViewById(R.id.saveDetailsBtn);

        if(alreadyHasDetails){

            uFName.setText(userDetails.getString("fName", ""));
            uLName.setText(userDetails.getString("lName", ""));
            uPhone.setText(userDetails.getString("Mobile", ""));
            uAddress.setText(userDetails.getString("Address", ""));
            uPincode.setText(userDetails.getString("Pincode", ""));
            uLandmark.setText(userDetails.getString("Landmark", ""));

        }

        if(getIntent().hasExtra("intentFromSplash")){
            intentFromSplash = getIntent().getBooleanExtra("intentFromSplash", false);

            if(intentFromSplash){
                txtHeader.setText("Fill Details");
                backBtn.setVisibility(View.GONE);
            }

        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
            }
        });

        getAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ProfileForm.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION
                    );
                } else {
                    for (int i = 0; i < 3; i++) {
                        getCurrentLocation();
                    }
                }
            }
        });

        saveDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uFName.getText().toString().equals("") || uPhone.getText().toString().equals("") || uAddress.getText().toString().equals("")
                        || uPincode.getText().toString().equals("") || uLandmark.getText().toString().equals("")){

                    Toast.makeText(ProfileForm.this, "Please fill all the details", Toast.LENGTH_SHORT).show();

                }else{

                    progressDialog = new ProgressDialog(ProfileForm.this);
                    progressDialog.setMessage("Saving...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    SharedPreferences.Editor editor = userDetails.edit();

                    editor.putBoolean("hasDetails", true);
//                    editor.putString("image", imageURI.toString());
                    editor.putString("fName", uFName.getText().toString());
                    editor.putString("lName", uLName.getText().toString());
                    editor.putString("Mobile", uPhone.getText().toString());
                    editor.putString("Address", uAddress.getText().toString());
                    editor.putString("Pincode", uPincode.getText().toString());
                    editor.putString("Landmark", uLandmark.getText().toString());

                    editor.apply();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            intentFromSplash = getIntent().getBooleanExtra("intentFromSplash", false);

                            if(!intentFromSplash){
                                finish();
                            }else{
                                Intent dashBoardIntent = new Intent(ProfileForm.this, dashboard.class);
                                dashBoardIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(dashBoardIntent);
                            }


                        }
                    },2000);

                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
                imageURI = data.getData();

                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);

                    userImage.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

    }

    private void getCurrentLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(ProfileForm.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);

                            Geocoder geocoder = new Geocoder(ProfileForm.this, Locale.getDefault());

                            List<Address> addresses  = null;
                            try {
                                addresses = geocoder.getFromLocation(latitude,longitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String address = addresses.get(0).getAddressLine(0);
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String zip = addresses.get(0).getPostalCode();
                            String country = addresses.get(0).getCountryName();

                            uAddress.setText(address);
                            uPincode.setText(zip);

                        }
                    }
                }, Looper.getMainLooper());
    }

    private void locationStatusCheck(final Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(context)
                    .setMessage("GPS not enabled")
                    .setPositiveButton("Open Location", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setCancelable(false)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .show();

        }
    }
}