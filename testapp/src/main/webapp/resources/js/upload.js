function checkImageType(fileFormat) {
	var pattern = /jpg|gif|png|jpeg/i;
	return fileFormat.match(pattern); //패턴과 일치하는지 t,f
}

function getFileName(fileLink){
	var idx = fileLink.indexOf("_") + 1;
	return fileLink.substring(idx, fileLink.length);
}

function getImgLink(fileLink){
	var front = fileLink.substr(0, 12); // '/20XX/XX/XX/'  0부터 12개를 잘라냄.
	var end = fileLink.substr(14); // 's_' 를 제거한 나머지.
	
	return front + end; //중간에 s_ 만 제거된 나머지가 전송됨
}

function getFileInfo(fullName){
	var fileName, imgsrc, getLink;
	
	var ext = fullName.substring(fullName.lastIndexOf('.') + 1, fullName.length).toLowerCase();
	
	if(checkImageType(ext)) {
		imgsrc = "/displayFile?fileName=" + fullName; //썸네일 링크 
		fileLink = fullName.substr(14); //파일 이름만 뽑아내기.
		getLink = "/displayFile?fileName=" + getImgLink(fullName); //원본 이미지 링크
	}else {
		imgsrc = "/resources/imgs/file.png";
		fileLink = fullName.substr(12); //썸네일이 없기 때문에 12번째부터 잘라내면 됨.
		getLink = "/displayFile?fileName=" + fullName; //파일 다운로드 링크
	}
	fileName = fileLink.substr(fileLink.indexOf("_") + 1); //UUID 제거한 파일이름.
	
	return {fileName:fileName, imgsrc:imgsrc, getLink:getLink, fullName:fullName};
}
