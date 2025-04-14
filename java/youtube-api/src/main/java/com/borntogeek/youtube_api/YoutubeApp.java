package com.borntogeek.youtube_api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoSnippet;

public class YoutubeApp {

	public static void main(String[] args) throws GeneralSecurityException, IOException, GoogleJsonResponseException {
		YouTube youtubeService = YoutubeHelper.getService();
		
		showChannelStatistics(youtubeService);
		listAllVideos(youtubeService);
		updateVideoTitle(youtubeService, "videoId", "New title");
	}

	private static void showChannelStatistics(YouTube youtubeService)
			throws GeneralSecurityException, IOException, GoogleJsonResponseException {
		YouTube.Channels.List request = youtubeService.channels().list("statistics");
		ChannelListResponse response = request.setMine(true).execute();

		List<Channel> channels = response.getItems();
		for (Channel channel : channels) {
			System.out.println("Channel ID: " + channel.getId());
			System.out.println(channel.getStatistics());
		}
	}

	private static void listAllVideos(YouTube youtubeService)
			throws GeneralSecurityException, IOException, GoogleJsonResponseException {
		String uploadsPlaylistId = getUploadsPlaylistId(youtubeService, "channelId");
		System.out.println("Upload playlist ID" + uploadsPlaylistId);
		listVideos(youtubeService, uploadsPlaylistId);
	}

	private static String getUploadsPlaylistId(YouTube youtubeService, String channelId) throws IOException {
		YouTube.Channels.List request = youtubeService.channels().list("contentDetails").setId(channelId);
		ChannelListResponse response = request.execute();
		List<Channel> channels = response.getItems();
		if (Objects.nonNull(channels) && !channels.isEmpty()) {
			return channels.get(0).getContentDetails().getRelatedPlaylists().getUploads();
		}
		
		System.out.println("Channel not found.");
		return null;
	}

	private static void listVideos(YouTube youtubeService, String uploadsPlaylistId) throws IOException {
		String nextPageToken = null;
		do {
			YouTube.PlaylistItems.List request = youtubeService.playlistItems().list("snippet,contentDetails")
					.setPlaylistId(uploadsPlaylistId).setMaxResults(10L).setPageToken(nextPageToken);
			PlaylistItemListResponse response = request.execute();
			List<PlaylistItem> items = response.getItems();

			for (PlaylistItem item : items) {
				String videoId = item.getContentDetails().getVideoId();
				Video video = getVideoById(youtubeService, videoId, "snippet,statistics");
				String title = video.getSnippet().getTitle();
				long viewCount = video.getStatistics().getViewCount().longValue();
				long commentCount = video.getStatistics().getCommentCount().longValue();
				
				System.out.println("Id: " + videoId + " | Title: " + title + " | Views: " + viewCount + " | Comments: " + commentCount);
			}

			nextPageToken = response.getNextPageToken();
		} while (nextPageToken != null);
	}

	private static Video getVideoById(YouTube youtubeService, String videoId, String part) throws IOException {
		YouTube.Videos.List request = youtubeService.videos().list(part).setId(videoId);
		VideoListResponse response = request.execute();
		List<Video> videos = response.getItems();
		if (Objects.nonNull(videos) && !videos.isEmpty()) {
			return videos.get(0);
		}

		System.out.println("Video not found.");
		return null;
	}

	private static void updateVideoTitle(YouTube youtubeService, String videoId, String newTitle)
			throws IOException, GoogleJsonResponseException {
		Video video = getVideoById(youtubeService, videoId, "snippet");
		
		VideoSnippet snippet = video.getSnippet();
		snippet.setTitle(newTitle);
		
		// Uncomment this to update also the description of the video
		//snippet.setDescription(snippet.getDescription() + " New description");
		
		video.setSnippet(snippet);

		YouTube.Videos.Update updateRequest = youtubeService.videos().update("snippet", video);
		Video updatedVideo = updateRequest.execute();

		System.out.println("Updated title: " + updatedVideo.getSnippet().getTitle());

	}
}