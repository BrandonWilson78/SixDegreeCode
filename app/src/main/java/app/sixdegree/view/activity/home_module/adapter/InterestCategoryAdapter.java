package app.sixdegree.view.activity.home_module.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.InterestRowBinding;
import app.sixdegree.network.responses.exlporeres.Data;
import app.sixdegree.viewModel.InterestCategoryVm;
import io.reactivex.subjects.BehaviorSubject;

public class InterestCategoryAdapter extends RecyclerView.Adapter<InterestCategoryAdapter.MyViewHolder> {
    List<Data> data;
  public   BehaviorSubject<List<String>> selectedObs = BehaviorSubject.create();
   public List<String> selectedList = new ArrayList<>();

    public InterestCategoryAdapter() {
        data = new ArrayList<>();
    }

    public void updateData(List<Data> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.interest_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Data dataModel = data.get(position);
        holder.setViewModel(new InterestCategoryVm(dataModel));
        holder.binding.name.setOnClickListener(v -> {
            if (selectedList.contains(String.valueOf(dataModel.getId()))) {
                selectedList.remove(String.valueOf(dataModel.getId()));
            } else {
                selectedList.add(String.valueOf(dataModel.getId()));
            }
            selectedObs.onNext(selectedList);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        InterestRowBinding binding;

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

        void setViewModel(InterestCategoryVm viewModel) {
            if (binding != null) {
                binding.setViewModel(viewModel);
            }
        }
    }
}