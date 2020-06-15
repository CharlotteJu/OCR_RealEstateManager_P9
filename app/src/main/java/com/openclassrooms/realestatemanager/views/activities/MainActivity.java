package com.openclassrooms.realestatemanager.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.views.fragments.DetailFragment;

public class MainActivity extends AppCompatActivity {

    DetailFragment fragment = new DetailFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_frame_layout, fragment).commit();

    }



}
