package tbject.com.bombswepper.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import tbject.com.bombswepper.R;
import tbject.com.bombswepper.pojo.Player;

public class MapsTab extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tab);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null)
            onMapReady(mMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //insertPlayersToMaps
        mMap = googleMap;
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        MarkerOptions markerOptions=null;
        for (Player player : Menu.getInstance().getPlayers()) {
            markerOptions = new MarkerOptions();
            markerOptions.position(player.getLocation());
            markerOptions.title(player.getName());
            markerOptions.snippet("Time:" +player.getTime()+"\nAddress:"+ player.getAddress());
            mMap.addMarker(markerOptions);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(markerOptions.getPosition()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(7));
    }


}
