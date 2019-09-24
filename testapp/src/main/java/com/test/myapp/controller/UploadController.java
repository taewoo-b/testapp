package com.test.myapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.test.myapp.domain.UserVO;
import com.test.myapp.service.BoardService;
import com.test.myapp.util.FileUtils;
import com.test.myapp.util.MediaUtils;

@Controller
public class UploadController {
	@Inject
	ServletContext context;
	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	private static final String upload = "uploads/";
	
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
	@RequestMapping("/getAttach/{bno}")
	public List<String> getAttach(@PathVariable("bno")int bno) throws Exception {
		return bs.getAttach(bno);
	}
	
	@Inject
	private BoardService bs;

	
	
	
	@ResponseBody
	@RequestMapping(value="/uploadAjax", method= RequestMethod.POST, produces ="text/plain;charset=UTF-8" )
	public ResponseEntity<String> uploadAjax(MultipartFile file, HttpSession session) throws Exception {
		logger.info(context.getRealPath(""));
		logger.info("오리지널 파일이름 : " + file.getOriginalFilename());
		logger.info("크기 : " + file.getSize());
		logger.info("컨텐츠 타입 : " + file.getContentType());
		
		UserVO vo = (UserVO)session.getAttribute("login");
		
		if(vo == null){
			return new ResponseEntity<>("failed", HttpStatus.OK);
		}
		
		String uploadPath = context.getRealPath("") + upload;
		
		return new ResponseEntity<>(FileUtils.uploadFile(uploadPath, 
				file.getOriginalFilename(), 
				file.getBytes()), HttpStatus.CREATED);
	}
	
	@ResponseBody
	@RequestMapping(value="/deleteFile", method=RequestMethod.POST)
	public ResponseEntity<String> deleteFile(String fileName, HttpSession session) {
		logger.info("파일 삭제 : " + fileName);
		UserVO user = (UserVO)session.getAttribute("login");
		
		if (user == null){
			return new ResponseEntity<String>("Deleting file is failed. please check your permission.", HttpStatus.OK);
		}
		try {
			String writer = bs.getWriter(fileName); //글쓴이 가져오기
			
			if(writer != null && !writer.equals(user.getUid())){ //글쓴이와 일치하지 않는다면.
				logger.info(writer);
				return new ResponseEntity<String>("Deleting file is failed. please check your permission.", HttpStatus.OK);
			}
		} catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<String>("Deleting file is failed. please check your permission", HttpStatus.OK);
		}
				
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

	@ResponseBody
	@RequestMapping(value="/deleteAllFiles", method=RequestMethod.POST)
	public ResponseEntity<String> deleteFiles(@RequestParam("files[]") String[] files, HttpSession session){
		logger.info("모든 파일을 삭제합니다 : " + files);
		UserVO user = (UserVO)session.getAttribute("login");
		if (user == null){
			return new ResponseEntity<String>("Deleting file is failed. please check your permission.", HttpStatus.OK);
		}
		
		if(files == null || files.length == 0){
			return new ResponseEntity<String>("deleted", HttpStatus.OK);
		}
		
		for (String fileName : files) {
			
			try {
				String writer = bs.getWriter(fileName); //글쓴이 가져오기
				
				if(writer != null && !writer.equals(user.getUid())){ //글쓴이와 일치하지 않는다면.
					logger.info(writer);
					return new ResponseEntity<String>("Deleting file is failed. please check your permission", HttpStatus.OK);
				}
			} catch (Exception e){
				e.printStackTrace();
				return new ResponseEntity<String>("Deleting file is failed. please check your permission", HttpStatus.OK);
			}
			
			String uploadPath = context.getRealPath("") + upload;
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
			MediaType mType = MediaUtils.getMediaType(formatName);
			
			if(mType != null) {
				String front = fileName.substring(0, 12);
				String end = fileName.substring(14);
				
				
				new File(uploadPath + (front + end).replace('/', File.separatorChar)).delete();
			}
			
			new File(uploadPath + fileName.replace('/', File.separatorChar)).delete();
		}
		
		return new ResponseEntity<String> ("deleted", HttpStatus.OK);
	}

	






}

