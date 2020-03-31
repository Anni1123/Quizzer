package com.example.quizzer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.Viewholder>{
    public CategoriesAdapter(List<CategoriesModel> categoriesModels) {
        this.categoriesModels = categoriesModels;
    }

    private List<CategoriesModel> categoriesModels;

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.setData(categoriesModels.get(position).getUrl(),categoriesModels.get(position).getName(),categoriesModels.get(position).getSets());
    }

    @Override
    public int getItemCount() {
        return categoriesModels.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        private CircleImageView circleImageView;
        private TextView textView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            circleImageView=(CircleImageView)itemView.findViewById(R.id.image_view);
            textView=itemView.findViewById(R.id.title);

        }
        private void setData(String url,final String title,final int sets){
            Glide.with(itemView.getContext()).load(url).into(circleImageView);
            this.textView.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(itemView.getContext(),SetActivity.class);
                    intent.putExtra("title",title);
                    intent.putExtra("sets",sets);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}