package com.example.quizzer.Admin;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quizzer.R;

import java.util.List;

public class GriddAdapter extends BaseAdapter {

    public List<String> sets;
    protected GriddListener gridListener;
    public GriddAdapter(List<String> sets, String category,GriddListener gridListener) {
        this.sets = sets;
        this.gridListener=gridListener;
        this.category = category;
    }

    private String category;
    @Override
    public int getCount() {
        return sets.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;
        if(convertView==null){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item,parent,false);
        }
        else {
            view=convertView;
        }
        if(position==0){
            ((TextView)view.findViewById(R.id.textView)).setText("+");
        }
        else {
            ((TextView)view.findViewById(R.id.textView)).setText(String.valueOf(position));

        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                  gridListener.addset();
                }
                else {
                    Intent questionIntent = new Intent(parent.getContext(), QuestionActivity.class);
                    questionIntent.putExtra("category", category);
                    questionIntent.putExtra("setId", sets.get(position-1));
                    parent.getContext().startActivity(questionIntent);
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(position!=0) {
                    gridListener.onLongClick(sets.get(position-1),position);
                }
                return false;
            }
        });
        return view;
    }
    public interface GriddListener{
        void addset();
        void onLongClick(String setId,int position);
    }
}