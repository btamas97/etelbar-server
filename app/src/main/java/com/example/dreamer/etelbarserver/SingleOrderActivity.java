package com.example.dreamer.etelbarserver;


import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleOrderActivity extends AppCompatActivity {
    private String order_key = null;
    private String order_status = null;
    private String user_name, user_image;
    private int table_number;
    private int total_price;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private CircleImageView profilePicture;
    private TextView userName;
    private TextView tableNumber;
    private TextView totalPrice;
    public static List<Food> orders = new ArrayList<>();
    FoodRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        order_key = getIntent().getExtras().getString("orderID");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Order");

        profilePicture =  findViewById(R.id.profilePicture);
        userName = (TextView) findViewById(R.id.userName);
        tableNumber = (TextView) findViewById(R.id.tableNumber);
        totalPrice = findViewById(R.id.totalPrice);

        mAuth = FirebaseAuth.getInstance();
        mDatabase.child(order_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    user_name = (String) dataSnapshot.child("name").getValue();
                    user_image = (String) dataSnapshot.child("profilephoto").getValue();
                    table_number = dataSnapshot.child("tablenumber").getValue(Integer.class);
                    total_price = dataSnapshot.child("totalprice").getValue(Integer.class);
                    Iterable<DataSnapshot> children = dataSnapshot.child("orders").getChildren();
                    orders.clear();
                    for(DataSnapshot child:children){
                        Food food = child.getValue(Food.class);
                        orders.add(food);
                    }
                    initRecyclerView();
                }
                else {
                    startActivity(new Intent(SingleOrderActivity.this,OrdersActivity.class));
                    finish();
                }

                userName.setText(user_name);
                Picasso.with(SingleOrderActivity.this).load(user_image).into(profilePicture);
                tableNumber.setText(String.valueOf(table_number));
                totalPrice.setText(String.valueOf(total_price+"Ft"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        order_status = getIntent().getExtras().getString("orderStatus");
        Button statusButton=(Button)findViewById(R.id.status_button);
        Button tableButton = findViewById(R.id.btn_free_table);
        if (order_status.equals("paid")){
            statusButton.setVisibility(View.GONE);
            tableButton.setVisibility(View.VISIBLE);
        }else {
            statusButton.setVisibility(View.VISIBLE);
            tableButton.setVisibility(View.GONE);
        }


    }
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.orderRecycler);
        adapter = new FoodRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void changeStatus(View view) {
        order_status = getIntent().getExtras().getString("orderStatus");
        Toast.makeText(SingleOrderActivity.this,order_status,Toast.LENGTH_SHORT).show();
        if(order_status.equals("open")){
            mDatabase.child(order_key).child("status").setValue("served");
        } else if(order_status.equals("served")){
            mDatabase.child(order_key).child("status").setValue("paid");
        }

    }

    public void deleteOrder(View view) {
        tableStatusToFree();
        mDatabase.child(order_key).removeValue();
    }
    public void freeTable(View view) {
        tableStatusToFree();
    }
    public void tableStatusToFree(){
        final DatabaseReference mDR = FirebaseDatabase.getInstance().getReference("Table");
        Query mQuery = mDR.orderByChild("number").equalTo(table_number);
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
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SingleOrderActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
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
