package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
    private String setId;
    private Dialog load;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private List<QuestionModel> bookmarkslist;
    private static final String FILE_NAME="QUIZZER";
    private static final String KEY_NAME="QUESTIONS";
    private int matchedposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        load=new Dialog(this);
        load.setContentView(R.layout.loading);
        preferences=getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor=preferences.edit();
        gson=new Gson();
        load.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
        load.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        load.setCancelable(false);
        setId=getIntent().getStringExtra("setId");
        quest=(TextView)findViewById(R.id.question);
        noIndicator=(TextView)findViewById(R.id.number);
        bookmark=(FloatingActionButton)findViewById(R.id.bookmark);
        optionlist=(LinearLayout)findViewById(R.id.l2);
        share=(Button)findViewById(R.id.share);
        next=(Button)findViewById(R.id.next);

        getBookmarks();
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(matchmodel()){
                    bookmarkslist.remove(matchedposition);
                    bookmark.setImageDrawable(getDrawable(R.drawable.bookmarkbtn));
                }
                else {
                    bookmarkslist.add(list.get(position));
                    bookmark.setImageDrawable(getDrawable(R.drawable.bookmark));
                }
            }
        });
        list=new ArrayList<>();
        load.show();
        reference.child("SETS").child(setId).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    list.add(new QuestionModel(id,question,a,b,c,d,correctans,setId));
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
                    share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String body=list.get(position).getQuestion()+ "\n" +list.get(position).getOptiona()+ "\n" +
                                    list.get(position).getOptionb()+ "\n" +list.get(position).getOptionc()+ "\n" +list.get(position).getOptiond();
                            Intent shareintent=new Intent(Intent.ACTION_SEND);
                            shareintent.setType("text/plain");
                            shareintent.putExtra(Intent.EXTRA_SUBJECT,"Challenge");
                            shareintent.putExtra(Intent.EXTRA_TEXT,body);
                            startActivity(Intent.createChooser(shareintent,"Share via "));
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

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
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
                                if(matchmodel()){
                                    bookmark.setImageDrawable(getDrawable(R.drawable.bookmarkbtn));
                                }
                                else {
                                    bookmark.setImageDrawable(getDrawable(R.drawable.bookmark));
                                }
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
    private void getBookmarks(){
        String json=preferences.getString(KEY_NAME,"");
        Type type=new TypeToken<List<QuestionModel>>(){}.getType();
        bookmarkslist=gson.fromJson(json,type);
        if(bookmarkslist==null){
            bookmarkslist=new ArrayList<>();
        }
    }
    private boolean matchmodel(){
        boolean matchlist=false;
        int i=0;
        for (QuestionModel questionModel:bookmarkslist){
            if(questionModel.getQuestion().equals(list.get(position).getQuestion())&&
            questionModel.getCorrectans().equals(list.get(position).getCorrectans())&&
            questionModel.getSetNo()==list.get(position).getSetNo()){
                matchlist=true;
                matchedposition=i;
            }
            i++;
        }
        return matchlist;
    }
    private void storeBookmarks(){
        String json=gson.toJson(bookmarkslist);
        editor.putString(KEY_NAME,json);
        editor.commit();
    }
}
