package com.example.paniermobil.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.paniermobil.Models.NewProductsModel;
import com.example.paniermobil.Models.PopularProductModel;
import com.example.paniermobil.Models.ShowAllModel;
import com.example.paniermobil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating, name, description, price, quantity;
    Button addToCart, buyNow;
    ImageView addItems, removeItems;

    int totalQuantity = 1;
    int totalPrice = 0;
    //New Products
    NewProductsModel newProductsModel = null;

    //show All
    ShowAllModel showAllModel = null;
    FirebaseAuth auth;
    //Popular product
    PopularProductModel popularProductModel = null;
    private FirebaseFirestore firestore;
    private Object drawableImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        final Object obj = getIntent().getSerializableExtra("detailed");

        if(obj instanceof NewProductsModel){
            newProductsModel = (NewProductsModel) obj;
        }else if (obj instanceof  PopularProductModel){
            popularProductModel = (PopularProductModel) obj;
        }else if (obj instanceof  ShowAllModel){
            showAllModel = (ShowAllModel) obj;
        }


        detailedImg = findViewById(R.id.detailed_img);
        name = findViewById(R.id.detailed_name);
        rating = findViewById(R.id.rating);
        description = findViewById(R.id.detailed_description);
        price = findViewById(R.id.detailed_price);
        quantity = findViewById(R.id.quantity);
        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);

        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);

        //New products
        if (newProductsModel != null){
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailedImg);
            name.setText(newProductsModel.getName());
            rating.setText(newProductsModel.getRating());
            description.setText(newProductsModel.getDescription());
            price.setText(String.valueOf(newProductsModel.getPrice()));
            name.setText(newProductsModel.getName());

            totalPrice = newProductsModel.getPrice() * totalQuantity;
        }
        //Popular products
        if (popularProductModel  != null){
            Glide.with(getApplicationContext()).load(popularProductModel .getImg_url()).into(detailedImg);
            name.setText(popularProductModel .getName());
            rating.setText(popularProductModel .getRating());
            description.setText(popularProductModel.getDescription());
            price.setText(String.valueOf(popularProductModel .getPrice()));
            name.setText(popularProductModel .getName());

            totalPrice = popularProductModel.getPrice() * totalQuantity;
        }

        //Show All products
        if (showAllModel  != null){
            Glide.with(getApplicationContext()).load(showAllModel .getImg_url()).into(detailedImg);
            name.setText(showAllModel .getName());
            rating.setText(showAllModel .getRating());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel .getPrice()));
            name.setText(showAllModel .getName());

            totalPrice = showAllModel.getPrice() * totalQuantity;
        }

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtoCart();
            }
        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity < 10 ){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if(newProductsModel != null){
                        totalPrice = newProductsModel.getPrice() * totalQuantity;
                    }
                    if(popularProductModel != null){
                        totalPrice = popularProductModel.getPrice() * totalQuantity;
                    }
                    if(showAllModel != null){
                        totalPrice = showAllModel.getPrice() * totalQuantity;
                    }
                }
            }
        });

        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });
    }

    private void addtoCart() {
        String saveCurrentTime,saveCurrentDate;
        Calendar calForaDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForaDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForaDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("productName",name.getText().toString());
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalprice",totalPrice);


        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(DetailsActivity.this, "Added To A Cart", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}