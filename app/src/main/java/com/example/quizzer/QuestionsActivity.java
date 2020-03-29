package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference();
    private TextView quest,noIndicator;
    private FloatingActionButton bookmark;
    private LinearLayout optionlist;
    private Button share,next;
    private int count=0;
    private int position=0;
    List<QuestionModel> list;
    private int score=0;
    private String category;
    private int setNo;
    private Dialog load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        load=new Dialog(this);
        load.setContentView(R.layout.loading);
        load.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
        load.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        load.setCancelable(false);

        category=getIntent().getStringExtra("category");
        setNo=getIntent().getIntExtra("setNo",1);
        quest=(TextView)findViewById(R.id.question);
        noIndicator=(TextView)findViewById(R.id.number);
        bookmark=(FloatingActionButton)findViewById(R.id.bookmark);
        optionlist=(LinearLayout)findViewById(R.id.l2);
        share=(Button)findViewById(R.id.share);
        next=(Button)findViewById(R.id.next);


        list=new ArrayList<>();
        load.show();
        reference.child("SETS").child(category).child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    list.add(dataSnapshot1.getValue(QuestionModel.class));
                }
                if(list.size()>0){
                    for (int i=0;i<4;i++){
                        optionlist.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkAnswer((Button)v);
                            }
                        });
                    }
                    playAnim(quest,0,list.get(position).getQuestion());

                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            next.setEnabled(false);
                            next.setAlpha(0.7f);
                            enableOption(true);
                            position++;
                            if(position==list.size()){
                                Intent scoreIntent=new Intent(QuestionsActivity.this,ScoreActivity.class);
                                scoreIntent.putExtra("score",score);
                                scoreIntent.putExtra("total",list.size());
                                startActivity(scoreIntent);
                                finish();
                                return;
                            }
                            count=0;
                            playAnim(quest,0,list.get(position).getQuestion());
                        }
                    });
                }else {
                    Toast.makeText(QuestionsActivity.this,"no questions",Toast.LENGTH_LONG).show();
                }
                load.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(QuestionsActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                load.dismiss();
                finish();
            }
        });

    }
    private void playAnim(final View view, final int value, final String data){
        view.animate().alpha(value).scaleY(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        if(value==0&& count<4){
                            String option="";
                            if(count==0){
                                option=list.get(position).getOptiona();
                            }
                            else  if(count==1){
                                option=list.get(position).getOptionb();
                            }
                            else  if(count==2){
                                option=list.get(position).getOptionc();
                            }
                            else  if(count==3){
                                option=list.get(position).getOptiond();
                            }
                            playAnim(optionlist.getChildAt(count),0,option);
                            count++;
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(value==0){
                            try {
                                ((TextView)view).setText(data);
                                noIndicator.setText(position+1+"/"+list.size());

                            }catch (ClassCastException e){
                                ((Button)view).setText(data);
                            }
                            view.setTag(data);
                            playAnim(view,1,data);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }
    private void checkAnswer(Button selectedOption){

        enableOption(false);
        next.setEnabled(true);
        next.setAlpha(1);
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
