<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="DBConnection.DbConnection"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Connection"%>
<%@page import="twitter4j.Twitter"%>
<%@page import="java.util.List"%>
<%@page import="twitter4j.Status"%>
<%@page import="twitter4j.Paging"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<html xmlns:h="http://java.sun.com/jsf/html" xmlns:f="http://java.sun.com/jsf/core">
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title>Sign in with Twitter example</title>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
        <script src="http://code.jquery.com/jquery.min.js"></script>
        <script src=http://code.jquery.com/ui/1.11.4/jquery-ui.min.js></script>
        <!-- Bootstrap Core scripts -->
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js" type="text/javascript"></script>
        <script>
            $(document).ready(function () {
                $('#chooseTimeline').hide();

                $.get('./timelines', function (data) {
                    $('#loggedIn').append(data);
                });


                $('#post').click(function () {
                    alert("Tweet Posted.");
                });

            });
        </script>   

        <style>
            .columnBorder {
                margin-top:10px;
                border: #cdcdcd medium solid;
                border-radius: 10px;
                -moz-border-radius: 10px;
                -webkit-border-radius: 10px;
            }
        </style>
    </head>


    <body>
        <%!
            public static Connection getConnection() throws SQLException {
                Connection conn = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    String jdbc = "jdbc:mysql://localhost/Sample";
                    String user = "root";
                    String pass = "root";
                    conn = DriverManager.getConnection(jdbc, user, pass);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
                return conn;
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
                    Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
                return sb.toString();
            }
        %>
        <div class="container-fluid">
            <tag:notloggedin>
                <p><br><a href="signin"><img src="./images/Sign-in-with-Twitter-darker.png" id="btn1"/></a>
                    </tag:notloggedin>
                    <tag:loggedin>
                    <!-- Page Content -->


                <div class="row margin-b-2 bg-info">

                    <div class="col-sm-6 col-md-3">
                        <!-- Empty Div -->
                    </div>

                    <div class="col-sm-6 col-md-3">
                        <h3>Welcome ${twitter.screenName} (${twitter.id})</h3> 
                        <button type="button" class="btn btn-primary" data-toggle="modal" data-target=".bs-example-modal-lg">Choose Time Line</button>
                        <br><!-- Large modal -->

                        <p>
                    </div>

                    <div class="col-sm-6 col-md-3">



                        <div class="modal row fade bs-example-modal-lg" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
                            <div class="modal-dialog modal-sm" width="50%">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                        <h4 class="modal-title" id="exampleModalLabel">Choose whose timeline to view.</h4>
                                    </div>
                                    <p class="text-center"></p>
                                    <form class="form-horizontal" action="./insertUsers" method="post">
                                        <div class="form-group">
                                            <p class="text-center">Timeline 1: <input type="text" name="timeline1" /></p>
                                            <p class="text-center">Timeline 2: <input type="text" name="timeline2" /></p>
                                            <p class="text-center">Timeline 3: <input type="text" name="timeline3" /></p>
                                            <p class="text-center"><input class="btn btn-info" type="submit" name="saveTimeline" value="Save Changes" style="width:10em;"/></p>
                                        </div>
                                    </form>
                                    <input type="hidden" name="hiddentimeline1" value = ""/>
                                    <input type="hidden" name="hiddentimeline2" />
                                    <input type="hidden" name="hiddentimeline3" />
                                </div>
                            </div>
                        </div>

                    </div>

                    <div class="col-sm-6 col-md-3">
                        <br> <a href="./logout"><img src="./images/twitter_logout.png"/></a><br>
                    </div>
                    <br>
                </div>


                <!-- Show Different time lines -->
                <div class="row margin-b-2" >
                    <div class="col-sm-6 col-md-3 columnBorder"  id="loggedIn">
                        <h3>@${twitter.screenName}'s Time line</h3>


                    </div>
                    <div class="col-sm-6 col-md-3 columnBorder">
                        <h3>Showing time line 2</h3>
                        <img class="img-responsive thumbnail" src="http://placehold.it/700x350" alt="">
                        <div class="caption">
                            <h4><a href="#">Image title</a></h4>
                            <p>Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.</p>
                        </div>
                    </div>
                    <div class="col-sm-6 col-md-3 columnBorder">
                        <h3>Showing time line 3</h3>
                        <img class="img-responsive thumbnail" src="http://placehold.it/700x350" alt="">
                        <div class="caption">
                            <h4><a href="#">Image title</a></h4>
                            <p>Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.</p>
                        </div>
                    </div>
                    <div class="col-sm-6 col-md-3 columnBorder">
                        <h3>Showing time line 4</h3>
                        <img class="img-responsive thumbnail" src="http://placehold.it/700x350" alt="">
                        <div class="caption">
                            <h4><a href="#">Image title</a></h4>
                            <p>Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.</p>
                        </div>
                    </div>
                </div>
                <!-- /.row -->

                <hr>
                <footer class="margin-tb-3">
                    <div class="row">
                        <div class="col-lg-12">
                            <p class="text-center">Copyright &copy; Sitename 2015</p>
                        </div>
                    </div>
                </footer>
            </div>
            <!-- /.container-fluid -->



        </tag:loggedin>
    </body>
</html>

