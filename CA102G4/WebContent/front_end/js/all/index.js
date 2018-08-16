$(document).ready(function () {
	
	$(function(){
	    var len = 100; // 超過100個字以"..."取代
	    $(".news-grid-info-bottom-text-content").each(function(i){
	        if($(this).text().length>len){
	            $(this).attr("title",$(this).text());
	            var text=$(this).text().substring(0,len-1)+"...";
	            $(this).text(text);
	        }
	    });
	});
	
});