/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter4j.examples.signin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 *
 * @author Babathurpe
 */
public class UserTimelines extends HttpServlet {

    private static final long serialVersionUID = 2132731135996613711L;
    User user;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        
        
        try {
            PrintWriter out = response.getWriter();
            User currentUser = (User) twitter.showUser(twitter.getId());
            request.getSession().setAttribute("current_User", currentUser);
            //long userid = twitter.getId();
            Paging paging = new Paging(1, 19);
            //User currentUser = twitter.showUser(twitter.getId());
            String userImage = currentUser.getProfileImageURL();
            List<Status> rawJSON = twitter.getHomeTimeline(paging);
            out.println("<hr>");
            out.println("<form class=\"form-horizontal\" action=\"./post\" method=\"post\">");
            out.println("<div class=\"form-group\"><label for=\"twitterUpdate\" class=\"col-sm-2 control-label\"><img class=\"thumbnail\" src=\"" 
                    +userImage+ "\" ></label> <div class=\"col-sm-10\"><textarea class=\"form-control\" cols=\"30\" rows=\"2\" name=\"twitterUpdate\" "
                    + "placeholder=\"Update Status\" required ></textarea></div><br><br><br><input class=\"btn btn-warning pull-right ButtonMargin\" type=\"submit\" name=\"post\" id=\"post\" value=\"Post Tweet\"/></div>");
            out.println("</form>");
            out.println("<div class=\"caption\">");
            for (Status status : rawJSON) {
                status.getUser().getId();
                String url = status.getUser().getMiniProfileImageURL();
                out.println("<p><img class=\"img-thumbnail\" src=" + url + "/> @" + status.getUser().getScreenName() + "<br/> " + status.getText() + "<br/> Created: <em class=\"success\">" + status.getCreatedAt() + "</em></p>");
            }
            out.println("</div>");
        } catch (TwitterException ex) {
            Logger.getLogger(UserTimelines.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
