//Make Sure You Having Database libraries and your sql server is on

package RRS;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class railway {
//	Methods
	boolean createConnection(String s_fname, String s_lname, String s_password) {
		// Accepting value from database
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/RRS_CJ", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from signup");

			while (rs.next()) {
				String Name = rs.getString("Name");
				String Surname = rs.getString("Surname");
				String Password = rs.getString("Password");

				if (Name.equals(s_fname)) {
					if (Surname.equals(s_lname)) {
						if (Password.equals(s_password)) {
							System.out.println("SUCCESSFULLY LOGIN :) \n");
							return true;
						}
					}
				}
			}

		} catch (ClassNotFoundException ex) {
			Logger.getLogger(railway.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SQLException ex) {
			Logger.getLogger(railway.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	private int check(String Source, String Destination) {
		int Source_Station_ID = 0;
		int Destination_Station_ID = 0;
		int Distance = 0;
		boolean S_Error = false, D_Error = false;
		Source = Source.toUpperCase();
		Destination = Destination.toUpperCase();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/RRS_CJ", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from station");

			while (rs.next()) {
				Source_Station_ID = Integer.parseInt(rs.getString("Station_ID"));
				String Station_Name = rs.getString("Station_Name");
				if (Source.equals(Station_Name)) {
					S_Error = false;
					break;
				} else {
					S_Error = true;
				}
			}

			rs = stmt.executeQuery("select * from station");
			while (rs.next()) {
				Destination_Station_ID = Integer.parseInt(rs.getString("Station_ID"));
				String Station_Name = rs.getString("Station_Name");
				if (Destination.equals(Station_Name)) {
					D_Error = false;
					break;
				} else {
					D_Error = true;
				}
			}

			System.out.println();
			if (S_Error == true) {
				System.out.println("SOURCE NOT FOUND....");
			}
			if (D_Error == true) {
				System.out.println("DESTINATION NOT FOUND....");
				return Distance;
			}

		} catch (ClassNotFoundException ex) {
			Logger.getLogger(railway.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SQLException ex) {
			Logger.getLogger(railway.class.getName()).log(Level.SEVERE, null, ex);
		}

		// Calculating station distance between source and destination
		if (Source_Station_ID > Destination_Station_ID) {
			Distance = Source_Station_ID - (Destination_Station_ID + 1);
			return Distance;
		} else {
			Distance = Destination_Station_ID - (Source_Station_ID + 1);
			return Distance;
		}
	}

	private int Fare(int Distance, int Ticket, String Return) {
		Return = Return.toUpperCase();
		int Fare = 0;
		if (Distance < 3) {
			Fare = 5;
		} else if (Distance < 8) {
			Fare = 10;
		} else if (Distance < 17) {
			Fare = 15;
		} else if (Distance <= 23) {
			Fare = 20;
		}
		Fare = Fare * Ticket;
		if (Return.equals("Y")) {
			Fare = Fare * 2;
		}
		System.out.println("Fare: " + Fare);
		return Fare;
	}

//	Main
	public static void main(String[] args) {

		railway RRS = new railway();

		Scanner sc = new Scanner(System.in);
		Scanner int_sc = new Scanner(System.in);
		String Name = null, Surname = null;
		String Password = null, confirm_password = null;
		int Fare = 0;

		while (true) {
			sc.reset();
			int_sc.reset();
			System.out.println(
					"\n==========================================================================================");
			System.out.println("\t\t\tWELOCOME TO RAILWAY RESERVATION");
			System.out.println(
					"==========================================================================================\n");
			System.out.println("\t1.SIGNUP");
			System.out.println("\t2.LOGIN");
			System.out.println("\t3.EXIT\n");

			System.out.print("\tSELECT AN OPTION:-");
			int Select_option = int_sc.nextInt();

			// 1 st page
			if (Select_option == 1) {
				System.out.println(
						"\n\n==========================================================================================");
				System.out.println("\t\t\t\t\tSign UP");
				System.out.println(
						"==========================================================================================\n");

				System.out.print("ENTER YOUR NAME:-");
				Name = sc.next();

				System.out.print("ENTER YOUR SURNAME :-");
				Surname = sc.next();

				System.out.print("PASSWORD :-");
				Password = sc.next();

				System.out.print("CONFIRM PASSWORD :-");
				confirm_password = sc.next();
				if (Password.equals(confirm_password)) {
					String query = "insert into signup values('" + Name + "','" + Surname + "','" + Password + "')";
					// Inserting value in database
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/RRS_CJ", "root", "");
						Statement stmt = con.createStatement();
						stmt.execute(query);
						stmt.close();
					} catch (ClassNotFoundException ex) {
						Logger.getLogger(railway.class.getName()).log(Level.SEVERE, null, ex);
					} catch (SQLException ex) {
						Logger.getLogger(railway.class.getName()).log(Level.SEVERE, null, ex);
					}
					System.out.print("SIGNUP SUCCESSFULL!!!\n");
				} else {
					System.out.println("'PASSWORD' & 'CONFIRM PASSWORD' NOT MATCHING.\n");
				}

			}

			if (Select_option == 2) {
				System.out.println(
						"\n\n==========================================================================================");
				System.out.println("\t\t\t\t\tLogin");
				System.out.println(
						"==========================================================================================\n");
				System.out.print("NAME :-");
				Name = sc.next();
				System.out.print("SURNAME :-");
				Surname = sc.next();
				System.out.print("PASSWORD :-");
				Password = sc.next();

				boolean login = RRS.createConnection(Name, Surname, Password);
				while (true) {
					if (login == true) {

						System.out.println(
								"\n\n==========================================================================================");
						System.out.print("\t\t\tRAILWAY TICKET RESERVATION\n");
						System.out.println(
								"==========================================================================================\n");
						System.out.println("\nUSER: " + Name + " " + Surname);
						System.out.println("\n\t1. BOOK TICKET");
						System.out.println("\t2. VIEW TICKET");
						System.out.print("\n\tENTER CHOICE (Enter '0' for exit): ");
						int Choice = int_sc.nextInt();

						String Source = "NULL";
						String Destination = "NULL";
						String Return = "NULL";
						int Ticket = 0;

						switch (Choice) {

						case 1:
							System.out.println(
									"\n\n==========================================================================================");
							System.out.print("\t\t\t\tBOOK TICKET\n");
							System.out.println(
									"==========================================================================================\n");
							System.out.println("\nUSER: " + Name + " " + Surname);
							System.out.print("\n\nENTER SOURCE: ");
							Source = sc.next();

							System.out.print("ENTER DESTINATION: ");
							Destination = sc.next();

							System.out.print("ENTER TICKET: ");
							Ticket = int_sc.nextInt();

							System.out.print("DO YOU WANT RETURN TICKET(Y/N): ");
							Return = sc.next();
							int Distance = RRS.check(Source, Destination);

							if (Distance > 0) {
								Fare = RRS.Fare(Distance, Ticket, Return);
							} else if (Distance == -1) {
								Fare = RRS.Fare(Distance, Ticket, Return);
							} else {
								System.out.println("\n\nThere is error in data please write again.");
							}
							sc.reset();
							int_sc.reset();
							break;
						case 2:
							System.out.println(
									"\n\n==========================================================================================");
							System.out.print("\t\t\t\tVIEW TICKET\n");
							System.out.println(
									"==========================================================================================\n");
							System.out.println("\nUSER: " + Name + " " + Surname);
							System.out.println("Source: " + Source.toUpperCase());
							System.out.println("Destination: " + Destination.toUpperCase());
							System.out.println("Seat: " + Ticket);
							System.out.println("Fare: " + Fare);
							System.out.println("Return: " + Return);
							sc.reset();
							int_sc.reset();
							break;
						default:
							System.out.println("\nError!!! YOU ENTER WRONG CHOICE......");
							break;
						}
						if (Choice == 0) {
							System.out.println("\nExiting....");
							break;
						}

					} else {
						System.out.println("\nError!!! YOU ENTER WRONG DATA..");
						break;
					}
				}

			}

			if (Select_option == 3) {
				System.out.print("EXIT SUCCESSFULLY....HAVE A NICE DAY!!!");
				break;
			}
		}

	}

}
