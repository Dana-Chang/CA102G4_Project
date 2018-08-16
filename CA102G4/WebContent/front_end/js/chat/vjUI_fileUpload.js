/********Need JQuery******/
if(!vj_tmp_object){
	var vj_tmp_object=0;
}

(function(){
	window.vjUI_fileUpload = vjUI_fileUpload ;
	//提供給：拖拉到指定位置時，當下聊天對話的編號
	var dragCR_Id="";
	//提供給：拖拉到指定位置時，當下聊天對話的名稱
	var dragCR_Name="";
	
	var self="";
	var core="";
	
	function vjUI_fileUpload(ob){
		this.css=Array();
		this.css.dragEnter={"background-color":"#E0E0E0"};//拖拉進去指定區塊時，背景變成灰色
		this.css.dragOut={"background-color":"white"};    //拖拉離開指定區塊時，背景變成白色
		this.loadCallback=function(){};					  //callback function
		this.loadFileing=0;								  //是否正在載入user圖片
		this.userUploadNum=0;							  //user拖拉進的檔案數
		this.loadSuccess=0;								  //載入成功的檔案數
		this.files=Array();								  //目前user給的圖片有那些
		this.dragInBox="document.body";
		if(window.vj_core){
			core=new vj_core({"level":1});
			this.debug=function(data,level){core.debug(data,level);};
		}else{
			this.debug=function(){return false;}
		}
		for(pro in ob){
			this[pro]=ob[pro];
		}
	
		if(!vj_tmp_object){vj_tmp_object=0;}
		vj_tmp_object++;
		eval('vj_tmp_object'+vj_tmp_object+'=this;');
		this.vbName='vj_tmp_object'+vj_tmp_object;
		self=this;
		setTimeout(this.vbName+".init()",10);
	}
	
	
	var o=vjUI_fileUpload.prototype;
	
	o.init=function(){
		if(self.dragInBox){
			var d=self.dragInBox.split(/[,\s]/);
			var n=d.length;
			for(var i=0;i<n;i++){
				self.attachEvent($(d[i]));
			}
		}
	}
	
	/*****************清除目前的檔案數****************/
	o.clearFiles=function(){
		this.files=Array();
	}
	
	/******拖拉《移到指定區塊》時，改變CSS：背景變灰色*******/
	o.hover=function(obj){
		$(obj).css(self.css.dragEnter);
	};
	
	/******拖拉《移出指定區塊》時，改變CSS：背景變回白色*****/
	o.unhover=function(obj){
		$(obj).css(self.css.dragOut);
	}
	
	//對tag 綁事件
	o.attachEvent=function(obj){
		if(!document.addEventListener){
			return false;
		}
		//$(obj).bind("dragenter",self.dragEnter);
		$(obj).bind("dragover",self.dragOver);
		$(obj).bind("dragleave",self.dragLeave);
		//$(obj).bind("drop",self.dragDrop); //error
		
		$(obj).each(function(){
			$(this).get(0).addEventListener("dragenter", self.dragEnter, false)
			$(this).get(0).addEventListener("drop", self.dragDrop, false)
		});
	};
	

	/********拖拉進入指定區塊時觸發drop：開始**********/
	o.dragEnter=function(event){
		self.debug("dragEnter");
		self.hover(this);
		event.stopPropagation();
		event.preventDefault();
	};
	/********拖拉進入指定區塊時觸發drop：結束**********/
	
	
	/********拖拉離開指定區塊時觸發drop：開始**********/
	o.dragLeave=function(event){
		self.debug("drag move",3);
		self.unhover(this);
		event.stopPropagation();
		event.preventDefault();
	};
	/********拖拉離開指定區塊時觸發drop：結束**********/
	
	
	/********拖拉滑過指定區塊時觸發drop：開始**********/
	o.dragOver=function(event){
		self.debug("drag move",3);
		event.stopPropagation();
		event.preventDefault();
	};
	
	
	/********拖拉放掉時觸發drop：開始**********/
	o.dragDrop=function(event){
		event.stopPropagation();
		event.preventDefault();
		//取得拖拉進的該聊天室編號及名稱
		dragCR_Id = $(this).parent().attr("id");
		dragCR_Name =$(this).prev().text().trim();

		//呼叫第137行，取得user拉進來的圖片
		self.getLocalFiles(event);
		//設定離開時CSS的樣式
		self.unhover(this);
	};
	/********拖拉放掉時觸發drop：結束**********/
	
	
	/****取得檔案base64 code 並存在array*****/
	o.setFile=function(file_base64){
		var n=this.files.length;
		for(var i=0;i<n;i++){
			if(file_base64==this.files[i]){
				return false;
			}
		}
		this.files[n]=file_base64;
	};
	
	/***取得user 拉進來的圖片 multiple ***/
	o.getLocalFiles=function(event){
		var dt = event.dataTransfer;
		var files = dt.files;
		var n = files.length;
		self.userUploadNum=n;
		self.loadSuccess=0;
		self.loadFileing=1;
		for (var i = 0; i < n; i++) {
			try {
				var file = files[i];
				self.debug(file);
				self.getFile(file);
			} catch (e) {
				self.debug(e.message);
			}
		}
	};
	
	/*****取得user 拉進來的圖片 single*****/
	o.getFile=function(file){
		var fileName=file.name;
		var fileSize=file.size ;
		var type=file.type;
		//var slice=file.mozSlice(0,1);//切割檔案從 start~end , mozilla才有
		//webkitSlice 
		//var fullPath=file.mozFullPath;
		//webkitRelativePath
		
		var reader = new FileReader();
		reader.onerror = function(evt) {
			var message;
			// REF: http://www.w3.org/TR/FileAPI/#ErrorDescriptions
			switch(evt.target.error.code) {
				case 1:
					message = file.name + " not found.";
					break;
					
				case 2:
					message = file.name + " has changed on disk, please re-try.";
					break;
					
				case 3:
					messsage = "Upload cancelled.";
					break;
					
				case 4:
					message = "Cannot read " + file.name + ".";
					break;
					
				case 5:
					message = "File too large for browser to upload.";
					break;
			}
			self.userUploadNum--;
			self.debug("error:"+message);
			
			if(self.userUploadNum==self.loadSuccess){//已載入完畢
				self.loadCallback(self.files,dragCR_Id,dragCR_Name);///************我捕的IDNAME
			}
		}
		
		/****載入 reader 並預覽***/
		reader.onloadend = self.fileOnLoadend;
		reader.readAsDataURL(file);
	};
	
	o.fileOnLoadend=function(event){
		var data = event.target.result;
		var base64StartIndex = data.indexOf(',') + 1;
		if(base64StartIndex < data.length) {
			self.setFile(data);
		}
		self.loadSuccess++;
		if(self.userUploadNum==self.loadSuccess){//已載入完畢
			//*********************補的IDNAME***************************//
			self.loadCallback(self.files,dragCR_Id,dragCR_Name);
		}
	};
	
	/*選完檔案自動上傳*/
//	o.fileSelectedAjaxUpload=function(ob,event){
//		var file=ob.files[0];
//		this.ajaxUpload(file);
//	};
//	o.ajaxUpload=function(file){
//		if(!file){
//			alert("檔案不存在");
//			return "";
//		}
//		var vFD = new FormData();
//		vFD.append("author", "Jackie");
//		vFD.append("fileName",file);
//		
//		var oXHR = new XMLHttpRequest();
//		oXHR.upload.addEventListener('progress', this.fileUploadProgress, false);
//		oXHR.addEventListener('load', this.fileUploadComplete, false);
//		oXHR.open('POST', this.uploadUrl);
//	    oXHR.send(vFD);
//	};
	
//	o.fileUploadProgress=function(event){
//		
//		var obj=$("#"+self.progressBox);
//		if (event.lengthComputable) {
//			var percentComplete = Math.round(event.loaded * 100 / event.total);
//			$(obj).html(percentComplete.toString() + '%');
//        }
//        else {
//			$(obj).html('unable to compute');
//        }
//	};
//	
//	o.fileUploadComplete=function(event){
//		$("#"+self.progressBox).html('complete');
//	};
	
	o.fileUploadFailed=function(){};
	
	o.fileUploadCanceled=function(){
		
		
	};

}());