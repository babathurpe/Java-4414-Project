/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter4j.examples.signin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author Babathurpe
 */
public class Timelines extends HttpServlet{
    private static final long serialVersionUID = 2132731135996613711L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //request.setCharacterEncoding("UTF-8");
        String text = request.getParameter("text");
        System.out.println(text);
        Twitter twitter = (Twitter)request.getSession().getAttribute("twitter");
        try {
            twitter.updateStatus(text);
        } catch (TwitterException e) {
            throw new ServletException(e);
        }
        response.sendRedirect(request.getContextPath()+ "/");
    }
}
