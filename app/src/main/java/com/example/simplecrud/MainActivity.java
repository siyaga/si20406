package com.example.simplecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String DATADIRI_NAME = "artistName";
    public static final String DATADIRI_NOMOR = "artistNomor";
    public static final String DATADIRI_ID = "artistId";

    EditText e,o;
    Button b;
    Spinner sp;
    DatabaseReference databaseArtist;
    ListView lv;
    List<DataDiri> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtist = FirebaseDatabase.getInstance().getReference("datadiri");

        e = findViewById(R.id.et);
        o = findViewById(R.id.eo);
        b = findViewById(R.id.btn);
        lv = findViewById(R.id.li);
        list = new ArrayList<>();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataDiri dataDiri = list.get(position);
                Intent in = new Intent(getApplicationContext(),AddTrackActivity.class);
                in.putExtra(DATADIRI_ID, dataDiri.getId());
                in.putExtra(DATADIRI_NOMOR, dataDiri.getNomor());
                in.putExtra(DATADIRI_NAME, dataDiri.getName());
                startActivity(in);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DataDiri ar = list.get(position);
                showUpdateDialog(ar.getId(),ar.getName());
                return true;
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        databaseArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot as : dataSnapshot.getChildren()){
                    DataDiri dataDiri = as.getValue(DataDiri.class);
                    list.add(dataDiri);
                }

                DataDiriList adapter = new DataDiriList(MainActivity.this,list);
                lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final String dataID, String artistName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.na);
        final EditText editTextNomor = (EditText) dialogView.findViewById(R.id.no);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.bun);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteArtist);

        dialogBuilder.setTitle(artistName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String nomor = editTextNomor.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    updateDataDiri(dataID, name, nomor);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDataDiri(dataID);
            }
        });
     }

     private boolean deleteDataDiri(String id){
         DatabaseReference dR = FirebaseDatabase.getInstance().getReference("datadiri").child(id);
         dR.removeValue();

         DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("lagufavorite").child(id);
         drTracks.removeValue();
         Toast.makeText(getApplicationContext(), "Data Diri TerDeleted", Toast.LENGTH_LONG).show();

         return true;
     }
    private boolean updateDataDiri(String id, String name, String nomor) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("datadiri").child(id);

        //updating artist
        DataDiri dataDiri = new DataDiri(id, name,nomor);
        dR.setValue(dataDiri);
        Toast.makeText(getApplicationContext(), "Data Diri TerUpdated", Toast.LENGTH_LONG).show();
        return true;
    }

    public void add(){
        String name = e.getText().toString();
        String nomor = o.getText().toString();
        if(!TextUtils.isEmpty(name)){

            String id = databaseArtist.push().getKey();
            DataDiri art = new DataDiri(id, name,nomor);
            databaseArtist.child(id).setValue(art);
            Toast.makeText(getApplicationContext(),"DataDiri added Successfully",Toast.LENGTH_SHORT).show();
            e.setText("");
            o.setText("");
        }else{
            Toast.makeText(getApplicationContext(),"Enter a valid name.",Toast.LENGTH_SHORT).show();
        }
    }

}
