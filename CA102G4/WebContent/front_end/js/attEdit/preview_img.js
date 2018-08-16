$("#updateImg").change(function(event) {
	readURL(this, event);
});
function readURL(input, event) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function(e) {
			$("#preview_img").attr('src', e.target.result);
		}
		reader.readAsDataURL(input.files[0]);
	}
}
