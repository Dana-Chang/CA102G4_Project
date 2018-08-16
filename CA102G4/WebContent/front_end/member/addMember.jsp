<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.mem.model.*"%>


<!DOCTYPE html>
<html>

<head>
    <title>Travel Maker</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="keywords" content="TravelMaker,Travelmaker" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>
    <link href="css/blog_semantic.min.css" rel="stylesheet" type="text/css">
    <link href="css/google_icon.css" rel="stylesheet" type="text/css">
    <link rel="shortcut icon" href="favicon.ico">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/animate.css">
    <link href="css/login_style.css" rel="stylesheet" type="text/css" media="all" />
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/login.css">
    <link rel="stylesheet" href="css/modal.css">

    <script src="js/modernizr-2.6.2.min.js"></script>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">

	<script>
		function show() {
		 var inputs = document.getElementsByTagName('input');
		 
		document.form1.mem_Name.value="李柴柴";
		document.form1.mem_Account.value="ca102g4@gmail.com.tw";
		document.form1.mem_Password.value="a12345678";

		}
		
		
		function chk(){
			if(!document.form1.name.value){
				alert('test!');
				return false;
			}else if(!document.form1.modal-password.value){
				alert('mima ?');
				return false;
			}
		}
	</script>
 </head>

<body>
   
<!-- Modal Register -->


        <!-- Modal Register content-->
        
        <%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font style="color:red">請修正以下錯誤:</font>
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li style="color:red">${message.value}</li>
		</c:forEach>
	</ul>
</c:if>
        
        <div background-color="lightblue;">
            <form METHOD="post" ACTION="member.do" name="form1" class="fh5co-form animate-box-modal" data-animate-effect="fadeIn" onsubmit="return chk();">
                <h2>Sign Up</h2>

                <div class="form-group">
                    <label for="name" class="sr-only">Name</label>
                    <input name="mem_Name" size="45" value="${param.mem_Name}" type="text" class="form-control" id="name" placeholder="name" autocomplete="off">
                </div>
                <div class="form-group">
                    <label for="account" class="sr-only">Account</label>
                    <input name="mem_Account" size="45" value="${param.mem_Account}" type="text" class="form-control" id="account" placeholder="account" autocomplete="off">
                </div>
                <div class="form-group">
                    <label for="password" class="sr-only">Password</label>
                    <input name="mem_Password" size="45" value="${param.mem_Password}" type="password" class="form-control" id="modal-password" placeholder="password" autocomplete="off">
                </div>
                <div class="form-group">
                    <label for="re-password" class="sr-only">Re-type Password</label>
                    <input type="Password" class="form-control" id="re-password" placeholder="Re-type Password" autocomplete="off">
                </div>

                <div class="form-group">
                    <label for="verification code" class="sr-only">verification code</label>
                    <input type="verification code" class="form-control" id="verification code" placeholder="verification code" autocomplete="off">
                </div>
                <div class="form_btn">
                
                    </div>

                <div class="form-group">
                    <p>Already registered? <a href="index.html">Sign In</a></p>
                </div>
                <div class="form-group">
                	<input type="hidden" name="action" value="insert" class="btn btn-primary">
                    <input type="submit" value="Confirm" class="btn btn-primary">  
                            
                    <input type="button" value="Cancel" class="btn btn-primary" onclick="history.back()">
                </div>
                    <input type="button" onclick="show()" style="background-color:pink;">
            </form>
            
        </div>
        <!-- //Modal Register content-->

<!-- //Modal Register -->
    

</body>

</html>





