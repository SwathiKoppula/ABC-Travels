package com.service;

import java.sql.SQLException;
import java.util.Scanner;
import com.model.Constants;
import com.model.Main;
import com.repository.UserRepository;

public class UserService {
	
	public static UserRepository UserRepository = new UserRepository();
	Scanner scanner = new Scanner(System.in);
	
	public void registerNewAdmin() throws ClassNotFoundException, SQLException{
		 
		
	   System.out.println("\nNew Admin User Registration");

	    System.out.print("Enter first name: ");
	    String firstName = scanner.nextLine();

	    System.out.print("Enter last name: ");
	    String lastName = scanner.nextLine();

	    System.out.print("Enter mobile number: ");
	    long mobileNumber = scanner.nextLong();
	    scanner.nextLine();

	    System.out.print("Enter gender: ");
	    String gender = scanner.nextLine();

	    System.out.print("Enter email: ");
	    String email = scanner.nextLine();

	    System.out.print("Enter password: ");
	    String password = scanner.nextLine();
	    
	    UserRepository.addUser(firstName,lastName,mobileNumber,gender,email,password);
	    System.out.println(firstName+"Registered");
	    	
	}

	public void login() throws ClassNotFoundException, SQLException {
		
		System.out.print("Enter mobile number: ");
		  long mobileNumber = scanner.nextLong();
		 scanner.nextLine();
		 if(UserRepository.isUserAvailable(mobileNumber)) {
		 if(UserRepository.checkAccountStatus(mobileNumber)) {
			 int attempts = loginAttempt(mobileNumber);
				if(attempts>0&&attempts<=(Constants.noOfAttempts+1)) {
					Main.showBookingOptions(mobileNumber);
				}
				else {
					UserRepository.updateAccountStatus(mobileNumber);
				}
		 }
		 else {
			 System.out.println("our account is locked");
		 }
	}
		 else {
			 System.out.println("first regirester with mobile number");
		 }
		
		}
		

	private int loginAttempt(long mobileNumber) throws ClassNotFoundException, SQLException {
		
		int failedcount = 1;
		while(failedcount<=(Constants.noOfAttempts+1)) {
			
		 System.out.print("Enter password: ");
		 String password = scanner.nextLine();
		 if(UserRepository.isValidUser(mobileNumber,password)) {
			return failedcount;
		 }
		 else {
			 failedcount++;
		 }
	   }
		return -1;
	}

}
