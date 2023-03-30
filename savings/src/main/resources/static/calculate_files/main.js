// js slider home
$(window).bind("load", function() {
    $('.slider .inner-slider').owlCarousel({
        loop:false,
        margin:10,
        nav:false,
        items:1
    })
 });

 $(window).bind("load", function() {
    $('.inner-tab-tg').owlCarousel({
        loop:true,
        margin:10,
        responsiveClass:true,
        dots:false,
        nav:false,
        autoplay:true,
        autoplayTimeout:3000,
        autoplayHoverPause:true,
        responsive:{
            0:{
                items:1
            },
            600:{
                items:2
            },
            768:{
                items:2
            },
            1024:{
                items:3
            }
            ,
            1200:{
                items:4
            }
        }
    })
 });
 $(window).bind("load", function() {
    if(window.innerWidth > 480 ){
        $('.tab-content .inner-tab:not(.inner-mang-luoi) ').owlCarousel({
            loop:true,
            margin: 10,
            responsiveClass:true,
            dots:false,
            nav:true,
            autoplay:true,
            autoplayTimeout:3000,
            autoplayHoverPause:true,
            responsive:{
                0:{
                    items:1
                },
                480:{
                    items:2,
                    slideBy: 2,
                    nav:true,
                    autoplay:false,
                },
                768:{
                    items:3, 
                    slideBy: 3,
                    nav:true,
                    autoplay:false,
                },
                1024:{
                    items:3,
                    slideBy: 3,
                    nav:true,
                }
            }
        })
    }
 });
 
//
 $(document).ready( function() {
    if(window.innerWidth > 480 ){
        $('.news-home .inner-tab').addClass( 'owl-carousel' );
    }
  } );

 // Tab home

 $('#nav-tab a').on('click', function (e) {
    e.preventDefault()
    $(this).tab('show')
})

// List category

$(window).bind('load', function(){
    $(".multi-cat li.has-child i").on('click', function(){
        $(this).siblings(".sub-cat").stop().toggle(200);
        $(this).parents().toggleClass('active');
    });

    $(".sub-cat li.has-child i").on('click', function(){
        $(this).siblings(".sub-cat-child").toggle(200);
    });

    $('li.has-child i').click( function(){
        if ( $(this).hasClass('active') ) {
            $(this).removeClass('active');
        } else {
            $('li.has-child i.active').removeClass('active');
            $(this).addClass('active');    
        }
    });
    
    // the 
    $('.multi-the li.has-child').click(function(){
        $('.multi-the li.has-child').removeClass('open');
        $(this).addClass('open');
    });

});

//   =======================

$(document).ready(function() {
    if(window.innerWidth > 1200){
        $(".menu-item").hover(
            function() {
                $(this).children('.mega-menu').stop(true, true).addClass('active');
            },
            function() {
                $(this).children('.mega-menu').stop(true, true).removeClass('active');
            }
        );
        $(".menu-mega-item").hover(
            function() {
                $(this).children('.animate-menu').stop(true, true).addClass('active');
            },
            function() {
                $(this).children('.animate-menu').stop(true, true).removeClass('active');
            }
        );
    }
    $('.show-search').on('click', function(){
        $('.form-search').stop().slideToggle(0);
    });
    $('.btn-toggle-menu').on('click', function(){
        $(this).siblings('div').stop().slideToggle(100);
        $(this).siblings('a').find('i').toggleClass('rotate').stop();
        $(this).stop().toggleClass('active');
    });

	$('.close-menu').click(function(){
        $('.overlay-mobile').hide(100);
        $('.menu-wrap').removeClass('active');
    });
    $('.overlay-mobile').click(function(){
        $(this).hide(100);
        $('.menu-wrap').removeClass('active');
    });
    $('.menu-bar').click(function(){
        $('.menu-wrap').addClass('active');
        $('.overlay-mobile').show(200);
    });

    $('.btn-log').click(function(){
        $('.log-vcb ul').slideToggle(100);
    });
	$(".go-to-top").click(function(){
		$("html, body").animate({ scrollTop: 0 }, "slow");
	});
});

$(window).bind('load', function(){
    $('.go-to-top').click(function(){
        $("html, body").animate({scrollTop: 0}, 1000);
    });
    $('.expand-footer').click(function(){
        $('.toggle-footer').slideToggle(300);
        $(this).toggleClass('active');
    });
    $('.link-top .control a').on('click', function(){
        $(this).parents('.link-top').toggleClass('active');
    });
    $(window).bind('scroll', function(){
        $('.link-top').removeClass('active');
    })
});


// =====================
$(window).bind('load', function(){
    if(window.innerWidth < 1200){
        $('.box-link-icon .footer-link .footer-link-title').click(function(){
            $(this).siblings('.list-link').stop().slideToggle();
            $(this).toggleClass('active');
    
            $(this).parents('.col-12.col-md-6.col-xl-3').siblings().find('.list-link').hide();
            $(this).parents('.col-12.col-md-6.col-xl-3').siblings().find('.footer-link-title').removeClass('active');
        });
    }
	if(window.innerWidth <= 575 && $('.js-slider-cat').length > 0){
        $('.js-slider-cat').addClass(['owl-carousel']);
        $('.js-slider-cat').owlCarousel({
            loop:true,
            margin: 0,
            responsiveClass:true,
            dots:false,
            nav:true,
            autoplay:false,
            autoplayTimeout:3000,
            autoplayHoverPause:true,
            items: 1,
        })
    }
    if(window.innerWidth <= 1200 &&  ($('.category-parent').length > 0 || $('.category-child').length > 0) ){
        let widget_category =  '<aside><div class="widget widget-category switch-cat">' + $('.widget-category').html() + '</div></aside>';
        $('.widget-category').remove();
		//add class chuyen box
		$('.nha-dau-tu').parent().prepend('<div class="content-category no-content"></div>');
		$('.personal-content').parent().prepend('<div class="content-category no-content"></div>');
		$('.list-personal-content').parent().prepend('<div class="content-category no-content"></div>');
		$('.nav-atm').parent().prepend('<div class="content-category no-content"></div>');
		$('.box-tab-details').parent().prepend('<div class="content-category no-content"></div>');
		$('.cong-cu-tinh-toan').parent().prepend('<div class="content-category no-content"></div>');
		
        $('.content-category').addClass(['d-flex', 'flex-column'])
        $('.content-category').prepend(widget_category);
        $('.switch-cat .widget-title').click(function(){
            $(this).siblings().toggle();
        });
        
        $(".multi-cat li.has-child i").on('click', function(){
            $(this).siblings(".sub-cat").stop().toggle(200);
            $(this).parents().toggleClass('active');
        });

        $(".sub-cat li.has-child i").on('click', function(){
            $(this).siblings(".sub-cat-child").toggle(200);
        });
    }
	
	if(window.innerWidth <= 575 && $('.ul-thanhvien').length > 0){
		$.each($('.ul-thanhvien .odd'), function (index, ele) {
		  let avatar =  $(ele).find('.avatar').html();
		  $(ele).find('.avatar').remove();
		  $(ele).prepend("<div class='avatar'>" + avatar + "</div>");
		  //console.log(avatar);
		  
		});
	}
});

function getHiddenCode() {
    $.ajax({
        type: "POST",
        url: "/Usercontrols/Category/antiCSRF.ashx",
        success: function (data) {
            if (!data.Error) {
                $("#hdAntiCSRF").val(data.Title);
            } else {
                window.location.reload();
            }
        }
    });
}