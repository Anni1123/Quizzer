package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CategoriesActivity extends AppCompatActivity {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference();
        private RecyclerView recyclerView;
        private Dialog load;
        List<CategoriesModel> list;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_category);
            recyclerView=(RecyclerView)findViewById(R.id.rv);
            Toolbar toolbar=findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Categories");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            load=new Dialog(this);
            load.setContentView(R.layout.loading);
            load.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
            load.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            load.setCancelable(false);
            LinearLayoutManager layoutManager=new LinearLayoutManager(this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            list=new ArrayList<>();
            final CategoriesAdapter categoriesAdapter=new CategoriesAdapter(list);
            recyclerView.setAdapter(categoriesAdapter);
            load.show();
            reference.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        list.add(dataSnapshot1.getValue(CategoriesModel.class));
                    }
                    categoriesAdapter.notifyDataSetChanged();
                    load.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(CategoriesActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                    load.dismiss();
                    finish();
                }
            });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
            return super.onCreateOptionsMenu(menu);
    }

    @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if(item.getItemId()==R.id.add){
              Toast.makeText(this,"Done",Toast.LENGTH_LONG).show();
            }
            return super.onOptionsItemSelected(item);
        }
    }

