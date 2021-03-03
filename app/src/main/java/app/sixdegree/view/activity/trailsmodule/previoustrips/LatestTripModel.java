package app.sixdegree.view.activity.trailsmodule.previoustrips;

public class LatestTripModel {

    int id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id( int user_id ) {
        this.user_id = user_id;
    }

    int user_id;
    public String getImage() {
        return image;
    }

    public void setImage( String image ) {
        this.image = image;
    }

    String image;

    public LatestTripModel( int id, String name, String startdate, int trailcount,String image ,int user_id) {
        this.id = id;
        this.name = name;
        this.startdate = startdate;
        this.trailcount = trailcount;
        this.image =   image;
        this.user_id =   user_id;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate( String startdate ) {
        this.startdate = startdate;
    }

    public int getTrailcount() {
        return trailcount;
    }

    public void setTrailcount( int trailcount ) {
        this.trailcount = trailcount;
    }

    String name;
    String startdate;
    int trailcount;
}
