package utilities;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.testng.ITestResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class ExcelUtil {
    private XSSFSheet xlsxWorkSheet;
    private XSSFWorkbook xlsxWorkBook;
    private XSSFCell xlsxCell;
    private XSSFRow xlsxRow;
    private HSSFSheet xlsWorkSheet;
    private HSSFWorkbook xlsWorkBook;
    private HSSFCell xlsCell;
    private HSSFRow xlsRow;

    FileInputStream fis = null;
    FileOutputStream fos = null;

    public ExcelUtil(String path, String sheetName){
        try{
            File file = new File(path);
            if(file.getAbsolutePath().endsWith(".xlsx")){
                fis = new FileInputStream(file);
                xlsxWorkBook = new XSSFWorkbook(fis);
                xlsxWorkSheet = xlsxWorkBook.getSheet(sheetName);
            } else if(file.getAbsolutePath().endsWith(".xls")){
                fis = new FileInputStream(file);
                xlsWorkBook = new HSSFWorkbook(fis);
                xlsWorkSheet = xlsWorkBook.getSheet(sheetName);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + e.getStackTrace());
        }
    }


    /**
     * Execute Tis method to release excel enstance
     *
     * @throws Exception
     */
    public void releaseExcel() throws Exception{
        if(fis!= null)
            fis.close();
    }

    public void releaseExcel(String path, String sheetName) throws Exception{
        if(fis!= null)
            fis.close();
        fos = new FileOutputStream(path);
        xlsxWorkBook.write(fos);
        fos.close();
    }

    public int rowsizeX(){
        int rowNum = xlsxWorkSheet.getLastRowNum() +1;
        return rowNum;
    }

    public int columnsizeX(){
        int colNum = xlsxWorkSheet.getRow(0).getLastCellNum();
        return colNum;
    }

    public String getCellData(int rowNum, int colNum){
        try{
            return xlsxWorkSheet.getRow(rowNum).getCell(colNum).toString();
        }catch (NullPointerException e){
            return "";
        }
    }

    public void setCellData(int rowNum, int colNum, String cellValue){
        try{
            xlsxWorkSheet.getRow(rowNum).getCell(colNum).setCellValue(cellValue);
        }catch (NullPointerException e){
            Cell cell = xlsxWorkSheet.getRow(rowNum).createCell(colNum);
            cell.setCellValue(cellValue);
        }
    }

    /**
     *
     * Get coulun number based on the column name. This method will throw an
     *
     * @throwsexception
     */

    public int findCloumnByName(String columnName) throws Exception{
        int columnsize = columnsizeX();
        int rowNum = 0;
        int colNum = 0;

        try{
            for(int i=0; i< columnsize; i++){
                if(xlsxWorkSheet.getRow(rowNum).getCell(i).toString().equalsIgnoreCase(columnName)){
                    colNum = i;
                    break;
                }
            }
        } catch (NullPointerException e){
            throw  new Exception("No columns found with the column name " + columnName);
        }
        return colNum;
    }


    public int findRowNumByCloumnNameAndValue(String columnLabel, String columnValue) throws Exception {
        int columnNumberoftestName = findCloumnByName(columnLabel);
        int numberofUsedRows = rowsizeX();
        int rowNum = 0;

            for (int i = 0; i < numberofUsedRows; i++) {
                String value = getCellData(i, +columnNumberoftestName);
                if (value.equalsIgnoreCase(columnValue)) {
                    rowNum = i;
                }
            }
            if (rowNum != 0)
                return rowNum;
            else
                throw new Exception("Value " + columnValue + " is not found in column " + columnLabel);
    }

    public int findRowNumByCloumnNameAndCondition(String columnLabel, String columnValue, String condition, String conditionLabel) throws Exception {
        int columnNumberoftestName = findCloumnByName(columnLabel);
        int columnNumberofCond = findCloumnByName(conditionLabel);
        int numberofUsedRows = rowsizeX();
        int rowNum = 0;

        for (int i = 0; i < numberofUsedRows; i++) {
            String value = getCellData(i, +columnNumberoftestName);
            String condValue = getCellData(i, +columnNumberofCond);

            if (value.equalsIgnoreCase(columnValue) && condValue.equalsIgnoreCase(condition)) {
                rowNum = i;
                break;
            }
        }
//        if (rowNum != 0)
            return rowNum;
//        else
//            throw new Exception("Value " + columnValue + " is not found in column " + columnLabel)
    }


    public String getDataByColumnLabel(String columnLabelforDesiredData, String columnLabelofSearch, String columnValueofSearch) throws Exception{
        int rowNum = findRowNumByCloumnNameAndValue(columnLabelofSearch, columnLabelforDesiredData);
        String data = getCellData(rowNum, findCloumnByName(columnLabelforDesiredData));
        return  data;
    }


    /**
     * This method gets the data from a cell based on the value of another cell on the same row
     *
     * @throws Exception
     */


    public String getTestDataByTestName(String testName, String columnLabelofTestName, String columnLabelofTestData)
            throws Exception{
        int rowNum = findRowNumByCloumnNameAndValue(columnLabelofTestName, testName);
        int colNum = findCloumnByName(columnLabelofTestData);
        return getCellData(rowNum, colNum);
    }


    public String getDataByRowAndColumnName(int rowNum, String columnLabelofTestData)
        throws Exception{
        int colNum = findCloumnByName(columnLabelofTestData);
        return getCellData(rowNum, colNum);
    }

    public String getTestDataByTestNameAndCondition(String testName, String columnLabelofTestName, String columnLabelofTestData, String ConditionData, String columnLabelofConditionData)
            throws Exception{
        int colNum =0;
        int rowNum = findRowNumByCloumnNameAndCondition(columnLabelofTestName, testName, ConditionData, columnLabelofConditionData);
        int conditionColNum = findCloumnByName(columnLabelofConditionData);
        if(getCellData(rowNum, conditionColNum).equalsIgnoreCase(ConditionData))
            colNum = findCloumnByName(columnLabelofTestData);
        return getCellData(rowNum, colNum);
    }

    public void setTestDataByTestName(String testName, String columnLabelofTestName, String columnLabelofTestData, String cellValue) throws Exception{
        int colNum =findCloumnByName(columnLabelofTestData);
        int rowNum = findRowNumByCloumnNameAndValue(columnLabelofTestName, testName);
        setCellData(rowNum, colNum, cellValue);
    }

    public void setTestDataByTestName(String testName, String columnLabelofTestName, String keyset, String columnLabelofKeyset, String columnLabelofTestData, String cellValue) throws Exception{
        int colNum =findCloumnByName(columnLabelofTestData);
        int rowNum = findRowNumByCloumnNameAndCondition(columnLabelofTestName, testName, keyset, columnLabelofKeyset);
        setCellData(rowNum, colNum, cellValue);
    }


    public Object[][] FindTestNamesByTemplateName(String templateColumnLabel, String templateName, String executionFlagColumnLabel, String testNameColumnLabel) throws  Exception{
        int columnNum = findCloumnByName(templateColumnLabel);
        int exFlagColumnNum = findCloumnByName(executionFlagColumnLabel);
        Object[][] aoj;

        ArrayList<String> names = new ArrayList<>();
        for (int i=0; i<rowsizeX(); i++){
            if(getCellData(i, exFlagColumnNum).equalsIgnoreCase("TRUE")
                && getCellData(i, columnNum).equalsIgnoreCase(templateName)){
                names.add(getCellData(i, findCloumnByName(testNameColumnLabel)));
            }
        }
        aoj = new
                Object[names.size()][1];
        for(int i=0; i<names.size(); i++)
            aoj[i] = new Object[] {names.get(i)};

        return aoj;
    }


    public Object[][] FindTestNamesAndScenarioByTemplateName(String templateColumnLabel, String templateName, String executionFlagColumnLabel, String testNameColumnLabel) throws  Exception{
        int columnNum = findCloumnByName(templateColumnLabel);
        int exFlagColumnNum = findCloumnByName(executionFlagColumnLabel);
        Object[][] aoj;

        ArrayList<String> names = new ArrayList<>();
        for (int i=0; i<rowsizeX(); i++){
            if(getCellData(i, exFlagColumnNum).equalsIgnoreCase("TRUE")
                    && getCellData(i, columnNum).equalsIgnoreCase(templateName)){
                names.add(getCellData(i, findCloumnByName(testNameColumnLabel)) + ";" + getCellData(i, findCloumnByName("Scenario")));
            }
        }
        aoj = new Object[names.size()][1];
        for(int i=0; i<names.size(); i++)
            aoj[i] = new Object[] {names.get(i)};

        return aoj;
    }


    public Object[][] FindTestNamesByTemplateName(String templateColumnLabel, String templateName, String executionFlagColumnLabel, String testNameColumnLabel, String conditionColumn) throws  Exception{
        int columnNum = findCloumnByName(templateColumnLabel);
        int exFlagColumnNum = findCloumnByName(executionFlagColumnLabel);
        int exCondition = findCloumnByName(conditionColumn);
        Object[][] aoj;

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> keyset = new ArrayList<>();

        for (int i=0; i<rowsizeX(); i++){
            if(getCellData(i, exFlagColumnNum).equalsIgnoreCase("TRUE")
                    && getCellData(i, columnNum).equalsIgnoreCase(templateName)){
                names.add(getCellData(i, findCloumnByName(testNameColumnLabel)));
                keyset.add(getCellData(i, findCloumnByName(conditionColumn)));
            }
        }
        aoj = new
                Object[names.size()][1];
        for(int i=0; i<names.size(); i++)
            aoj[i] = new Object[] {names.get(i), keyset.get(i)};

        return aoj;
    }


    public Object[] getAllTestNamesbyColumnLabel(String testNameColumnLabel, String executionFlagColumnLabel) throws  Exception{

        int exFlagColumnNum = findCloumnByName(executionFlagColumnLabel);

        ArrayList<String> names = new ArrayList<>();
        for (int i=0; i<rowsizeX(); i++){
            if(getCellData(i, exFlagColumnNum).equalsIgnoreCase("TRUE")){
                names.add(getCellData(i, findCloumnByName(testNameColumnLabel)));
            }
        }

        return names.toArray();
    }

//
//    public void updateTestResult(ITestResult result, String testName) throws Exception {
//
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
//        setTestDataByTestName(testName, "Test Name", "Execution Data", formatter.format(date));
//        System.out.println(formatter.format(date));
//        if (result.getStatus() == ITestResult.SUCCESS)
//        {
//            setTestDataByTestName(testName, "Test Name", "Exection Flag", "FALSE");
//            setTestDataByTestName(testName, "Test Name", "Status", "Passed");
//        } else if (result.getStatus() == ITestResult.FAILURE);
//        {
//            setTestDataByTestName(testName, "Test Name", "Status", "Failed");
//        }
//    }
//
//
//    public void updateTestResult(ITestResult result, String testName, String keyset) throws Exception {
//
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
//        setTestDataByTestName(testName, "Test Name", keyset, "Keyset","Execution Data", formatter.format(date));
//        System.out.println(formatter.format(date));
//        if (result.getStatus() == ITestResult.SUCCESS)
//        {
//            setTestDataByTestName(testName, "Test Name", keyset, "Keyset","Exection Flag", "FALSE");
//            setTestDataByTestName(testName, "Test Name", keyset, "Keyset","Status", "Passed");
//        } else if (result.getStatus() == ITestResult.FAILURE);
//        {
//            setTestDataByTestName(testName, "Test Name", keyset, "Keyset","Status", "Failed");
//        }
//    }


    public HashMap<String,String> getAllDatabyTestName(String testName) {
        int columnsize = columnsizeX();
        int rowNum = 0;
        int ColumnNum = 0;
        HashMap<String, String> map = new HashMap<>();
        try{
            for (int i=0; i<columnsize; i++){
                String columnLabel = xlsxWorkSheet.getRow(rowNum).getCell(i).toString();
                map.put(columnLabel, getTestDataByTestName(testName, "Test Name", columnLabel));
                }
            }catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return map;
    }


    public HashMap<String,String> getAllDatabyTestName(String testName, String columnName) {
        int columnsize = columnsizeX();
        int rowNum = 0;
        int ColumnNum = 0;
        HashMap<String, String> map = new HashMap<>();
        try{
            for (int i=0; i<columnsize; i++){
                String columnLabel = xlsxWorkSheet.getRow(rowNum).getCell(i).toString();
                map.put(columnLabel, getTestDataByTestName(testName, columnName, columnLabel));
            }
        }catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return map;
    }


    public HashMap<String,String> getAllDatabyTestName(String testName, String columnName, String conditionData, String columnLabelCondition) {
        int columnsize = columnsizeX();
        int rowNum = 0;
        int ColumnNum = 0;
        HashMap<String, String> map = new HashMap<>();
        try{
            for (int i=0; i<columnsize; i++){
                String columnLabel = xlsxWorkSheet.getRow(rowNum).getCell(i).toString();
                map.put(columnLabel, getTestDataByTestNameAndCondition(testName, columnName, columnLabel, conditionData, columnLabelCondition));
            }
        }catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return map;
    }


    /** This method returns data from all desired columns in a n excel sheet for all rows that falls under the criteria
     * <pre>
     *
     *     Parameters:
     *     templateColumnLabel : Column label of the Automation Template. Example: "Automation Template".
     *     templateName : Name of the Template
     *     columnsToIgnore : String array to ignore the columns that so not represent a data column. for example:
     *     String[] columnToIgnore = {"Description", "Execution Flag", "Expected Result", "Automation Template", "Actual Result", "Execution Data", "Status";}
     *     FlagColumnLabel : Column label of execution flag
     */

    public Object[][] getAllDataByColumnLabel(String templateColumnLabel, String templateName, String[] columnsToIgnore, String exFlagColumnLabel) throws Exception {
        int columnsize = columnsizeX();
        int size = 0;
        int columnNumTemplate = findCloumnByName(templateColumnLabel);
        int exFlagColumnNum = findCloumnByName(exFlagColumnLabel);

        ArrayList<String> dataString = new ArrayList<>();
        ArrayList<Object> data = new ArrayList<>();

        for(int i=0; i<rowsizeX(); i++) {
            if (getCellData(i, exFlagColumnNum).equalsIgnoreCase("TRUE") && getCellData(i, columnNumTemplate).equalsIgnoreCase(templateName)) {
                for (int j = 0; j < columnsize; j++) {
                    String columnLabel = xlsxWorkSheet.getRow(0).getCell(j).toString();
                    if (!Arrays.asList(columnsToIgnore).contains(columnLabel)) {
                        dataString.add(getCellData(i, j));
                    }
                }
                data.add(dataString.toArray());
                dataString.clear();
            }
        }
        Object[][] dataArray = new Object[data.size()][data.get(0).toString().length()];
        for(int i=0; i< data.size(); i++) {//dataArray[i] = data.get(i);
        }
        return  dataArray;
}
}
