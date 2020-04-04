package com.example.quizzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.viewholder>{
    public BookmarksAdapter(List<QuestionModel> list) {
        this.list = list;
    }

    private List<QuestionModel>list;
    @NonNull
    @Override
    public BookmarksAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksAdapter.viewholder holder, int position) {
        holder.setData(list.get(position).getQuestion(),list.get(position).getAnswer(),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder{

        private TextView ques,ans;
        private ImageButton image;
        public viewholder(@NonNull View itemView)
        {
            super(itemView);
            ques=itemView.findViewById(R.id.questions);
            ans=itemView.findViewById(R.id.answer);
            image=itemView.findViewById(R.id.delete);
        }
        private void setData(String question,String answer,final int position){
            this.ques.setText(question);
            this.ans.setText(answer);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }
    }
}
