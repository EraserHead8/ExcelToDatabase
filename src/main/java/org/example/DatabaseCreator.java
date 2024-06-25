import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseCreator {
    public static void createDatabaseAndTablesIfNotExist(Connection connection, String dbName, String username, String password) throws SQLException {
        if (!databaseExists(connection, dbName)) {
            createDatabase(connection, dbName);
        }

        try (Connection dbConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, username, password)) {
            createTablesIfNotExist(dbConnection);
        }
    }

    private static boolean databaseExists(Connection connection, String dbName) throws SQLException {
        String sql = "SELECT 1 FROM pg_database WHERE datname = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dbName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private static void createDatabase(Connection connection, String dbName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE " + dbName);
            System.out.println("Database created: " + dbName);
        }
    }

    private static void createTablesIfNotExist(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // Создание таблиц
            String createProductsTable = "CREATE TABLE IF NOT EXISTS products (" +
                    "seller_sku VARCHAR(255) PRIMARY KEY," +
                    "name VARCHAR(255)," +
                    "category VARCHAR(255)," +
                    "account_name VARCHAR(255)," +
                    "brand VARCHAR(255))";
            statement.executeUpdate(createProductsTable);

            String createArticlesTable = "CREATE TABLE IF NOT EXISTS articles (" +
                    "seller_sku VARCHAR(255) PRIMARY KEY REFERENCES products(seller_sku) ON DELETE CASCADE," +
                    "barcode VARCHAR(255)," +
                    "supplier_sku VARCHAR(255)," +
                    "wb_sku VARCHAR(255))";
            statement.executeUpdate(createArticlesTable);

            String createPricingTable = "CREATE TABLE IF NOT EXISTS pricing (" +
                    "seller_sku VARCHAR(255) PRIMARY KEY REFERENCES products(seller_sku) ON DELETE CASCADE," +
                    "purchase_price DECIMAL(10, 2)," +
                    "regular_price DECIMAL(10, 2)," +
                    "retail_price DECIMAL(10, 2)," +
                    "discount_price DECIMAL(10, 2)," +
                    "supplier_stock INTEGER," +
                    "our_stock INTEGER," +
                    "warehouse_stock INTEGER)";
            statement.executeUpdate(createPricingTable);

            String createAccountingTable = "CREATE TABLE IF NOT EXISTS accounting (" +
                    "seller_sku VARCHAR(255) PRIMARY KEY REFERENCES products(seller_sku) ON DELETE CASCADE," +
                    "wb_logistics_fee DECIMAL(10, 2)," +
                    "wb_commission DECIMAL(10, 2)," +
                    "our_logistics DECIMAL(10, 2)," +
                    "salary DECIMAL(10, 2)," +
                    "packaging DECIMAL(10, 2)," +
                    "credit DECIMAL(10, 2)," +
                    "designer DECIMAL(10, 2)," +
                    "admin DECIMAL(10, 2))";
            statement.executeUpdate(createAccountingTable);

            String createProductDimensionsTable = "CREATE TABLE IF NOT EXISTS product_dimensions (" +
                    "seller_sku VARCHAR(255) PRIMARY KEY REFERENCES products(seller_sku) ON DELETE CASCADE," +
                    "width DECIMAL(10, 2)," +
                    "length DECIMAL(10, 2)," +
                    "height DECIMAL(10, 2)," +
                    "weight DECIMAL(10, 2)," +
                    "photo_url_1 TEXT," +
                    "photo_url_2 TEXT," +
                    "photo_url_3 TEXT," +
                    "photo_url_4 TEXT," +
                    "photo_url_5 TEXT," +
                    "photo_url_6 TEXT,"

                    +
                    "photo_url_7 TEXT)";
            statement.executeUpdate(createProductDimensionsTable);

            System.out.println("Tables created or already exist.");
        }
    }
}
