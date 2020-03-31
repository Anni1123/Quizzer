package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CategoriesActivity extends AppCompatActivity {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference();
        private RecyclerView recyclerView;
        private Dialog load,categoryd;
        List<CategoriesModel> list;
        private CircleImageView image;
        private EditText name;
        private Button add;
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
            setCategoryd();
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
                categoryd.show();
            }
            return super.onOptionsItemSelected(item);
        }
        private void setCategoryd(){
            categoryd=new Dialog(this);
            categoryd.setContentView(R.layout.add_category_layout);
            categoryd.getWindow().setBackgroundDrawable(getDrawable(R.drawable.square_shape));
            categoryd.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            categoryd.setCancelable(true);
            image=categoryd.findViewById(R.id.image);
            name=categoryd.findViewById(R.id.categoryname);
            add=categoryd.findViewById(R.id.addd);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryintent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryintent,101);
                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(name.getText().toString().isEmpty()){
                        name.setError("required");
                        return;
                    }
                    categoryd.dismiss();
                }
            });
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101){
            if(resultCode==RESULT_OK){
                Uri images=data.getData();
                image.setImageURI(images);
            }
        }
    }
}

