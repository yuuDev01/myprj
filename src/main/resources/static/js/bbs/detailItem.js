'use strict';
//textArea => ck_editor 대체
ClassicEditor
		.create( document.querySelector( '#bcontent' ), {
			removePlugins: ['Title'],
			toolbar: {
				items: [
					'heading',
					'|',
					'underline',
					'bold',
					'italic',
					'link',
					'|',
					'bulletedList',
					'numberedList',
					'alignment',
					'|',
					'imageUpload',
					'blockQuote',
					'insertTable',
					'mediaEmbed',
					'undo',
					'redo',
					'|',
					'fontBackgroundColor',
					'fontColor',
					'fontSize',
					'fontFamily',
					'highlight',
				]
			},
			language: 'ko',
			image: {
				toolbar: [
					'imageTextAlternative',
					'imageStyle:full',
					'imageStyle:side'
				]
			},
			table: {
				contentToolbar: [
					'tableColumn',
					'tableRow',
					'mergeTableCells',
					'tableCellProperties',
					'tableProperties'
				]
			},
		} )
		.then( editor => {
			window.editor = editor;
			editor.isReadOnly = true;
		} )
		.catch( error => {
			console.error( error );
		} );
			
const $replyBtn 		= document.getElementById('replyBtn');
const $modifyBtn 		= document.getElementById('modifyBtn');
const $beforePopUpDelBtn	= document.getElementById('beforePopUpDelBtn');   		
const $popUpDelBtn 	= document.getElementById('popUpDelBtn');  		
const $listBtn 			= document.getElementById('listBtn');

const handler = (res, e) => {
	//console.log(e);
	if(res.rtcd == '00'){
		const cate = e.target.dataset.cate;
		location.href = `/bbs/list?cate=${cate}`;
	}else{
		//alert('삭제오류!');
		return false;
	}
}
//답글
$replyBtn?.addEventListener("click",e=>{
		const bnum = e.target.dataset.bnum;
	location.href=`/bbs/reply/${bnum}`;	
});

//수정
$modifyBtn?.addEventListener("click", e=>{
	const bnum = e.target.dataset.bnum;
	location.href = `/bbs/${bnum}/edit`;
});


//삭제
$beforePopUpDelBtn?.addEventListener("click", e=>{
	const {bnum,cate} = e.target.dataset;
	$popUpDelBtn.dataset.bnum = bnum;
	$popUpDelBtn.dataset.cate = cate;

});

$popUpDelBtn?.addEventListener("click", e=>{
	const bnum = e.target.dataset.bnum;
	const cate = e.target.dataset.cate;
	const url = `/bbs/${cate}/${bnum}`;
	
	request.delete(url)
				 .then(res=>res.json())
				 .then(res=>handler(res, e))
				 .catch(err=>console.log(err));					 	
});

//목록
$listBtn?.addEventListener("click", e=>{
	const cate = e.target.dataset.cate;
	location.href = `/bbs/list?cate=${cate}`;
});
