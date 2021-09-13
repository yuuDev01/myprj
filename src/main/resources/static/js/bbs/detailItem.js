'use strict';
const $replyBtn 	= document.getElementById('replyBtn');
const $modifyBtn 	= document.getElementById('modifyBtn');  		
const $delBtn 		= document.getElementById('delBtn');  		
const $listBtn 		= document.getElementById('listBtn');

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
$delBtn?.addEventListener("click", e=>{
	const bnum = e.target.dataset.bnum;
	const url = `/bbs/${bnum}`;
	
	if(confirm('삭제하시겠습니까?')){
		request.delete(url)
					 .then(res=>res.json())
					 .then(res=>handler(res, e))
					 .catch(err=>console.log(err));					 
	}	
	
});

//목록
$listBtn?.addEventListener("click", e=>{
	const cate = e.target.dataset.cate;
	location.href = `/bbs/list?cate=${cate}`;
});