/********Need JQuery******/
if(!vj_tmp_object){
	var vj_tmp_object=0;
}

(function(){
	window.vjUI_fileUpload=vjUI_fileUpload;
	var self="";
	var core="";
	function vjUI_fileUpload(ob){
		this.css=Array();
		this.css.dragEnter={"border":"1px solid blue","background":"#efefef"};
		this.css.dragOut={"border":"1px solid #a00","background":"white"};
		this.loadCallback=function(){};//callback function
		this.loadFileing=0;//是否正在載入user圖片
		this.userUploadNum=0;//user拖拉進的檔案數
		this.loadSuccess=0;//載入成功的檔案數
		this.files=Array();//目前user給的圖片有那些
		this.dragInBox="document.body";
		if(window.vj_core){
			core=new vj_core({"level":1});
			this.debug=function(data,level){core.debug(data,level);};
		}
		else{
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
	
	/*****清除目前的檔案數***/
	o.clearFiles=function(){
		this.files=Array();
	}
	
	//滑鼠移上去
	o.hover=function(obj){
		$(obj).css(self.css.dragEnter);
	};
	
	//滑鼠移開
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
	
	/*****拉圖片進來***/
	o.dragEnter=function(event){
		self.debug("dragEnter");
		self.hover(this);
		event.stopPropagation();
		event.preventDefault();
		
	};
	
	//離開
	o.dragLeave=function(event){
		self.debug("drag move",3);
		self.unhover(this);
		event.stopPropagation();
		event.preventDefault();
	};
	
	/*****/
	
	/*drag and move*/
	o.dragOver=function(event){
		self.debug("drag move",3);
	
		
		event.stopPropagation();
		event.preventDefault();
	};
	
	
	/*drop*/
	o.dragDrop=function(event){
		event.stopPropagation();
		event.preventDefault(); 
		self.getLocalFiles(event);
		self.unhover(this);
	};
	
	/*取得檔案base64 code 並存在array*/
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
				self.loadCallback(self.files);
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
			self.loadCallback(self.files);
		}
	};
	
	/*選完檔案自動上傳*/
	o.fileSelectedAjaxUpload=function(ob,event){
		var file=ob.files[0];
		this.ajaxUpload(file);
	};
	o.ajaxUpload=function(file){
		if(!file){
			alert("檔案不存在");
			return "";
		}
		var vFD = new FormData();
		vFD.append("author", "Jackie");
		vFD.append("fileName",file);
		
		var oXHR = new XMLHttpRequest();
		oXHR.upload.addEventListener('progress', this.fileUploadProgress, false);
		oXHR.addEventListener('load', this.fileUploadComplete, false);
		oXHR.open('POST', this.uploadUrl);
	    oXHR.send(vFD);
	};
	
	o.fileUploadProgress=function(event){
		
		var obj=$("#"+self.progressBox);
		if (event.lengthComputable) {
			var percentComplete = Math.round(event.loaded * 100 / event.total);
			$(obj).html(percentComplete.toString() + '%');
        }
        else {
			$(obj).html('unable to compute');
        }
	};
	
	o.fileUploadComplete=function(event){
		$("#"+self.progressBox).html('complete');
	};
	
	o.fileUploadFailed=function(){};
	
	o.fileUploadCanceled=function(){
		
		
	};

}());