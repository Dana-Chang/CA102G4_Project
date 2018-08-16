$(document).ready(function () {
	
	$(function(){
	    var len = 27; // 超過27個字以"..."取代
	    $(".br_reason").each(function(i){
	        if($(this).text().length>len){
	            $(this).attr("title",$(this).text());
	            var text=$(this).text().substring(0,len-1)+"...";
	            $(this).text(text);
	        }
	    });
	});
	   
	$(function(){
	    var len = 27; // 超過27個字以"..."取代
	    $(".bmr_reason").each(function(i){
	        if($(this).text().length>len){
	            $(this).attr("title",$(this).text());
	            var text=$(this).text().substring(0,len-1)+"...";
	            $(this).text(text);
	        }
	    });
	});
	
	$(function(){
	    var len = 27; // 超過27個字以"..."取代
	    $("p.mem_id").each(function(i){
	        if($(this).text().length>len){
	            $(this).attr("title",$(this).text());
	            var text=$(this).text().substring(0,len-1)+"...";
	            $(this).text(text);
	        }
	    });
	});
	
	$(".updateReportStatusBtn").click(function(){
		$("#blogReportDialogMemID").val($(this).parentsUntil("tbody").find(".mem_id").html());
		$("#blogReportDialogBlogID").val($(this).parentsUntil("tbody").find(".blog_id").html());
		$("#blogOwner").val($(this).parentsUntil("tbody").find("input[name=hidden_blogOwner]").val());
		$(".blogReportDialogContentBlogID").html($(this).parentsUntil("tbody").find(".blog_id").html());
		$(".blogReportDialogContentBrStatus").html($(this).parentsUntil("tbody").find(".br_status").html());
		$(".blogReportDialogContentBrReason").html($(this).parentsUntil("tbody").find("input[name=hidden_blog_reason]").val());
		$("#blogReportManageDialog").dialog("open");
	});
	
	$("#blogReportManageDialog").dialog({
	       autoOpen: false,
	       closeOnEscape: false,
	       hide: 'blind',
	       show: 'blind',
	       title: '審核視窗',
	       modal: true,
	       width: 600,
//	       height: 500,
	       autofocus: false,
	       resizable: false,
	       draggable: false,
	       buttons: {
	           "送出": function () {
	           	$(this).dialog("close");
	           	sendblogReportResult();
	           	$(".blogReportManageDialogForm").submit();
	           },
	           "返回": function () {
	               $(this).dialog("close");
	               $(".ui.selection.dropdown").find("div.text").addClass("default");
	               $("div.text.default").html("請審核該檢舉是否成功");
	           }
	       }
	   });
	
	$(".updateReportMessageStatusBtn").click(function(){
		$("#blogMessageReportDialogMemID").val($(this).parentsUntil("tbody").find(".mem_id").html());
		$("#blogMessageReportDialogMessage_id").val($(this).parentsUntil("tbody").find(".message_id").html());
		$("#messageOwner").val($(this).parentsUntil("tbody").find("input[name=hidden_mem_id]").val());
		$(".blogMessageReportDialogContentMessageID").html($(this).parentsUntil("tbody").find(".message_id").html());
		$(".blogMessageReportDialogContentBmrStatus").html($(this).parentsUntil("tbody").find(".bmr_status").html());
		$(".blogMessageReportDialogContentBmrReason").html($(this).parentsUntil("tbody").find("input[name=hidden_reason]").val());
		$(".blogMessageReportDialogContentBlogMessage").html($(this).parentsUntil("tbody").find("input[name=hidden_message]").val());
		$("#blogMessageReportManageDialog").dialog("open");
	});
	
	$("#blogMessageReportManageDialog").dialog({
	       autoOpen: false,
	       closeOnEscape: false,
	       hide: 'blind',
	       show: 'blind',
	       title: '審核視窗',
	       modal: true,
	       width: 600,
	       autofocus: false,
	       resizable: false,
	       draggable: false,
	       buttons: {
	           "送出": function () {
	           	$(this).dialog("close");
	           	sendblogMessageReportResult();
	           	$(".blogMessageReportManageDialogForm").submit();
	           },
	           "返回": function () {
	               $(this).dialog("close");
	               $(".ui.selection.dropdown").find("div.text").addClass("default");
	               $("div.text.default").html("請審核該檢舉是否成功");
	           }
	       }
	   });
	
	   
    $(function () {
        $('.ui.dropdown').dropdown({
            transition: 'horizontal flip',
            duration: 800,
        });
    });
    
    /* 取消AutoFocus */
    $.ui.dialog.prototype._focusTabbable = function () {};
    /* //取消AutoFocus */
    
    $(".item").click(function(){
    	$("#blogReportDialogBrStatus").val($(this).attr("value"));
    	$("#blogMessageReportDialogBmrStatus").val($(this).attr("value"));
    });
    
});