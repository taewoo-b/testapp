package com.test.myapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.test.myapp.util.FileUtils;
import com.test.myapp.util.MediaUtils;

@Controller
public class UploadController {
	@Inject
	ServletContext context;
	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	private static final String upload = "uploads/";
	
	@ResponseBody
	@RequestMapping(value="/uploadAjax", 
		method= RequestMethod.POST, produces ="text/plain;charset=UTF-8" )
	public ResponseEntity<String> uploadAjax(MultipartFile file) throws Exception {
		logger.info(context.getRealPath(""));
		logger.info("오리지널 파일이름 : " + file.getOriginalFilename());
		logger.info("크기 : " + file.getSize());
		logger.info("컨텐츠 타입 : " + file.getContentType());
		
		String uploadPath = context.getRealPath("") + upload;
		
		return new ResponseEntity<>(FileUtils.uploadFile(uploadPath, 
				file.getOriginalFilename(), 
				file.getBytes()), HttpStatus.CREATED);
	}
	
	@ResponseBody
	@RequestMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String fileName)throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;
		
		logger.info("파일 명  : " + fileName);
		
		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
			
			MediaType mType = MediaUtils.getMediaType(formatName); 
			
			HttpHeaders headers = new HttpHeaders();
			
			String uploadPath = context.getRealPath("") + upload;
			
			in = new FileInputStream(uploadPath + fileName);
			//File 유틸리티는 \, / 는 전부 같다고 처리해준다.
			//따라서 요청되는 2017/01/20/파일명 형식과 context.getRealPath의 \로 구분되는 형식이 합쳐져도 문제 없다.
			
			if(mType != null) {
				//이미지 파일이라면 해당 파일의 타입으로 헤더의 컨텐츠 타입을 정의하여 전송한다.
				headers.setContentType(mType);
			}else {
				//그렇지 않다면 파일 스트림으로 헤더를 정의하여 다운로드가 이루어질 수 있도록 한다.
				fileName = fileName.substring(fileName.indexOf("_") + 1);
				//UUID를 제거한 파일명.
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				//옥텟 스트림 : 8비트를 한 블럭으로 하는 배열 스트림, 즉 바이트단위 전송이다. 모든 컴퓨터시스템이8비트 = 1바이트는 아니기에 옥텟이란 명칭을 써서 8비트를 명확하게 한다.
				headers.add("Content-Disposition",  "attachment; filename=\"" + 
						new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
			}
			
			entity =  new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e){
			e.printStackTrace();
			//에러내용을 프린트하고
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
			//잘못된 요청응답을 전송함.
		} finally {
			in.close(); //마지막으로 인풋 스트림은 꼭 닫아줘야함.
		}
		
		return entity;
	}
	
	@ResponseBody
	@RequestMapping(value="deleteFile", method=RequestMethod.POST)
	public ResponseEntity<String> deleteFile(String fileName){
		logger.info("파일 삭제 : " + fileName);
		
		String formatName = fileName.substring(fileName.lastIndexOf(".")+ 1);
		
		MediaType mType = MediaUtils.getMediaType(formatName);
		String uploadPath = context.getRealPath("") + upload;
		
		if(mType != null){
			//그림이라면 썸네일과 원본이미지 모두를 삭제해야함. 따라서 원본이미지를 먼저 삭제
			String front = fileName.substring(0, 12);
			String end = fileName.substring(14);
			new File(uploadPath + (front + end).replace('/', File.separatorChar)).delete();
		}
		//썸네일 또는 일반파일 삭제
		new File(uploadPath + fileName.replace('/', File.separatorChar)).delete();
		
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}



}

