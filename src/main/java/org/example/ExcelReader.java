import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.text.DecimalFormat;

public class ExcelReader {
    public void readAndProcessExcel(InputStream inputStream, DatabaseHandler dbHandler) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheet("Work");
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропустить заголовок

                String seller_sku = getStringCellValue(row.getCell(0));
                String name = getStringCellValue(row.getCell(1));
                String category = getStringCellValue(row.getCell(2));
                String account_name = getStringCellValue(row.getCell(3));
                String brand = getStringCellValue(row.getCell(4));
                String barcode = getStringCellValue(row.getCell(5));
                String supplier_sku = getStringCellValue(row.getCell(6));
                String wb_sku = getStringCellValue(row.getCell(7));
                double purchase_price = getNumericCellValue(row.getCell(8));
                double regular_price = getNumericCellValue(row.getCell(9));
                double retail_price = getNumericCellValue(row.getCell(10));
                double discount_price = getNumericCellValue(row.getCell(11));
                int supplier_stock = (int) getNumericCellValue(row.getCell(12));
                int our_stock = (int) getNumericCellValue(row.getCell(13));
                int warehouse_stock = (int) getNumericCellValue(row.getCell(14));
                double wb_logistics_fee = getNumericCellValue(row.getCell(15));
                double wb_commission = getNumericCellValue(row.getCell(16));
                double our_logistics = getNumericCellValue(row.getCell(17));
                double salary = getNumericCellValue(row.getCell(18));
                double packaging = getNumericCellValue(row.getCell(19));
                double credit = getNumericCellValue(row.getCell(20));
                double designer = getNumericCellValue(row.getCell(21));
                double width = getNumericCellValue(row.getCell(22));
                double length = getNumericCellValue(row.getCell(23));
                double height = getNumericCellValue(row.getCell(24));
                double weight = getNumericCellValue(row.getCell(25));
                String photo_url_1 = getStringCellValue(row.getCell(26));
                String photo_url_2 = getStringCellValue(row.getCell(27));
                String photo_url_3 = getStringCellValue(row.getCell(28));
                String photo_url_4 = getStringCellValue(row.getCell(29));
                String photo_url_5 = getStringCellValue(row.getCell(30));
                String photo_url_6 = getStringCellValue(row.getCell(31));
                String photo_url_7 = getStringCellValue(row.getCell(32));

                // Вставка или обновление данных в базе данных
                dbHandler.upsertProduct(seller_sku, name, category, account_name, brand);
                dbHandler.upsertArticle(seller_sku, barcode, supplier_sku, wb_sku);
                dbHandler.upsertPricing(seller_sku, purchase_price, regular_price, retail_price, discount_price, supplier_stock, our_stock, warehouse_stock);
                dbHandler.upsertAccounting(seller_sku, wb_logistics_fee, wb_commission, our_logistics, salary, packaging, credit, designer, 0); // Добавьте admin параметр
                dbHandler.upsertProductDimensions(seller_sku, width, length, height, weight, photo_url_1, photo_url_2, photo_url_3, photo_url_4, photo_url_5, photo_url_6, photo_url_7);
            }
        }
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    DecimalFormat df = new DecimalFormat("0.##########");
                    return df.format(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return evaluateFormulaCell(cell);
            default:
                return "";
        }
    }

    private double getNumericCellValue(Cell cell) {
        if (cell == null) {
            return 0.0;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    System.err.println("Cannot parse double from string: " + cell.getStringCellValue());
                    return 0.0;
                }
            case FORMULA:
                return evaluateFormulaCellAsNumeric(cell);
            default:
                return 0.0;
        }
    }

    private String evaluateFormulaCell(Cell cell) {
        try {
            FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
            CellValue cellValue = evaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case STRING:
                    return cellValue.getStringValue();
                case NUMERIC:
                    DecimalFormat df = new DecimalFormat("0.##########");
                    return df.format(cellValue.getNumberValue());
                case BOOLEAN:
                    return String.valueOf(cellValue.getBooleanValue());
                default:
                    return "";
            }
        } catch (Exception e) {
            System.err.println("Error evaluating formula: " + e.getMessage());
            return "";
        }
    }

    private double evaluateFormulaCellAsNumeric(Cell cell) {
        try {
            FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
            CellValue cellValue = evaluator.evaluate(cell);
            return cellValue.getNumberValue();
        } catch (Exception e) {
            System.err.println("Error evaluating formula as numeric: " + e.getMessage());
            return 0.0;
        }
    }
}