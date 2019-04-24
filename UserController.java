package com.yiibai.springmvc;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sun.org.apache.regexp.internal.recompile;


@Controller
public class UserController {
	@RequestMapping("/upload")
	public String upload() {
		return "upload";
	}
	
	SQLHandler sqlHandler = new SQLHandler();
	SortingHandler sortingHandler = new SortingHandler(sqlHandler);
	
	@RequestMapping("/login")
	public @ResponseBody Student login(@RequestBody Login login) {
		
		Student stu = new Student();
		ResultSet rs = sqlHandler.query("Student", "*", "studentID","=", login.getStudentID());
		try {
			if(rs.next()) {
				if(login.getPassword().equals(rs.getString("password"))){					
					stu.setStudentID(rs.getString("studentID"));
					stu.setGender(rs.getString("gender"));
					stu.setFirstname(rs.getString("firstname"));
					stu.setSurname(rs.getString("surname"));
					stu.setProgramme(rs.getString("programme"));
					stu.setEmail(rs.getString("email"));
					stu.setPreference(rs.getString("preference"));
					stu.setAccount(rs.getString("account"));
					stu.setPassword(rs.getString("password"));
					return stu;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stu;
	}
	
@RequestMapping("/testJson.action")
public @ResponseBody User testJson(@RequestBody User user) {
	 	
 	ResultSet rs = sqlHandler.query("Student", "surName","firstName", "=",user.getId());
     try {
    		 if(rs.next()) {
    			 
    		 } else {
    			 user.setId(null);
    		 }
 	} catch (SQLException e) {
 		e.printStackTrace();
 	}
    return user;
}

@RequestMapping("/viewActivity")
public @ResponseBody SuccessMessage viewActivity(@RequestBody UpdateInfo updateInfo){
	SuccessMessage message = new SuccessMessage();
	sortingHandler.updatePreferenceByActivity("view", updateInfo.getStudentID(), updateInfo.getActivityID());
    message.setMessage("success");
	return message; 
}

@RequestMapping("/ApplyActivity")
public @ResponseBody SuccessMessage ApplyActivity(@RequestBody UpdateInfo updateInfo) {
	SuccessMessage message = new SuccessMessage();
    ArrayList<String> values = new ArrayList<String>(); 
	String activityID = updateInfo.getActivityID();
	String studentID = updateInfo.getStudentID();
	int n = Integer.parseInt(studentID)+Integer.parseInt(activityID);
	String parID = n+"";
	values.add(parID);
	values.add(activityID);
	values.add(studentID);
    boolean result = sqlHandler.insert("Participator",values);
	if(result) {
		message.setMessage("success");
		sortingHandler.updatePreferenceByActivity("apply", studentID, activityID);
	}else {
		message.setMessage("fail");
	}
    
	return message;
} 

@RequestMapping("/QuitActivity")
public @ResponseBody SuccessMessage QuitActivity(@RequestBody UpdateInfo updateInfo) {
	SuccessMessage message = new SuccessMessage();
	String activityID = updateInfo.getActivityID();
	String studentID = updateInfo.getStudentID();
	int n = Integer.parseInt(studentID)+Integer.parseInt(activityID);
	String parID = n+"";
	boolean result = sqlHandler.delete("Participator", "participatorID",parID);
	if(result) {
		message.setMessage("success");
		sortingHandler.updatePreferenceByActivity("quit", studentID, activityID);
	}else {
		message.setMessage("fail");
	}
	return message;
} 

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
@RequestMapping("/RequestSociety")
public @ResponseBody SocietyInformation RequestSociety(){
	SocietyInformation societyInfo = new SocietyInformation();
	ResultSet rs = sqlHandler.query("Society", "*", "societyID", "!=", "0");
	ArrayList<HashMap<String,String>> map = new ArrayList<HashMap<String,String>>();
	try {
		while(rs.next()) {
			HashMap<String,String> map2 = new HashMap<String,String>();
			String societyID = rs.getString("societyID");
			map2.put("societyID",societyID);
			String name = rs.getString("name");
			map2.put("name",name);
			String type = rs.getString("type");
			map2.put("type",type);
			String description = rs.getString("description");
			map2.put("description",description);
			String poster = rs.getString("poster");
			map2.put("poster",poster);
			map.add(map2);
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	societyInfo.setMap(map);
	return societyInfo;
}

@RequestMapping("/SocietyActivityList")
public @ResponseBody ActivityInformation SocietyActivityList(@RequestBody UpdateInfo updateInfo) {
    ActivityInformation activityInfo = new ActivityInformation();
    String societyID = updateInfo.getSocietyID();
    ResultSet rs = sqlHandler.query("activity", "*", "societyID", "=", societyID);
    ArrayList<HashMap<String,String>> map = new ArrayList<HashMap<String,String>>();
    try {
		while(rs.next()) {
			HashMap<String,String> map2 = new HashMap<String, String>();  
			String activityID = rs.getString("activityID");
			map2.put("activityID",activityID);
			String name = rs.getString("name");
			map2.put("name",name);
			String type = rs.getString("type");
			map2.put("type",type);
			String description  = rs.getString("description");
			map2.put("description",description);
			String location = rs.getString("location");
			map2.put("location",location);
			String time = rs.getString("time");
			map2.put("time",time);
			String participatorNum = rs.getString("participatorNum");
			map2.put("participatorNum",participatorNum);
			map2.put("societyID",societyID);
			String poster = rs.getString("poster");
			map2.put("poster",poster);
			map.add(map2);
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
    activityInfo.setMap(map);    
    return activityInfo;
}

@RequestMapping("/ApplySociety")
public @ResponseBody SuccessMessage ApplySociety(@RequestBody UpdateInfo updateInfo) {
	SuccessMessage message = new SuccessMessage();
	String studentID = updateInfo.getStudentID();
	String societyID = updateInfo.getSocietyID();
	int n = Integer.parseInt(studentID)+Integer.parseInt(societyID);
	String memberID = n+"";
	ArrayList<String> values = new ArrayList<String>(); 
	values.add(memberID);
	values.add(studentID);
	values.add(societyID);
	boolean result = sqlHandler.insert("SocietyMember", values);
	if(result) {
		message.setMessage("success");
	    sortingHandler.updatePreferenceBySociety("join", studentID, societyID);
	}else {
		message.setMessage("fail");		
	}
	return  message;
}


@RequestMapping("/QuitSociety")
public @ResponseBody SuccessMessage QuitSociety(@RequestBody UpdateInfo updateInfo) {
	SuccessMessage message = new SuccessMessage();
	String studentID = updateInfo.getStudentID();
	String societyID = updateInfo.getSocietyID();
	int n = Integer.parseInt(studentID)+Integer.parseInt(societyID);
	String memberID = n+"";
	boolean result  = sqlHandler.delete("SocietyMember", "memberID", memberID);
	if(result) {
		message.setMessage("success");
		sortingHandler.updatePreferenceBySociety("quit", studentID, societyID);	
	}else {
		message.setMessage("fail");
	}	
	return  message;
}
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

@RequestMapping("/uploadFile1")
@ResponseBody
public String uploadFile1(MultipartHttpServletRequest muiltRequest,HttpServletRequest servletRequest) throws Exception{
    String resultString = "";
    String filename = "";//Դ�ļ���
    String fileName = muiltRequest.getFileNames().next(); //�õ��ļ�����ע�⡣��content-type �е�name="file1"���������������ļ�����
    MultipartFile file = muiltRequest.getFile(fileName);   //�õ����ļ�
    if (file.getOriginalFilename().length() > 0) {
        filename = new String(file.getOriginalFilename().getBytes("ISO-8859-1"),"UTF-8");//����ת��,�õ�Դ�ļ���
        //�õ������ļ�Ŀ¼
        String path = this.getClass().getResource("/").getPath();
        path = path.replaceAll("(.)/\\..*", "$1") + servletRequest.getContextPath(); //������Ŀ¼
        String filePath = path + "/WebContent/upload";

        File uploadFile = new File(filePath,     //���������ļ�
                filename);

        InputStream inputStream = file.getInputStream();
        FileUtils.copyInputStreamToFile(inputStream,  //�ѻ�ȡ���ļ��洢��ָ��λ��
                uploadFile);
                filename = new String(filename.getBytes(),"ISO-8859-1");//ת�뷵��
    resultString = filename + " upload Success";
    }

    return resultString;
}

@RequestMapping("/uploadFile2")
@ResponseBody
public String uploadFile2(MultipartHttpServletRequest muiltRequest,HttpServletRequest servletRequest) throws Exception{
String resultString = "";
String filename = "";//Դ�ļ���
String fileName = muiltRequest.getFileNames().next(); //�õ��ļ�����ע�⡣��content-type �е�name="file1"���������������ļ�����
MultipartFile file = muiltRequest.getFile(fileName);   //�õ����ļ�
if (file.getOriginalFilename().length() > 0) {
    filename = new String(file.getOriginalFilename().getBytes("ISO-8859-1"),"UTF-8");//����ת��,�õ�Դ�ļ���
    //�õ������ļ�Ŀ¼
    String path = "/www/image/";

    File uploadFile = new File(path,     //���������ļ�
            filename);

    InputStream inputStream = file.getInputStream();
    FileUtils.copyInputStreamToFile(inputStream,  //�ѻ�ȡ���ļ��洢��ָ��λ��
            uploadFile);
            filename = new String(filename.getBytes(),"ISO-8859-1");//ת�뷵��
resultString = filename + " upload Success";
}

return resultString;
}


@RequestMapping("/RequestList")
public @ResponseBody ActivityInformation RequestList(@RequestBody Student student){
    String id = student.getStudentID();
    String[] resultList = sortingHandler.getList(id);
    ArrayList<HashMap<String,String>> map = new ArrayList<HashMap<String,String>>();
    for(int i = 0; i < resultList.length; i++) {
    	ResultSet rs = sqlHandler.query("Activity", "*", "activityID", "=", resultList[i]);
    	HashMap<String,String> map2 = new HashMap<String,String>();
    	try {
    		map2.put("activityID",resultList[i]);
			while(rs.next()) {
				String name = rs.getString("name");
				map2.put("name",name);
				String type = rs.getString("type");
				map2.put("type",type);
				String description  = rs.getString("description");
				map2.put("description",description);
				String location = rs.getString("location");
				map2.put("location",location);
				String time = rs.getString("time");
				map2.put("time",time);
				String participatorNum = rs.getString("participatorNum");
				map2.put("participatorNum",participatorNum);
				String  societyID = rs.getString("societyID");
				map2.put("societyID",societyID);
				String poster = rs.getString("poster");
				map2.put("poster",poster);
			}
		    map.add(map2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    ActivityInformation activityInfo = new ActivityInformation();
    activityInfo.setMap(map);
    return activityInfo;    
}


@RequestMapping("/addPic")
public void insertUser (MultipartFile pic) throws IOException {
	System.out.println("00000");
  String originalFileName = pic.getOriginalFilename();
  System.out.println("1111111");
  if(pic!=null && originalFileName!=null && originalFileName.length()>0){
    String pic_path = "/www/image/";
    String newFileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
    File newFile = new File(pic_path+newFileName);
    pic.transferTo(newFile);
  }
 }

@RequestMapping(value="/multipleSave", method=RequestMethod.POST )
public @ResponseBody String multipleSave(@RequestParam("file") MultipartFile[] files){
    String fileName = null;
	String msg = "";
	if (files != null && files.length >0) {
		for(int i =0 ;i< files.length; i++){
            try {
                fileName = files[i].getOriginalFilename();
                byte[] bytes = files[i].getBytes();
                BufferedOutputStream buffStream = 
                        new BufferedOutputStream(new FileOutputStream(new File("/www/image/" + fileName)));
                buffStream.write(bytes);
                buffStream.close();
                msg += "You have successfully uploaded " + fileName;
            } catch (Exception e) {
                return "You failed to upload " + fileName + ": " + e.getMessage();
            }
		}
		return msg;
    } else {
        return "Unable to upload. File is empty.";
    }
}

@RequestMapping("/RequestSocietyMember")
public @ResponseBody SocietyInformation RequestSocietyMember(@RequestBody Login login){
	SocietyInformation societyInfo = new SocietyInformation();
	ArrayList<HashMap<String,String>> map = new ArrayList<HashMap<String,String>>();
	try {
		ResultSet rs = sqlHandler.query("Administrator", "*", "studentID","=", login.getStudentID());
		if(rs.next()) {
		ResultSet rs2 = sqlHandler.query("SocietyMember", "*", "societyID","=", rs.getString("societyID"));
		while(rs2.next()) {
			ResultSet rs3 = sqlHandler.query("Student", "*", "studentID","=", rs2.getString("studentID"));
			if(rs3.next()) {
			HashMap<String,String> map2 = new HashMap<String,String>();
			String firstname = rs3.getString("firstname");
			map2.put("firstname",firstname);
			String studentID = rs3.getString("studentID");
			map2.put("studentID",studentID);
			map.add(map2);
			}
		}
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	societyInfo.setMap(map);
	return societyInfo;
}

}
