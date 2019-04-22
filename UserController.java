package com.yiibai.springmvc;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


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
		ResultSet rs = sqlHandler.query("Student", "*", "account","=", login.getAccount());
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
 		// TODO Auto-generated catch block
 		e.printStackTrace();
 	}
    return user;
}

@RequestMapping("/viewActivity")
public void viewActivity(@RequestBody UpdateInfo updateInfo){
	sortingHandler.updatePreferenceByActivity("view", updateInfo.getStudentID(), updateInfo.getActivityID());
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



@RequestMapping("/RequestList")
public @ResponseBody ActivityInformation RequestList(@RequestBody Student student){
    String id = student.getStudentID();
    String[] resultList = sortingHandler.getList(id);
    HashMap<String,HashMap<String,String>> map = new HashMap<String,HashMap<String,String>>();
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
		    map.put(resultList[i],map2);
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
}