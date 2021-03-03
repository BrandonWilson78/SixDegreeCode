package app.sixdegree.view.activity.blockedusers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.BlockedUserRowViewbinding;
import app.sixdegree.FriendsRowBinding;
import app.sixdegree.R;
import app.sixdegree.network.responses.followerres.CommonFriendsModel;
import app.sixdegree.viewModel.BlockedUsersRowVm;
import app.sixdegree.viewModel.BlockedUsersVm;
import app.sixdegree.viewModel.FriendsVm;

public class BlockedUsersAdapter extends RecyclerView.Adapter<BlockedUsersAdapter.DataViewHolder> {
    private List<CommonFriendsModel> data;
    boolean isFollower;
    String token;
    public BlockedUsersAdapter( Boolean isFollower, String  token) {
        this.data = new ArrayList<>();
        this.isFollower = isFollower;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.blockeduser_row, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        CommonFriendsModel dataModel = data.get(position);
        holder.setViewModel(new BlockedUsersRowVm(dataModel, this));
        holder.binding.btnStatus.setTag(isFollower);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onViewAttachedToWindow(DataViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(DataViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    public void updateData(@Nullable List<CommonFriendsModel> data) {
        if (data == null || data.isEmpty()) {
            this.data.clear();
        } else {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void removeRow(CommonFriendsModel model) {
        data.remove(model);
        notifyDataSetChanged();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        BlockedUserRowViewbinding binding;

        DataViewHolder(View itemView) {
            super(itemView);
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

        void setViewModel(BlockedUsersRowVm viewModel) {
            if (binding != null) {
                binding.setViewModel(viewModel);
            }
        }

    }

}
