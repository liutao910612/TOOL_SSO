<!DOCTYPE HTML>
<html lang="en">
<head>
    <title>index</title>
    <meta content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="/style/bootstrap.min.css" >
    <link rel="stylesheet" href="/style/bootstrap-theme.min.css" >
    <script src="/script/jquery-3.1.1.js"></script>
    <script src="/script/bootstrap.min.js"></script>
    <style>
        .form-horizontal{
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <form class="form-horizontal" id="loginForm">
        <input type="hidden" name="url" value="${url}" id="url">
        <div class="form-group">
            <label for="username" class="col-sm-1 control-label">用户名</label>
            <div class="col-sm-5">
                <input  class="form-control" name="username" id="username" placeholder="username">
            </div>
        </div>
        <div class="form-group">
            <label for="password" class="col-sm-1 control-label">密码</label>
            <div class="col-sm-5">
                <input  class="form-control" name="password" id="password" placeholder="password">
            </div>
        </div>
    </form>
    <div class="form-group">
        <div class="col-sm-offset-1 col-sm-5">
            <button class="btn btn-default" id="loginButton">登录</button>
        </div>
    </div>
</body>
<script>
    $(function () {
       $("#loginButton").on("click",function () {
           console.log("enter login button");
           login.login();

       })
    });

    var login = {
        login:function(){
            debugger;
            var username = $("#username").val();
            var password = $("#password").val();
            var url = $("#url").val();
            var user = {
                username:username,
                password:password,
                url:url
            };

            var formData = JSON.stringify(user);
            var success = "";
            var username = "";
            $.ajax({
                url : '/sso/server/login-status',
                type : 'POST',
                data : formData,
                contentType : "application/json",
                dataType : 'json',
                async:false,
                success: function(data){
                    success = data.code;
                    username = data.username;　　　　
                }
            });

            if (success == 1){
                window.location.href = "index?username="+username;
            }else{
                alert("username or password is error");
            }
        }
    }
</script>
</html>
