package app.sixdegree.view.activity.home_module;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.ExploreGridBinding;
import app.sixdegree.network.responses.getprevioustrips.Data;
import app.sixdegree.utils.AppUtils;
import app.sixdegree.utils.GlideCircleWithBorder;
import app.sixdegree.view.activity.Splash;
import app.sixdegree.view.activity.trailsmodule.TrailDetailsActivity;
import app.sixdegree.view.activity.trailsmodule.previoustrips.PreviousTrips;
import app.sixdegree.viewModel.ExploreGridVm;


public class PreviousTripsAdapter extends RecyclerView.Adapter<PreviousTripsAdapter.MyViewHolder> {
    ArrayList<Data> data = new ArrayList<>();
    Context context;
    String id="";
    String sessionid="";

    public PreviousTripsAdapter( Context context ,ArrayList<Data>data ,String id,String sessionid) {

        this.context=context;
        this.data=data;
        this.id=id;
        this.sessionid=sessionid;
     }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.previous_trips_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Data dataModel = data.get(position);
        if(id.equals( sessionid)){
            holder.iv_delete.setVisibility( View.VISIBLE );
        }else {
            holder.iv_delete.setVisibility( View.GONE );
        }

        holder.name.setText( dataModel.getName() );

        if(dataModel.getTrails().size()!=0 ){
            holder.trails.setText( String.valueOf(dataModel.getTrails().size()) );
        }else {
            holder.trails.setText( "0");
        }

        Glide.with(context)
                .load(dataModel.getTripPicture())
                 .centerCrop()
                .into(holder.image_bg);

        holder.ll_parent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent=new Intent( context, TrailDetailsActivity.class );
                intent.putExtra( "id",String.valueOf(dataModel.getId() ));
                intent.putExtra( "user_id",String.valueOf(dataModel.getUserId() ));
                intent.putExtra( "tagged","");
                context.startActivity( intent );
            }
        } );

        holder.date.setText( AppUtils.getMonthYear(dataModel.getStartDate()) );



        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Trip");
                builder.setMessage("Are you sure you want to delete this trip?");
                builder.setPositiveButton("Delete", (dialog, which) -> {

                    ((PreviousTrips)context).deleteTrip(String.valueOf(dataModel.getId()));

                });

                builder.setNegativeButton("cancel", (dialog, which) -> {

                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
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