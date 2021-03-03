package app.sixdegree.view.activity.home_module.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;

import app.sixdegree.databinding.TripsofmatcheduserRowBinding;
import app.sixdegree.network.responses.sixdegreesearch.TripsofMatchedUser;
import app.sixdegree.network.responses.tripsinterest.Data;
import app.sixdegree.viewModel.ExploreGridVm;
import app.sixdegree.viewModel.TripsOfMatchedUsersVm;


public class TripOfSearchedUsersAdapter extends RecyclerView.Adapter<TripOfSearchedUsersAdapter.MyViewHolder> {
    List<TripsofMatchedUser> data = new ArrayList<>();

    public TripOfSearchedUsersAdapter() {
        data = new ArrayList<>();
    }

    public void updateData(List<TripsofMatchedUser> data) {
        this.data=data;
         notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tripsofmatcheduser_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TripsofMatchedUser dataModel = data.get(position);
        holder.setViewModel(new TripsOfMatchedUsersVm(dataModel));
     }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TripsofmatcheduserRowBinding binding;

        public MyViewHolder(View view) {
            super(view);
            bind();
        }

        void bind() {
            if (binding == null) {
                binding = DataBindingUtil.bind(itemView);
            }
        }

        void unbind() {
            if (binding != null) {
                binding.unbind(); // Don't forget to unbind
            }
        }

        void setViewModel(TripsOfMatchedUsersVm viewModel) {
            if (binding != null) {
                binding.setViewModel(viewModel);
            }
        }
    }
    @Override
    public void onViewAttachedToWindow(MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }
}