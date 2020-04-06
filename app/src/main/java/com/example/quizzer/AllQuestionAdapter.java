package com.example.quizzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllQuestionAdapter extends RecyclerView.Adapter<AllQuestionAdapter.viewholder> {

    private List<AllQuestionModel> list;

    public AllQuestionAdapter(List<AllQuestionModel> list, CheckAnswer checkAnswer) {
        this.list = list;
        this.checkAnswer = checkAnswer;
    }

    private CheckAnswer checkAnswer;

    @NonNull
    @Override
    public AllQuestionAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_row,parent,false);
        return new AllQuestionAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllQuestionAdapter.viewholder holder, int position) {
        holder.setData(list.get(position).getOptiona(),list.get(position).getOptionb(),list.get(position).getOptionc()
        ,list.get(position).getOptiond(),list.get(position).getQuestion());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class viewholder extends RecyclerView.ViewHolder{

        private Button a,b,c,d;
        private TextView question;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            a=(Button)itemView.findViewById(R.id.button1);
            b=(Button)itemView.findViewById(R.id.button2);
            c=(Button)itemView.findViewById(R.id.button3);
            d=(Button)itemView.findViewById(R.id.button4);
            question=(TextView)itemView.findViewById(R.id.questionhere);

        }
        private void setData(final String  a,final String b,final String c,final String  d,final String question){

            this.question.setText(question);
            this.a.setText(a);
            this.b.setText(b);
            this.c.setText(c);
            this.d.setText(d);
            this.a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer.onCheck(a);
                }
            });
            this.b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer.onCheck(b);
                }
            });
            this.c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer.onCheck(c);
                }
            });
            this.d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer.onCheck(d);
                }
            });
        }
    }
    public interface CheckAnswer{
        void onCheck(String a);
    }
}
