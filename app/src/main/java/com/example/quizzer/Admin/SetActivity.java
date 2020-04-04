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

public class SetActivity extends AppCompatActivity {

    private GridView gridView;
    private Dialog load;
    private GriddAdapter gridAdapter;
    private String categoryName;
    private DatabaseReference mref;
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
        gridAdapter=new GriddAdapter(getIntent().getIntExtra("sets", 0), getIntent().getStringExtra("title"), new GriddAdapter.GriddListener() {
            @Override
            public void addset() {
                load.show();
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                database.getReference().child("Categories").child(getIntent().getStringExtra("key")).child("sets").setValue(getIntent().getIntExtra("sets", 0)+1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            gridAdapter.sets++;
                            gridAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(SetActivity.this,"fail",Toast.LENGTH_LONG).show();
                        }
                        load.dismiss();
                    }
                });
            }

            @Override
            public void onLongClick(final int setNo) {
                new AlertDialog.Builder(SetActivity.this,R.style.Theme_AppCompat_Light_Dialog).
                        setTitle("Delete Set"+setNo).setMessage("Are you Sure to delete this Delete?").
                        setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                load.show();
                                mref.child("SETS").child(categoryName).child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                            String id=dataSnapshot1.getKey();
                                            mref.child("SETS").child(categoryName).child("questions").child(id).removeValue();
                                        }
                                        gridAdapter.sets--;
                                        load.dismiss();
                                        gridAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(SetActivity.this,"Error Found",Toast.LENGTH_LONG).show();
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