package app.sixdegree.viewModel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class StatisticsVm extends BaseVm {
    @Bindable
    String country = "100";

    @Bindable
    String percentage = "176%";
    @Bindable
    String totaldays = "12";

    @Bindable
    String totalhours = "0";
    @Bindable
    String totalSeconds = "0";
    @Bindable
    String totalMinutes = "0";

    public String getTotalkms() {
        return totalkms;
    }

    public void setTotalkms(String totalkms) {
        this.totalkms = totalkms;
        notifyPropertyChanged(BR.totalkms);
    }

    @Bindable
    String totalkms = "0";
    @Bindable
    String tripTrails = "0";

    public String getTripTrails() {
        return tripTrails;
    }

    public void setTripTrails(String tripTrails) {
        this.tripTrails = tripTrails;
        notifyPropertyChanged(BR.tripTrails);
    }

    public String getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(String totalSeconds) {
        this.totalSeconds = totalSeconds;
        notifyPropertyChanged(BR.totalSeconds);
    }

    public String getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(String totalMinutes) {
        this.totalMinutes = totalMinutes;
        notifyPropertyChanged(BR.totalMinutes);
    }

    public String getTotalhours() {
        return totalhours;
    }

    public void setTotalhours(String totalhours) {
        this.totalhours = totalhours;
        notifyPropertyChanged(BR.totalhours);
    }


    public String getTotaldays() {
        return totaldays;
    }

    public void setTotaldays(String totaldays) {
        this.totaldays = totaldays;
        notifyPropertyChanged(BR.totalDays);
    }


    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;

    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        notifyPropertyChanged(BR.country);
    }


}
