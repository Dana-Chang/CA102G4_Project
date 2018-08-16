$(document).ready(function(){
	
	/*測試把聊天區塊存起來後取出*/
	if(sessionStorage.length != 0){
		var len = sessionStorage.length;
		for(var i=0;i<len; i++) {
		    var key = sessionStorage.key(i);
		    var value = sessionStorage[key];
		    if(key != "marginRight" && value != "undefined"){
		    	$("body").append(value);
		    	console.log("這邊在取sessionStorage的值:");
		    }
		}
		$(".input_chatContainer").toggleClass("chatWindow_Content");
	}else{
		//當session空的時候，代表沒有任何開啟的聊天對話視窗
		console.log("sessionStorage是空的，所以設定初始位置");
		sessionStorage.setItem("marginRight",350);
	}
	
	
    //放大聊天列表視窗
    $(".chatHeader").click(function(e){
       $(".chatContainer").toggleClass("chatWindow"); 
    });
    
    //放大聊天內容視窗,因為是動態產生DOM所以不能用上面的寫法
    $("body").on("click",".input_chatHeader",function(){
    	$(this).parent().toggleClass("chatWindow_Content");
    	$(this).next().scrollTop(99999999); //讓聊天內容的捲軸都是在最下面
    });
    
	//當按下聊天送出按鈕後會呼叫sendMessage(動態產生DOM)
	$("body").on("click",".input_chatFooter span.input-group-addon.btn",sendMessage);
	//當按下enter時會呼叫sendMessage(動態產生DOM)
	$("body").on("keypress","input[name='message']",function(e){if(e.keyCode == 13){sendMessage(e.target)}});
	
	//滑鼠移動到聊天視窗時上的關閉及加入好友，會跳出tooltip(動態產生DOM)
	$("body").on("mouseover","span[data-toggle='tooltip']",function(){
		$(this).tooltip();
	});
	

	//當按下登出按鈕後，sessionStorage清空
	$("body").on("click","div.top-banner-right i.fas.fa-sign-out-alt",function(){
		sessionStorage.clear();
	});
	
	
	//當按下聊天對話內容的叉叉時，關閉對應的視窗
	$("body").on("click",".close",function(e){
		var crId_close = $(this).parent().parent().parent().attr("value");
		$(".input_chatContainer_select[value='"+crId_close+"']").remove(); //刪除對應的聊天內容視窗 or
		$(".input_chatContainer[value='"+crId_close+"']").remove(); //刪除對應的聊天內容視窗
		
		var right = Number(sessionStorage.getItem("marginRight"));
		console.log("關閉時，目前長度"+right);
		if(right <= 350){
			sessionStorage.setItem("marginRight",350);
		}else{
			sessionStorage.setItem("marginRight",right-290);
		}
		console.log("關閉後，目前長度"+sessionStorage.getItem("marginRight"));
		sessionStorage.removeItem(crId_close); //從session移出關閉的聊天對話區塊
		e.stopPropagation();
		
	});
	
    //點選《建立新聊天對話》的圖示，會跳出建立聊天的小視窗
    $("#chat_addFri_span").click(function(e){
        $("#chat_AddFri_Modal").modal({backdrop:'static'});
        //避免上次使用搜尋功能，會將list隱藏起來
        $("#chat_AddFri_Modal > div > form > div > div.modal-body > div:nth-child(3) > div:nth-child(1) > div.ui.middle.aligned.selection.list > .item").attr("style",{"display":"display"});
        $("#search_Fri_modal").css("border-color","rgba(34, 36, 38, .15)");//清除未選擇人員的所設定的紅框
        $("#chatName").css("border-color",""); //清除若聊天室未輸入所設定的紅框
        e.stopPropagation();
    });
    
    //點選《加入更多人》的的圖示，會跳出加入更多人的小視窗(動態產生DOM)
    $("body").on("click","span.addOneFri",function(e){ 
    	//避免上次使用搜尋，會把list隱藏
    	//先清除上次以選的名單
    	$("input[type='checkbox']").removeAttr("checked");
    	//先開啟上一次被disable掉的checkbox
    	$("#chat_AddMoreFri_Modal input[type='checkbox']").attr("disabled",false);
    	$("#search_addMoreFri").css("border-color","rgba(34, 36, 38, .15)");
        //拿到按下加入的某間聊天對話ID
        var crId = $(this).parent().parent().parent().attr("id"); 
        var myFriList = document.getElementsByName("select_fri");
        //先拿到我的好友名單

        $.ajax({
        	url:"/CA102G4/chatRoom.do",
        	type:"POST",
        	data:{action:"getCRJ_List",chatRoomId:crId},
        	success:function(data){
        		var jsonObj = JSON.parse(data);
        		for (i in jsonObj) {
        	        for ( var x = 0; x < myFriList.length; x++) {
        	        	if(myFriList[x].value.trim() === jsonObj[i].memId.trim()){
        	        		//若是已經參加對話的人員，將會把checkbox disabled
        	        		$("#chat_AddMoreFri_Modal #"+myFriList[x].value.trim()+"").attr("disabled",true);
        	        	}
        	        }
        		}
        		$("#chat_AddMoreFri_Modal").attr("value",crId);
        	},
        	error:function(){
        		alert("失敗,未呼叫到chatRoom.do");
        	}
        });

        var crName = $(this).parent().parent().text().replace('加入更多好友', '').replace('名單','');
        e.stopPropagation();
        //開啟《加入更多人》的視窗時，若先前有使用搜尋會導致item被display:none，所以要取消，且搜尋框中的字也要清掉
        $("#chat_AddMoreFri_Modal .modal-content .modal-body .ui.middle.aligned.selection.list>.item").attr("style",{"display":"display"});
        $("#chat_AddMoreFri_Modal .modal-content .modal-body #search_addMoreFri").val("");
        
        $("#chat_AddMoreFri_Modal .modal-title").text(crName.trim()+"::加入更多人");
        $("#chat_AddMoreFri_Modal").modal({backdrop:'static'});
    });
    
    //點選《名單》的的圖示，會跳出目前參加聊天對話的名單，可以踢出別人(動態產生DOM)
    $("body").on("click","span.chatJoinList",function(e){
    	var crId = $(this).parent().parent().parent().attr("id"); //拿到按下加入的某間聊天對話ID
        var crName = $(this).parent().parent().text().replace('加入更多好友', '');
        e.stopPropagation();
        $("#chatRoomJoin_Modal .modal-title").text(crName.trim()+"::參與名單");
        $("#chatRoomJoin_Modal .modal-body").empty();
        $.ajax({
        	url:"/CA102G4/chatRoom.do",
        	type:"POST",
        	data:{action:"getCRJ_List",chatRoomId:crId},
        	success:function(data){
        		console.log(data);
        		if(data.trim() != "空"){	
	        		var jsonObj = JSON.parse(data);
	        		for (i in jsonObj) {
	            		$("#chatRoomJoin_Modal .modal-body").append(
		    				"<div class='item'>"+
		    					"<table>"+
		        					"<tr>"+
		            					"<td style='width: 60px;'>"+
		            						"<img class='ui avatar image' src='/CA102G4/front_end/readPic?action=member&id="+jsonObj[i].memId+"'>"+
		            					"</td>"+
		            					"<td style='width: 60px;'>"+
		            						"<span class='content'>"+jsonObj[i].memName+"</span>"+
		            					"</td>"+
		            					"<td style='width: 60px;'>" +
		            						"<form method='POST' name='delMemFromCR' style='display: initial;'>"+
			            						"<input type='hidden' name='action' value='delOneCRFri'/>"+
			            						"<input type='hidden' name='delMemId' value='"+jsonObj[i].memId+"'/>"+
			            						"<input type='hidden' name='delfromCR' value='"+crId+"'/>"+
			            					"</form>" +
		            						"<button class='btn btn-warning' name='delMemFromCR_btn'>退出群組</button>"+
		            					"</td>"+
		        					"</tr>"+
		    					"</table>"+
		    				"</div>"
	            		);
	            		
	            		//代表是一對一聊天，所以不給退出
	            		if(jsonObj[i].joinInitCnt == 2){
	            			$("#chatRoomJoin_Modal .modal-body input[value='"+jsonObj[i].memId+"']").parent().next().attr("disabled","disabled");
	            		}
	            		
	        		}
        		}else{
        			$("#chatRoomJoin_Modal .modal-body").append("該聊天對話已無人參與，即將自動重新整理頁面");
        			window.setTimeout(function(){location.href=requestURLForJS},800);
        		}
        		$("#chatRoomJoin_Modal").modal({backdrop:'static'});
        	},
        	error:function(){
        		alert("失敗,未呼叫到chatRoom.do");
        	}
        });
        
    });
    
    //當我在《名單》按下退出群組的按鈕時：
    $("body").on("click","button[name='delMemFromCR_btn']",function(e){
    	var temMemId = $(this).prev().children("input[name='delMemId']").val();
    	var myleaveCR = $(this).prev().children("input[name='delfromCR']").val();
    	$.ajax({
    		url:"/CA102G4/chatRoom.do",
    		type:"POST",
        	data:$(this).prev().serialize(),
        	success:function(data){
        		console.log(data);
        		if(data == 0){
        			alert("發生錯誤：未成功刪除");
        		}else if(data == 1){
        			/**當刪除成功的時候，但刪到的是我自己時。**/
        			if(loginMemId_Now.trim() === temMemId.trim()){
        				window.setTimeout(function(){location.href=requestURLForJS},1);
        				sessionStorage.removeItem(myleaveCR); //從session移出關閉的聊天對話區塊
        			}
            		$("#chatRoomJoin_Modal").modal('hide');
        		}
        	},
        	error:function(){
        		alert("失敗,未呼叫到chatRoom.do");
        	}
	
    	});

    });
    //當我在《加入更多人》視窗，按下確認的按鈕時：  傳值格式
    $("body").on("click","button[name='AddMoreMem_btn']",function(e){

    	/**再送出加入更多人的名單時，先比對是否有選擇人員**/
		 if($("#chat_AddMoreFri_Modal input:checked").length == 0 ){
			  alert("請選擇人員");
			  $("#search_addMoreFri").css("border-color","red");
			  return;
		 }
    	
    	//拿到加入更多人視窗的div value給input傳給後端
    	$("input[name='addMoreFri_CRId']").val($("#chat_AddMoreFri_Modal").attr("value"));

    	$.ajax({
    		url:"/CA102G4/chatRoom.do",
    		type:"POST",
        	data:$(this).parent().parent().parent().serialize(),
        	success:function(data){
        		console.log(data);
        	  if(data.trim() === "新增完成"){
        		  alert(data);
	        	  //完成加入更多人後，要將原搜尋方塊寫的字清空，並將modal關掉	
	  			  $("#search_addMoreFri").val("");
				  $("#chat_AddMoreFri_Modal").modal('hide');
				  window.setTimeout(function(){location.href=requestURLForJS},1);
        	  }else{
        		  alert(data);
        	  }
        	},
        	error:function(){
        		alert("失敗,未呼叫到chatRoom.do");
        	}
	
    	});

    });
    
    

    
    //《聊天對話小視窗》中的搜尋好友
    $("#search_Fri").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("div.chatContext>ul.list-group > li.list-group-item").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    //《建立聊天對話中》的搜尋好友清單
    $("#search_Fri_modal").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#chat_AddFri_Modal .modal-body div.item").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
    
    //《加入更多人》的搜尋好友清單
    $("#search_addMoreFri").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#chat_AddMoreFri_Modal .modal-body div.item").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    //建立聊天對話選取好友的效果
    $("input[type='checkbox']").on("change", function() {
        if(this.checked){
            var img_src=$(this).next().children("img").attr('src');
            var fri_name=$(this).next().children("span").text();
            var str="<div class='item'><img class='ui avatar image' src='"+img_src+"'><span class='content'>"+fri_name+"</span></div>";
            $("#select_FriList").append(str);
        }else{
            var img_src=$(this).next().children("img").attr('src');
            $("#select_FriList img[src='"+img_src+"']").parent().remove();     
        }
    });

    //建立聊天對話視窗後，取消時都會把以勾選的checkboix設定未選擇狀況及輸入盒清空
    $(".modal-footer>button[data-dismiss='modal']").click(function(){
    	 $("#search_Fri_modal").val("");
         $("input[type='checkbox']").removeAttr("checked");
         $("#chatName").val("");
         $("#select_FriList").empty();
    });
    
  
    //************************聊天對話中，若有傳送圖片，點選圖片時會將照片放大***********************//
    $("body").on("click",".input_chatContext>div>span img",function(){
    	$("#chatRoomPhoto_Modal .modal-body > img").attr("src",$(this).attr("src"));
    	$("#chatRoomPhoto_Modal").modal({backdrop:'static'});
    });
    
    
    
    
});



//從聊天列表直接點選好友時，要先確認是否已有建立過一對一聊天對話? 有的話直接呼叫openChatRoom(chatRoom_id,chatRoom_Name)
//如果沒有的話，要先新增完後，再呼叫openChatRoom(chatRoom_id,chatRoom_Name)
function checkOneByOneCR(login_memId,click_friId,initCount){
	$.ajax({
		url:"/CA102G4/chatRoom.do",
		type:"POST",
    	data:{action:"checkOnebyOneCR",memId:login_memId,friId:click_friId,joinCnt:initCount},
    	success:function(data){
    		var tempArray =data.trim().split(",");
    		openChatRoom(tempArray[0],tempArray[1],'2');/*******補*************/
    	},
    	error:function(){
    		alert("失敗,未呼叫到chatRoom.do");
    	}

	});
}


/**再送出新增對話時，先比對是否有選擇人員且有設定聊天名稱,尚未確認為何放在JS不能跑??**/
function checkAddNewCR(){
	 if($("#chat_AddFri_Modal input:checked").length < 2 ){
		  alert("請選擇人員(至少兩位)");
		  $("#search_Fri_modal").css("border-color","red");
		  return false;
	 }else{
		  $("#search_Fri_modal").val("");
		  $("#select_FriList").empty();
		  $("#chat_AddFri_Modal").modal('toggle');
	 }	
	 return true;
}





//發送訊息時觸發
function sendMessage(e){
	var inputMessage;
	if(loginMemId_Now === ""){
		alert("錯誤：發出訊息，未取到會員ID");
		return;
	}
	//console.log(e.nodeName);
	if(e.nodeName == "INPUT"){
		inputMessage = $(e); //來自於keypress input
	}else{
		inputMessage = $(this).prev(); //來自於span btn
	}
	
	//console.log(inputMessage);
	var message = inputMessage.val().trim();
	//alert("準備送出的訊息為:"+message);
	
	if(message === ""){
		alert("未輸入訊息不能送出喔");
		inputMessage.focus();
	}else{
		var date = new Date();
		var date_str = date.toJSON().substr(0,10);
		var time_str = date.toTimeString().substr(0,8);
		var to_chatRoomName="";
		var chatRoomID="";
		var type="chat";
		if(e.nodeName == "INPUT"){
			to_chatRoomName = $(e).parent().parent().prev().prev().text().trim();
			chatRoomID = $(e).parent().parent().parent().attr("id"); 
		}else{
			to_chatRoomName = $(this).parent().parent().prev().prev().text().trim();
			chatRoomID = $(this).parent().parent().parent().attr("id"); 
		}
		//存放----發給哪個聊天室?自己的會員ID?訊息內容?發送時間?
		var jsonObj ={
			"TYPE":type,
			"TO_CHATROOMID":chatRoomID,
			"TO_CRNAME":to_chatRoomName,
			"MEM_ID":loginMemId_Now,
			"MSG":message,
			"TIME":date_str+" "+time_str
		};
		webSocket.send(JSON.stringify(jsonObj));
		inputMessage.val("");
		inputMessage.focus();
	}
}


//************************為了拖拉圖片才新增物件 STEP1**************************//
function createNode(){
	var file_upload = new vjUI_fileUpload({"dragInBox":".input_chatContext",
		   "loadCallback":function(files,cr_id,cr_name){
			var date = new Date();
			var date_str = date.toJSON().substr(0,10);
			var time_str = date.toTimeString().substr(0,8);
			
			var n=files.length;

			for(var i=0;i<n;i++){
				var jsonObj ={
						"TYPE":"chat",
						"TO_CHATROOMID":cr_id,
						"TO_CRNAME":cr_name,
						"MEM_ID":loginMemId_Now,
						"MSG":"<img src='"+files[i]+"' style='width:100px;height:auto'>",
						"TIME":date_str+" "+time_str
				 };
				 webSocket.send(JSON.stringify(jsonObj));
			 }
			
			 file_upload.clearFiles();
		}
	});

}

//************************為了拖拉圖片才新增物件 STEP2**************************//
$(document).ready(function () {
	//先創建
	createNode();
	
	//create an observer instance (透過這個去監聽body是否有變動DOM元素，若有變動將再次創建，裡面在註冊指定區塊事件)
	var observer = new MutationObserver(function (mutations) {
	    mutations.forEach(function (mutation) {
	       createNode();
	    });
	});
	
	// configuration of the observer:
	var config = {
	    attributes: true,
	    childList: true,
	    characterData: true
	};
	var targetNode = document.body;
	// pass in the target node, as well as the observer options
	observer.observe(targetNode, config);

});













