package app.sixdegree.view.activity.sendcontacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.network.ApiFactory;
import app.sixdegree.network.ApiService;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.fetchcontacts.Data;
import app.sixdegree.network.responses.followerres.CommonFriendsModel;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.CircleTransform;
import app.sixdegree.utils.GlideCircleWithBorder;
import app.sixdegree.view.activity.SearchByNameActivity;
import app.sixdegree.view.activity.chatchat.ChatDetails;
import app.sixdegree.view.activity.home_module.fragments.ProfileFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
ApiService apiService;
    Context context;
    int width = 0;
    private List<app.sixdegree.network.responses.fetchcontacts.Data> chatUserList;
    AppSession appSession;

    public ContactsAdapter( List<app.sixdegree.network.responses.fetchcontacts.Data> dataList, Context context, int width, AppSession appSession) {
        this.chatUserList = dataList;
        this.context = context;
        this.width = width;
        this.appSession=appSession;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.contacts_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

          apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
        final app.sixdegree.network.responses.fetchcontacts.Data chatM = chatUserList.get(position);
      //frind status==  no_relation, confirm_request, pending_request, friends

        if(chatM.getFriend_status().equals( "friends" )){
            holder.tv_status.setText( "Already added" );
            holder.tv_status.setVisibility(  View.VISIBLE );

            holder.iv_follow.setVisibility( View.GONE );
        }
        else if(chatM.getFriend_status().equals( "confirm_request" )){
            holder.tv_status.setText( "Confirm request" );
            holder.tv_status.setVisibility(  View.VISIBLE );

            holder.iv_follow.setVisibility( View.GONE );
        } else if(chatM.getFriend_status().equals( "pending_request" )){
            holder.tv_status.setText( "Pending request" );
            holder.tv_status.setVisibility(  View.VISIBLE );

            holder.iv_follow.setVisibility( View.GONE );
        }else {
            holder.tv_status.setText( "" );
            holder.tv_status.setVisibility(  View.GONE );
            holder.iv_follow.setVisibility( View.VISIBLE );
        }


         holder.name.setText( chatM.getName());
         if (chatM.getProfileImage() != null) {
          /*Picasso.with(context).load(chatM.getProfileImage())
                    .transform(new CircleTransform())
                    .into(holder.img);*/


             Glide.with(context)
                     .load(chatM.getProfileImage())
                     .apply( RequestOptions.circleCropTransform())
                     .transform(new GlideCircleWithBorder(context, 2, Color.parseColor("#4fb89b")))

                     .into(holder.img);
        } else {

        }


         holder.iv_follow.setOnClickListener( new View.OnClickListener() {
             @Override
             public void onClick( View v ) {
                 followUser(String.valueOf(chatM.getId()),holder.pbar,chatM);
             }
         } );


        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AllContacts)v.getContext()).gotToHomeActvity("searchbyname",String.valueOf(chatM.getId()),
                        chatM.getFollow_status(),chatM.getFriend_status());
            }
        });

    }


    @Override
    public int getItemCount() {
        return chatUserList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        ImageView iv_follow;
        TextView tv_status;
        TextView unread;
        ProgressBar pbar;


        RelativeLayout parent;

        public MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.image);
            iv_follow = view.findViewById(R.id.iv_follow);
            name = view.findViewById(R.id.name_comment);
            tv_status = view.findViewById(R.id.tv_status);
            pbar = view.findViewById(R.id.pbar);



            parent = view.findViewById(R.id.parent);

        }
    }
    public void followUser( String userid, ProgressBar pbar,Data data) {
        pbar.setVisibility( View.VISIBLE );
        apiService.sendFriendRequest(appSession.getToken(), userid).subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<BaseRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRes res) {


                        if (res.getStatus()) {
                            removeRow(  data);
                        }
                        else {

                            Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        pbar.setVisibility( View.GONE );
                     }

                    @Override
                    public void onComplete() {
                        pbar.setVisibility( View.GONE );
                     }
                });
    }



    public void removeRow( Data model) {
        chatUserList.remove(model);
        notifyDataSetChanged();
    }

    public void goToProfileFragment(View view,String friendid,String followstatus,String friendstatus){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        ProfileFragment myFragment = new ProfileFragment(friendid,followstatus,friendstatus);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myFragment).addToBackStack(null).commit();    }
}
