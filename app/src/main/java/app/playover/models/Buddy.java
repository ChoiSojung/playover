package app.playover.models;

public class Buddy {
    private boolean blocked;
    private String uid;

    public Buddy() {

    }

    public void setBlocked(boolean isBlocked) {
        blocked = isBlocked;
    }

    public boolean getBlocked() {
        return blocked;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "uId= '" + uid + '\'' +
                "blocked= '" + String.valueOf(blocked) + '\'' +
                '}';
    }

}
