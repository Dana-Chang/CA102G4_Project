$(document).ready(function () {
	
	$("#delete").dialog({
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
	
    $(".delete.form").click(function () {
        $("#delete").dialog("open");
        i = $(this);
    });
    
    $(".btn.mx-2.editBlog").click(function(){
    	$(this).parent().submit();
    });
        
    $(document).ready(function () {
        $(".btn.mx-2.editBlog").click(function(event){
        	event.preventDefault();
        });
        
       $(".btn.mx-2.delBlog").click(function(event){
        	event.preventDefault();
       });
    });
});