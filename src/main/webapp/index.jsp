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

                $.get('./savedUser1', function (data) {
                    $('#user1').append(data);
                });
                
                $.get('./savedUser2', function (data) {
                    $('#user2').append(data);
                });
                
                $.get('./savedUser3', function (data) {
                    $('#user3').append(data);
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

            .ButtonMargin { 
                margin-right: 20px;
            }
        </style>
    </head>


    <body>
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
                        <button type="button" class="btn btn-primary" data-toggle="modal" data-target=".bs-example-modal-lg">
                            <span class="glyphicon glyphicon-user" aria-hidden="true"></span> Choose Time Line</button>
                        <br><!-- Large modal -->

                        <p>
                    </div>

                    <div class="col-sm-6 col-md-3">

                        <div class="modal row fade bs-example-modal-lg" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true" >
                            <div class="modal-dialog modal-sm" style="width:25%;">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                        <h4 class="modal-title" id="exampleModalLabel">Choose whose timeline to view.</h4>
                                    </div>
                                    <p class="text-center"></p>
                                    <form name="userTimeline" class="form-horizontal" action="./savedUser1" method="post">
                                        <div class="form-group">
                                            <p class="text-center">Timeline 1: <input type="text" name="timeline1" required autofocus /></p>
                                            <p class="text-center">Timeline 2: <input type="text" name="timeline2" required /></p>
                                            <p class="text-center">Timeline 3: <input type="text" name="timeline3" required /></p>
                                            <p class="text-center"><input class="btn btn-info" type="submit" name="saveTimeline" value="Save Changes"/></p>
                                            <p class="text-center"><span style="color: red;"><b>*</b></span> You can update/change users anytime.</p>
                                            <p class="text-center"><span style="color: red;"><b>*</b></span> Fill the fields above, click on 'Save Changes'.</p>
                                            <p class="text-center"><span style="color: red;"><b>*</b></span> Refresh the page for changes (might require login).</p>
                                        </div>
                                    </form>
                                    
                                </div>
                            </div>
                        </div>

                    </div>

                    <div class="col-sm-6 col-md-3">
                        <br> <a href="./logout"><button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-off" aria-hidden="true"></span> Logout</button> </a><br>
                    </div>
                    <br>
                </div>


                <!-- Show Different time lines -->
                <div class="row margin-b-2" >
                    <!-- Column 1 - Logged in twitter owner's timeline -->
                    <div class="col-sm-6 col-md-3 columnBorder"  id="loggedIn">
                        <h3>@${twitter.screenName}'s Timeline</h3>

                    </div>
                        
                    <!-- Column 2 - Saved User1's timeline -->
                    <div class="col-sm-6 col-md-3 columnBorder" id="user1">
                        
                    </div>
                    
                    <!-- Column 3 - Saved User2's timeline -->
                    <div class="col-sm-6 col-md-3 columnBorder" id="user2">
                        
                    </div>
                    
                    <!-- Column 4 - Saved User3's timeline -->
                    <div class="col-sm-6 col-md-3 columnBorder" id="user3">
                        
                    </div>
                </div>

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

