
package app.sixdegree.network.responses.searchfriends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    Boolean isSelected=false;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;

    public String getRealtion() {
        return relation;
    }

    public void setRealtion( String relation ) {
        this.relation = relation;
    }

    @SerializedName("relation")
    @Expose
    private String relation;

    public String getFriend_status() {
        return friend_status;
    }

    public void setFriend_status( String friend_status ) {
        this.friend_status = friend_status;
    }

    @SerializedName("friend_status")
    @Expose
    private String friend_status;
    @SerializedName("first_name")
    @Expose
    private Object firstName;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("home")
    @Expose
    private String home;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("follow_status")
    @Expose
    private String followStatus;
    @SerializedName("travelling")
    @Expose
    private Boolean travelling;
    @SerializedName("mutual_friends")
    @Expose
    private Long mutualFriends;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getFirstName() {
        return firstName;
    }

    public void setFirstName(Object firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(String followStatus) {
        this.followStatus = followStatus;
    }

    public Boolean getTravelling() {
        return travelling;
    }

    public void setTravelling(Boolean travelling) {
        this.travelling = travelling;
    }

    public Long getMutualFriends() {
        return mutualFriends;
    }

    public void setMutualFriends(Long mutualFriends) {
        this.mutualFriends = mutualFriends;
    }

}
