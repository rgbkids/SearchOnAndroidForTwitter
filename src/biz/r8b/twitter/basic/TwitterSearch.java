package biz.r8b.twitter.basic;

import java.util.List;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterSearch {

	// ------------------------------------------------------------------------------------------------------
	public static final String TWITTER_CONSUMER_KEY    = "xRJA7xzgEcEFPSUg4ZQ"; //"uRyQeVBxxtmj8vC1r78tQ";
	public static final String TWITTER_CONSUMER_SECRET = "HPgk8a4xNENwnpIO3qtaL4ksx68ziJzH5D31SDOjDc"; //"X4ylHJsJRaL4WguBKxpzLSdue3LyCoBPELR1rEp4po";
	// ------------------------------------------------------------------------------------------------------

	private Twitter twitter;

	//
	public TwitterSearch () {
		twitter = new TwitterFactory().getInstance();
    	twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
//    	twitter.setOAuthAccessToken(new AccessToken("", ""));
	}

	//
	List<Status> search(String word) {
		List<Status> twitterSerches = null;

        try {
        	Query query = new Query(word);
//        	query = query.page(1);
        	QueryResult result = twitter.search(query);
        	twitterSerches = result.getTweets();
        } catch (Exception e) {
        }

		return twitterSerches;
	}

	//
	public static void main(String[] args) {
		TwitterSearch ts = new TwitterSearch();
		List<Status> res = ts.search("#android");
		for (Status tweet : res) {
//			System.out.println(tweet.getProfileImageUrl());
		}
	}
}