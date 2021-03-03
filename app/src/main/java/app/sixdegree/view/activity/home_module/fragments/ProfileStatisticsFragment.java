package app.sixdegree.view.activity.home_module.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.sixdegree.R;
import app.sixdegree.databinding.FragmentProfileStatisticsBinding;
import app.sixdegree.model.roomdb.CountriesDao;
import app.sixdegree.model.roomdb.Country;
import app.sixdegree.model.roomdb.DatabaseClient;
import app.sixdegree.network.responses.settings_mod.profile.UserProfile;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.adapter.CountryGridAdapter;
import app.sixdegree.viewModel.StatisticsVm;

public class ProfileStatisticsFragment extends Fragment {
    UserProfile userProfile;
    String map_url = "https://chart.googleapis.com/chart?cht=map:fixed=-65,-180,86,180&chs=460x350&";
    List<String> visitedCountries;
    CountriesDao dao;
    ArrayList<Country> datalist = new ArrayList<>();
    CountryGridAdapter countryGridAdapter;
    FragmentProfileStatisticsBinding binding;
    String country_clr = "4fb89b";
    String clr = "";
    ArrayList<String> datanames = new ArrayList<>();
    int width = 0;
    int height = 0;
    double percentage=0.0;
    private StatisticsVm statisticsVm;

    public ProfileStatisticsFragment(String trail_data) {
        if (!trail_data.isEmpty()) {
            userProfile = new Gson().fromJson(trail_data, UserProfile.class);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_statistics, container, false);
        binding.countryRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.flagsRv.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        statisticsVm = new StatisticsVm();
        statisticsVm.setSessionInstance(new AppSession(getActivity()));


        binding.setViewModel(statisticsVm);
        dao = DatabaseClient
                .getInstance(getContext())
                .getAppDatabase()
                .CountriesDao();
        if (!userProfile.getData().getCountriesVisitedName().isEmpty()){


            if(userProfile != null){


                String[] names = userProfile.getData().getCountriesVisitedName().split( "," );
                visitedCountries = new ArrayList<String>( Arrays.asList( names ) );
                Log.e( "names", "--"+visitedCountries );


                for (int i = 0; i < visitedCountries.size(); i++) {
                    getTasks( visitedCountries.get( i ) );


                }


                //set percentage


                statisticsVm.setPercentage( String.valueOf( calculatePercentage( visitedCountries.size() ) )+"%" );

                //set total visited countries
                statisticsVm.setCountry( String.valueOf( visitedCountries.size() ) );

                //set total days
                statisticsVm.setTotaldays( String.valueOf( userProfile.getData().getTotalDays() ) );

                //set hours
                statisticsVm.setTotalhours( String.valueOf( userProfile.getData().getTotalDays() * 24 ) );


                // set minutes
                statisticsVm.setTotalMinutes( String.valueOf( userProfile.getData().getTotalDays() * 24 * 60 ) );


                // set seconds
                statisticsVm.setTotalSeconds( String.valueOf( userProfile.getData().getTotalDays() * 24 * 60 * 60 ) );


                if(userProfile != null){

                    if(userProfile.getData().getTrailsCount()==0){
                        statisticsVm.setTripTrails( "N/A" );
                    }else {
                        // set trip trails
                        statisticsVm.setTripTrails( userProfile.getData().getTrailsCount()+" Pitstops" );
                    }



                }else{
                    statisticsVm.setTripTrails( "N/A" );
                }


                if(userProfile.getData().getTotalDistance()==0.0){
                    statisticsVm.setTotalkms( "N/A" );
                }else {

                    // set total kms
                    statisticsVm.setTotalkms( userProfile.getData().getTotalDistance()+" kilometers" );
                }

            }
        }

//
//        datalist.add(new CountryModel("europe", true, "DE", ""));
//        datalist.add(new CountryModel("Africa", true, "CF", ""));
//        datalist.add(new CountryModel("South America", false, "US", ""));
//        datalist.add(new CountryModel("Asia", true, "IN", ""));
//        datalist.add(new CountryModel("Australia", true, "AU", ""));
//        datalist.add(new CountryModel("antartica", false, " ", ""));
//        datalist.add(new CountryModel("North america", false, " ", ""));


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        countryGridAdapter = new CountryGridAdapter(getActivity(), datalist, false, width);
        binding.countryRv.setAdapter(countryGridAdapter);
        return binding.getRoot();
    }

    private void getTasks(String country) {
        class GetTasks extends AsyncTask<Void, Void, Country> {

            @Override
            protected Country doInBackground(Void... voids) {
                return dao.findByName(country.toLowerCase());
            }

            @Override
            protected void onPostExecute(Country tasks) {
                super.onPostExecute(tasks);
                if (tasks != null) {
                    Log.e("profilefragsta", "simar" + tasks.getName());

                    datalist.add(tasks);

                    // set countries on map
                    for (int i = 0; i < datalist.size(); i++) {
                        // if (datalist.get(i).isSelected()) {
                        clr += "|" + country_clr;
                        datanames.add(datalist.get(i).getAlpha2Code());
                        // }
                    }
                    String country_names = TextUtils.join("|", datanames);

                    String map_url_params = "chld=" + country_names + "&chco=B3BCC0" + clr + "";
                    Log.e("url", map_url_params);
                    Picasso.with(getActivity())
                            .load(map_url + map_url_params)
                            .into(binding.map);
                    binding.flagsRv.setAdapter(new CountryGridAdapter(getActivity(), datalist, true, width));
                    countryGridAdapter.notifyDataSetChanged();
                }


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public double calculatePercentage(double totalvisitedcoun){
        double price=0.0;
        //195 total countries
        price=totalvisitedcoun*195/100;

        return price;
    }
}
