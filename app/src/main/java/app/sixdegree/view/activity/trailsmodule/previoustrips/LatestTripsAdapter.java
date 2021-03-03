package app.sixdegree.view.activity.trailsmodule.previoustrips;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import app.sixdegree.R;
import app.sixdegree.network.responses.getlatesttrip.Data;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.view.activity.trailsmodule.TrailDetailsActivity;


public class LatestTripsAdapter extends RecyclerView.Adapter<LatestTripsAdapter.MyViewHolder> {

    Context context;
    String id="";
    String   user_id="";
ArrayList<LatestTripModel>latestTripModelArrayList=new ArrayList<>(  );
    public LatestTripsAdapter( Context context , ArrayList<LatestTripModel>latestTripModelArrayList ,String id,String user_id) {
this.id=id;
        this.context=context;
        this.user_id=user_id;
        this.latestTripModelArrayList=latestTripModelArrayList;
     }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.previous_trips_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(id.equals( user_id)){
            holder.iv_delete.setVisibility( View.VISIBLE );
        }else {
            holder.iv_delete.setVisibility( View.GONE );
        }

if(latestTripModelArrayList.size()==0){

}{
            holder.name.setText( latestTripModelArrayList.get( 0 ).getName() );



            if(latestTripModelArrayList.get( 0 ).getTrailcount()==0){
                holder.trails.setText("0" );
            }else{
                holder.trails.setText( String.valueOf(latestTripModelArrayList.get( 0 ).getTrailcount()) );
            }



            Glide.with(context)
                    .load(latestTripModelArrayList.get( 0 ).getImage())

                    .into(holder.image_bg);

            holder.ll_parent.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    Intent intent=new Intent( context, TrailDetailsActivity.class );
                    intent.putExtra( "id",String.valueOf(latestTripModelArrayList.get( 0 ).getId() ));
                    intent.putExtra( "tagged","");

                    intent.putExtra("user_id",String.valueOf(latestTripModelArrayList.get(0).getUser_id()));
                    context.startActivity( intent );
                }
            } );

            holder.date.setText( AppUtils.getMonthYear(latestTripModelArrayList.get( 0 ).getStartdate()) );
        }



    }

    @Override
    public int getItemCount() {
        return latestTripModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
ImageView image_bg;
ImageView iv_go;
ImageView iv_delete;
TextView name;
TextView trails;
TextView date;
LinearLayout ll_parent;

        public MyViewHolder(View view) {
            super(view);

image_bg=view.findViewById( R.id.image_bg );
            name=view.findViewById( R.id.name );
            date=view.findViewById( R.id.date );
            trails=view.findViewById( R.id.trails );
            ll_parent=view.findViewById( R.id.ll_parent );
            iv_go=view.findViewById( R.id.iv_go );
            iv_delete=view.findViewById( R.id.iv_delete );
         }


    }

}