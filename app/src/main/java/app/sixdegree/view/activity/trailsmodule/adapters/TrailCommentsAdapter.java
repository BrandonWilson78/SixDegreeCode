package app.sixdegree.view.activity.trailsmodule.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.CommentRowBinding;
import app.sixdegree.network.responses.fetchcomments.Data;
import app.sixdegree.viewModel.CommentRowVm;

public class TrailCommentsAdapter extends RecyclerView.Adapter<TrailCommentsAdapter.MyViewHolder> {


    List<Data> data;


    public TrailCommentsAdapter() {
        data = new ArrayList<>();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_row
                        , parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyViewHolder viewHolder = holder;
        CommentRowVm commentRowVm = new CommentRowVm(data.get(position));
        viewHolder.binding.setViewModel(commentRowVm);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Data> data) {
        this.data = data;
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CommentRowBinding binding;

        public MyViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);


        }
    }
}