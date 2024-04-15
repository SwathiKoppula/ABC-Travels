package com.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.repository.JourneyRepository;
import com.repository.UserRepository;

public class JourneyService {

      static JourneyRepository journeyRepository = new JourneyRepository();
      public static UserRepository UserRepository = new UserRepository();
	
	public void planJourney(long mobileNumber) throws ClassNotFoundException, SQLException {
		
		Scanner scanner = new Scanner(System.in);
        System.out.println("\n Plan Journey");
        String source;
        String destination;
        LocalDate journeyDate;
        int noOfPassengers;
         boolean check = true;
        
         
        // Getting journey details from the user
        while(check) {
        	
//        	System.out.println("enter the mobile number");
//        	mobileNumber = scanner.nextLong();
//        	if(UserRepository.isUserAvailable(mobileNumber)&&UserRepository.checkAccountStatus(mobileNumber)) {
        System.out.print("Enter source: ");
         source = scanner.nextLine();
         
         System.out.print("Enter destination: ");
             destination = scanner.nextLine();
              
             System.out.print("Enter journey date (YYYY-MM-DD): ");
           String journeyDateString = scanner.nextLine();
              journeyDate = LocalDate.parse(journeyDateString, DateTimeFormatter.ISO_LOCAL_DATE);
              
              System.out.print("Enter number of passengers: ");
                  noOfPassengers = scanner.nextInt();
                  scanner.nextLine();
                  
          if(validateDate(journeyDate)&&(!source.equalsIgnoreCase(destination))&& (noOfPassengers>0)) {
        	  
        	  bookAvailableRoutes(source,destination,journeyDate,noOfPassengers, mobileNumber);
        	  check = false;
          }
          else {
        	  System.out.println("check the datails entered");
          }
             
        }
      //}
       scanner.close();
		
	}

	private boolean validateDate(LocalDate journeyDate) {
		if(journeyDate.isAfter(LocalDate.now())) {
			return true;
		}
		return false;
	}

	public void reScheduleJourney() throws ClassNotFoundException, SQLException {
		
		Scanner scanner = new Scanner(System.in);
		LocalDate journeyDate;
		System.out.println("enter your bookingId");
		int bookingId = scanner.nextInt();
		scanner.nextLine();
		if(journeyRepository.isBookingIdPresent(bookingId)) {
			
			System.out.println("enter new journeyDate");
			String journeyDateString = scanner.nextLine();
            journeyDate = LocalDate.parse(journeyDateString, DateTimeFormatter.ISO_LOCAL_DATE);
            if(validateDate(journeyDate)) {
            	int bookingId2 = journeyRepository.updateJourney(bookingId,journeyDate);
            	if(bookingId2>0) {
            		System.out.println("booking confirmed");
                	System.out.println("your booking id is"+bookingId2);
                	int noOfPassengers = journeyRepository.getNoOfPassengers(bookingId);
        			journeyRepository.updateSeats(bookingId,noOfPassengers);
        
            	}
            	else{
    				System.out.println("buses are not available in this date");
    			}
            }
            else {
            	System.out.println("date is not valid");
            }
		} 	
            	
		scanner.close();
	}
	
	public void bookAvailableRoutes(String source,String destination,LocalDate journeyDate,int noOfPassengers,long mobileNumber) throws ClassNotFoundException, SQLException {
		int bookingId = journeyRepository.bookTickets(source,destination,journeyDate,noOfPassengers,mobileNumber);
	   if(bookingId==0) {
		   System.out.println("buses are not available for this"+destination);
	   }
	   else { 
		   System.out.println("tickets booked");
	  System.out.println("your booking number is"+bookingId);
	   }
	}
	
	
	
}

