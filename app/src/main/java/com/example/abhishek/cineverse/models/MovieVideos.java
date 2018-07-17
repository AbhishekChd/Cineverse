package com.example.abhishek.cineverse.models;

import com.example.abhishek.cineverse.data.UrlContract;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideos {
    @SerializedName("results")
    public List<VideoResult> videos;

    @SerializedName("id")
    public int id;

    public class VideoResult {
        // The name of the video
        @SerializedName("name")
        String name;

        @SerializedName("key")
        String videoId;

        /**
         * @return Returns YouTube video url
         */
        public String getVideoUrl() {
            return String.format(UrlContract.YOUTUBE_VIDEO_URL, videoId);
        }

        /**
         * @return Returns YouTube video thumbnail url
         */
        public String getVideoThumbnailUrl() {
            return String.format(UrlContract.YOUTUBE_THUMBNAIL_URL, videoId);
        }
    }
}
