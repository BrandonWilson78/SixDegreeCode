package app.sixdegree.view.activity.home_module.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.GlobalFriendRowViewBinding;
import app.sixdegree.network.responses.followerres.CommonFriendsModel;
import app.sixdegree.network.responses.getfriendslisting.Data;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.chatchat.ChatDetails;
import app.sixdegree.viewModel.ViewFriendRowVm;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder> {

    List<app.sixdegree.network.responses.getfriendslisting.Data> data;

AppSession appSession;
    public FriendsAdapter(AppSession appSession) {
        this.data = new ArrayList<>();
        this.appSession=appSession;

        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.global_friend_row
                        , parent, false);
        return new MyViewHolder(itemView);



    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyViewHolder viewHolder = holder;
        Data datalisting = data.get(position);
        ViewFriendRowVm commentRowVm = new ViewFriendRowVm(datalisting,this,appSession);


        viewHolder.binding.setViewModel(commentRowVm);

//        if(appSession.getData().getId()!=data.get( position ).getTo_user_id()){
//holder.ll.setVisibility( View.VISIBLE );
//
//        }else {
//            holder.ll.setVisibility( View.GONE );
//          }


        holder.iv_msg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                goToChatDetailActivity( v,datalisting.getFriendName(),datalisting.getFriendImage(), String.valueOf( datalisting.getFriendId() ) );
            }
        } );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<app.sixdegree.network.responses.getfriendslisting.Data> data) {
        this.data = data;
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        GlobalFriendRowViewBinding binding;


        LinearLayout ll;
        ImageView iv_msg;
        public MyViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);

ll=view.findViewById( R.id.ll );
            iv_msg=view.findViewById( R.id.iv_msg );
        }
    }
    public void removeRow(Data commentRowVm) {
        data.remove(commentRowVm);
        notifyDataSetChanged();
    }



    public void goToChatDetailActivity( View v,String name,String image,String id){
        Intent intent = new Intent(v.getContext(), ChatDetails.class);
        intent.putExtra("name",name);
        intent.putExtra("image",image);
        intent.putExtra("chat_group_id", "0");
        intent.putExtra("from_user_id",String.valueOf(appSession.getData().getId()));
        intent.putExtra("to_user_id", id);
        v.getContext().startActivity(intent);
    }

}