package com.example.dreamer.etelbarserver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Food {
    private String name;
    private String image;
    private int price;
    public Food(){

    }
    public Food(String name, int price, String image  ){

        this.name = name;
        this.image = image;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public String getImage() {
        return this.image;
    }

    public int getPrice(){
        return this.price;
    }
}
