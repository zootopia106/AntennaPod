package de.podfetcher.util;

import java.util.Comparator;

import de.podfetcher.feed.FeedItem;

/** Compares the pubDate of two FeedItems for sorting*/
public class FeedItemPubdateComparator implements Comparator<FeedItem> {

	/** Returns a new instance of this comparator in reverse order. 
	public static FeedItemPubdateComparator newInstance() {
		FeedItemPubdateComparator
	}*/
	@Override
	public int compare(FeedItem lhs, FeedItem rhs) {
		return -lhs.getPubDate().compareTo(rhs.getPubDate());
	}

}
