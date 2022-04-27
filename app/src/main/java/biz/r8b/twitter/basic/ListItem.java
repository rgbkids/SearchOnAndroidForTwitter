package biz.r8b.twitter.basic;

import java.util.Date;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import android.graphics.Bitmap;
import android.view.View;

public class ListItem {
    public Bitmap image;
    public String name = "";
    public String screenName = "";
    public String comment = "";
    public long id;
	public User user;
    public String profileImageURL;
    public String date = "";
	public boolean isFavorited;
	public boolean isRetweet;
	protected String description;
	protected boolean isPublic;
//	public UserMentionEntity[] userMentionEntities;
//	protected URLEntity[] urlEntities;
	public long inReplyToStatusId;
	public boolean isProtected;
	public boolean isRetweetedByMe;
	public HashtagEntity[] hashtagEntities;
	public MediaEntity[] mediaEntities;
	public URLEntity[] urlEntities;
	public UserMentionEntity[] userMentionEntities;
	public String retweetScreenName;
	public boolean marking = true;
	public long retweetCount;
	public boolean midoku;
	public GeoLocation geoLocation;

	public String getGeoCode() {
    	return "[Map] " + getGeoMapUrl();
	}

	public String getGeoMapUrl() {
    	return "" + "http://maps.google.co.jp/maps?q=" + geoLocation.getLatitude() + ",+" + geoLocation.getLongitude();
	}

	void test() {
//		hashtagEntities[0].getText()
	}

	//
	public Date createdAt;
	public boolean isUserStream;
	protected boolean isNewTweetInfo;
//	public View newTweetInfoView;
	protected int newTweetNum;
	public String source;

}
