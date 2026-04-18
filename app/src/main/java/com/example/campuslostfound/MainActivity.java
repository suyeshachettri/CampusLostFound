package com.example.campuslostfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerItems;
    FloatingActionButton fabAdd;

    ArrayList<Item> itemList;
    ItemAdapter adapter;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerItems = findViewById(R.id.recyclerItems);
        fabAdd = findViewById(R.id.fabAdd);

        recyclerItems.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();
        adapter = new ItemAdapter(this, itemList);
        recyclerItems.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddItemActivity.class));
        });

        loadItems();
    }

    private void loadItems() {
        db.collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Item item = doc.toObject(Item.class);
                        item.id = doc.getId();

                        // Skip incomplete documents
                        if (item.name != null && item.location != null && item.type != null) {
                            itemList.add(item);
                        }
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }
}