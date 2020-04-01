package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private Button add,excel;
    private RecyclerView recyclerView;
    private QuestionAdapter adapter;
    private Dialog load;
    private List<QuestionsModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar=findViewById(R.id.toolbar4);
        load=new Dialog(this);

        load.setContentView(R.layout.loading);
        load.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
        load.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        load.setCancelable(false);
        final String name=getIntent().getStringExtra("category");
        final int set=getIntent().getIntExtra("setNo",1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name+"/set"+set);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add=findViewById(R.id.addbtn);
        excel=findViewById(R.id.excel);
        recyclerView=findViewById(R.id.review);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
         list=new ArrayList<>();
        adapter=new QuestionAdapter(list);
        recyclerView.setAdapter(adapter);
        getData(name,set);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addQuestion=new Intent(QuestionActivity.this,AddNewQuestionActivity.class);
                addQuestion.putExtra("category",name);
                addQuestion.putExtra("set",set);
                startActivity(addQuestion);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getData(String name, final int set){
        load.show();
        FirebaseDatabase.getInstance().getReference().child("SETS").child(name).child("questions").orderByChild("setNo").equalTo(set).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String id=dataSnapshot1.getKey();
                    String question=dataSnapshot1.child("question").getValue().toString();
                    String a=dataSnapshot1.child("optiona").getValue().toString();
                    String b=dataSnapshot1.child("optionb").getValue().toString();
                    String c=dataSnapshot1.child("optionc").getValue().toString();
                    String d=dataSnapshot1.child("optiond").getValue().toString();
                    String correctans=dataSnapshot1.child("correctans").getValue().toString();
                    list.add(new QuestionsModel(id,question,a,b,c,d,correctans,set));
                }
                load.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuestionActivity.this,"Error",Toast.LENGTH_LONG).show();
                load.dismiss();
                finish();
            }
        });
    }

}
