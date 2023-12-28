import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProductOrderMain {

	public static void main(String[] args) {
		
		//Qno.1 Inserting product details into database
		try {
			ProductDOA.insertProductDetailsIntoDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// First of all displaying all product details from database to the customer
		try {
			List<Product> productlist = ProductDOA.displayAllProductDetails();
				System.out.println(":::::The list of all products is as below::::::");
				System.out.println();
				System.out.printf("%-20s%-20s%-20s%-20s%-20s%n", "ProductID", "ProductName", "ProductDescription", "ProductPrice", "ProductQuantity");
			
				for(Product p: productlist) {
					System.out.printf("%-20s%-20s%-20s%-20s%-20s%n", p.getProductID(), p.getProductName(),
						p.getProductDescription(), p.getProductPrice(), p.getProductQuantity());
					}
			
			} catch (Exception e) {
					e.printStackTrace();
				}	

		//Qno. 2 Displaying customer related product details
		try {
			Product pd = ProductDOA.displayOnlyParticularProductDetails();		
			System.out.printf("%-20s%-20s%-20s%-20s%-20s%n", "ProductID", "ProductName", "ProductDescription",
					"ProductPrice", "ProductQuantity");
			System.out.printf("%-20s%-20s%-20s%-20s%-20s%n", pd.getProductID(), pd.getProductName(),
					pd.getProductDescription(), pd.getProductPrice(), pd.getProductQuantity());
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		//Qno.3 CustomerPlacingOrder
			int noOfOrder = ProductDOA.customerOrderAndInsertOrderDetailsIntoDB();
			
		
			
		//Qno.4 BillGenerationInPDF
			ProductDOA.generateBillForEachOrder(noOfOrder);
			
	}
}
		
		
		
		
	
