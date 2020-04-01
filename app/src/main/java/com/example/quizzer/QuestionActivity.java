package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private Button add,excel;
    private RecyclerView recyclerView;
    private QuestionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar=findViewById(R.id.toolbar4);
        String name=getIntent().getStringExtra("category");
        int set=getIntent().getIntExtra("setNo",1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name+"/set"+set);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add=findViewById(R.id.addbtn);
        excel=findViewById(R.id.excel);
        recyclerView=findViewById(R.id.review);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
       List<QuestionsModel> list=new ArrayList<>();
       list.add(new QuestionsModel("abcc","Question?","a","b","c","d","a",set));
        list.add(new QuestionsModel("abcc","Question?","a","b","c","d","a",set));
        list.add(new QuestionsModel("abcc","Question?","a","b","c","d","a",set));
        list.add(new QuestionsModel("abcc","Question?","a","b","c","d","a",set));
        list.add(new QuestionsModel("abcc","Question?","a","b","c","d","a",set));
        list.add(new QuestionsModel("abcc","Question?","a","b","c","d","a",set));
        list.add(new QuestionsModel("abcc","Question?","a","b","c","d","a",set));
        list.add(new QuestionsModel("abcc","Question?","a","b","c","d","a",set));
        adapter=new QuestionAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
