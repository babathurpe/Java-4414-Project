package twitterApi;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SigninServlet extends HttpServlet {
    private static final long serialVersionUID = -6205814293093350242L;
    
    private final static String CONSUMER_KEY = "FjMjYxxLFAumEp2kuW4t0kMYj";
    private final static String CONSUMER_KEY_SECRET = "fkUg2WQo1z80cNbepPsYzLzlu3Gu1B5lDbUUdzmmubv0hXE64f";
    //private final static String ACCESS_TOKEN = "135157353-4asswUtw0V8AoNG2zylBPIGPdwdcL49NCy5EfFRo";
    //private final static String ACCESS_TOKEN_SECRET = "fWCNaso7XOVxIjCS5E6m48K9Wqy1HvZhMm6CDh7WWUXFi";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         
        //Instantiate a re-usable and thread-safe factory
        TwitterFactory twitterFactory = new TwitterFactory();
 
        //Instantiate a new Twitter instance
        Twitter twitter = twitterFactory.getInstance();
 
        //setup OAuth Consumer Credentials
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
        request.getSession().setAttribute("twitter", twitter);
        try {
            StringBuffer callbackURL = request.getRequestURL();
            int index = callbackURL.lastIndexOf("/");
            callbackURL.replace(index, callbackURL.length(), "").append("/callback");

            RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL.toString());
            request.getSession().setAttribute("requestToken", requestToken);
            response.sendRedirect(requestToken.getAuthenticationURL());

        } catch (TwitterException e) {
            throw new ServletException(e);
        }

    }
}
