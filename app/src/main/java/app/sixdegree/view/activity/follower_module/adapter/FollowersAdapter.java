package app.sixdegree.view.activity.follower_module.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.FriendsRowBinding;
import app.sixdegree.R;
import app.sixdegree.network.responses.followerres.CommonFriendsModel;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.SearchByNameActivity;
import app.sixdegree.view.activity.follower_module.FriendsActivity;
import app.sixdegree.viewModel.FriendsVm;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.DataViewHolder> {
    private List<CommonFriendsModel> data;
    boolean isFollower;
    String token;
    String nameforfriends="";

    AppSession appSession;
    public FollowersAdapter(Boolean isFollower,String  token,String nameforfriends,AppSession appSession) {
        this.data = new ArrayList<>();
        this.isFollower = isFollower;
        this.token = token;
        this.nameforfriends = nameforfriends;
        this.appSession=appSession;
    }

    public String getToken() {
        return token;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_row, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        CommonFriendsModel dataModel = data.get(position);
        holder.setViewModel(new FriendsVm(dataModel, this,isFollower,nameforfriends));
        holder.binding.btnStatus.setTag(isFollower);


        Log.e("id ==id","--"+nameforfriends+"=="+String.valueOf(appSession.getData().getId()));
if(nameforfriends.equals(String.valueOf(appSession.getData().getId())  )){
    holder.tv_remove.setVisibility( View.VISIBLE );
}else {
    holder.tv_remove.setVisibility( View.GONE );
}
holder.parent.setOnClickListener( new View.OnClickListener() {
    @Override
    public void onClick( View v ) {
        ((FriendsActivity)v.getContext()).gotToHomeActvity("searchbyname",String.valueOf(data.get(position).getId())
               ,data.get( position ).getFollow_status(),data.get( position ).getFriend_status());
    }
} );
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
        FriendsRowBinding binding;

        TextView tv_remove;
        RelativeLayout parent;
        DataViewHolder(View itemView) {
            super(itemView);
             tv_remove=itemView.findViewById( R.id. btn_status);
            parent=itemView.findViewById( R.id. parent);
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

        void setViewModel(FriendsVm viewModel) {
            if (binding != null) {
                binding.setViewModel(viewModel);
            }
        }

    }





}
