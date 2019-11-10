package com.example.dreamer.etelbarserver;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Order {
    private String name,status,profilephoto;
    private int tablenumber;
    private int totalPrice;
    private List<Food> orders;

    public Order(){

    }
    public Order(String name, String profilephoto, String status, int tablenumber,List<Food> orders,int totalPrice){
        this.name = name;
        this.profilephoto = profilephoto;
        this.tablenumber = tablenumber;
        this.orders=orders;
        this.status = status;
        this.totalPrice =totalPrice;
    }

    public String getName() {
        return this.name;
    }

    public String getProfilephoto() {
        return this.profilephoto;
    }

    public int getTablenumber() {
        return this.tablenumber;
    }

    public String getStatus() {
        return this.status;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public OrderViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView user_name = (TextView) mView.findViewById(R.id.orderUserName);
            user_name.setText(name);
        }

        public void setProfilePicture(Context ctx, String profilephoto){
            CircleImageView food_image = mView.findViewById(R.id.userImage);
            Picasso.with(ctx).load(profilephoto).into(food_image);
        }

        public void setTableNumber(int tablenumber){
            TextView table_number = (TextView) mView.findViewById(R.id.tableNumber);
            table_number.setText(String.valueOf(tablenumber));
        }
    }
}
