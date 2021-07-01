package com.example.simplecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddTrackActivity extends AppCompatActivity {

    TextView artName;
    SeekBar seek;
    Button btn;
    EditText et;
    ListView trackView;
    DatabaseReference databaseTracks;
    List<Track> tracks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        artName = findViewById(R.id.arName);
        seek = findViewById(R.id.rate);
        btn = findViewById(R.id.btnRate);
        et = findViewById(R.id.song);
        trackView = findViewById(R.id.liTrack);
        tracks = new ArrayList<>();

        Intent in = getIntent();
        String id = in.getStringExtra(MainActivity.DATADIRI_ID);
        String name = in.getStringExtra(MainActivity.DATADIRI_NAME);
        artName.setText(name);
        databaseTracks = FirebaseDatabase.getInstance().getReference("lagufavorite").child(in.getStringExtra(MainActivity.DATADIRI_ID));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrek();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tracks.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Track track = ds.getValue(Track.class);
                    tracks.add(track);
                }
                TrackList trackAdapter = new TrackList(AddTrackActivity.this,tracks);
                trackView.setAdapter(trackAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void saveTrek(){
        String trackName = et.getText().toString();
        int rating = seek.getProgress();
        if(!TextUtils.isEmpty(trackName)){
            String id = databaseTracks.push().getKey();
            Track track = new Track( id, trackName, rating);
            databaseTracks.child(id).setValue(track);

            Toast.makeText(getApplicationContext(),"Lagu Favorite Saved Succesfully",Toast.LENGTH_SHORT).show();

        }
    }
}
