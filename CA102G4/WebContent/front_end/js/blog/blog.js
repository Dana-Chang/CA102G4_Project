$(document).ready(function () {

//    $("body > div.ui.container > div > a > div > div > img").error(function () {
//        $(this).attr("src", "../CA102G4/front_end/images/all/null.png");
//    });

    $('[data-content="建立文章遊記"]').popup();

//        $(".itemRight").click(function () {
//            if (!$(this).hasClass('dropdown browse')) {
//                $(this)
//                    .addClass('active')
//                    .closest('.ui.menu')
//                    .find('.itemRight')
//                    .not($(this))
//                    .removeClass('active');
//            }
//        });

    $(".ui.basic.floating.dropdown.button").one("click", function () {
        $(this).dropdown("toggle");
    })
    
});
