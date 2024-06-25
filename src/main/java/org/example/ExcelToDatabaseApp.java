import java.sql.Connection;
import java.sql.DriverManager;
import java.io.FileInputStream;
import java.io.File;

public class ExcelToDatabaseApp {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:postgresql://localhost:5432/mybusiness";
        String username = "postgres";
        String password = "1234";
        String excelFilePath = "/Users/eraserhead/Library/CloudStorage/GoogleDrive-makc200690@gmail.com/Мой диск/AQUA/project/CRM/BD_CRM_AQUA.xlsx";

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", username, password)) {
            DatabaseCreator.createDatabaseAndTablesIfNotExist(connection, "mybusiness", username, password);

            try (FileInputStream inputStream = new FileInputStream(new File(excelFilePath))) {
                Connection mybusinessConnection = DriverManager.getConnection(jdbcURL, username, password);
                mybusinessConnection.setAutoCommit(false); // Использование транзакций

                ExcelReader excelReader = new ExcelReader();
                DatabaseHandler dbHandler = new DatabaseHandler(mybusinessConnection);

                excelReader.readAndProcessExcel(inputStream, dbHandler);

                mybusinessConnection.commit(); // Коммит транзакций
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}