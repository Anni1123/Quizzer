package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

public class AddNewQuestionActivity extends AppCompatActivity {
    private EditText question;
    private RadioGroup option;
    private LinearLayout answer;
    private Button upload;
    private String categoryname;
    private int set;
private Dialog load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_question);
        upload=(Button)findViewById(R.id.button);
        question=(EditText)findViewById(R.id.questionset);
        option=(RadioGroup)findViewById(R.id.options);
        answer=(LinearLayout)findViewById(R.id.answer);
        categoryname=getIntent().getStringExtra("category");
        load=new Dialog(this);
        load.setContentView(R.layout.loading);
        load.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
        load.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        load.setCancelable(false);
        set=getIntent().getIntExtra("set",-1);
        if(set==-1){
            finish();
            return;
        }
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(question.getText().toString().isEmpty()){
                    question.setError("required");
                    return;
                }
                uplod();
            }
        });
    }
    private void uplod(){

        int correct=-1;
        for (int i=0;i<option.getChildCount();i++){
            EditText answers=(EditText)answer.getChildAt(i);
            if(answers.getText().toString().isEmpty()){
                answers.setError("Required");
                return;
            }
            RadioButton radioButton=(RadioButton)option.getChildAt(i);
            if(radioButton.isChecked()){
                correct=i;
                break;
            }
        }
        if(correct==-1){
            Toast.makeText(AddNewQuestionActivity.this,"Mark Correct Answer",Toast.LENGTH_LONG).show();
            return;
        }
        final HashMap<String,Object> map=new HashMap<>();
        map.put("correctans",((EditText)answer.getChildAt(correct)).getText().toString());
        map.put("optiona",((EditText)answer.getChildAt(3)).getText().toString());
        map.put("optionb",((EditText)answer.getChildAt(2)).getText().toString());
        map.put("optionc",((EditText)answer.getChildAt(1)).getText().toString());
        map.put("optiond",((EditText)answer.getChildAt(0)).getText().toString());
        map.put("question",question.getText().toString());
        map.put("setNo",set);
        final String uid=UUID.randomUUID().toString();
        load.show();
        FirebaseDatabase.getInstance().getReference().child("SETS").child(categoryname).
                child("questions").child(uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    QuestionsModel questionsModel=new QuestionsModel(uid,map.get("question").toString(),map.get("optiona").toString()
                    ,map.get("optionb").toString(),
                            map.get("optionc").toString(),
                            map.get("optiond").toString(),
                            map.get("correctans").toString(),
                            (int)map.get("setNo"));
                    QuestionActivity.list.add(questionsModel);
                    finish();

                }else {

                }
                load.dismiss();
            }
        });

    }
}
