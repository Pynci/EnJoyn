package it.unimib.enjoyn.model;

public class EventLocation {
    String id, name;
    double longitude, latitude;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getLatitudeToString(){
        return Double.toString(latitude);
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
