package com.example.dreamer.etelbarserver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if (firebaseAuth.getCurrentUser()==null){
                Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
            }
            }
        };

    }

    public void addFoodButtonClicked(View view) {
        Intent addFoodIntent = new Intent(MainActivity.this,AddFoodActivity.class);
        startActivity(addFoodIntent);
    }

    public void viewOrdersClicked(View view) {
        startActivity(new Intent(MainActivity.this, OrdersActivity.class));
    }

    public void freeAllTablesClicked(View view) {
        final DatabaseReference mDR = FirebaseDatabase.getInstance().getReference("Table");
        Query mQuery = mDR.orderByChild("isFree").equalTo(false);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        mDR.child(i.getKey()).child("isFree").setValue(true);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
