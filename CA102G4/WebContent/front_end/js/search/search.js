$(document).ready(function() {

	var appendNumber = 4;
	var prependNumber = 1;
	var swiper = new Swiper('.swiper-container', {
		slidesPerView : 3,
		centeredSlides : false,
		spaceBetween : 30,
		navigation : {
			nextEl : '.fas.fa-chevron-right',
			prevEl : '.fas.fa-chevron-left',
		},
	});

	// $(".item").click(function () {
	// if (!$(this).hasClass('dropdown browse')) {
	// $(this)
	// .addClass('active')
	// .closest('.ui.menu')
	// .find('.item')
	// .not($(this))
	// .removeClass('active');
	// }
	// });

	var swiper2 = new Swiper('.swiper-container2', {
		effect : 'cube',
		grabCursor : true,
		cubeEffect : {
			shadow : true,
			slideShadows : true,
			shadowOffset : 20,
			shadowScale : 0.94,
		},
		pagination : {
			el : '.swiper-pagination',
			clickable : true,
		},
	});

});

	$(window).load(function() {
		$(".swiper-wrapper.userSwiper-wrapper").css({
			transitionProperty : "transform"
		});
		$(".swiper-slide.swiperCard").css({
			transitionProperty : "transform"
		});

		$(".submitKeyword").click(function(){
			$(".keywordForm").submit();
		});
		
		$(function(){
		    var len = 100; // 超過100個字以"..."取代
		    $(".userInfoDescription").each(function(i){
		        if($(this).text().length>len){
		            $(this).attr("title",$(this).text());
		            var text=$(this).text().substring(0,len-1)+"...";
		            $(this).text(text);
		        }
		    });
		});

		$(function(){
		    var len = 200; // 超過100個字以"..."取代
		    $(".descriptionBlogContent").each(function(i){
		        if($(this).text().length>len){
		            $(this).attr("title",$(this).text());
		            var text=$(this).text().substring(0,len-1)+"...";
		            $(this).text(text);
		        }
		    });
		});
		
		$(function(){
		    var len = 200; // 超過200個字以"..."取代
		    $(".memberExtra").each(function(i){
		        if($(this).text().length>len){
		            $(this).attr("title",$(this).text());
		            var text=$(this).text().substring(0,len-1)+"...";
		            $(this).text(text);
		        }
		    });
		});
		
		$(function(){
		    var len = 200; // 超過200個字以"..."取代
		    $(".attractionsExtra").each(function(i){
		        if($(this).text().length>len){
		            $(this).attr("title",$(this).text());
		            var text=$(this).text().substring(0,len-1)+"...";
		            $(this).text(text);
		        }
		    });
		});
		
		$(function(){
			var len = 200; // 超過200個字以"..."取代
			$(".descriptionTogetherContent").each(function(i){
				if($(this).text().length>len){
					$(this).attr("title",$(this).text());
					var text=$(this).text().substring(0,len-1)+"...";
					$(this).text(text);
				}
			});
		});
});
