package com.example.itchyfeet;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, Serializable,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;
    private Marker currentMarker = null;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;

    private static LatLng defaultLocation = new LatLng(37.56, 126.97);
    private static final String TAG = "GoogleMap";
    private static final int REQUEST_CODE_GPS = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;

    private long UPDATE_INTERVAL = 1000;
    private long FASTEST_INTERVAL = 1000;

    private Activity mActivity;
    private ClientService clientservice;
    boolean askPermissionOnceAgain = false;
    public static Client client;
    String nickid;
    String getKakaoID;
    String ShareCommand = "share1";
    GlobalApplication GlobalID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.itchyfeet.R.layout.travelnote_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        mActivity = this;

        try {
            clientservice = new ClientService();
            clientservice.CompareName(GlobalID.KakaoID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (client.receivetarrayList.size() > 0) {
            nickid = client.receivetarrayList.get(0).getTitle();
        }



        if (nickid.equals("NoNickName")) {
            try {
                clientservice = new ClientService();
                clientservice.LoginConnect(GlobalID.KakaoID,GlobalID.NickName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (GlobalID.Share.equals(true)) {
            ShareCommand = ShareCommand + "@" + GlobalID.Sender + "@" + GlobalID.KakaoID
                    + "@" + GlobalID.ShareTravelName + "@" + GlobalID.ShareTravelDate
                    + "@xxxxxx";
            GlobalID.Share = false;

            try {
                clientservice = new ClientService();
                clientservice.SharePlan(ShareCommand);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(
                    getApplicationContext(),
                    ShareMapActivity.class);
            startActivity(intent);
        }

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();

        //mGoogleApiClient.connect();

        //GPS 꺼졌는지 검사해서 다시 물어보는것 구현해야함.

        //앱 정보에서 퍼미션을 허가했는지를 다시 검사해봐야 한다.
        if (askPermissionOnceAgain) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermissionOnceAgain = false;

                checkPermissions();
            }
        }
    }



    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        //위치 업데이트 중지
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.unregisterConnectionCallbacks(this);
            mGoogleApiClient.unregisterConnectionFailedListener(this);

            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }

            mGoogleApiClient.disconnect();
        }

        super.onDestroy();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        Log.e(TAG, "onMapReady");

        mGoogleMap = map;
        //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //API 23 이상이면 런타임 퍼미션 처리 필요

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {

                ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            } else {
                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showGPSDisabledAlertToUser();

                } else {
                    buildGoogleApiClient();

                    mGoogleMap.setMyLocationEnabled(true);
                }
            }
        } else {
            //API 23 미만은 추가 퍼미션 관련 처리가 필요없다.

            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDisabledAlertToUser();

            } else {
                buildGoogleApiClient();

                mGoogleMap.setMyLocationEnabled(true);
            }
        }
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "BMJUA.ttf");
        /* 1. 일정 추가 BUTTON */
        Button b = (Button) findViewById(R.id.btnNext);
        b.setTypeface(typeFace);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        MainMapActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent);
                finish();// 다음 화면으로 넘어간다
            }
        });
        /* 2. 일정 보기 BUTTON */
        Button b2 = (Button) findViewById(R.id.btnNext2);
        b2.setTypeface(typeFace);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        ListViewActivity.class);
                startActivity(intent2);
                finish();
            }
        });
        /* 3. 공유하기 BUTTON */
        Button b3 = (Button) findViewById(R.id.btnNext3);
        b3.setTypeface(typeFace);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        ShareEntireActivity.class); // 다음 넘어갈 클래스 지정
                ; // 다음 화면으로 넘어간다
                startActivity(intent3);
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {

        String errorMessage = "";

        if (currentMarker != null) currentMarker.remove();

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        List<Address> addresses = null;

        try {

            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            errorMessage = "지오코더 서비스 사용불가";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = "잘못된 GPS 좌표";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();

        }

        String markerTitle;
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "주소 미발견";
                Log.e(TAG, errorMessage);
            }
            markerTitle = errorMessage;
        } else {
            Address address = addresses.get(0);
            markerTitle = address.getAddressLine(0).toString();
        }

        //현재 위치에 마커 생성
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(markerTitle);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentMarker = mGoogleMap.addMarker(markerOptions);

        //지도 상에서 보여지는 영역 이동(카메라 이동)
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mGoogleMap.getUiSettings().setCompassEnabled(true);

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }


    //성공적으로 GoogleApiClient 객체 연결되었을 때 실행
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "onConnected");

        mLocationRequest = new LocationRequest();
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //위치 업데이트 시작
            //PendingResult<Status> requestLocationUpdates (GoogleApiClient client, LocationRequest request, LocationListener listener)
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            //백그라운드 처리가 필요하면 아래 메소드 사용
            //Requests location updates with a callback on the specified PendingIntent.
            //This method is suited for the background use cases, more specifically for receiving location updates,
            // even when the app has been killed by the system. In order to do so, use a PendingIntent for a started service.
            //PendingResult<Status> requestLocationUpdates (GoogleApiClient client, LocationRequest request, PendingIntent callbackIntent)
        }

    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        //구글 플레이 서비스 연결이 해제되었을 때, 재연결 시도
        Log.e(TAG, "onConnectionSuspended");
        mGoogleApiClient.connect();
    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        boolean fineLocationRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        // case 1:
        // 사용자가 퍼미션 요청을 거부한 경우
        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.
        if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED && fineLocationRationale == true)
            showDialogforPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");

            // 사용자가 대화상자에서 Don't ask again(다시 묻지 않음) 체크 박스를 설정하고 거부를 선택하면 case 2로 먼저 가게 됩니다.
            // case 2:
            //  사용자가 퍼미션 거부와 Don't ask again(다시 묻지 않음) 체크 박스를 설정한 경우 또는 설정에서 앱의 퍼미션을 취소한 경우
        else if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED && fineLocationRationale == false)
            showDialogforNeedSetting("???퍼미션 거부 + Don't ask again(다시 묻지 않음) 체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다.");

            // case 3:
            // showDialogforNeedSettingr이 호출되어 사용자에게 메시지박스를 보여주며 권한 요청을 물어본다. 이때 예를 선택하면
            // 앱정보가 표시되는데 여기서 권한을 허용해주면 이곳으로 오게된다.
            // 앱정보에서 권한을 허용해주지 않으면 case 2로 가게된다.
        else if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {

            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDisabledAlertToUser();

            } else {
                buildGoogleApiClient();

                mGoogleMap.setMyLocationEnabled(true);
            }

        }
    }

    //퍼미션 요청 결과를 체크한다.
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        //요청한 퍼미션의 타입을 요청 코드로 확인하고 grantResults.length 요청 결과 1개 이상(요청한 퍼미션에 대한 결과가 1개이상)
        if (permsRequestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.length > 0) {

            //퍼미션 결과가 PackageManager.PERMISSION_GRANTED이면  사용자가 퍼미션 요청을 허용된 경우이다.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showGPSDisabledAlertToUser();
                }

                if (mGoogleApiClient == null) buildGoogleApiClient();

                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    mGoogleMap.setMyLocationEnabled(true);
                }

            } else //퍼미션 결과가 PackageManager.PERMISSION_DENIED이면 사용자가 퍼미션 요청을 거부한 경우이다.
                //대화상자 처리
                checkPermissions();
        }
    }

    private void showDialogforPermission(String msg) {

        final AlertDialog.Builder myDialog = new AlertDialog.Builder(MainActivity.this);
        myDialog.setTitle("알림");
        myDialog.setMessage(msg);
        myDialog.setCancelable(false);
        myDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
            }
        });
        myDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        myDialog.show();
    }

    private void showDialogforNeedSetting(String msg) {

        final AlertDialog.Builder myDialog = new AlertDialog.Builder(MainActivity.this);
        myDialog.setTitle("알림");
        myDialog.setMessage(msg);
        myDialog.setCancelable(false);
        myDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        myDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        myDialog.show();
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    //GPS 활성화를 위한 다이얼로그 보여주기
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage("GPS가 비활성화 되어있습니다. 활성화 할까요?")
                .setCancelable(false)
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(callGPSSettingIntent, REQUEST_CODE_GPS);
                    }
                });

        alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    //결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_GPS:

                //사용자가 GPS 활성 시켰는지 검사
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // GPS 가 ON으로 변경되었을 때의 처리.

                    buildGoogleApiClient();

                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else { //사용자가 GPS 활성하지 않았을 경우 처리

                }

                break;
        }
    }
}