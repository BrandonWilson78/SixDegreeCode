package app.sixdegree.model;

public class SignupModel {

    String name="";
    String email="";
    String password="";

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber( String phonenumber ) {
        this.phonenumber = phonenumber;
    }

    String phonenumber="";
    String profile="";
    String homeLoc="";
    String lat="0";
    String lng="";

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public SignupModel(String name, String email,String phonenumber, String password, String profile, String homeLoc, String lat, String lng) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
        this.profile = profile;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProfile() {
        return profile;
    }

    public String getHomeLoc() {
        return homeLoc;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setHomeLoc(String homeLoc) {
        this.homeLoc = homeLoc;
    }
}
