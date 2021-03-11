package com.example.blood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdpter extends RecyclerView.Adapter<PostAdpter.MyViewHolder> {
    private Context mcontext;
    private List<postinfo> postlist;
    public RecyclerTouchListener.ClickListener  onhi;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,all;
        public Button button;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            all = (TextView) view.findViewById(R.id.all);

            button=view.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onhi.onClick(v, getAdapterPosition());
                    //  Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }

            });

        }
    }


    public PostAdpter(Context context, List<postinfo> postlist, RecyclerTouchListener.ClickListener  onhi1) {
        this.postlist = postlist;
        this.mcontext=context;
        this.onhi=onhi1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_post_adpter, parent, false);

        return new MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        postinfo po = postlist.get(position);
        holder.name.setText(po.getName());
        String s=po.getBloodgroup()+" blood are needed "+po.getBag()+" "+po.getType()+" patient in "+po.getPlace()+" phone number "+po.getPhone();
        holder.all.setText(s);

    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }
}