import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class ProductDOA {

	public static void insertProductDetailsIntoDB() throws SQLException {
		try {
			int id = 100;

			System.out.println("Enter the no. of product details admin want to register in the product table");
			Scanner sc = new Scanner(System.in);
			int noOfProductDetails = sc.nextInt();

			int status = 0;
			int count = 0;
			for (int i = 0; i < noOfProductDetails; i++) {

				Connection conn = DatabaseConn.getConnection();
				String SQL = "insert into product (productID, productname, productdescription, productprice, productquantity)"
						+ "values(?,?,?,?,?)";
				Product product = new Product();

				product.setProductID(++id);
				System.out.println("Input Product Name: ");
				product.setProductName(sc.next());
				System.out.println("Input Product Description: ");
				product.setProductDescription(sc.next());
				System.out.println("Input Product Price: ");
				product.setProductPrice(sc.nextInt());
				System.out.println("Input Availabe Product Quantity: ");
				product.setProductQuantity(sc.nextInt());

				PreparedStatement ps = conn.prepareStatement(SQL);
				ps.setInt(1, product.getProductID());
				ps.setString(2, product.getProductName());
				ps.setString(3, product.getProductDescription());
				ps.setInt(4, product.getProductPrice());
				ps.setInt(5, product.getProductQuantity());

				status = ps.executeUpdate();

				if (status == 1) {
					System.out.println(++count + " record inserted into DB successfully.");

				}

				ps.close();
				conn.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static List<Product> displayAllProductDetails() throws SQLException {

		List<Product> productlist = new ArrayList<Product>();
		try {
			Connection conn = DatabaseConn.getConnection();
			String sql = "select * from product";

			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(sql);

			while (rs.next()) {
				Product pd = new Product();

				pd.setProductID(rs.getInt(1));
				pd.setProductName(rs.getString(2));
				pd.setProductDescription(rs.getString(3));
				pd.setProductPrice(rs.getInt(4));
				pd.setProductQuantity(rs.getInt(5));

				productlist.add(pd);
			}

			ps.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return productlist;

	}

	public static Product displayOnlyParticularProductDetails() {

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the product name you want to search?");
		String productName = sc.next();
		Product pd = new Product();
		try {
			Connection conn = DatabaseConn.getConnection();
			String sql = "select * from product where productname=?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, productName);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				pd.setProductID(rs.getInt(1));
				pd.setProductName(rs.getString(2));
				pd.setProductDescription(rs.getString(3));
				pd.setProductPrice(rs.getInt(4));
				pd.setProductQuantity(rs.getInt(5));
			}

			ps.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return pd;
	}

	public static int customerOrderAndInsertOrderDetailsIntoDB() {
			
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter how many product items you want to order?");
		int noOfOrder = sc.nextInt();
		try {
			System.out.println("Thank you for ordering " + noOfOrder + " products.");
			System.out.println("****************************************************");
			int orderCounting = 1;
			int recordCounting = 1;
			for (int i = 0; i < noOfOrder; i++) {
			
				Order order = new Order();
				
				System.out.println( orderCounting++ + " product name you want to order?");
				order.setProductName(sc.next()); // Product name inserted into object

				System.out.println("Enter the quantity?");
				order.setProductQuantity(sc.nextInt()); // Product order quantity inserted into object
				
				
				// Fetching from the name of product and matching the name to set the price of
				// the product

					// First establishing connection with product table
						Connection conn = DatabaseConn.getConnection();
						String SQL = "select productname, productprice from product";
				
						PreparedStatement ps = conn.prepareStatement(SQL);
						ResultSet rs = ps.executeQuery();

							while (rs.next()) {
								String pn = rs.getString(1); // here we can write column name or column index.

									if (pn.equalsIgnoreCase(order.getProductName())) {
											int pp = rs.getInt(2);
												order.setProductPrice(pp); // Product price inserted into object
										}
									}

								order.setProductTotalPrice(order.getProductQuantity() * order.getProductPrice()); // Product total price
																													// inserted into
																													// object

				if (order.getProductTotalPrice() > 1000) {
					System.out.println("Total price amounted for the product order is > 1000, you got a discount of Rs100 for this orer!!");
					order.setProductdiscout(100);
					order.setProductTotalPrice(order.getProductTotalPrice() - order.getProductdiscout());
					System.out.println();
				} else {
					System.out.println("Total price amounted for the product order is < 1000, there will be no discount for this order.");
					order.setProductdiscout(0);
					System.out.println();
				}
				
				String newSQL = "insert into ordered (orderID, productname, productquantity, productprice, producttotalprice)"
						+ "values(?,?,?,?,?)";
				
				
				PreparedStatement newps = conn.prepareStatement(newSQL);
				newps.setInt(1, order.getOrderID());
				newps.setString(2, order.getProductName());
				newps.setInt(3, order.getProductQuantity());
				newps.setInt(4, order.getProductPrice());
				newps.setInt(5, order.getProductTotalPrice());

				int status = newps.executeUpdate();
					if (status == 1) {
						System.out.println(recordCounting++ + " Order recorded into Order table successfully.");
						System.out.println();
						}
				ps.close();
				newps.close();
				conn.close();
				System.out.println();
			
			} 
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return noOfOrder;
			
	}		
			
	
	public static void generateBillForEachOrder(int noOfOrder) {
			
	         	String heading = "*********Thank you for placing the orders*********";
	     		
	     		System.out.println();
	 		try {
	 			
	 			String Title1 = "OrderID: ";
	     		String Title2 = "Product Name: ";
	     		String Title3 = "Product Quantity: ";
	     		String Title4 = "Product Price: ";
	     		String Title5 = "Product Total Price:";
	     		
	 		for(int i=0; i<noOfOrder; i++) {
	 				
	 			
	 			Connection conn = DatabaseConn.getConnection();
	 			String sql = "select * from ordered";

				 Order ord = new Order();
				 PreparedStatement ps = conn.prepareStatement(sql);
				 ResultSet rs = ps.executeQuery();
																	
				if (rs.next()) {
					ord.setOrderID(rs.getInt(1));
					ord.setProductName(rs.getString(2));
					ord.setProductQuantity(rs.getInt(3));
					ord.setProductPrice(rs.getInt(4));
					ord.setProductTotalPrice(rs.getInt(5));
					
					File templateFile = new File("C:/Users/s011271sur/eclipse-workspace/ProductOrder/src/main/resource/OrderBill"+i+".pdf"); 
	 				PDDocument pdDocument = new PDDocument();
	 				
					PDPage pdPages = new PDPage();
					pdDocument.addPage(pdPages);
					
					PDPageContentStream pdPageContentStream = new PDPageContentStream(pdDocument, pdPages);
			        pdPageContentStream.beginText();
			        pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
			        pdPageContentStream.setLeading(14.6f); //
			        pdPageContentStream.newLineAtOffset(25, pdPages.getTrimBox().getHeight()-25);
		 			
			        pdPageContentStream.showText(heading);
			        pdPageContentStream.newLine();
			        pdPageContentStream.newLine();
			        
			        pdPageContentStream.showText(Title1);
			        String values1 = Integer.toString(ord.getOrderID());
			    	pdPageContentStream.showText(values1);
			        pdPageContentStream.newLine();
			        
			        pdPageContentStream.showText(Title2);
			        String Values2 = ord.getProductName();
			    	pdPageContentStream.showText(Values2);
			        pdPageContentStream.newLine();
			        
			        pdPageContentStream.showText(Title3);
			        String Values3 =Integer.toString(ord.getProductQuantity());
			    	pdPageContentStream.showText(Values3);
			        pdPageContentStream.newLine();
			        
			        pdPageContentStream.showText(Title4);
			        String Values4 = Integer.toString(ord.getProductPrice());
			    	pdPageContentStream.showText(Values4);
			        pdPageContentStream.newLine();
			        
			        pdPageContentStream.showText(Title5);
			        String Values5 = Integer.toString(ord.getProductTotalPrice());
			    	pdPageContentStream.showText(Values5);
			        pdPageContentStream.newLine();
			        
					pdPageContentStream.endText();
			        pdPageContentStream.close();
			        pdDocument.save(templateFile);	        
			        pdDocument.close();
				}				
				ps.close();
				conn.close();			
			}			
			
			System.out.println("PDFs for all the orderd products been successfully created!!");
	 			
	 		} catch(Exception e) {
	 		e.printStackTrace();
	 		}
	}
}


