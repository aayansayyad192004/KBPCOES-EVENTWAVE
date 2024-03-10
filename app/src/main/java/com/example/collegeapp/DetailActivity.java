
package com.example.collegeapp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
            TextView title;
            ImageView upload;
            TextView uploadtext;
            Uri imageuri = null;
            ImageView imageView;



    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<ImageModel> models;
    FloatingActionButton fab;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Images");


    @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_detail);
                title = findViewById(R.id.detailTitle);
                imageView = findViewById(R.id.detailImage);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        models=new ArrayList<>();
        adapter=new Adapter(models,this);
        recyclerView.setAdapter(adapter);
        fab=findViewById(R.id.fab);


        if(AppData.isLoggedIn){
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailActivity.this,uploadinActivity.class);
                    startActivity(intent);

                }

            });}
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ImageModel model=dataSnapshot.getValue(ImageModel.class);
                    models.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


                title.setText(getIntent().getExtras().getString("Caption"));
                String imgURL = getIntent().getStringExtra("imageURL");

                Glide.with(this)
                        .load(imgURL)
                        .into(imageView);


                upload = findViewById(R.id.uploadpdf);
    uploadtext=findViewById(R.id.uploadtext);
                // After Clicking on this we will be
                // redirected to choose pdf


                if (AppData.isLoggedIn) {
                    upload.setVisibility(View.VISIBLE);
                    upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent galleryIntent = new Intent();
                            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                            // We will be redirected to choose pdf
                            galleryIntent.setType("application/pdf");
                            startActivityForResult(galleryIntent, 1);
                        }
                    });
                }
                else{
                    upload.setVisibility(View.INVISIBLE);
                    uploadtext.setVisibility(View.INVISIBLE);
                }
            }
                ProgressDialog dialog;

                @Override
                protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                    super.onActivityResult(requestCode, resultCode, data);
                    if (resultCode == RESULT_OK) {

                        // Here we are initialising the progress dialog box
                        dialog = new ProgressDialog(this);
                        dialog.setMessage("Uploading");

                        // this will show message uploading
                        // while pdf is uploading
                        dialog.show();
                        imageuri = data.getData();
                        final String timestamp = "" + System.currentTimeMillis();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        final String messagePushID = timestamp;
                        Toast.makeText(DetailActivity.this, imageuri.toString(), Toast.LENGTH_SHORT).show();

                        // Here we are uploading the pdf in firebase storage with the name of current time
                        final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");
                        Toast.makeText(DetailActivity.this, filepath.getName(), Toast.LENGTH_SHORT).show();
                        filepath.putFile(imageuri).continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull Task task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return filepath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    // After uploading is done it progress
                                    // dialog box will be dismissed
                                    dialog.dismiss();
                                    Uri uri = task.getResult();
                                    String myurl;
                                    myurl = uri.toString();
                                    Toast.makeText(DetailActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(DetailActivity.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }


