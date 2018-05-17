package weather.andrewlaurien.com.myweather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;

import weather.andrewlaurien.com.myweather.Adapter.WeatherAdapter;
import weather.andrewlaurien.com.myweather.Model.DailyWeatherReport;
import weather.andrewlaurien.com.myweather.Utilities.WebRequest;

public class MainActivity extends AppCompatActivity {


    final int PERMISSION_ACCESS_COARSE_LOCATION = 111;
    final int PERMISSION_ACCESS_FINE_LOCATION = 222;

    private GoogleApiClient mGoogleApiClient;
    Context mcontext;


    private FusedLocationProviderClient mFusedLocationClient;

    boolean mConnected = false;

    LocationManager locationManager;
    LocationListener locationListener;
    public static RecyclerView recyclerView;
    public static WeatherAdapter weatherAdapter;

    public static ArrayList<DailyWeatherReport> weatherReportArrayList = new ArrayList<>();
    public static TextView txtlocation;
    public static TextView txtTemperature;
    public static TextView txtWeather;
    public static TextView txtDate;
    public static ImageView imgWeather;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    public static ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mcontext = this;

        txtTemperature = findViewById(R.id.textView);
        txtlocation = findViewById(R.id.textView2);
        txtWeather = findViewById(R.id.textView4);
        txtDate = findViewById(R.id.textView3);
        imgWeather = findViewById(R.id.imageView);


        recyclerView = findViewById(R.id.recyclerview);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(mcontext);
            dialog.setMessage("Location is not enabled.");
            dialog.setPositiveButton("Enabled Location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mcontext.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        } else {
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("Fetching Weather forecas. please wait.");
            progressDialog.setTitle("ProgressDialog"); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);

        }


        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.d("Here1", "Hello1");

                //txtlocation.setText(""+location.getLatitude());
                WebRequest.getInstance(mcontext).fetchWeatherData(location);

                if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                    // onLocationChanged method is fired by gps provider.
                    // After this point, getLastKnownLocation(); method returns not-null
                    // for LocationManager.GPS_PROVIDER
                }

                if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
                    // onLocationChanged method is fired by network provider.
                    // After this point, getLastKnownLocation(); method returns not-null
                    // for LocationManager.NETWORK_PROVIDER
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

        };


        requestLocation();


    }


    public void requestLocation() {


        boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionLocation) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
            Log.d("Permission", "NONE");
        } else {

            Log.d("Permission", "YES");
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 60, 500, locationListener);

            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //WebRequest.getInstance(mcontext).fetchWeatherData(lastKnownLocation);

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // mGoogleApiClient.disconnect();
    }


//    @Override
//    public void onLocationChanged(Location location) {
//
//        Log.d("Change", "Yes");
//        WebRequest.getInstance(mcontext).fetchWeatherData(location);

//        //if (mConnected) {
//
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//            Log.d("Here", "Hello");
//            double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
//            String units = "imperial";
//            // String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
//            //         lat, lon, units, APP_ID);
//            // new GetWeatherTask(textView).execute(url);
//            // textView2.setText("" + lastLocation.getLatitude());
//            WebRequest.getInstance(mcontext).fetchWeatherData(lastLocation);
//
//        } else {
//
//            Log.d("Here", "1");
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                    PERMISSION_ACCESS_COARSE_LOCATION);
//        }
//        //}

//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                    requestLocation();
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
