'use strict';
const $dropbox 							= document.getElementById('dropbox');
const $imgFile 							= document.getElementById('imgFile');
const $profileImg   				= document.getElementById('profileImg');
const $popUpDelBtn 					= document.getElementById('popUpDelBtn');
const $changeProfileImgBtn 	= document.getElementById('changeProfileImgBtn');
const $modifyNicknameBtn 	 	= document.getElementById('modifyNicknameBtn');
const $profileInfo 					=	document.getElementById('profileInfo');

const dragover_e = e => {
	e.preventDefault();//필수: 생략하면 drop이벤트가 발생하지 않는다.
}
const dragenter_e = e => {
  e.target.style.borderColor="red";
  //console.log(e);	
}
const dragleave_e = e => {
  e.target.style.borderColor="gray";
  //console.log(e);
}

const changeImage_h = (res,reader) => {
	//console.log(res);
	if(res.rtcd == '00'){	
		$profileInfo.dataset.cate = res.data.cate;
		$profileInfo.dataset.sfname = res.data.sfname;
		$profileImg.src =  reader.result;
	}else{
		console.error(res);
	}
}

const changeImage = files => {
	//파일갯수 체크
	if(files.length > 1 ) {
		alert('파일을 하나만 선택하세요!');
		return false;
	} 
	//확장자 체크
	const mimeType = files[0].type;
	const slashPosition = mimeType.indexOf('/');
	const fileType = mimeType.substring(0,slashPosition);

	if(fileType !== 'image') {
		alert('이미지 파일만 가능합니다.');
		return false;
	}
	//파일사이즈 체크
	if(files[0].size > 1024*1024) {
		alert('1M가 이하만 가능합니다.');
		return false;
	}

  // 파일을 읽는 작업 시작
  //readAsDataURL 메서드는 파일을 data URL 형식으로 만들어 준다. 
  //파일을 서버에 업로드하지 않고도 조작할 수 있음을 의미한다. 
  //포멧을 변환하거나, 데이터를 분석하여 변조하는 일이 가능해 진다.	
  const reader = new FileReader();
  reader.readAsDataURL(files[0]);
  reader.addEventListener("load",e=>{ 		
		const {cate,sfname} = $profileInfo.dataset;
		//신규
		if(!sfname){
			console.log('신규');
			const url = `/attach/A0401/`;
			const formData = new FormData();
			formData.append("file",files[0]);
			request.mpost(url,formData)
						 .then(res=>res.json())
						 .then(res=>changeImage_h(res,reader))
						 .catch(err=>console.log('Error:', err));					

		//변경							
		}else{
			console.log('변경');
			const url = `/attach/${cate}/${sfname}/`;
			const formData = new FormData();
			formData.append("file",files[0]);
			request.mput(url,formData)
						 .then(res=>res.json())
						 .then(res=>changeImage_h(res,reader))
						 .catch(err=>console.log('Error:', err));	
		}		
	
	});
}
const drop_e = e=>{
  e.preventDefault();
  e.target.style.borderColor="gray";
  const files = e.dataTransfer.files;
  //console.log("drop:",files); 
  changeImage(files);
}
const click_e =  e=>{
  const ev = new MouseEvent('click',{bubbles:true, cancelable:false});
  $imgFile.dispatchEvent(ev);	
}
const change_e = e=>{
  const files = $imgFile.files;
  //console.log("change",files);   
	changeImage(files);
}

//프로파일 이미지 삭제 성공 후처리  
const popUpDelBtn_h = res => {
	//console.dir(res);
	if(res.rtcd == '00'){
		console.log('이미지 삭제됨!');
		$profileImg.src='/img/noprofile.jpg';
		$profileInfo.dataset.cate = '';
		$profileInfo.dataset.sfname = '';
	}else{
		console.log('이미지 삭제실패!');
	}	
} 
 
//프로파일 이미지 삭제 요청 
const popUpDelBtn_e = e => {
	const {cate,sfname} = $profileInfo.dataset;
	if(!cate || !sfname) return false;

	const url = `/attach/${cate}/${sfname}`;
	request.delete(url)
			 .then(res=>res.json())
			 .then(res=>popUpDelBtn_h(res))
			 .catch(err=>console.error('Error:', err));	
};

//별칭 수정 요청 성공 후처리 
const  modifyNickname_h = (e,res) => {
	if(res.rtcd == '00'){
		e.target.value = res.data;
		location.reload();
		rtmsg_nickname.textContent = '수정되었습니다.';
	}else{
		rtmsg_nickname.textContent = '다시 시도바람니다.';
	}
};
//별칭 수정 요청
const modifyNickname_e = e => {
	const nickname = document.getElementById('nickname').value;
	const url = `/members/profile/nickname`;
	fetch(url, {	method: 'PATCH',
								body : nickname
							})
	.then(res=>res.json())
	.then(res=>modifyNickname_h(e,res))
	.catch(err=>console.error('Error:', err));	
};
 
$dropbox.addEventListener('dragover',dragover_e); 
$dropbox.addEventListener('dragenter',dragenter_e);
$dropbox.addEventListener('dragleave',dragleave_e);
$dropbox.addEventListener('drop', drop_e);
$dropbox.addEventListener('click',click_e);
$imgFile.addEventListener('change',change_e);

//이미지 변경,삭제
$changeProfileImgBtn.addEventListener('click',click_e);
$popUpDelBtn.addEventListener('click', popUpDelBtn_e);
//별칭 수정
$modifyNicknameBtn.addEventListener('click',modifyNickname_e);
