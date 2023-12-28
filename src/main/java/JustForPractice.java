import java.sql.*;
import java.util.Scanner;
import java.util.UUID;

public class JustForPractice {

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database_name";
    static final String USER = "your_username";
    static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            createTables(connection);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("1. Register Product\n2. Search Product\n3. Place Order\n4. Exit");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        registerProduct(connection);
                        break;
                    case 2:
                        searchProduct(connection);
                        break;
                    case 3:
                        placeOrder(connection);
                        break;
                    case 4:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // Create Product table
            String createProductTableSQL = "CREATE TABLE IF NOT EXISTS Product (" +
                    "productId VARCHAR(36) PRIMARY KEY," +
                    "productName VARCHAR(255) NOT NULL," +
                    "description VARCHAR(255)," +
                    "price DOUBLE NOT NULL," +
                    "quantityAvailable INT NOT NULL" +
                    ")";
            statement.executeUpdate(createProductTableSQL);

            // Create Order table
            String createOrderTableSQL = "CREATE TABLE IF NOT EXISTS Orders (" +
                    "orderId VARCHAR(36) PRIMARY KEY," +
                    "productName VARCHAR(255) NOT NULL," +
                    "quantity INT NOT NULL," +
                    "price DOUBLE NOT NULL," +
                    "totalPrice DOUBLE NOT NULL," +
                    "discount DOUBLE NOT NULL" +
                    ")";
            statement.executeUpdate(createOrderTableSQL);
        }
    }

    private static void registerProduct(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter product name:");
        String productName = scanner.nextLine();
        System.out.println("Enter product description:");
        String description = scanner.nextLine();
        System.out.println("Enter product price:");
        double price = scanner.nextDouble();
        System.out.println("Enter quantity available:");
        int quantity = scanner.nextInt();

        // Generate UUID for productId
        String productId = UUID.randomUUID().toString();

        // Insert product into Product table
        String insertProductSQL = "INSERT INTO Product VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {
            preparedStatement.setString(1, productId);
            preparedStatement.setString(2, productName);
            preparedStatement.setString(3, description);
            preparedStatement.setDouble(4, price);
            preparedStatement.setInt(5, quantity);

            preparedStatement.executeUpdate();
            System.out.println("Product registered successfully!");
        }
    }

    private static void searchProduct(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter product name to search:");
        String productName = scanner.nextLine();

        // Search for the product by name
        String searchProductSQL = "SELECT * FROM Product WHERE productName LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(searchProductSQL)) {
            preparedStatement.setString(1, "%" + productName + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Product ID: " + resultSet.getString("productId"));
                System.out.println("Product Name: " + resultSet.getString("productName"));
                System.out.println("Description: " + resultSet.getString("description"));
                System.out.println("Price: " + resultSet.getDouble("price"));
                System.out.println("Quantity Available: " + resultSet.getInt("quantityAvailable"));
                System.out.println("---------------------------");
            }
        }
    }

    private static void placeOrder(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter product name to order:");
        String productName = scanner.nextLine();

        // Check if the product is available
        String checkProductAvailabilitySQL = "SELECT * FROM Product WHERE productName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkProductAvailabilitySQL)) {
            preparedStatement.setString(1, productName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int quantityAvailable = resultSet.getInt("quantityAvailable");

                if (quantityAvailable > 0) {
                    System.out.println("Enter quantity to order:");
                    int orderQuantity = scanner.nextInt();
                    if (orderQuantity > quantityAvailable) {
                        System.out.println("Insufficient quantity available!");
                        return;
                    }

                    // Generate UUID for orderId
                    String orderId = UUID.randomUUID().toString();

                    // Calculate total price and apply any discounts (you can customize this logic)
                    double price = resultSet.getDouble("price");
                    double discount = 0.0; // you can implement discount logic here
                    double totalPrice = price * orderQuantity * (1 - discount);

                    // Insert order into Orders table
                    String insertOrderSQL = "INSERT INTO Orders VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement orderStatement = connection.prepareStatement(insertOrderSQL)) {
                        orderStatement.setString(1, orderId);
                        orderStatement.setString(2, productName);
                        orderStatement.setInt(3, orderQuantity);
                        orderStatement.setDouble(4, price);
                        orderStatement.setDouble(5, totalPrice);
                        orderStatement.setDouble(6, discount);

                        orderStatement.executeUpdate();
                        System.out.println("Order placed successfully!");
                    }

                    // Update quantity in Product table
                    int updatedQuantity = quantityAvailable - orderQuantity;
                    String updateQuantitySQL = "UPDATE Product SET quantityAvailable = ? WHERE productName = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuantitySQL)) {
                        updateStatement.setInt(1, updatedQuantity);
                        updateStatement.setString(2, productName);

                        updateStatement.executeUpdate();
                    }
                } else {
                    System.out.println("Product out of stock!");
                }
            } else {
                System.out.println("Product not found!");
            }
        }
    }
}