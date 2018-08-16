
$(document).ready(function () {
	

$(".fas.fa-exclamation-triangle").click(function () {
        $("#reportPicDialog").dialog("open");
    });
    
    $("#reportPicDialog").dialog({
        autoOpen: false,
        closeOnEscape: false,
        hide: 'blind',
        show: 'blind',
        title: '訊息',
        modal: true,
        width: 500,
        height: 300,
        autofocus: false,
        resizable: false,
        draggable: false,
        buttons: {
            "確定": function () {
            	$(this).dialog("close");
            	$(".ui.reportMessage.form").submit();
            },
            "取消": function () {
                $(this).dialog("close");
                $(".reportReasonContent").val("");
            }
        }
    });
    
    $("#reportPicDialog2").dialog({
        autoOpen: true,
        closeOnEscape: false,
        hide: 'blind',
        show: 'blind',
        title: '訊息',
        modal: true,
        width: 220,
        autofocus: false,
        resizable: false,
        draggable: false,
        buttons: {
            "確定": function () {
            	$(this).dialog("close");
            }
        }
    });
    
});