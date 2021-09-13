'use strict';		
const $delFiles 	= document.querySelectorAll('i.fas.fa-backspace');  		
const $listBtn 		= document.getElementById('listBtn');

const handler = (res,target) => {
	//console.log(e);
	if(res.rtcd == '00'){
		const $parent = target.closest('div');
		const $child = target.closest('p');
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
			const sfname = e.target.dataset.sfname;
			const url = `/bbs/attach/${sfname}`;
			if(confirm('삭제하시겠습니까?')){
				request.delete(url)
							 .then(res=>res.json())
							 .then(res=>handler(res,e.target))
							 .catch(err=>console.log(err));					 
			}							
					
		});
	});
}

//목록
$listBtn?.addEventListener("click", e=>{
	const cate = e.target.dataset.cate;
	location.href = `/bbs/list?cate=${cate}`;
});