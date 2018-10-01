<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css" rel="stylesheet" id="bootstrap-css">
        <script src="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/js/bootstrap.min.js"></script>
        <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <link rel="stylesheet" type="text/css" href="mycss2.css">

        <link rel="stylesheet" type="text/css" href="css/cssfil.css">
        <title>Sign Up</title>
    </head>

    <body>

        <div id="indhold">
        <h1>Sign up</h1>
        <form method="POST" action="Control">
            <label>Username</label>
            <input type="text" name="new_username" class="span3">
            <label>Password</label>
            <input type="text" name="new_pwd" class="span3">
            <label>Email Address</label>
            <input type="email" name="new_email" class="span3">
            <label>Balance</label>
            <input type="text" name="new_balance" class="span3">

            <label><input type="checkbox" name="terms"> I agree with the NiMo LeMA <a href="#">Terms and Conditions</a>.</label>
            <input type="hidden" name="origin" value="createUser"><br><br>
            <input type="submit" value="Sign up" class="btn btn-primary pull-right">
            <div class="clearfix"></div>
        </form>
        
        </div>

    </body>
</html>