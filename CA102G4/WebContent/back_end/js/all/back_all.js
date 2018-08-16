$(document).ready(function () {
    //點選側邊欄按鈕時，會調整siderbar及Content CSS，達到收合效果。
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
        $('#content').toggleClass('active');
    });
    
    //點選其他地方時，會讓sidebar的子選單隱藏起來。
    $('*').click(function(){
        $(".dropdown .dropdown-menu").collapse('hide');
    });
    
});