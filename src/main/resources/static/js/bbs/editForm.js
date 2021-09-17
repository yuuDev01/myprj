'use strict';		
//textArea => ck_editor 대체
ClassicEditor
		.create( document.querySelector( '#bcontent' ), {
		 plugin:['Markdown','MediaEmbed','MediaEmbedToolbar'],
			toolbar: {
				items: [
					'heading',
					'|',
					'underline',
					'bold',
					'italic',
					'link',
					'bulletedList',
					'numberedList',
					'todoList',
					'|',
					'outdent',
					'indent',
					'|',
					'imageInsert',
					'imageUpload',
					'blockQuote',
					'insertTable',
					'mediaEmbed',
					'markdown',
					'undo',
					'redo',
					'|',
					'highlight',
					'fontFamily',
					'fontColor',
					'fontBackgroundColor',
					'fontSize',
					'|',
					'htmlEmbed',
					'specialCharacters'
				]
			},
			language: 'en',
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
		})
		.then( editor => {
			window.editor = editor;
		} )
		.catch( error => {
			console.error( error );
		} );	
		
const $delFiles 	= document.querySelectorAll('i.fas.fa-backspace');  		
const $listBtn 		= document.getElementById('listBtn');
const $popUpDelBtn 		= document.getElementById('popUpDelBtn');  		

const handler = (res,target) => {
	//console.log(e);
	if(res.rtcd == '00'){
		/*
		const $parent = target.closest('div');
		const $child = target.closest('p');*/
		const $parent = document.getElementById('attachList');
		const $child  = $parent.querySelector(`[data-sfname='${target.dataset.sfname}']`).closest('p');
		$parent.removeChild($child);
	}else{
		//alert('삭제오류!');
		return false;
	}
}

//첨부파일 개별 삭제
if($delFiles){
	Array.from($delFiles).forEach(ele=>{
		ele.addEventListener("click",e=>{
			const {cate,sfname} = e.target.dataset;
			$popUpDelBtn.dataset.cate = cate;
			$popUpDelBtn.dataset.sfname = sfname;					
		});
	});
}

$popUpDelBtn?.addEventListener('click',e=>{
	const {cate,sfname} = e.target.dataset;
	const url = `/attach/${cate}/${sfname}`;
	request.delete(url)
				 .then(res=>res.json())
				 .then(res=>handler(res,e.target))
				 .catch(err=>console.log(err));					 
});

//목록
$listBtn?.addEventListener("click", e=>{
	const cate = e.target.dataset.cate;
	location.href = `/bbs/list?cate=${cate}`;
});
