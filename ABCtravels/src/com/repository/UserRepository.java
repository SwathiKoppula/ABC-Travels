package com.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.model.Constants;

public class UserRepository {
	
	public static Connection getconnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");//com.mysql.cj.jdbc.Driver
		 return DriverManager.getConnection(Constants.url,Constants.username,Constants.password);
	}

	public void addUser(String firstName,String lastName,long mobileNumber,String gender,String email,String password) throws SQLException, ClassNotFoundException {
		Connection con = UserRepository.getconnection();
		 PreparedStatement statement = con.prepareStatement("insert into users values(?,?,?,?,?,?,?)");
		 statement.setLong(1, mobileNumber);
		 statement.setString(2,firstName);
		 statement.setString(3,lastName);
		 statement.setString(4,gender);
		 statement.setString(5,email);
		 statement.setString(6,password);
		 statement.setString(7,"active");
		 statement.executeUpdate();	
		 System.out.println("Registered successfully!!");
	}
	
	public boolean isValidUser(long mobileNumber,String password) throws ClassNotFoundException, SQLException {
		
		int count = 0;
		Connection con = UserRepository.getconnection();
		 PreparedStatement statement = con.prepareStatement("select count(*) from users where mobileNumber=? and password=?");
		 statement.setLong(1, mobileNumber);
		 statement.setString(2, password);
		 ResultSet result = statement.executeQuery();
		 while(result.next()) {
			 count = result.getInt("count(*)");
		 }
		 if(count==1) {
			return true; 
		 }
		return false;
		
	}
	public boolean checkAccountStatus(long mobileNumber) throws ClassNotFoundException, SQLException {

		String status="";
		Connection con = UserRepository.getconnection();
		 PreparedStatement statement = con.prepareStatement("select accountstatus from users where mobileNumber=?");
		 statement.setLong(1, mobileNumber);
		 ResultSet result = statement.executeQuery();
		 while(result.next()) {
		  status = result.getString(1);
		 }
		 if(status.equalsIgnoreCase("locked")) {
			 return false;
		 }
		return true;
		
	}
	
	public void updateAccountStatus(long mobileNumber) throws ClassNotFoundException, SQLException {
		
		Connection con = UserRepository.getconnection();
		 PreparedStatement statement = con.prepareStatement("update users set accountstatus='locked' where mobileNumber=?");
		 statement.setLong(1, mobileNumber);
		 statement.executeUpdate();
		 
	}

	public boolean isUserAvailable(long mobileNumber) throws ClassNotFoundException, SQLException {
		
		 int count=0;
		Connection con = UserRepository.getconnection();
		 PreparedStatement statement = con.prepareStatement("select count(*) from users where mobileNumber=?");
		 statement.setLong(1, mobileNumber);
		 ResultSet result = statement.executeQuery();
		 while(result.next()) {
			 count = result.getInt(1);
		 }
		 if(count==1) {
			 return true;
		 }
		 return false;
	}
	
}
