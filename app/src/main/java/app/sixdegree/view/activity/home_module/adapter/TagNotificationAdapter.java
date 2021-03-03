package app.sixdegree.view.activity.home_module.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.network.responses.gettagnotifications.Data;
import app.sixdegree.utils.AppSession;
import app.sixdegree.utils.CircleTransform;
import app.sixdegree.utils.GlideCircleWithBorder;
import app.sixdegree.view.activity.chatchat.ChatDetails;
import app.sixdegree.view.activity.home_module.fragments.FriendsFragment;
import app.sixdegree.view.activity.home_module.fragments.NotificationFragment;
import app.sixdegree.view.activity.trailsmodule.SIngleTrailView;
import app.sixdegree.view.activity.trailsmodule.TrailDetailsActivity;


public class TagNotificationAdapter extends RecyclerView.Adapter<TagNotificationAdapter.MyViewHolder> {

    Context context;
    AppSession appSession;
    NotificationFragment notificationFragment;
    List<Data> tagnotofocations = new ArrayList<>();

    public TagNotificationAdapter(Context context, List<Data> tagnotofocations, AppSession appSession, NotificationFragment notificationFragment) {
        this.appSession = appSession;
        this.notificationFragment = notificationFragment;
        this.context = context;
        this.tagnotofocations = tagnotofocations;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_notifcation_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (tagnotofocations.get(position).getTrip_id() == null) {
            if (tagnotofocations.get(position).getProfile_image() != null) {
           /*     Picasso.with(context).load(tagnotofocations.get(position).getProfile_image())
                  .transform(new CircleTransform())
                        .into(holder.img);*/


                Glide.with(context)
                        .load(tagnotofocations.get(position).getProfile_image())
                        .apply(RequestOptions.circleCropTransform())
                        .transform(new GlideCircleWithBorder(context, 2, Color.parseColor("#4fb89c")))
                        .into(holder.img);
            } else {

            }
        } else {
          /*  Picasso.with(context).load(tagnotofocations.get(position).getTrip_picture())
                    .transform(new CircleTransform())
                    .into(holder.img);*/

            Glide.with(context)
                    .load(tagnotofocations.get(position).getTrip_picture())
                    .apply(RequestOptions.circleCropTransform())
                    .transform(new GlideCircleWithBorder(context, 2, Color.parseColor("#4fb89c")))

                    .into(holder.img);
        }


        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   new_trail_added
                //    new_trip_added
//              //  trail_like
//                   //     chat
//                new_friend_request
//                        accepted_friend_request
//            //    trail_like
//                      //  trail_comment
//                new_follow
//                        trip_tag


                   if (tagnotofocations.get(position).getNotifiable_type()==null) {
                    Intent intent = new Intent(context, TrailDetailsActivity.class);
                    intent.putExtra("id", String.valueOf(tagnotofocations.get(position).getTrip_id()));
                    intent.putExtra("tagged", "tagged");
                    intent.putExtra("user_id", String.valueOf(tagnotofocations.get(position).getNotification_by()));
                    context.startActivity(intent);
                }

                else
                if (tagnotofocations.get(position).getNotifiable_type().equals("new_trail_added")) {
                    Intent intent = new Intent(context, TrailDetailsActivity.class);
                    intent.putExtra("id", String.valueOf(tagnotofocations.get(position).getTrip_id()));
                    intent.putExtra("tagged", "tagged");
                    intent.putExtra("user_id", String.valueOf(tagnotofocations.get(position).getNotification_by()));
                    context.startActivity(intent);
                }
                else
                if (tagnotofocations.get(position).getNotifiable_type().equals("trip_updation")) {
                    Intent intent = new Intent(context, TrailDetailsActivity.class);
                    intent.putExtra("id", String.valueOf(tagnotofocations.get(position).getTrip_id()));
                    intent.putExtra("tagged", "tagged");
                    intent.putExtra("user_id", String.valueOf(tagnotofocations.get(position).getNotification_by()));
                    context.startActivity(intent);
                }  else
                if (tagnotofocations.get(position).getNotifiable_type().equals("trip_trail_updation")) {
                    Intent intent = new Intent(context, TrailDetailsActivity.class);
                    intent.putExtra("id", String.valueOf(tagnotofocations.get(position).getTrip_id()));
                    intent.putExtra("tagged", "tagged");
                    intent.putExtra("user_id", String.valueOf(tagnotofocations.get(position).getNotification_by()));
                    context.startActivity(intent);
                }   else
                if (tagnotofocations.get(position).getNotifiable_type().equals("trip_updation")) {
                    Intent intent = new Intent(context, TrailDetailsActivity.class);
                    intent.putExtra("id", String.valueOf(tagnotofocations.get(position).getTrip_id()));
                    intent.putExtra("tagged", "tagged");
                    intent.putExtra("user_id", String.valueOf(tagnotofocations.get(position).getNotification_by()));
                    context.startActivity(intent);
                } else if (tagnotofocations.get(position).getNotifiable_type().equals("trail_like")) {
                    Intent intent = new Intent(context, SIngleTrailView.class);

                    intent.putExtra("trail_id", String.valueOf(tagnotofocations.get(position).getTrail_id()));
                    intent.putExtra("user_id", String.valueOf(tagnotofocations.get(position).getNotification_by()));
                    intent.putExtra("session_id", String.valueOf(appSession.getData().getId()));
                    //    Log.e("trip  m.trail_id--", "--" + getTrail_id());


                    context.startActivity(intent);
                } else if (tagnotofocations.get(position).getNotifiable_type().equals("trail_comment")) {
                    Intent intent = new Intent(context, SIngleTrailView.class);

                    intent.putExtra("trail_id", String.valueOf(tagnotofocations.get(position).getTrail_id()));
                    intent.putExtra("user_id", String.valueOf(tagnotofocations.get(position).getNotification_by()));
                    intent.putExtra("session_id", String.valueOf(appSession.getData().getId()));
                    //    Log.e("trip  m.trail_id--", "--" + getTrail_id());


                    context.startActivity(intent);
                } else if (tagnotofocations.get(position).getNotifiable_type().equals("chat")) {


                    if (tagnotofocations.get(position).getConversation_id()==null){


                    String[] separated = tagnotofocations.get(position).getData().split(" ");

                    Intent intent = new Intent(context, ChatDetails.class);
                    intent.putExtra("name", separated[0]);
                    intent.putExtra("image", tagnotofocations.get(position).getProfile_image());
                    intent.putExtra("chat_group_id", "0");
                    intent.putExtra("from_user_id", String.valueOf(appSession.getData().getId()));
                    intent.putExtra("to_user_id", String.valueOf(tagnotofocations.get(position).getNotification_by()));
                    context.startActivity(intent);

                    }
                    else {


                        String[] separated = tagnotofocations.get(position).getData().split(" ");

                        Intent intent = new Intent(context, ChatDetails.class);
                        intent.putExtra("name", separated[0]);
                        intent.putExtra("image", tagnotofocations.get(position).getProfile_image());
                        intent.putExtra("chat_group_id",   String.valueOf(tagnotofocations.get(position).getConversation_id()));
                        intent.putExtra("from_user_id", String.valueOf(appSession.getData().getId()));
                        intent.putExtra("to_user_id", String.valueOf(tagnotofocations.get(position).getNotification_by()));
                        context.startActivity(intent);


                    }
                } else if (tagnotofocations.get(position).getNotifiable_type().equals("new_trip_added")) {
                    Intent intent = new Intent(context, TrailDetailsActivity.class);
                    intent.putExtra("id", String.valueOf(tagnotofocations.get(position).getTrip_id()));
                    intent.putExtra("tagged", "tagged");
                    intent.putExtra("user_id", String.valueOf(tagnotofocations.get(position).getNotification_by()));
                    context.startActivity(intent);
                } else if (tagnotofocations.get(position).getNotifiable_type().equals("trip_tag")) {
                    Intent intent = new Intent(context, TrailDetailsActivity.class);
                    intent.putExtra("id", String.valueOf(tagnotofocations.get(position).getTrip_id()));
                    intent.putExtra("tagged", "tagged");
                    intent.putExtra("user_id", String.valueOf(tagnotofocations.get(position).getNotification_by()));
                    context.startActivity(intent);
                }else {

                }

     /*   if(tagnotofocations.get(position).getTrip_id()==null){

                }else {
Intent intent=new Intent( context, TrailDetailsActivity.class );
intent.putExtra( "id",String.valueOf( tagnotofocations.get( position ).getTrip_id()));
intent.putExtra( "tagged","");
intent.putExtra( "user_id",String.valueOf( tagnotofocations.get( position ).getNotification_by()));
context.startActivity( intent );
            }*/

            }
        });


        if (tagnotofocations.get(position).getData() != null) {
            holder.name.setText(tagnotofocations.get(position).getData());
          /*  if(tagnotofocations.get(position).getTrip_id()==null){
                holder.name.setText( tagnotofocations.get(position).getData());
             }else {
                String namedata=tagnotofocations.get( position ).getData();


                String namearray[]= namedata.split( " " );
                String name=namearray[0];
               // holder.name.setText( name + " tagged you in a " +tagnotofocations.get(position).getTrip_name() +" trip.");
                holder.name.setText(tagnotofocations.get(position).getData());
             }*/


        }

    }


    @Override
    public int getItemCount() {
        return tagnotofocations.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        TextView tv_extratext;
        TextView time;
        TextView unread;


        RelativeLayout parent;

        public MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name_comment);
            tv_extratext = view.findViewById(R.id.tv_extratext);


            parent = view.findViewById(R.id.parent);

        }
    }

}
