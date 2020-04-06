package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.xwpf.usermodel.TOC;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AllQuestionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private String setId;
    private int score=0;
    private int position=0;
    List<AllQuestionModel> list;
    private LinearLayout optionlist;
    FirebaseRecyclerAdapter<AllQuestionModel,QuestionViewHolder> firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_question);
        setId=getIntent().getStringExtra("setId");
        list=new ArrayList<>();
        View view=getLayoutInflater().inflate(R.layout.question_row,null);
        optionlist=(LinearLayout)view.findViewById(R.id.linear2);
        recyclerView=(RecyclerView)findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("SETS").child(setId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    list.add(dataSnapshot1.getValue(AllQuestionModel.class));
                    Toast.makeText(AllQuestionActivity.this,"Laoded Here Successfully", Toast.LENGTH_LONG).show();

                }
                if(list.size()>0) {
                    for (int i = 0; i < 4; i++) {
                        Toast.makeText(AllQuestionActivity.this,"Laoded Successfully", Toast.LENGTH_LONG).show();
                        optionlist.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(AllQuestionActivity.this,"Laoded Successfully", Toast.LENGTH_LONG).show();
                                checkAnswer(((Button)v));
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = databaseReference
                .limitToLast(50)
                .orderByKey();
        FirebaseRecyclerOptions<AllQuestionModel> options =
                new FirebaseRecyclerOptions.Builder<AllQuestionModel>()
                        .setQuery(query,AllQuestionModel.class)
                        .build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<AllQuestionModel, QuestionViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull QuestionViewHolder holder, int position, @NonNull AllQuestionModel model) {
                holder.setQuestion(model.getQuestion());
                holder.setA(model.getOptiona());
                holder.setB(model.getOptionb());
                holder.setC(model.getOptionc());
                holder.setD(model.getOptiond());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent questionIntent=new Intent(AllQuestionActivity.this,QuestionsActivity.class);
                        questionIntent.putExtra("setId",setId);
                        startActivity(questionIntent);
                    }
                });
            }

            @NonNull
            @Override
            public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.question_row, parent, false);
                return new QuestionViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class QuestionViewHolder extends RecyclerView.ViewHolder{
        View view;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setA(String A){
            Button a=(Button)view.findViewById(R.id.button1);
            a.setText(A);
        }
        public void setB(String B){
            Button b=(Button)view.findViewById(R.id.button2);
            b.setText(B);
        }
        public void setC(String C){
            Button c=(Button)view.findViewById(R.id.button3);
            c.setText(C);
        }
        public void setD(String D){
            Button d=(Button)view.findViewById(R.id.button4);
            d.setText(D);
        } public void setQuestion(String question){
            TextView q=(TextView) view.findViewById(R.id.questionhere);
            q.setText(question);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
    private void checkAnswer(Button selectedOption){
        enableOption(false);
        if(selectedOption.getText().toString().equals(list.get(position).getCorrectans())){
            score++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
        else {
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctans=(Button)optionlist.findViewWithTag(list.get(position).getCorrectans());
            correctans.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }

    }
    private void enableOption(Boolean enabled){
        for (int i=0;i<4;i++){
            optionlist.getChildAt(i).setEnabled(enabled);
            if(enabled){
                optionlist.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#969898")));
            }
        }
    }
}
