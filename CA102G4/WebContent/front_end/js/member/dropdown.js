$(document).ready(function(){
  
$(".has_children").click(function(){
  $(this).addClass("highlight")
  .children("#dropdown_item").show().end()
  
  .siblings().removeClass("highlight")
  .children("#dropdown_item").hide(); 
  
});

});