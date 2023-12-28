
public class Order {
			
		static int orderID=1000;
		String productName;
		int productQuantity;
		int productPrice;
		int productTotalPrice;
		int productdiscout;
		
		public int getOrderID() {
			return ++orderID;
		}
		public void setOrderID(int orderID) {
			this.orderID = orderID;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public int getProductQuantity() {
			return productQuantity;
		}
		public void setProductQuantity(int productQuantity) {
			this.productQuantity = productQuantity;
		}
		public int getProductPrice() {
			return productPrice;
		}
		public void setProductPrice(int productPrice) {
			this.productPrice = productPrice;
		}
		public int getProductTotalPrice() {
			return productTotalPrice;
		}
		public void setProductTotalPrice(int productTotalPrice) {
			this.productTotalPrice = productTotalPrice;
		}
		public int getProductdiscout() {
			return productdiscout;
		}
		public void setProductdiscout(int productdiscout) {
			this.productdiscout = productdiscout;
		}
	
		
		
}
