package com.example.quizzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class QuestionsActivity extends AppCompatActivity {

    private TextView quest,noIndicator;
    private FloatingActionButton bookmark;
    private LinearLayout option;
    private Button share,next;
    private int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        quest=(TextView)findViewById(R.id.question);
        noIndicator=(TextView)findViewById(R.id.number);
        bookmark=(FloatingActionButton)findViewById(R.id.bookmark);
        option=(LinearLayout)findViewById(R.id.l2);
        share=(Button)findViewById(R.id.share);
        next=(Button)findViewById(R.id.next);
next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        count=0;
        playAnim(quest,0);
    }
});
    }
    private void playAnim(final View view, final int value){
        view.animate().alpha(value).scaleY(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                        if(value==0&&count<4){
                            playAnim(option.getChildAt(count),0);
                            count++;
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        if(value==0){
                            playAnim(view,1);
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
}
