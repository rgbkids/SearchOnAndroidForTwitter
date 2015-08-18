package biz.r8b.twitter.basic;

import twitter4j.Status;

public interface Twitter4JUserStreamIF {

	void onStatus(Status status);

}
