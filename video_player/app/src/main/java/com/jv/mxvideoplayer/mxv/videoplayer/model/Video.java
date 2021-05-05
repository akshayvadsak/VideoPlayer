package com.jv.mxvideoplayer.mxv.videoplayer.model;



public class Video {
    private int id;
    private String data;
    private long size;
    private String mimeType;
    private long duration;
    private String resolution;
    private long dateTaken;
    private String bucketID, bucketDisplayName;

    public Video() {
    }

    public Video(String data) {
        this.data = data;
    }

    public Video(int id, String data, long size, String mimeType, long duration, String resolution,
                 long dateTaken, String bucketID, String bucketDisplayName) {
        this.id = id;
        this.data = data;
        this.size = size;
        this.mimeType = mimeType;
        this.duration = duration;
        this.resolution = resolution;
        this.dateTaken = dateTaken;
        this.bucketID = bucketID;
        this.bucketDisplayName = bucketDisplayName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
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

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ",\ndata='" + data + '\'' +
                ",\nsize=" + size +
                ",\tmimeType='" + mimeType + '\'' +
                ",\nduration=" + duration +
                ",\nresolution='" + resolution + '\'' +
                ",\tdateTaken=" + dateTaken +
                ",\nbucketID='" + bucketID + '\'' +
                ",\tbucketDisplayName='" + bucketDisplayName + '\'' +
                '}';
    }
}
