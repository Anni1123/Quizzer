package com.example.quizzer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {
    public CategoryAdapter(List<CategoryModel> categoryModels) {
        this.categoryModels = categoryModels;
    }

    private List<CategoryModel> categoryModels;

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.setData(categoryModels.get(position).getName(),position);
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        private CircleImageView circleImageView;
        private TextView textView;
        private ImageButton imageButton;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            circleImageView=(CircleImageView)itemView.findViewById(R.id.image_view);
            textView=itemView.findViewById(R.id.title);
            imageButton=itemView.findViewById(R.id.deleted);
        }
        private void setData(final String title,final int position){
            this.textView.setText(title);
            imageButton.setVisibility(View.INVISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(itemView.getContext(),SetsActivity.class);
                    intent.putExtra("title",title);
                    intent.putExtra("position",position);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
