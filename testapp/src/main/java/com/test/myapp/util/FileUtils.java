package com.test.myapp.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

public class FileUtils {
	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	public static String uploadFile(String uploadPath, String originalName, byte[] fileData) throws Exception {
		//별도의 데이터를 저장할 필요가 없기 때문에 static으로 선언됨.
		
		UUID uid = UUID.randomUUID(); //UUID값을 받아옴.
		
		String savedName = uid.toString() + "_" + originalName;
		String savedPath = calcPath(uploadPath);

		File target = new File(uploadPath + savedPath, savedName); 
		//uploadPath + savedPath 위치에 savedName이라는 파일을 생성한다.
		
		FileCopyUtils.copy(fileData, target); //targetFile로 업로드된 파일 복사.
		
		String fileFormat = originalName.substring(originalName.lastIndexOf(".") + 1); //확장자
		
		String uploadedFileName = null;
		
		if(MediaUtils.getMediaType(fileFormat) != null) {
			uploadedFileName = makeThumbnail(uploadPath, savedPath, savedName);
		}else {
			uploadedFileName = makeIcon(uploadPath, savedPath, savedName);
		}
		
		return uploadedFileName;
	}

	private static String makeIcon(String uploadPath, String path, String fileName) throws Exception {
		String iconName = uploadPath + path + File.separator + fileName;
		
		return iconName.substring(uploadPath.length()).replace(File.separatorChar, '/');
	}

	
	private static String calcPath(String uploadPath){
		Calendar cal = Calendar.getInstance();
		String yearPath = File.separator + cal.get(Calendar.YEAR);
		
		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
		
		String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));
		
		makeDir(uploadPath, yearPath, monthPath, datePath);
		
		logger.info(datePath); //디버깅용도
		
		return datePath;
	}

	private static void makeDir(String uploadPath, String... paths) {
		//리눅스 배포시 해당 디렉토리에 쓰기 권한이 있어야 폴더를 생성할 수 있음.
		if( new File(paths[paths.length -1]).exists() ) {
			return ; //해당 폴더가 존재하면 그냥 리턴
		}
		File upDir = new File(uploadPath); //업로드 디렉토리가 존재하는지 확인
		if(!upDir.exists()) //존재하지 않으면 생성하기.
			upDir.mkdir();
		
		for(String path : paths){
			File dirPath = new File(uploadPath + path);
			
			if(!dirPath.exists()) {
				dirPath.mkdir(); //해당 폴더가 존재하지 않으면 
			}
		}
	}
	
	//이미지 썸네일 생성
	private static String makeThumbnail(String uploadPath, String path, String fileName) throws Exception {
		BufferedImage sourceImg = ImageIO.read(new File(uploadPath + path, fileName));
		//Scalr = org.imgscalr.Scalr
		BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);
		//높이 100px에 맞추어 비율을 유지한채(automatic) 너비를 조절한다.
		String thumbnailName = uploadPath + path + File.separator + "s_" + fileName;
		
		File newFile = new File(thumbnailName);
		String formatName = fileName.substring(fileName.lastIndexOf(".") + 1); //확장자 가져오기
		
		ImageIO.write(destImg, formatName.toUpperCase(), newFile);
		
		//썸내일 파일경로 + 파일명에서 업로드 경로를 제외한 경로만 뽑아내어 해당 파일명에서 파일구분자를 \에서 /로 변경함. 리눅스는 관계없음.
		//브라우저 경로구분자도 /인데 윈도우는 \이기 때문에 이를 위해 치환하는 것도 있음.
		return thumbnailName.substring(uploadPath.length()).replace(File.separatorChar, '/');
	}
	
	


}
