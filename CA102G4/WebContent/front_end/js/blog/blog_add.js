$(document).ready(function () {

    $("#submitButton").popup();
    
    $("#myBtn").popup();
        
    var scrollCheckTimer = null,
    scrollDelay = 250;  
    
    $(window).scroll(function () {
    	
    	clearTimeout(scrollCheckTimer); 
    	
    	scrollCheckTimer = setTimeout(function(){
            if (document.body.scrollTop > 30 || document.documentElement.scrollTop > 30) {
            	$("#myBtn").css("display","block");
                $("#myBtn").animate({top:'441px'});
            }else{
            	$("#myBtn").animate({top:'1000px'});
            }
        } , scrollDelay );

    });

    $("#myBtn").click(function () {
        $('html,body').animate({
            scrollTop: 0
        }, 'slow');
    });

    $(".fa-angle-up").click(function () {
        $(".fa-angle-up").hide();
        $(".fa-angle-down").show();
        $(".contentTableDiv").slideToggle(400);
    });

    $(".fa-angle-down").click(function () {
        $(".fa-angle-down").hide();
        $(".fa-angle-up").show();
        $(".contentTableDiv").slideToggle(400);
    });

    //    $("#loadItineraryList").append("<div class='ItineraryListModalContent' id='model'><div class='ui selection dropdown'><input type='hidden' name='ItineraryList'><i class='dropdown icon'></i><div class='default text'>選擇您的行程</div><div class='menu'><div class='item' data-value='1'>黃世銘的韓國行程(5天)</div><div class='item' data-value='2'>黃世銘的日本行程(7天)</div><div class='item' data-value='3'>黃世銘的紐約行程(20天)</div><div class='item' data-value='4'>黃世銘的宿霧行程(7天)</div><div class='item' data-value='5'>黃世銘的泰國行程(6天)</div></div></div><input type='submit' value='確定' onclick='alert(4)'></div>")

    $(function () {
        $('.ui.dropdown').dropdown({
            transition: 'horizontal flip',
            duration: 800,
        });
    });
    /* 取消AutoFocus */
    $.ui.dialog.prototype._focusTabbable = function () {};
    /* //取消AutoFocus */
    $("#loadItineraryList").dialog({
        autoOpen: false,
        closeOnEscape: false,
        hide: 'blind',
        show: 'blind',
        title: '選擇插入的行程',
        modal: true,
        width: 'auto',
        autofocus: false,
        resizable: false,
        draggable: false,
        buttons: {
            "確定": function () {
//                if ($(".default.text").html() != '選擇您的行程') {
//                    $(".addItinerary>span").html($(".ItineraryInput").val());
//                    $(".loadItineraryTitle").empty();
//                    $(".loadItineraryContent").empty();
                	$("#form2").submit();
                    $(this).dialog("close");
//                } else {
//                    $(this).dialog("close");
//                }
            },
            "取消": function () {
                $(this).dialog("close");
            }
        }
    });

    $("#warning").dialog({
        autoOpen: true,
        closeOnEscape: false,
        hide: 'blind',
        show: 'blind',
        title: '錯誤訊息',
        modal: true,
        width: 'auto',
        autofocus: false,
        resizable: false,
        draggable: false,
        buttons: {
            "確定": function () {
            	$(this).dialog("close");
            }
        }
    });
    
    $("#loadItinerary").click(function () {
        $("#loadItineraryList").dialog("open");
    });

    $(".loadItineraryListSubmit").click(function () {
        $("#loadItineraryList").dialog("colse");

    });

    $(".tag").dialog({
        autoOpen: false,
        closeOnEscape: false,
        hide: 'blind',
        show: 'blind',
        title: '添加標籤',
        modal: true,
        width: 1200,
        height: 800,
        autofocus: false,
        resizable: false,
        draggable: false,
        buttons: {
            "繼續撰寫": function () {
            	$(this).dialog("close");
            },
            "發佈文章": function () {
                $(this).dialog("close");
            }
        }
    });
        
    $('.max.example .ui.normal.dropdown')
    .dropdown({
      maxSelections: 3
    });
    
    $("#submitButton").click(function(){
    	$("form[name=form2]").submit();
    });
    
    $(".item").click(function(){
    	$("#trip_no").val($(this).attr("value"));
    	$("#actionID").val("loadSelectedTrip");
    });
});
