package com.example.quizzer.Admin;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzer.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.viewholder> {
    public QuestionAdapter(List<QuestionsModel> list, String category, QuestionListener listener) {
        this.list = list;
        this.listener=listener;
        this.category=category;
    }

    private List<QuestionsModel> list;
    private String category;
    private QuestionListener listener;
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
        private void setData(String question, String answer, final int position){
            this.quest.setText(position+1+","+question);
            this.answer.setText("Ans,"+answer);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editintent=new Intent(itemView.getContext(), AddNewQuestionActivity.class);
                    editintent.putExtra("category",category);
                    editintent.putExtra("setId",list.get(position).getSet());
                    editintent.putExtra("position",position);
                    itemView.getContext().startActivity(editintent);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(position,list.get(position).getId());
                    return false;
                }
            });
        }
    }

    public interface QuestionListener{
        void onLongClick(int position,String id);
    }
}