'use strict';  		
const $writeBtn = document.getElementById('writeBtn');
const $listBtn  = document.getElementById('listBtn');

//답글
$writeBtn.addEventListener("click",e=>{
	const bnum = e.target.dataset.bnum;
	//console.log(writeForm.bcontent.value);
	const content = writeForm.bcontent.value;
	if(content.trim().length == 0){
		alert('내용을 입력해주세요');
		return;
	}
	writeForm.submit();
})


//목록
$listBtn.addEventListener("click", e=>{
	location.href = "/bbs/list";
});