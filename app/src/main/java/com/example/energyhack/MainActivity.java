package com.example.energyhack;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

public class MainActivity extends Activity implements UserLocationObjectListener {

    private final String MAPKIT_API_KEY = "72f6abf0-5fa4-4b60-8858-a3eaf5b60394";
    private MapView mapView;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DatabaseReference rootRef;
    PlacemarkMapObject placeMark;
    private UserLocationLayer userLocationLayer;

    private AppCompatImageView btn_profile, btn_calendar, btn_users, btn_chat;

    private class CustomTapListener implements MapObjectTapListener {
        public boolean onMapObjectTap(MapObject mapObject, Point point) {
            System.out.println("something");
            return true;
        }
    }

    CustomTapListener customTapListener = new CustomTapListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        btn_profile = (AppCompatImageView) findViewById(R.id.btn_profile);
        btn_calendar = (AppCompatImageView) findViewById(R.id.btn_calendar);
        btn_users = (AppCompatImageView) findViewById(R.id.btn_users);
        btn_chat = (AppCompatImageView) findViewById(R.id.btn_chat);

        setListeners();

        mapView = findViewById(R.id.mapview);
        mapView.getMap().setRotateGesturesEnabled(false);
        mapView.getMap().move(new CameraPosition(new Point(0, 0), 14, 0, 0));

        requestLocationPermission();

        MapKit mapKit = MapKitFactory.getInstance();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);

        userLocationLayer.setObjectListener(this);

        ImageProvider resourceBackedImage1 = ImageProvider.fromResource(this, R.drawable.run);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.326343,43.987292), resourceBackedImage1);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage2 = ImageProvider.fromResource(this, R.drawable.run);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.329961,44.019039), resourceBackedImage2);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage3 = ImageProvider.fromResource(this, R.drawable.run);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.308685,43.997183), resourceBackedImage3);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage4 = ImageProvider.fromResource(this, R.drawable.run);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.274489,43.97336), resourceBackedImage4);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage5 = ImageProvider.fromResource(this, R.drawable.run);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.276009,44.013398), resourceBackedImage5);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage6 = ImageProvider.fromResource(this, R.drawable.fitness);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.32331421716266,44.03400877690995), resourceBackedImage6);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage7 = ImageProvider.fromResource(this, R.drawable.fitness);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.28565474318271,43.979687635028554), resourceBackedImage7);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage8 = ImageProvider.fromResource(this, R.drawable.fitness);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.33879038511849,43.942642662757954), resourceBackedImage8);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage9 = ImageProvider.fromResource(this, R.drawable.fitness);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.22030085069901,43.94212746303836), resourceBackedImage9);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage10 = ImageProvider.fromResource(this, R.drawable.fitness);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.19758890977492,43.842276143706634), resourceBackedImage10);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage11 = ImageProvider.fromResource(this, R.drawable.food);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.31357607657531,43.989331094878374), resourceBackedImage11);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage12 = ImageProvider.fromResource(this, R.drawable.food);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.32827837080237,44.02241363092338), resourceBackedImage12);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage13 = ImageProvider.fromResource(this, R.drawable.food);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.32565184310324,44.004366212168186), resourceBackedImage13);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage14 = ImageProvider.fromResource(this, R.drawable.food);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.32416848866205,44.00822937707443), resourceBackedImage14);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage15 = ImageProvider.fromResource(this, R.drawable.food);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.33091691107307,44.002738405376014), resourceBackedImage15);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage16 = ImageProvider.fromResource(this, R.drawable.swim);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.319839,44.072839), resourceBackedImage16);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage17 = ImageProvider.fromResource(this, R.drawable.swim);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.355998,43.825254), resourceBackedImage17);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage18 = ImageProvider.fromResource(this, R.drawable.swim);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.275617296402764,44.021454050323484), resourceBackedImage18);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage19 = ImageProvider.fromResource(this, R.drawable.swim);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.370917,43.777617), resourceBackedImage19);
        placeMark.addTapListener(customTapListener);

        ImageProvider resourceBackedImage20 = ImageProvider.fromResource(this, R.drawable.swim);

        placeMark = mapView.getMap().getMapObjects().addPlacemark(new Point(56.50133941940466,43.53827747317333), resourceBackedImage20);
        placeMark.addTapListener(customTapListener);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
    }

    private void setListeners() {
        btn_chat.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), chat.class)));
        btn_profile.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), settings.class)));
        btn_calendar.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), calendar.class)));
        btn_users.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), UsersActivity.class)));
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                "android.permission.ACCESS_FINE_LOCATION")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.ACCESS_FINE_LOCATION"},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MapKitFactory.getInstance().onStop();
        mapView.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83)));

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, com.yandex.maps.mobile.R.drawable.arrow));

        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();

        userLocationView.getAccuracyCircle().setFillColor(Color.YELLOW & 0x99ffffff);
    }

    @Override
    public void onObjectRemoved(UserLocationView view) {
    }

    @Override
    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {
    }

    private void verifyUser() {
        String currentUserID = mAuth.getCurrentUser().getUid();

        rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("name").exists())
                {
                    Toast.makeText(MainActivity.this, "Привет", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent settingsIntent = new Intent(MainActivity.this, settings.class);
                    startActivity(settingsIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}