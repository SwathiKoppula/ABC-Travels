package com.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import com.model.Constants;

public class JourneyRepository {

	static RoutesRepository routesRepository = new RoutesRepository();
	Scanner scanner = new Scanner(System.in);
	ArrayList<Integer> busNumbers = new ArrayList<Integer>();
	
	public static Connection getconnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		 return DriverManager.getConnection(Constants.url,Constants.username,Constants.password);
	}

	
	public int bookTickets(String source,String destination,LocalDate journeyDate,int noOfPassengers,long mobileNumber) throws ClassNotFoundException, SQLException {
		float totalCost = 0;
		busNumbers = routesRepository.availableRoutes(source, destination, journeyDate, noOfPassengers);
		if(busNumbers.size()>0) {
		      for(int i=0;i<busNumbers.size();i++) {
		    	  System.out.println(i+busNumbers.get(i));
		    }
		    System.out.println("enter the bus number you want :");
		    int choice = scanner.nextInt();
		    int selectedBusNumber = busNumbers.get(choice-1); 
		    
		    totalCost = routesRepository.getTotalCost(selectedBusNumber,journeyDate,noOfPassengers);
		    
			int bookingId = bookBus(selectedBusNumber,totalCost,mobileNumber,noOfPassengers);
			
			routesRepository.decreseSeatsAvailable(selectedBusNumber,noOfPassengers);
			return bookingId;
		}
		
		else {
			return 0;
		}
		
	}

	public boolean isBookingIdPresent(int bookingId) throws ClassNotFoundException, SQLException {
		int count=0;
		Connection con = JourneyRepository.getconnection();
		PreparedStatement statement = con.prepareStatement("select count(*) from journey where journey_id=?");
		statement.setInt(1, bookingId);
		ResultSet result = statement.executeQuery();
		while(result.next()) {
		count = result.getInt(1);
		}
		if(count==1) {
			return true;
		}
		return false;
	}

	
//	public int updateJourney(int bookingId,LocalDate journeyDate) throws SQLException, ClassNotFoundException {
//		
//		 
//		String source="";
//		String destination = "";
//		int noOfPassengers = 0;
//		long mobileNumber = 0;
//		
//		Connection con = JourneyRepository.getconnection();
//		PreparedStatement statement = con.prepareStatement("select routes.source,routes.destination,journey.no_of_passengers,journey.mobileNumber from routes inner JOIN journey ON journey.bus_number = routes.bus_number where journey.journey_id=?");
//		statement.setInt(1, bookingId);
//		ResultSet result = statement.executeQuery();
//		while(result.next()) {
//		     source = result.getString(1);
//			 destination = result.getString(2);
//		     noOfPassengers = result.getInt(3);
//			 mobileNumber = result.getLong(4);
//		}
//		    deleteJourney(bookingId);	
//			int bookingId2 = bookTickets(source,destination,journeyDate,noOfPassengers,mobileNumber);
//			System.out.println(source+destination+journeyDate);
//			System.out.println(bookingId2);
//			return bookingId2;
//		
//	}
	
	private int  bookBus(int busNumber,float totalCost,long mobileNumber,int noOfPassengers) throws ClassNotFoundException, SQLException {
		Connection con = JourneyRepository.getconnection();
		PreparedStatement statement = con.prepareStatement("insert into journey (mobileNumber,total_cost,bus_number,no_of_passengers) values (?,?,?)");
		       statement.setLong(1,mobileNumber);
		       statement.setFloat(2,totalCost);
		       statement.setInt(3, busNumber);
		       statement.setInt(4, noOfPassengers);
		       statement.executeUpdate();
		       int journeyId = getJourneyId(busNumber,mobileNumber);
		       return journeyId;
		
	}
	
	public int getJourneyId(int busNumber, long mobileNumber) throws SQLException, ClassNotFoundException {
		
		int journeyId =0;
		Connection con = JourneyRepository.getconnection();
		PreparedStatement statement = con.prepareStatement("select journey_id from journey where mobileNumber=? and bus_number=?");
		statement.setLong(1, mobileNumber);
		statement.setInt(2, busNumber);
		ResultSet result = statement.executeQuery();
		while(result.next()) {
		journeyId = result.getInt("journey_id");
		}

		return journeyId ;
		
	}
	
//	public void deleteJourney(int bookingId) throws ClassNotFoundException, SQLException {
//		Connection con = JourneyRepository.getconnection();
//		PreparedStatement statement = con.prepareStatement("DELETE FROM journey WHERE journey_id = ?");
//		statement.setInt(1, bookingId);
//		statement.executeUpdate();
//		}
	
	public int updateJourney(int bookingId,LocalDate journeyDate) throws SQLException, ClassNotFoundException{
		
	     int busNumber = getBusNumberByDate(journeyDate);
	     Connection con = JourneyRepository.getconnection();
			PreparedStatement statement = con.prepareStatement("update journey set bus_number=? where journey_id=?");
			statement.setInt(1,busNumber);
			statement.setInt(2, bookingId);
			statement.executeUpdate();
			System.out.println("updateded successfully");
		return bookingId;
		
	}

	  public int getBusNumberByDate(LocalDate journeyDate) throws SQLException, ClassNotFoundException {
		Date date = Date.valueOf(journeyDate) ;
		int	busNumber = 0;
		Connection con = JourneyRepository.getconnection();
		PreparedStatement statement = con.prepareStatement("select bus_number from routes where date=?");
		statement.setDate(1,date);
		ResultSet result = statement.executeQuery();
		while(result.next()) {
		busNumber = result.getInt(1);
		}
		return busNumber;
	}
	  
	  
	  
	  public void updateSeats(int bookingId,int noOfPassengers) throws ClassNotFoundException, SQLException {
		  int busNumber = getBusNumberById(bookingId);
		  routesRepository.increseSeatsAvailable(busNumber,noOfPassengers);
		  
	  }
	  
	 public int getBusNumberById(int bookingId) throws ClassNotFoundException, SQLException {
		 
//		 int busNumber = 0;
		 Connection con = JourneyRepository.getconnection();
			PreparedStatement statement = con.prepareStatement("select bus_number from journey where journey_id=?");
			statement.setInt(1, bookingId);
			ResultSet result = statement.executeQuery();
			int busNumber = 0;
			while(result.next()) {
				busNumber = result.getInt(1);
			}
		return busNumber;
		 
	 }


	public int getNoOfPassengers(int bookingId) throws ClassNotFoundException, SQLException {
		
		int noOfPassengers = 0;
		Connection con = JourneyRepository.getconnection();
		PreparedStatement statement = con.prepareStatement("select no_of_passengers from journey where journey_id=?");
		statement.setInt(1,bookingId);
		ResultSet result = statement.executeQuery();
		while(result.next()) {
			noOfPassengers = result.getInt(1);
		}
		return noOfPassengers;
	}

}
