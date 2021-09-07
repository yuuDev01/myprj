'use strict';

const $modifyBtn = document.getElementById('modifyBtn');  		
const $delBtn = document.getElementById('delBtn');  		
const $listBtn = document.getElementById('listBtn');

const handler = e => {
	//console.log(e);
	if(e.rtcd == '00'){
		location.href = "/bbs/list";
	}else{
		//alert('삭제오류!');
		return false;
	}
}
//수정
$modifyBtn.addEventListener("click", e=>{
	const bnum = e.target.dataset.bnum;
	location.href = `/bbs/${bnum}/edit`;
});

//삭제
$delBtn.addEventListener("click", e=>{
	const bnum = e.target.dataset.bnum;
	const url = `/bbs/${bnum}`;
	
	if(confirm('삭제하시겠습니까?')){
		request.delete(url)
					 .then(res=>res.json())
					 .then(res=>handler(res))
					 .catch(err=>console.log(err));					 
	}	
	
});

//목록
$listBtn.addEventListener("click", e=>{
	location.href = "/bbs/list";
});
