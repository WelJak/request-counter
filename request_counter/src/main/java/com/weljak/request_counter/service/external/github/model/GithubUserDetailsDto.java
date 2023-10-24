package com.weljak.request_counter.service.external.github.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class GithubUserDetailsDto {
    @SerializedName("avatar_url")
    private final String avatarUrl;
    private final Object bio;
    private final String blog;
    private final String company;
    @SerializedName("created_at")
    private final ZonedDateTime createdAt;
    private final String email;
    @SerializedName("events_url")
    private final String eventsUrl;
    private final Long followers;
    @SerializedName("followers_url")
    private final String followersUrl;
    private final Long following;
    @SerializedName("following_url")
    private final String followingUrl;
    @SerializedName("gists_url")
    private final String gistsUrl;
    @SerializedName("gravatar_id")
    private final String gravatarId;
    private final Boolean hireable;
    @SerializedName("html_url")
    private final String htmlUrl;
    private final Long id;
    private final String location;
    private final String login;
    private final String name;
    @SerializedName("node_id")
    private final String nodeId;
    @SerializedName("organizations_url")
    private final String organizationsUrl;
    @SerializedName("public_gists")
    private final Long publicGists;
    @SerializedName("public_repos")
    private final Long publicRepos;
    @SerializedName("received_events_url")
    private final String receivedEventsUrl;
    @SerializedName("repos_url")
    private final String reposUrl;
    @SerializedName("site_admin")
    private final Boolean siteAdmin;
    @SerializedName("starred_url")
    private final String starredUrl;
    @SerializedName("subscriptions_url")
    private final String subscriptionsUrl;
    @SerializedName("twitter_username")
    private final String twitterUsername;
    private final String type;
    @SerializedName("updated_at")
    private final ZonedDateTime updatedAt;
    private final String url;

}
