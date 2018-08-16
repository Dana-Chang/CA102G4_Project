/**
 * @license Copyright (c) 2003-2018, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */
//    config.extraPlugins = 'cloudservices';
//    config.filebrowserUploadUrl = 'http://localhost:8081/HelloWorld/upload';
//    config.filebrowserImageUploadUrl = "http://localhost:8081/HelloWorld/upload";
//    config.filebrowserFlashUploadUrl = 'http://localhost:8081/HelloWorld/upload';
//    config.filebrowserImageBrowseUrl = '/browerServer?type=image';    
            //            cloudServices_tokenUrl: 'https://33750.cke-cs.com/token/dev/IOpRD8zfZYyKq5rtbPfDpUpvOoMEWpjtqo28pARGlEiF16ZngJdgZlEd8FrX',
            //            cloudServices_uploadUrl: 'https://33750.cke-cs.com/easyimage/upload/',
CKEDITOR.editorConfig = function( config ) {
    config.skin = 'moono-dark';
	config.extraPlugins = 'dragresize,base64image,base64imagedrop';
    config.image_previewText=' ';
	config.toolbarGroups = [
		{ name: 'insert', groups: [ 'base64image', 'insert' ] },
		{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },
		{ name: 'document', groups: [ 'mode', 'document', 'doctools' ] },
		{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
		{ name: 'forms', groups: [ 'forms' ] },
		{ name: 'links', groups: [ 'links' ] },
		{ name: 'tools', groups: [ 'tools' ] },
		'/',
		{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
		{ name: 'colors', groups: [ 'colors' ] },
		{ name: 'styles', groups: [ 'styles' ] },
		{ name: 'others', groups: [ 'others' ] },
		{ name: 'about', groups: [ 'about', 'source' ] }
	];

	config.removeButtons = 'About,Save,NewPage,Preview,Print,Templates,CreateDiv,Language,HiddenField,ShowBlocks,Scayt,Textarea,TextField,Flash,SpecialChar,PageBreak,Iframe,ImageButton,Find';
	
};

