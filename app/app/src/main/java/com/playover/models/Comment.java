package com.playover.models;

public class Comment {
    private String UId;
    private String CommentDetail;
    private String CommenDate;
    private String CommentPoster;
    private String PosterUId;

    public Comment(String uId, String commentDate, String commentDetail, String commentPoster, String posterUId){
        UId = uId;
        CommentDetail = commentDetail;
        CommenDate = commentDate;
        CommentPoster = commentPoster;
        PosterUId = posterUId;
    }
   public String getUId(){
        return  UId;

   }
   public String getPosterUId(){return PosterUId;}
   public String getCommentDetail(){
        return  CommentDetail;
   }
   public String getCommentDate(){
        return  CommenDate;
   }
   public String getCommentPoster(){
        return CommentPoster;
   }
    public void setUId(String UId)
    {
        this.UId = UId;
    }

}
