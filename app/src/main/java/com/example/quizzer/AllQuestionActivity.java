package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

public class AllQuestionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private String setId;
    FirebaseRecyclerAdapter<AllQuestionModel,QuestionViewHolder> firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_question);
        setId=getIntent().getStringExtra("setId");
        recyclerView=(RecyclerView)findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("SETS").child(setId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
}
