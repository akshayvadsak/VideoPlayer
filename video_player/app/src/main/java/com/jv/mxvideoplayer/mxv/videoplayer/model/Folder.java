package com.jv.mxvideoplayer.mxv.videoplayer.model;



public class Folder {
    private int id;
    private String bucketID, bucketDisplayName;
    private int count;
    private long size;
    private String location;

    public Folder() {
    }

    public Folder(int id, String bucketID, String bucketDisplayName, int count, long size, String location) {
        this.id = id;
        this.bucketID = bucketID;
        this.bucketDisplayName = bucketDisplayName;
        this.count = count;
        this.size = size;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBucketID() {
        return bucketID;
    }

    public void setBucketID(String bucketID) {
        this.bucketID = bucketID;
    }

    public String getBucketDisplayName() {
        return bucketDisplayName;
    }

    public void setBucketDisplayName(String bucketDisplayName) {
        this.bucketDisplayName = bucketDisplayName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "id=" + id +
                ",\tbucketID='" + bucketID + '\'' +
                ",\nbucketDisplayName='" + bucketDisplayName + '\'' +
                ",\tcount=" + count +
                ",\tsize=" + size +
                ",\nlocation='" + location + '\'' +
                '}';
    }
}
