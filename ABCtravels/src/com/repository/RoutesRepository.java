package com.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import com.model.Constants;

public class RoutesRepository {
	
	ArrayList<Integer> busNumbers = new ArrayList<Integer>();
	
	
	
	private Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection(Constants.url,Constants.username,Constants.password);
		return connection;
	}

	public ArrayList<Integer> availableRoutes(String source, String destination, LocalDate journeyDate, int noOfPassengers) throws ClassNotFoundException, SQLException {
		
		Date date = Date.valueOf(journeyDate) ;
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("select bus_number from routes where source=? and destination=? and date=? and seats_available>?");
		statement.setString(1,source);
		statement.setString(2, destination);
		statement.setDate(3,date);
		statement.setInt(4, noOfPassengers);
		ResultSet result = statement.executeQuery();
		
		while(result.next()) {
			busNumbers.add(result.getInt("bus_number"));
		}
		System.out.println(source);
		return busNumbers;
	}

	public float getTotalCost(int busNumber,LocalDate journeyDate,int noOfPassengers) throws ClassNotFoundException, SQLException {
		
		float totalCost = 0;
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("select price_for_one_person from routes where bus_number=?");
		statement.setInt(1, busNumber);
		ResultSet pricePerPassenger = statement.executeQuery();
		while(pricePerPassenger.next()) {
		totalCost = (pricePerPassenger.getFloat(1))*noOfPassengers;
		}
		 if (journeyDate.getDayOfWeek() == DayOfWeek.SATURDAY || journeyDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
             totalCost += 200; // Weekend surge
             totalCost += totalCost * 0.1; // Adding 10% GST
         }
		return totalCost;
	}

	
	
	public void decreseSeatsAvailable(int selectedBusNumber,int noOfPassengers) throws ClassNotFoundException, SQLException {
		
		int seatsAvailable = getseatsAvailable(selectedBusNumber);
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("update routes set seats_available=? where bus_number=?");
		statement.setInt(1, seatsAvailable-noOfPassengers);
		statement.setInt(2, selectedBusNumber);
		statement.executeUpdate();
		}

	public void increseSeatsAvailable(int busNumber,int noOfPassengers) throws ClassNotFoundException, SQLException {
		
		int seatsAvailable = getseatsAvailable(busNumber);
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("update routes set seats_available=? where bus_number=?");
		statement.setInt(1, seatsAvailable+noOfPassengers);
		statement.setInt(2, busNumber);
		statement.executeUpdate();
		
	}
	
	public int getseatsAvailable(int busNumber) throws ClassNotFoundException, SQLException {
		
		int seatsAvailable = 0;
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("select seats_available from routes where bus_number=?");
		statement.setInt(1, busNumber);
		ResultSet resultSet = statement.executeQuery();
		while(resultSet.next()) {
			seatsAvailable = resultSet.getInt(1);
		}
		return seatsAvailable;
		
	}
	
	

}

