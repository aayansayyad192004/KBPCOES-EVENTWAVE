package com.example.collegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class uploadinActivity extends AppCompatActivity {


    ImageView imageView;
    Button savebtn,uploadbtn;
    ProgressBar progressBar;

    FloatingActionButton fab;

    //firebase
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Images");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    Uri uri;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploaddetail);
        imageView=findViewById(R.id.image_view);
        savebtn=findViewById(R.id.save_btn);
        uploadbtn=findViewById(R.id.upload_btn);
        progressBar=findViewById(R.id.progressbar);

        //progressbar
        progressBar.setVisibility(View.INVISIBLE);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,2);
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri!=null){
                    UploadImageToFirebase(uri);
                }
                else{
                    Toast.makeText(uploadinActivity.this,"Please select an image!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UploadImageToFirebase(Uri uri) {

        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        StorageReference file= storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageModel model=new ImageModel(uri.toString());
                        String Smodel=reference.push().getKey();
                        reference.child(Smodel).setValue(model);
                        progressBar.setVisibility(View.INVISIBLE);
                        imageView.setImageResource(R.drawable.baseline_add_photo_alternate_24);
                        Toast.makeText(uploadinActivity.this,"Image Uploaded Successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(uploadinActivity.this, DetailActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onProgress(UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(uploadinActivity.this,"Failed!!!",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data!=null ){
            uri=data.getData();
            imageView.setImageURI(uri);
        }
    }

    }
