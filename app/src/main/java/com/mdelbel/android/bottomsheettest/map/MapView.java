package com.mdelbel.android.bottomsheettest.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mdelbel.android.bottomsheettest.R;

public class MapView extends CoordinatorLayout implements OnMapReadyCallback {

    private RecyclerView agencyCarousel;
    private int mapHeight;

    public MapView(Context context) {
        super(context);
        init();
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_map, this);

        agencyCarousel = findViewById(R.id.agency_carousel);
        initCarousel();
    }

    private void initCarousel() {
        RecyclerView.LayoutManager agencyCarouselManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(agencyCarousel);
        agencyCarousel.setLayoutManager(agencyCarouselManager);
        agencyCarousel.setAdapter(new AgencyAdapter());
    }

    public void initMap(@NonNull FragmentManager fragmentManager) {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.view_map_container, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setUpCarouselBehavior();

        centerMap(googleMap);
        addMarkers(googleMap);
    }

    private void setUpCarouselBehavior() {
        final FrameLayout mapContainer = findViewById(R.id.view_map_container);
        calculateMapHeight(mapContainer);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.agency_carousel_container));
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // do nothing
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mapContainer.getLayoutParams();
                params.height = mapHeight - (int) (bottomSheet.getLayoutParams().height * slideOffset);

                mapContainer.setLayoutParams(params);
            }
        });
    }

    private void calculateMapHeight(@NonNull final FrameLayout mapContainer) {
        ViewTreeObserver observer = mapContainer.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mapHeight = mapContainer.getHeight();
                mapContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void centerMap(@NonNull GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng coordinate = new LatLng(-31.416666666667, -64.183333333333);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 13);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
        googleMap.animateCamera(yourLocation);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void addMarkers(@NonNull GoogleMap googleMap) {
        double lat = -31.416666666667;
        double lng = -64.183333333333;
        for (int i = 0; i < 10; i++) {
            lat += Math.random() * 0.01;
            lng += Math.random() * 0.01;

            Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Sucursal " + i));
            marker.setTag(i);
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Integer pos = (Integer) marker.getTag();
                agencyCarousel.smoothScrollToPosition(pos);
                return false;
            }
        });
    }
}
