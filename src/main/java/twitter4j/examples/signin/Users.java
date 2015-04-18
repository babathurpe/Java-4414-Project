/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter4j.examples.signin;

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
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 *
 * @author Babathurpe
 */
public class Users extends HttpServlet {

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
            PrintWriter out = response.getWriter();
            User currentUser = (User) twitter.showUser(twitter.getId());
            Paging paging = new Paging(1, 20);
            String userImage = currentUser.getProfileImageURL();
            List<Status> rawJSON = twitter.getHomeTimeline(paging);
            out.println("");
            out.println("<form action=\"./post\" method=\"post\">");
            out.println("<label for=\"twitterUpdate\" class=\"col-sm-2 control-label\"><img class=\"img-responsive thumbnail\" src=\"" +userImage+ "\" ></label> <div class=\"col-sm-7\"><textarea cols=\"40\" rows=\"2\" name=\"twitterUpdate\" placeholder=\"Update Status\"></textarea></div>");
            out.println("<br><input class=\"btn btn-warning\" type=\"submit\" name=\"post\" id=\"post\" value=\"update\"/></form>");
            out.println("<div class=\"caption\">");
            for (Status status : rawJSON) {
                status.getUser().getId();
                String url = status.getUser().getMiniProfileImageURL();
                out.println("<p><img class=\"img-thumbnail\" src=" + url + "/> @" + status.getUser().getScreenName() + "<br/> " + status.getText() + "<br/> Created: <em class=\"success\">" + status.getCreatedAt() + "</em></p>");
            }
            out.println("</div>");
        } catch (TwitterException ex) {
            Logger.getLogger(Timelines.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * Sends or updates the timelines table which 
     * stores the usernames (twitter handle) to 
     * show tweets of.
     * --Limitation : All 3 fields have to filled and saved.
     * 
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        Set<String> keySet = request.getParameterMap().keySet();
        try {
            if (keySet.contains("timeline1") && keySet.contains("timeline2") && keySet.contains("timeline3")) {
                // There are some parameters
                String userId = String.valueOf(twitter.getId());
                String timeline = request.getParameter("timeline1");
                String timeline1 = request.getParameter("timeline2");
                String timeline2 = request.getParameter("timeline3");
                String result = getRow("Select user_id from timelines where user_id=?", userId);
                System.out.println(result +" User ID - "+ userId);
                if(result.isEmpty()){    
                    doUpdate("INSERT INTO timelines (user_id, timeline1, timeline2, timeline3) VALUES (?, ?, ?, ?)", userId, timeline, timeline1, timeline2);
                    //search for tweets by each user
                } else{
                    doUpdate("UPDATE timelines SET timeline1 = ?, timeline2 = ?, timeline3 = ? WHERE user_id = ?", timeline, timeline1, timeline2, userId);
                    //search for tweets by each user
                }
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            } else {
                // There are no parameters at all
                response.sendError(500, "Unsuccessful insert.");
                //out.println("Error: Not enough data to input. Please use a URL in the form /product?name=XXX&description=XXX&quantity=#");
            }
        } catch (TwitterException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);

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
                sb.append(rs.getInt("user_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    public int doUpdate(String query, String... params) {
        int numChanges = 0;
        try {
            Connection conn = DbConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            numChanges = pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numChanges;
    }
    
    public void getTimeLine(){
        
    }
}
