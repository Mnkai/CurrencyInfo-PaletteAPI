package moe.minori.currencyinfo;

/**
 * Created by minori on 15. 8. 19.
 */
public class Consts {
    /**
     * Palette external broadcasting API name
     */
    public final static String EXTERNAL_BROADCAST_API = "palette.twitter.externalBroadcast";

    /**
     * External API, write tweet on current active account / Extra: DATA (String), REPLY_TO (LONG - nullable)
     */
    public final static String REQ_WRITE_TWEET = "palette.twitter.externalBroadcast.tweet.write";

    // From support module
    /**
     * External API, notify on new tweet on timeline and mention / Extra: FROMTEXT, FROMTEXTSCREEN, FROMLONG, TEXT, REPLY_TO cf. {@link #NEW_STATUS_NONLIB}
     */
    public final static String NOTIFY_NEW_STATUS = "palette.twitter.externalBroadcast.tweet.streaming";

}
