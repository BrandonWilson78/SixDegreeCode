package app.sixdegree.view.activity.home_module.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.UserRowDataBinding;
import app.sixdegree.network.responses.sixdegreesearch.SixdegreeMatched;
import app.sixdegree.view.activity.SearchByNameActivity;
import app.sixdegree.view.activity.home_module.fragments.ProfileFragment;
import app.sixdegree.viewModel.SixDegreeSearchRowVm;

public class SearchByNameAdapter extends RecyclerView.Adapter<SearchByNameAdapter.MyViewHolder> {

   public List<SixdegreeMatched> data;
String token="";

    public SearchByNameAdapter( String token) {
        this.data = new ArrayList<>();
        this.token=token;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_row
                        , parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyViewHolder viewHolder = holder;
        SixDegreeSearchRowVm sixDegreeSearchRowVm = new SixDegreeSearchRowVm(data.get(position),token);
        viewHolder.binding.setViewModel(sixDegreeSearchRowVm);



        holder.parent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

          /*      if(holder.add.getVisibility()==View.VISIBLE){

                }else {*/

                    ((SearchByNameActivity)v.getContext()).gotToHomeActvity("searchbyname",String.valueOf(data.get(position).getId()),data.get( position ).getFollowStatus(),data.get( position ).getFriend_status());

                  /*   ((SearchByNameActivity)v.getContext()).loadFragment
                             (new ProfileFragment(String.valueOf(data.get(position).getId()),""));*/
            //    }

            }
        } );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<SixdegreeMatched> data) {
        Log.e("datainsetdata--","--"+data.size());
        this.data = data;

        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        UserRowDataBinding binding;


        ImageView add;
        RelativeLayout parent;

        public MyViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
add=view.findViewById( R.id. add);
            parent=view.findViewById( R.id. parent);

        }
    }

}