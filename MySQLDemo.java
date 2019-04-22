//package com.yiibai.springmvc;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//public class MySQLDemo {
// 
//     public static void main(String[] args) {
//       SQLHandler sqlhandler = new SQLHandler();
//       
//       ArrayList<String> list= new ArrayList<String>();
//       list.add("666666666");
//       list.add("male");
//       list.add("taiyu");
//       list.add("zhang");
//       list.add("cs");       
//       list.add("XXX");
//       list.add("");
//       list.add("11111113");
//       list.add("1111234");
//       boolean success = sqlhandler.insert("Student",list);
//       if(success)
//    	   System.out.println("SUCCESS INSERTING");
//      
//       
//       ResultSet rs = sqlhandler.query("Student", "surName","firstName", "yuchen");
//       try {
//		while(rs.next())
//		   System.out.println(rs.getString("surname"));
//       } catch (SQLException e) {
//	     e.printStackTrace();
//	   }
//       
//       success = sqlhandler.update("Student", "firstname", "taiyu", "programme", "ics");
//       if(success)
//    	   System.out.println("SUCCESS updating");
//       
////       success = sqlhandler.delete("Student", "firstname", "taiyu");
////       if(success)
////       	   System.out.println("deleting success");
//     }
//
// 
// }
//
