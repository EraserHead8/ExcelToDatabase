import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseHandler {
    private final Connection connection;

    public DatabaseHandler(Connection connection) {
        this.connection = connection;
    }

    public void upsertProduct(String seller_sku, String name, String category, String account_name, String brand) throws Exception {
        if (recordExists("products", seller_sku)) {
            updateProduct(seller_sku, name, category, account_name, brand);
        } else {
            insertProduct(seller_sku, name, category, account_name, brand);
        }
    }

    public void upsertArticle(String seller_sku, String barcode, String supplier_sku, String wb_sku) throws Exception {
        if (recordExists("articles", seller_sku)) {
            updateArticle(seller_sku, barcode, supplier_sku, wb_sku);
        } else {
            insertArticle(seller_sku, barcode, supplier_sku, wb_sku);
        }
    }

    public void upsertPricing(String seller_sku, double purchase_price, double regular_price, double retail_price, double discount_price, int supplier_stock, int our_stock, int warehouse_stock) throws Exception {
        if (recordExists("pricing", seller_sku)) {
            updatePricing(seller_sku, purchase_price, regular_price, retail_price, discount_price, supplier_stock, our_stock, warehouse_stock);
        } else {
            insertPricing(seller_sku, purchase_price, regular_price, retail_price, discount_price, supplier_stock, our_stock, warehouse_stock);
        }
    }

    public void upsertAccounting(String seller_sku, double wb_logistics_fee, double wb_commission, double our_logistics, double salary, double packaging, double credit, double designer, double admin) throws Exception {
        if (recordExists("accounting", seller_sku)) {
            updateAccounting(seller_sku, wb_logistics_fee, wb_commission, our_logistics, salary, packaging, credit, designer, admin);
        } else {
            insertAccounting(seller_sku, wb_logistics_fee, wb_commission, our_logistics, salary, packaging, credit, designer, admin);
        }
    }

    public void upsertProductDimensions(String seller_sku, double width, double length, double height, double weight, String photo_url_1, String photo_url_2, String photo_url_3, String photo_url_4, String photo_url_5, String photo_url_6, String photo_url_7) throws Exception {
        if (recordExists("product_dimensions", seller_sku)) {
            updateProductDimensions(seller_sku, width, length, height, weight, photo_url_1, photo_url_2, photo_url_3, photo_url_4, photo_url_5, photo_url_6, photo_url_7);
        } else {
            insertProductDimensions(seller_sku, width, length, height, weight, photo_url_1, photo_url_2, photo_url_3, photo_url_4, photo_url_5, photo_url_6, photo_url_7);
        }
    }

    private boolean recordExists(String tableName, String seller_sku) throws Exception {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE seller_sku = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, seller_sku);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private void insertProduct(String seller_sku, String name, String category, String account_name, String brand) throws Exception {
        String sql = "INSERT INTO products (seller_sku, name, category, account_name, brand) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, seller_sku);
            statement.setString(2, name);
            statement.setString(3, category);
            statement.setString(4, account_name);
            statement.setString(5, brand);
            statement.executeUpdate();
            System.out.println("Inserted into products: " + seller_sku);
        }
    }

    private void updateProduct(String seller_sku, String name, String category, String account_name, String brand) throws Exception {
        String sql = "UPDATE products SET name = ?, category = ?, account_name = ?, brand = ? WHERE seller_sku = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, category);
            statement.setString(3, account_name);
            statement.setString(4, brand);
            statement.setString(5, seller_sku);
            statement.executeUpdate();
            System.out.println("Updated products: " + seller_sku);
        }
    }

    private void insertArticle(String seller_sku, String barcode, String supplier_sku, String wb_sku) throws Exception {
        String sql = "INSERT INTO articles (seller_sku, barcode, supplier_sku, wb_sku) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, seller_sku);
            statement.setString(2, barcode);
            statement.setString(3, supplier_sku);
            statement.setString(4, wb_sku);
            statement.executeUpdate();
            System.out.println("Inserted into articles: " + seller_sku);
        }
    }

    private void updateArticle(String seller_sku, String barcode, String supplier_sku, String wb_sku) throws Exception {
        String sql = "UPDATE articles SET barcode = ?, supplier_sku = ?, wb_sku = ? WHERE seller_sku = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, barcode);
            statement.setString(2, supplier_sku);
            statement.setString(3, wb_sku);
            statement.setString(4, seller_sku);
            statement.executeUpdate();
            System.out.println("Updated articles: " + seller_sku);
        }
    }

    private void insertPricing(String seller_sku, double purchase_price, double regular_price, double retail_price, double discount_price, int supplier_stock, int our_stock, int warehouse_stock) throws Exception {
        String sql = "INSERT INTO pricing (seller_sku, purchase_price, regular_price, retail_price, discount_price, supplier_stock, our_stock, warehouse_stock) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, seller_sku);
            statement.setDouble(2, purchase_price);
            statement.setDouble(3, regular_price);
            statement.setDouble(4, retail_price);
            statement.setDouble(5, discount_price);
            statement.setInt(6, supplier_stock);
            statement.setInt(7, our_stock);
            statement.setInt(8, warehouse_stock);
            statement.executeUpdate();
            System.out.println("Inserted into pricing: " + seller_sku);
        }
    }

    private void updatePricing(String seller_sku, double purchase_price, double regular_price, double retail_price, double discount_price, int supplier_stock, int our_stock, int warehouse_stock) throws Exception {
        String sql = "UPDATE pricing SET purchase_price = ?, regular_price = ?, retail_price = ?, discount_price = ?, supplier_stock = ?, our_stock = ?, warehouse_stock = ? WHERE seller_sku = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, purchase_price);
            statement.setDouble(2, regular_price);
            statement.setDouble(3, retail_price);
            statement.setDouble(4, discount_price);
            statement.setInt(5, supplier_stock);
            statement.setInt(6, our_stock);
            statement.setInt(7, warehouse_stock);
            statement.setString(8, seller_sku);
            statement.executeUpdate();
            System.out.println("Updated pricing: " + seller_sku);
        }
    }

    private void insertAccounting(String seller_sku, double wb_logistics_fee, double wb_commission, double our_logistics, double salary, double packaging, double credit, double designer, double admin) throws Exception {
        String sql = "INSERT INTO accounting (seller_sku, wb_logistics_fee, wb_commission, our_logistics, salary, packaging, credit, designer, admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, seller_sku);
            statement.setDouble(2, wb_logistics_fee);
            statement.setDouble(3, wb_commission);
            statement.setDouble(4, our_logistics);
            statement.setDouble(5, salary);
            statement.setDouble(6, packaging);
            statement.setDouble(7, credit);
            statement.setDouble(8, designer);
            statement.setDouble(9, admin);
            statement.executeUpdate();
            System.out.println("Inserted into accounting: " + seller_sku);
        }
    }

    private void updateAccounting(String seller_sku, double wb_logistics_fee, double wb_commission, double our_logistics, double salary, double packaging, double credit, double designer, double admin) throws Exception {
        String sql = "UPDATE accounting SET wb_logistics_fee = ?, wb_commission = ?, our_logistics = ?, salary = ?, packaging = ?, credit = ?, designer = ?, admin = ? WHERE seller_sku = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, wb_logistics_fee);
            statement.setDouble(2, wb_commission);
            statement.setDouble(3, our_logistics);
            statement.setDouble(4, salary);
            statement.setDouble(5, packaging);
            statement.setDouble(6, credit);
            statement.setDouble(7, designer);
            statement.setDouble(8, admin);
            statement.setString(9, seller_sku);
            statement.executeUpdate();
            System.out.println("Updated accounting: " + seller_sku);
        }
    }

    private void insertProductDimensions(String seller_sku, double width, double length, double height, double weight, String photo_url_1, String photo_url_2, String photo_url_3, String photo_url_4, String photo_url_5, String photo_url_6, String photo_url_7) throws Exception {
        String sql = "INSERT INTO product_dimensions (seller_sku, width, length, height, weight, photo_url_1, photo_url_2, photo_url_3, photo_url_4, photo_url_5, photo_url_6, photo_url_7) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, seller_sku);
            statement.setDouble(2, width);
            statement.setDouble(3, length);
            statement.setDouble(4, height);
            statement.setDouble(5, weight);
            statement.setString(6, photo_url_1);
            statement.setString(7, photo_url_2);
            statement.setString(8, photo_url_3);
            statement.setString(9, photo_url_4);
            statement.setString(10, photo_url_5);
            statement.setString(11, photo_url_6);
            statement.setString(12, photo_url_7);
            statement.executeUpdate();
            System.out.println("Inserted into product_dimensions: " + seller_sku);
        }
    }

    private void updateProductDimensions(String seller_sku, double width, double length, double height, double weight, String photo_url_1, String photo_url_2, String photo_url_3, String photo_url_4, String photo_url_5, String photo_url_6, String photo_url_7) throws Exception {
        String sql = "UPDATE product_dimensions SET width = ?, length = ?, height = ?, weight = ?, photo_url_1 = ?, photo_url_2 = ?, photo_url_3 = ?, photo_url_4 = ?, photo_url_5 = ?, photo_url_6 = ?, photo_url_7 = ? WHERE seller_sku = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, width);
            statement.setDouble(2, length);
            statement.setDouble(3, height);
            statement.setDouble(4, weight);
            statement.setString(5, photo_url_1);
            statement.setString(6, photo_url_2);
            statement.setString(7, photo_url_3);
            statement.setString(8, photo_url_4);
            statement.setString(9, photo_url_5);
            statement.setString(10, photo_url_6);
            statement.setString(11, photo_url_7);
            statement.setString(12, seller_sku);
            statement.executeUpdate();
            System.out.println("Updated product_dimensions: " + seller_sku);
        }
    }
}
