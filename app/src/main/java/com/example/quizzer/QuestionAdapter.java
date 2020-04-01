package com.example.quizzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.viewholder> {
    public QuestionAdapter(List<QuestionsModel> list) {
        this.list = list;
    }

    private List<QuestionsModel> list;

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        String question=list.get(position).getQuestion();
        String answer=list.get(position).getAnswer();
        holder.setData(question,answer,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        private TextView quest,answer;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            quest=itemView.findViewById(R.id.quest);
            answer=itemView.findViewById(R.id.ans);
        }
        private void setData(String question,String answer,int position){
            this.quest.setText(position+1+","+question);
            this.answer.setText("Ans,"+answer);
        }
    }
}
