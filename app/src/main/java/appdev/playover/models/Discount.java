package appdev.playover.models;

public class Discount  {
    private String businessName;
    private String address;
    private String city;
    private String state;
    private String phone;
    private String website;
    private String details;
    private String category;
    private Comment comment;
    private String uId;
    private String posterUId;
    private String posterName;
    private String lastUpdate;

    public Discount(String uId, String businessName, String address, String city, String state, String phone,
                    String website, String details, String category, Comment comment, String PosterUId, String PosterName, String LastUpdate) {
        this.businessName = businessName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.phone = phone;
        this.website = website;
        this.details = details;
        this.category = category;
        this.comment = comment;
        this.uId = uId;
        this.posterUId = PosterUId;
        this.posterName = PosterName;
        this.lastUpdate = LastUpdate;

    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getState()

    {
        return state;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public String getDetails() {
        return details;
    }

    public String getCategory() {
        return category;
    }

    public Comment getComment() {
        return comment;
    }

    public String getUId(){
        return uId;
    }

    public String getPosterUId(){return posterUId;}

    public String getPosterName(){return posterName;}

    public String getLastUpdate(){return lastUpdate;}

    @Override
    public String toString() {
        return "Discount{" +
                "uId= '" + uId + '\'' +
                "businessName= '" + businessName + '\'' +
                ", address= '" + address + '\'' +
                ", city= '" + city + '\'' +
                ", state= '" + state + '\'' +
                ", phone= '" + phone + '\'' +
                ", website= " + website +
                ", details=' " + details + '\'' +
                ", category=' " + category + '\'' +
                ", comments=' " + comment + '\'' +
                ", posterUId=' " + posterUId + '\'' +
                ", posterName=' " + posterName + '\'' +
                ", lastUpdate=' " + lastUpdate + '\'' +
                '}';
    }
}
