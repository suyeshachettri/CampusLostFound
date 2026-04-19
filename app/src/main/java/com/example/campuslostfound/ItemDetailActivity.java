package com.example.campuslostfound;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ItemDetailActivity extends AppCompatActivity {

    ImageView detailImage;
    TextView detailName, detailLocation, detailDescription, detailType, detailStatus;
    Button btnMarkReturned;

    FirebaseFirestore db;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        detailImage = findViewById(R.id.detailImage);
        detailName = findViewById(R.id.detailName);
        detailLocation = findViewById(R.id.detailLocation);
        detailDescription = findViewById(R.id.detailDescription);
        detailType = findViewById(R.id.detailType);
        detailStatus = findViewById(R.id.detailStatus);
        btnMarkReturned = findViewById(R.id.btnMarkReturned);

        db = FirebaseFirestore.getInstance();

        id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String location = getIntent().getStringExtra("location");
        String description = getIntent().getStringExtra("description");
        String type = getIntent().getStringExtra("type");
        String image = getIntent().getStringExtra("image");
        String status = getIntent().getStringExtra("status");

        detailName.setText(name);
        detailLocation.setText("Location: " + location);
        detailDescription.setText("Description: " + description);
        detailType.setText("Type: " + type.toUpperCase());
        detailStatus.setText("Status: " + (status != null ? status : "active"));

        if (type != null && type.equals("lost")) {
            btnMarkReturned.setText("Mark as Found");
        } else {
            btnMarkReturned.setText("Mark as Returned");
        }

        if (image != null && !image.isEmpty()) {
            try {
                byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                detailImage.setImageBitmap(decodedBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        btnMarkReturned.setOnClickListener(v -> markReturned());
    }

    private void markReturned() {

        String type = getIntent().getStringExtra("type");

        String newStatus;

        if (type != null && type.equals("lost")) {
            newStatus = "found";
        } else {
            newStatus = "returned";
        }

        db.collection("items").document(id)
                .update("status", newStatus)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}