package com.example.dreamer.etelbarserver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FragmentOpenOrders extends android.support.v4.app.Fragment{
    View v;
    private RecyclerView myrecyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Query mQuery;

    public  FragmentOpenOrders(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.open_orders_fragment,container,false);
        mQuery  = mDatabase.orderByChild("status").equalTo("open");
        myrecyclerView = (RecyclerView) v.findViewById(R.id.open_recycler);
        FirebaseRecyclerAdapter<Order,Order.OrderViewHolder> recyclerViewAdapter = new FirebaseRecyclerAdapter<Order,Order.OrderViewHolder>(
                Order.class,
                R.layout.single_order_item,
                Order.OrderViewHolder.class,
                mQuery
        ) {
            @Override
            protected void populateViewHolder(Order.OrderViewHolder viewHolder, Order model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setProfilePicture(getContext(),model.getProfilephoto());
                viewHolder.setTableNumber(model.getTablenumber());

                final String order_key = getRef(position).getKey();
                final String order_status = model.getStatus();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleOrderActivity = new Intent(getActivity(),SingleOrderActivity.class);
                        singleOrderActivity.putExtra("orderID",order_key);
                        singleOrderActivity.putExtra("orderStatus",order_status);
                        startActivity(singleOrderActivity);
                    }
                });
            }
        };
        myrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerView.setAdapter(recyclerViewAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Order");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };
    }
}
