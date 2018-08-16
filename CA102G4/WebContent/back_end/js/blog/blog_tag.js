$(document).ready(function () {
	
	$("#updateTagDialog").css('overflow', 'hidden');
	
	$(".updateTagBtn").click(function(){
		$("p.btn_id").html($(this).parentsUntil("tbody").find("p.tagID").html());
		$("#btn_id").val($(this).parentsUntil("tbody").find("p[class=tagID]").html());
		$("#btn_class").val($(this).parentsUntil("tbody").find("p[class=tagClass]").html());
		$("#btn_name").val($(this).parentsUntil("tbody").find("p[class=tagName]").html());
		$("#updateTagDialog").dialog("open");
	});
	
	   $("#updateTagDialog").dialog({
	       autoOpen: false,
	       closeOnEscape: false,
	       hide: 'blind',
	       show: 'blind',
	       title: '訊息',
	       modal: true,
	       width: 350,
	       height: 310,
	       autofocus: false,
	       resizable: false,
	       draggable: false,
	       buttons: {
	           "確定": function () {
	           	$(this).dialog("close");
	           	$(".ui.updateContent.form").submit();
	           },
	           "取消": function () {
	               $(this).dialog("close");
	           }
	       }
	   });
	   
	   $(".btn.btn-danger").click(function(){
		   $("#deleteTagData").dialog("open");
		   i = $(this).parent();
	   });

	   $("#deleteTagData").dialog({
			 autoOpen: false,
			 closeOnEscape: false,
			 hide: 'blind',
			 show: 'blind',
			 title: '訊息',
			 modal: true,
			 width: '250px',
			 autofocus: false,
			 resizable: false,
			 draggable: false,
			 buttons: {
			     "確定": function () {
			      $(this).dialog("close");
			      i.submit();
			      },
			      "取消": function () {
			          $(this).dialog("close");
			      }
			 }
		});
	   
	   $(".insertTagBtn").click(function(){
			$("#insertTagDialog").dialog("open");
	   });
	   $("#insertTagDialog").dialog({
	       autoOpen: false,
	       closeOnEscape: false,
	       hide: 'blind',
	       show: 'blind',
	       title: '訊息',
	       modal: true,
	       width: 350,
	       height: 310,
	       autofocus: false,
	       resizable: false,
	       draggable: false,
	       buttons: {
	           "確定": function () {
	           	$(this).dialog("close");
	           	$(".ui.insertContent.form").submit();
	           },
	           "取消": function () {
	               $(this).dialog("close");
	               $("input[name=blogTagNameClass]").val("");
	               $("input[name=blogTagName]").val("");
	           }
	       }
	   });

});