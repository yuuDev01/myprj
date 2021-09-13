'use strict';  		
const $listBtn = document.getElementById('listBtn');

//목록
$listBtn.addEventListener("click", e=>{
	const cate = e.target.dataset.cate;
	location.href = `/bbs/list?cate=${cate}`;
});