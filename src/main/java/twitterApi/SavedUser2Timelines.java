/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterApi;

import DBConnection.DbConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author Babathurpe
 */
public class SavedUser2Timelines extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        try {
            String userId = String.valueOf(twitter.getId());
            String savedUser = getRow("Select timeline2 from timelines where user_id=?", userId);
            System.out.println("User - " + savedUser);
            PrintWriter out = response.getWriter();
            if (savedUser.isEmpty()) {
                out.println("<h4>You have no user saved.</h4>");
                out.println("<p>Click on 'Choose Timeline' above to save a list of whose recent tweets you want to see.</p>");
            } else {
                List<Status> statuses = twitter.getUserTimeline("@" + savedUser);
                if (statuses == null) {
                    out.println("<h3 style=\"color: red;\">Error getting tweets.</h3>");
                    out.println("<h4>This may be due to:</h4>");
                    out.println("<p>Click on 'Choose Timeline' above to save a list of whose recent tweets you want to see.</p>");
                } else {
                    out.println("<h3>@" + savedUser + "'s Timeline</h3><hr>");
                    out.println("<div class=\"caption\">");
                    for (Status status : statuses) {
                        status.getUser().getId();
                        String url = status.getUser().getMiniProfileImageURL();
                        out.println("<p><img class=\"img-thumbnail\" src=" + url + "/> @" + status.getUser().getScreenName() + "<br/> " + status.getText() + "<br/> Created: <em class=\"success\">" + status.getCreatedAt() + "</em></p>");
                    }
                    out.println("</div>");
                }
            }
        } catch (TwitterException ex) {
            Logger.getLogger(UserTimelines.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String getRow(String query, String... params) {
        StringBuilder sb = new StringBuilder();
        try {
            Connection conn = DbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sb.append(rs.getString("timeline2"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SavedUser2Timelines.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(sb);
        return sb.toString();
    }
}
