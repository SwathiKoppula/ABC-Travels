package com.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import com.service.JourneyService;
import com.service.UserService;



public class Main {
	 
	public static JourneyService JourneyService = new JourneyService();
	public static UserService UserService = new UserService();

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		 if (displayCompanyLogo()) {
	            showMenuOptions();
	        } else {
	            System.out.println("Failed to load company logo. Exiting...");
	        }

	}
	 
	
	
	 private static void showMenuOptions() throws ClassNotFoundException, SQLException{
	        int choice;
	        boolean running = true;
	        Scanner scanner = new Scanner(System.in);

	        while (running) {
	        	
	        	System.out.println("\nMenu Options:");
	            System.out.println("1. New Admin User Registration");
	            System.out.println("2. Login");
	            System.out.println("3.Exit");
	            
	            System.out.print("Enter your choice: ");
	            choice = Integer.parseInt(scanner.nextLine());
	            switch (choice) {
	            case 1:
	                UserService.registerNewAdmin();
	                break;
	            case 2:
	                UserService.login();
	                break;
	            case 3:
	                System.out.println("Exiting...");
	                running = false;
	                break;
	            default:
	                System.out.println("Invalid choice. Please enter a correct option.");
	                break;
	        }
	    }
	        
//	    scanner.close();
	}
	 
	 public static void showBookingOptions(long mobileNumber) throws ClassNotFoundException, SQLException{
		  

		 Scanner scanner = new Scanner(System.in);
		 
			 int choice;
		 System.out.println("\n booking Options:");
		 System.out.println("1.Plan journey");
         System.out.println("2.Reschedule booking date");
         System.out.println("3.Exit");
         
         System.out.print("Enter your choice: ");
         choice = scanner.nextInt();
         scanner.nextLine();
         
         
         switch (choice) {
         case 1:
        	  JourneyService.planJourney(mobileNumber);
             break;
         case 2:
        	 JourneyService.reScheduleJourney();
             break;
          default:
             System.out.println("Invalid choice. Please enter a correct option.");
             break;
	   }
         scanner.close();
	 }
	 private static boolean displayCompanyLogo() {
	        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\koppu\\OneDrive\\Documents\\Companylogo.txt"))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                System.out.println(line);
	            }
	            return true; // Logo loaded successfully
	        } catch(IOException e) {
	            System.err.println("Error reading company logo file: " + e.getMessage());
	            return false; // Logo failed
	        }
	    }

}
