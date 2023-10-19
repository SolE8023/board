(function($, window, undefined) {
	"use strict";

	$.fn.pageup = function(options) {

		var options = $.extend({}, $.fn.pageup.defaults, options);

		return this.each(function() {

			var $this = $(this);
			
			$(document).scroll(function(){
				if ($(this).scrollTop() > options.offset) {
					$this.fadeIn(options.fadeDelay);
				} else {
					$this.fadeOut(options.fadeDelay);
				}
			});
		
			$this.click(function(){
				$('html, body').animate({scrollTop : 0}, options.scrollDuration);
				return false;
			});

		});

		return $this;
	};

	$.fn.pageup.defaults = {
		offset: 100,
		fadeDelay: 500,
		scrollDuration: 400
	};

})(jQuery);


function myFunction() {
  document.getElementById("dropdown").classList.toggle("show");
}

// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {
	var dropdowns = document.getElementsByClassName("dropmenu");
	var i;
	for (i = 0; i < dropdowns.length; i++) {
	  var openDropdown = dropdowns[i];
	  if (openDropdown.classList.contains('show')) {
		openDropdown.classList.remove('show');
	  }
	}
  }
}

	

function myFunction2() {
	document.getElementById("dropdown2").classList.toggle("show");
}

// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {
	var dropdowns = document.getElementsByClassName("dropmenu");
	var i;
	for (i = 0; i < dropdowns.length; i++) {
	  var openDropdown = dropdowns[i];
	  if (openDropdown.classList.contains('show')) {
		openDropdown.classList.remove('show');
	  }
	}
  }
}

function myFunction3() {
  document.getElementById("dropdown3").classList.toggle("show");
}

// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {
	var dropdowns = document.getElementsByClassName("dropmenu");
	var i;
	for (i = 0; i < dropdowns.length; i++) {
	  var openDropdown = dropdowns[i];
	  if (openDropdown.classList.contains('show')) {
		openDropdown.classList.remove('show');
	  }
	}
  }
}


		$(document).ready(function(){ 
			var fileTarget = $('.filebox .upload-hidden'); 
			fileTarget.on('change', function(){ //
				if(window.FileReader){ // modern browser 
					var filename = $(this)[0].files[0].name; 
				} else { // old IE 
					var filename = $(this).val().split('/').pop().split('\\').pop(); // 
				} 
				// 
				$(this).siblings('.upload-name').val(filename);
			}); 
		});

