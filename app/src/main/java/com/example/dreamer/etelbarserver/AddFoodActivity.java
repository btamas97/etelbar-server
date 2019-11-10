package com.example.dreamer.etelbarserver;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddFoodActivity extends AppCompatActivity {

    private ImageButton foodImage;
    private static final int GALLREQ =1;
    private EditText name,desc,price;
    private Uri uri = null;
    private StorageReference storageReference = null;
    private DatabaseReference mRef;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Spinner dropdown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        dropdown = findViewById(R.id.categorySpinner);
        String[] items = new String[]{"előétel", "főétel", "desszert","ital"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        name = (EditText) findViewById(R.id.itemName);
        desc = (EditText) findViewById(R.id.itemDesc);
        price = (EditText) findViewById(R.id.itemPrice);
        storageReference = FirebaseStorage.getInstance().getReference("Item");


        mRef = FirebaseDatabase.getInstance().getReference("Item");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(AddFoodActivity.this,LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void addItemButtonClicked(View view) {
        final String name_text = name.getText().toString().trim();
        final String desc_text = desc.getText().toString().trim();
        final String price_text = price.getText().toString().trim();
        final int price = Integer.parseInt(price_text);
        final String category_text = dropdown.getSelectedItem().toString();

        if(!TextUtils.isEmpty(name_text) && !TextUtils.isEmpty(desc_text) &&!TextUtils.isEmpty(price_text)){
            StorageReference filepath = storageReference.child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloaduri = taskSnapshot.getDownloadUrl();
                    Toast.makeText(AddFoodActivity.this,"Image uploaded!",Toast.LENGTH_LONG).show();
                    final DatabaseReference newPost = mRef.push();
                    newPost.child("name").setValue(name_text);
                    newPost.child("category").setValue(category_text);
                    newPost.child("desc").setValue(desc_text);
                    newPost.child("price").setValue(price);
                    newPost.child("image").setValue(downloaduri.toString());
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLREQ && resultCode == RESULT_OK){
            uri = data.getData();
            foodImage =(ImageButton) findViewById(R.id.foodImageButton);
            foodImage.setImageURI(uri);
        }
    }

    public void imageButtonClicked(View view) {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLREQ);

    }
     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
