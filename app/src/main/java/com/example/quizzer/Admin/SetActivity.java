package com.example.quizzer.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.quizzer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.UUID;

public class SetActivity extends AppCompatActivity {

    private GridView gridView;
    private Dialog load;
    private GriddAdapter gridAdapter;
    private String categoryName;
    private DatabaseReference mref;
    private List<String> sets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);
        gridView=(GridView)findViewById(R.id.grid_view);
        Toolbar toolbar=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categoryName=getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(categoryName);
        mref=FirebaseDatabase.getInstance().getReference();
        load=new Dialog(this);

        load.setContentView(R.layout.loading);
        load.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
        load.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        load.setCancelable(false);
        sets=CategoriesActivity.list.get(getIntent().getIntExtra("position", 0)).getSets();
        gridAdapter=new GriddAdapter(sets, getIntent().getStringExtra("title"), new GriddAdapter.GriddListener() {
            @Override
            public void addset() {
                load.show();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                final String id = UUID.randomUUID().toString();
                database.child("Categories").child(getIntent().getStringExtra("key")).child("sets").child(id).setValue("SET ID").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            sets.add(id);
                            gridAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(SetActivity.this, "fail", Toast.LENGTH_LONG).show();
                        }
                        load.dismiss();
                    }
                });
            }
            @Override
            public void onLongClick(final String setId,int position) {
                new AlertDialog.Builder(SetActivity.this,R.style.Theme_AppCompat_Light_Dialog).
                        setTitle("Delete Set"+position).setMessage("Are you Sure to delete this Delete?").
                        setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                load.show();
                                mref.child("SETS").child(setId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mref.child("Categories").child(CategoriesActivity.list.get(getIntent().getIntExtra("position", 0)).getKey()).
                                                    child("sets").child(setId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                       sets.remove(setId);
                                                        gridAdapter.notifyDataSetChanged();
                                                    }
                                                    load.dismiss();
                                                }
                                            });

                                        } else {
                                            Toast.makeText(SetActivity.this,"Error",Toast.LENGTH_LONG).show();
                                        }
                                        load.dismiss();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel",null).setIcon(R.drawable.ic_dialog_alert).show();
            }
        });
        gridView.setAdapter(gridAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}