package com.example.quizzer.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

import com.example.quizzer.MainActivity;
import com.example.quizzer.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class CategoriesActivity extends AppCompatActivity {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference();
    private RecyclerView recyclerView;
    private Dialog load,categoryd;
     public  static List<CategoriesModel> list;
    private CircleImageView image;
    private EditText name;
    private CategoriesAdapter categoriesAdapter;
    private Uri images;
    private Button add;
    private String downloadUrl;
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
        categoriesAdapter=new CategoriesAdapter(list, new CategoriesAdapter.DeleteListener() {
            @Override
            public void onDelete(final String key, final int position) {
                new AlertDialog.Builder(CategoriesActivity.this,R.style.Theme_AppCompat_Light_Dialog).
                        setTitle("Delete Category").
                        setMessage("Are you Sure to delete this Category?").
                        setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                load.show();
                                reference.child("Categories").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            for(String setIds:list.get(position).getSets()) {
                                                reference.child("SETS").child("sets").removeValue();
                                                list.remove(position);
                                            }
                                            categoriesAdapter.notifyDataSetChanged();
                                                    }
                                        else {
                                            Toast.makeText(CategoriesActivity.this,"Fail",Toast.LENGTH_LONG).show();
                                            }
                                        load.dismiss();
                                                }
                                            });
                                        }

                        }).setNegativeButton("Cancel",null)
                        .setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        });
        recyclerView.setAdapter(categoriesAdapter);
        load.show();
        reference.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    List<String > sets=new ArrayList<>();
                    for(DataSnapshot dataSnapshot2:dataSnapshot1.child("sets").getChildren()){
                        sets.add(dataSnapshot2.getKey());
                    }
                    list.add(new CategoriesModel(dataSnapshot1.child("name").getValue().toString(),dataSnapshot1.child("url").getValue().toString()
                            ,dataSnapshot1.getKey(),sets));
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
        if(item.getItemId()==R.id.logout){
            new AlertDialog.Builder(CategoriesActivity.this,R.style.Theme_AppCompat_Light_Dialog).
                    setTitle("Logging Out").setMessage("Are you Sure to delete this Delete?").
                    setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            load.show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(CategoriesActivity.this, MainActivity.class));
                            finish();
                        }
                    }).setNegativeButton("Cancel",null).setIcon(R.drawable.ic_dialog_alert).show();
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
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryintent,101);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()||name.getText()==null){
                    name.setError("required");
                    return;
                }
                for (CategoriesModel categoriesModel:list){
                    if(name.getText().toString().equals(categoriesModel.getName())){
                        name.setError("Category Name Already Exists");
                        return;
                    }
                }
                if(image==null){
                    Toast.makeText(CategoriesActivity.this,"Please select a image",Toast.LENGTH_LONG).show();
                    return;
                }
                categoryd.dismiss();
                uploadData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101){
            if(resultCode==RESULT_OK){
                images=data.getData();
                image.setImageURI(images);
            }
        }
    }
    private void uploadData(){
        load.show();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        final StorageReference imageRefernec=storageReference.child("Categories").child(images.getLastPathSegment());
        UploadTask uploadTask=imageRefernec.putFile(images);
        Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()) {
                    throw task.getException();
                }
                return imageRefernec.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadUrl=task.getResult().toString();
                            uploadName();
                        }
                        load.dismiss();
                    }
                });
            }
        });
    }
    private void uploadName(){
        Map<String ,Object> map=new HashMap<>();
        map.put("name",name.getText().toString());
        map.put("url",downloadUrl);
        map.put("sets",0);
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final String id= UUID.randomUUID().toString();
        database.getReference().child("Categories").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    list.add(new CategoriesModel(name.getText().toString(),id, downloadUrl,new ArrayList<String>()));
                    categoriesAdapter.notifyDataSetChanged();
                }
                load.dismiss();
            }
        });
    }
}