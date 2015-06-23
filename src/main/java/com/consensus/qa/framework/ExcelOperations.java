package com.consensus.qa.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.consensus.qa.pages.PaymentRequiredPage.CardType;
import com.consensus.qa.pages.ServiceProviderVerificationPage.IdType;

public class ExcelOperations {
	public enum FileNames{
		VerizonEdgeUp, VerizonRegression, GlobalTestData,
	}

	public enum SheetName{
		IMEI_3G, IMEI_4G, Sim_2FF,Sim_3FF, Sim_4FF, VerizonEdgeUp, EdgeUpgradeEligibility, UpgradeEligibility, 
		S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, S12,S13,S14,S15,S16,S17,S18,S19,S20,S21,S22,S23,S24, 
		iPhone6,IMEI_3FF, iPhone5C,iPhone4S,Basic, JetPack, NPANXX, DepositSSNs, DownPaymentSSNs, ICCID_3FF,
		IndividualEligibility, ALTUpgrade, EarlyEdge,
	}

	public enum Status{	UNUSED, INUSE, USED,STATUS, }

	static XSSFWorkbook workBook;
	static XSSFSheet worksheet;
	static FileInputStream fileInput;
	Row row;
	String testType = BrowserSettings.readConfig("BrowserType");
	final String VerizonEdgeUpSheet = "TestData\\VerizonEdgeUp.xlsx";
	final String VerizonEdgeUpSheetInternal = "TestData\\VerizonEdgeUpInternal.xlsx";
	final String VerizonRegressionNumberPortSheet = "TestData\\VerizonRegressionWithNumberPortInternal.xlsx";
	final String VerizonRegressionNumberPortSheetInternal = "TestData\\VerizonRegressionWithNumberPortInternal.xlsx";
	final String GlobalTestDataSheet = "TestData\\GlobalTestData.xlsx";

	//Get Sim Type
	//Inputs: excelFileName && sheet name
	public String GetSimType(FileNames fileName, SheetName sheetName) throws IOException
	{
		String filePath = FilePath(fileName);
		String simType = null;
		try{
			String sheet = sheetName.toString();
			if(sheetName.toString().contains("_")){
				String[] Name = sheetName.toString().split("_");
				sheet = Name[1];
			}
			SetStatusForSimTypSheets(sheet, fileName);
			fileInput= new FileInputStream(new File(filePath));                 
			workBook = new XSSFWorkbook(fileInput);
			XSSFSheet workSheet = workBook.getSheet(sheet);
			for(int i=0;i<workSheet.getPhysicalNumberOfRows();i++){
				Row currentRow = workSheet.getRow(i);
				int type = 1;
				Cell cell = currentRow.getCell(1);
				try{
					type = cell.getCellType();
					System.out.println(type);
					System.out.println(workSheet.getRow(i).getCell(1).toString());
					System.out.println(cell.getStringCellValue().toLowerCase());
					if(type == 1 && cell.getStringCellValue().toLowerCase().equals(Status.UNUSED.toString().toLowerCase()))
					{
						cell.setCellValue(Status.INUSE.toString());
						simType = currentRow.getCell(0).getStringCellValue();
						break;
					}
				}

				catch(Exception e){
					if(type!=1){
						cell = currentRow.createCell(1);
						cell.setCellValue(Status.UNUSED.toString());
					}
				}
			}
		}
		catch(Exception ex){
			Log.error(ex.toString());
		}
		finally{
			WriteAndCloseFile(filePath, fileInput, workBook);
		}
		return simType;
	}

	public void SetStatusForSimTypSheets(String sheet, FileNames fileName) throws IOException
	{
		int statusColumn;
		XSSFSheet workSheet = null;
		String filePath = FilePath(fileName);
		fileInput= new FileInputStream(new File(filePath));
		workBook = GetWorkBook(fileName);
		for(int i=0;i<workBook.getNumberOfSheets();i++){
			if(workBook.getSheetName(i).toLowerCase().contains(sheet.toLowerCase())){
				workSheet = workBook.getSheetAt(i);
				break;
			}
		}

		try{
			for(int i=0;i<workSheet.getPhysicalNumberOfRows();i++)
			{
				Row statusRow = workSheet.getRow(i);
				for(int ct=0;ct<statusRow.getPhysicalNumberOfCells();ct++)
				{
					System.out.println(statusRow.getCell(ct));
				}
				int numOfCells = statusRow.getPhysicalNumberOfCells();
				if(workSheet.getRow(i).getCell
						(numOfCells-1).getStringCellValue().toUpperCase().contains(Status.STATUS.toString())
						||workSheet.getRow(0).getCell
						(numOfCells-1).getStringCellValue().toUpperCase().contains(Status.UNUSED.toString())
						||workSheet.getRow(0).getCell
						(numOfCells-1).getStringCellValue().toUpperCase().contains(Status.INUSE.toString())
						||workSheet.getRow(0).getCell
						(numOfCells-1).getStringCellValue().toUpperCase().contains(Status.USED.toString())){
					statusColumn = numOfCells-1;
				}
				else{
					statusColumn = numOfCells;
					Cell statusCell = workSheet.getRow(i).createCell(statusColumn);
					statusCell.setCellValue(Status.UNUSED.toString());
					continue;
				}
				System.out.println(statusRow.getCell(statusColumn));
				try{
					if(statusRow.getCell(statusColumn)==null){

						Cell cell = row.createCell(statusColumn);
						cell.setCellValue(Status.UNUSED.toString());
					}
				}
				catch (Exception ex) {
					row.createCell(statusColumn).setCellValue(Status.UNUSED.toString());
				}
			}
		}
		catch (Exception ex) {
			Log.error(ex.toString());
		}
		finally{
			WriteAndCloseFile(filePath, fileInput, workBook);
		}
	}

	//Mark A Cell to USED from INUSE or FREE
	public void SetCellValueToUsed( FileNames fileName, SheetName sheetName,String searchString)
			throws IOException{
		String filePath = FilePath(fileName); 
		fileInput= new FileInputStream(new File(filePath));
		workBook = new XSSFWorkbook(fileInput);
		String sheet = sheetName.toString();
		int rowIndex = -1;

		try{
			if(sheetName.toString().contains("_")){
				String[] Name = sheetName.toString().split("_");
				if(Name[0].contains("IMEI")){
					sheet = Name[1] + " " +Name[0];
				}
				else
					sheet = Name[1];
			}
			worksheet = workBook.getSheet(sheet); 
			System.out.println(worksheet.getSheetName());
			Iterator<Row> rowIterator = worksheet.rowIterator();
			while (rowIterator.hasNext()){
				Row row = rowIterator.next();

				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()){
					Cell cell = cellIterator.next();
					if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && 
							String.valueOf(Double.valueOf(cell.getNumericCellValue()).longValue())
							.equals(searchString)){
						rowIndex = row.getRowNum();
						break;
					}
					else if(cell.getCellType()==Cell.CELL_TYPE_STRING ){
						if(cell.getStringCellValue().equals(searchString))
							rowIndex = row.getRowNum();
						break;
					}
				}
			}
			if(rowIndex!=-1){
				boolean flag = false;
				Row searchStringRow = worksheet.getRow(rowIndex);
				System.out.println(searchStringRow.getPhysicalNumberOfCells());
				Iterator<Cell> statusChangeCell = searchStringRow.cellIterator();
				while(statusChangeCell.hasNext()){
					Cell statusCell = statusChangeCell.next();
					if(statusCell.getStringCellValue().toLowerCase().equals(Status.INUSE.toString().toLowerCase())){
						statusCell.setCellValue(Status.USED.toString());
						flag = true;
					}
				}
				if(flag == false)
					Log.error("FAILED TO FIND INUSE FIELD for " + searchString + " @FileName: " + filePath + ", Sheet: " + sheet );
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
			WriteAndCloseFile(filePath, fileInput, workBook);
		}
	}

	//Get IMEI number
	//INPUTS: ExcelFileName; Work Sheet Name
	public String GetIMEINumber(FileNames fileName, SheetName workSheet) throws IOException
	{
		String  filePath = FilePath(fileName);
		String sheet = null;
		String tempSheet=null;
		String imei = "";
		int statusColumn = -1;
		try{
			int numOfWorkSheets = 0;

			fileInput= new FileInputStream(new File(filePath));                 
			workBook = new XSSFWorkbook(fileInput);
			numOfWorkSheets = workBook.getNumberOfSheets();
			for(int count = 0; count < numOfWorkSheets;count++){
				if(workSheet.toString().toLowerCase().contains(workBook.getSheetName(count).toLowerCase())){
					sheet = workBook.getSheetName(count);
					break;
				}
			}
			worksheet = workBook.getSheet(sheet);
			if (sheet.equals("iPhone 5C")){
				tempSheet = sheet;
				sheet = "iPhone 4S";
			}

			switch(sheet){
			case "4G IMEI":
				for(int i=0;i<worksheet.getPhysicalNumberOfRows();i++){
					row = worksheet.getRow(i);
					Cell cell = row.getCell(2);
					if(row.getCell(5).toString().equals(Status.UNUSED.toString())){
						if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
							imei = String.valueOf(cell.getNumericCellValue());
						}
						else
							imei = cell.getStringCellValue();
						row.getCell(5).setCellValue(Status.INUSE.toString());
						break;
					}
				}
				return imei;


			case "3G":
				for(int i=0;i<worksheet.getPhysicalNumberOfRows();i++){
					row = worksheet.getRow(i);
					if(row.getCell(4).toString().equals(Status.UNUSED.toString())){
						imei = String.valueOf(row.getCell(2).getNumericCellValue());
						worksheet.getRow(i).getCell(4).setCellValue(Status.INUSE.toString());
						break;
					}
				}

			case "Jetpack":
				statusColumn = CreateStatusColumn(filePath, sheet);
				if(statusColumn==-1){ 
					statusColumn=3;
				}

				for(int i=2;i<worksheet.getPhysicalNumberOfRows();i++){
					row = worksheet.getRow(i);
					Cell cell = row.getCell(1);
					if(row.getCell(statusColumn).toString().equals(Status.UNUSED.toString())){
						if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
							imei = String.valueOf((long) cell.getNumericCellValue());
						}
						else
							imei = cell.getStringCellValue();
						row.getCell(statusColumn).setCellValue(Status.INUSE.toString());
						break;
					}
				}

			case "iPhone 4S":
				if(tempSheet.contains("5C"))
					statusColumn = CreateStatusColumn(filePath, tempSheet);
				if(statusColumn==-1){
					Cell cell = worksheet.getRow(0).createCell(4);
					cell.setCellValue(Status.STATUS.toString());
					statusColumn=4;
				}
				for(int i=1;i<worksheet.getPhysicalNumberOfRows();i++){
					row = worksheet.getRow(i);
					Cell cell = row.getCell(0);
					if(row.getCell(statusColumn).getCellType()==Cell.CELL_TYPE_BLANK){
						row.createCell(statusColumn).setCellValue(Status.UNUSED.toString());
					}
					if(row.getCell(statusColumn).toString().equals(Status.UNUSED.toString())){
						if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
							imei = String.valueOf((long) cell.getNumericCellValue());
						}
						else
							imei = cell.getStringCellValue();
						if(imei!="" || imei!=null)
							row.getCell(statusColumn).setCellValue(Status.INUSE.toString());
						break;
					}
				}
			}
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

		finally{
			WriteAndCloseFile(filePath, fileInput, workBook);
		}

		return imei;
	}

	@SuppressWarnings("unused")
	public AccountDetails GetAccountDetails(FileNames fileName, SheetName worksheetName) throws IOException
	{
		String filePath = FilePath(fileName);
		fileInput= new FileInputStream(new File(filePath));                 
		workBook = new XSSFWorkbook(fileInput);
		XSSFSheet workSheet = GetSheetFromWorkBook(workBook, worksheetName.toString());
		AccountDetails accountDetails = new AccountDetails();

		try{
			int mtnIndex = -1; int passwordIndex = -1; int ssnIndex = -1;int statusCol = -1;
			Row row = workSheet.getRow(0);
			Iterator<Cell> cellIterator = row.cellIterator();
			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				if(String.valueOf(cell.getStringCellValue()).contains("MTN")){
					mtnIndex = cell.getColumnIndex();
				}
				else if(String.valueOf(cell.getStringCellValue()).contains("Password")){
					passwordIndex = cell.getColumnIndex();
				}
				else if(String.valueOf(cell.getStringCellValue()).contains("SSN")){
					ssnIndex = cell.getColumnIndex();
				}
				else if(String.valueOf(cell.getStringCellValue()).contains(Status.STATUS.toString())){
					statusCol = cell.getColumnIndex();
				}
				if(mtnIndex!=-1 && passwordIndex!=-1&&ssnIndex!=-1&&statusCol!=-1){
					break;
				}
			}
			if(statusCol==-1){
				statusCol = (workSheet.getRow(0).getPhysicalNumberOfCells());
				Cell cell = workSheet.getRow(0).createCell(statusCol);
				cell.setCellValue(Status.STATUS.toString());
			}
			for(int i=1;i<workSheet.getPhysicalNumberOfRows();i++)
			{
				Cell cell = null;
				if(workSheet.getRow(i).getCell(statusCol)==null || 
						(workSheet.getRow(i).getCell(statusCol).getCellType()==Cell.CELL_TYPE_BLANK)){
					cell = workSheet.getRow(i).createCell(statusCol);
					cell.setCellValue(Status.UNUSED.toString());
				}
				cell = workSheet.getRow(i).getCell(statusCol);
				if(cell.getStringCellValue().toString().equals(Status.UNUSED.toString())){
					accountDetails.MTN = String.valueOf(workSheet.getRow(i).getCell(mtnIndex).getNumericCellValue());
					accountDetails.Password = workSheet.getRow(i).getCell(passwordIndex).getStringCellValue();
					accountDetails.SSN = String.valueOf(workSheet.getRow(i).getCell(ssnIndex).getNumericCellValue());

					cell.setCellValue(Status.INUSE.toString());
					break;
				}
			}
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

		finally{
			WriteAndCloseFile(filePath, fileInput, workBook);
		}

		if(accountDetails==null)
			Log.error("FAILED To get account details; one among MTN/Password/SSN is blank");
		return accountDetails;
	}

	@SuppressWarnings("unused")
	//Inputs IDType; State
	public CustomerDetails GetCustomerDetails(IdType idType) throws IOException, NullPointerException
	{
		CustomerDetails customerDetails = new CustomerDetails();
		String filePath = FilePath(FileNames.GlobalTestData);
		fileInput= new FileInputStream(new File(filePath));                 
		workBook = new XSSFWorkbook(fileInput);
		worksheet = workBook.getSheet("Customer");
		System.out.println(worksheet.getSheetName());
		String IDType = "Driver's License";
		if(idType.toString().equals("USPASSPORT")){
			IDType = "Passport";
		}
		else if(idType.toString().equals("STATEID")){
			IDType = "STATE ID";

		}

		try{
			int statusColumn= -1;

			for(int i=0;i<worksheet.getPhysicalNumberOfRows();i++){
				row = worksheet.getRow(i);

				if(i==0){
					int numOfCells = row.getPhysicalNumberOfCells();
					if(worksheet.getRow(0).getCell(numOfCells-1).getStringCellValue().toUpperCase().contains
							(Status.STATUS.toString())){
						statusColumn = numOfCells-1;
					}
					else{
						statusColumn = numOfCells;
						Cell statusCell = row.createCell(statusColumn);
						statusCell.setCellValue(Status.STATUS.toString());
						continue;
					}
				}

				if(row.getCell(statusColumn)==null){

					Cell cell = row.createCell(statusColumn);
					cell.setCellValue(Status.UNUSED.toString());
				}

				if(row.getCell(statusColumn).toString().equals(Status.UNUSED.toString()) && 
						row.getCell(12).toString().equals(IDType)){
					customerDetails.FirstName = String.valueOf(row.getCell(0));
					customerDetails.LastName = String.valueOf(row.getCell(1));
					customerDetails.Address1 = String.valueOf(row.getCell(2));
					customerDetails.City = String.valueOf(row.getCell(4));
					customerDetails.State = String.valueOf(row.getCell(5));
					System.out.println(customerDetails.State.toString());
					if(row.getCell(6).getCellType()!=Cell.CELL_TYPE_BLANK)
						customerDetails.Zip = String.valueOf((int) row.getCell(6).getNumericCellValue());
					if(row.getCell(7).getCellType()!=Cell.CELL_TYPE_BLANK){
						System.out.println(row.getCell(7).getCellType());
						customerDetails.PhNum = String.valueOf((int)row.getCell(7).getNumericCellValue());}
					if(row.getCell(8).getCellType()!=Cell.CELL_TYPE_BLANK)
						customerDetails.EMail = String.valueOf(row.getCell(8));
					if(row.getCell(9).getCellType()!=Cell.CELL_TYPE_BLANK)
						customerDetails.BirthdayMonth = String.valueOf(row.getCell(9));
					if(row.getCell(10).getCellType()!=Cell.CELL_TYPE_BLANK)
						customerDetails.BirthdayDay =String.valueOf((int) row.getCell(10).getNumericCellValue());
					if(row.getCell(11).getCellType()!=Cell.CELL_TYPE_BLANK)
						customerDetails.BirthdayYear =String.valueOf((int) row.getCell(11).getNumericCellValue());
					if(row.getCell(12).getCellType()!=Cell.CELL_TYPE_BLANK)
						customerDetails.IDType = String.valueOf(row.getCell(12));
					if(row.getCell(13).getCellType()!=Cell.CELL_TYPE_BLANK)
						customerDetails.IDState = String.valueOf(row.getCell(13));
					if(row.getCell(14).getCellType()!=Cell.CELL_TYPE_BLANK)
						customerDetails.IDNumber = String.valueOf(row.getCell(14));
					if(row.getCell(15).getCellType()!=Cell.CELL_TYPE_BLANK)
						customerDetails.IDExpirationMonth = String.valueOf(row.getCell(15));
					if(row.getCell(16).getCellType()!=Cell.CELL_TYPE_BLANK)
						customerDetails.IDExpirationYear = String.valueOf((int) row.getCell(16).getNumericCellValue());
					if(row.getCell(17).getCellType()!=Cell.CELL_TYPE_BLANK)
					row.getCell(statusColumn).setCellValue(Status.INUSE.toString());
					if(customerDetails!=null)
						break;
				}
			}
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

		finally{
			WriteAndCloseFile(filePath, fileInput, workBook);
		}

		if(customerDetails==null){
			System.out.println("FAILED to get customer details ");
		}

		return customerDetails;
	}

	@SuppressWarnings("unused")
	public CreditCardDetails GetCreditCardDetails(CardType cardType) throws IOException
	{
		CreditCardDetails creditCardDetails = new CreditCardDetails();
		String filePath = FilePath(FileNames.GlobalTestData);
		fileInput= new FileInputStream(new File(filePath));                 
		workBook = new XSSFWorkbook(fileInput);
		worksheet = workBook.getSheet("CreditCard");

		try{
			int cardRow= -1;

			for(int i=1;i<worksheet.getPhysicalNumberOfRows();i++){
				row = worksheet.getRow(i);

				if(row.getCell(0).getCellType()!=Cell.CELL_TYPE_BLANK){
					if(row.getCell(0).toString().equals(cardType)){
						creditCardDetails.Number = row.getCell(1).getStringCellValue();
						creditCardDetails.ExpiryMonth = row.getCell(2).getStringCellValue();
						if( row.getCell(3).getCellType()!=Cell.CELL_TYPE_NUMERIC)
						{
							creditCardDetails.ExpiryYear = String.valueOf(row.getCell(3).getNumericCellValue());
						}
						else
							creditCardDetails.ExpiryYear = row.getCell(3).getStringCellValue();
						creditCardDetails.CVV = String.valueOf((int) row.getCell(2).getNumericCellValue());
						if(creditCardDetails!=null)
							break;
					}
				}

			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		finally{
			WriteAndCloseFile(filePath, fileInput, workBook);
		}

		if(creditCardDetails==null){
			System.out.println("FAILED to get Credit card details ");
		}

		return creditCardDetails;
	}

	public int CreateStatusColumn(String filePath, String workSheetName) throws IOException
	{
		//String filePath = FilePath(fileName);
		fileInput= new FileInputStream(new File(filePath));
		workBook = new XSSFWorkbook(fileInput);
		XSSFSheet worksheet = GetSheetFromWorkBook(workBook, workSheetName);
		int statusColumn = -1;
		try{
			for(int i=0;i<worksheet.getPhysicalNumberOfRows();i++)
			{
				row = worksheet.getRow(i);
				if(i==0){
					int numOfCells = row.getPhysicalNumberOfCells();
					if(worksheet.getRow(0).getCell
							(numOfCells-1).getStringCellValue().toUpperCase().contains(Status.STATUS.toString())){
						statusColumn = numOfCells-1;
					}
					else{
						statusColumn = numOfCells;
						Cell statusCell = row.createCell(statusColumn);
						statusCell.setCellValue(Status.STATUS.toString());
						continue;
					}
				}
				if(row.getCell(statusColumn)==null){

					Cell cell = row.createCell(statusColumn);
					cell.setCellValue(Status.UNUSED.toString());
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return statusColumn;
	}

	@SuppressWarnings("unused")
	public NPANXX GetNumberPortData(FileNames fileName, SheetName workSheetName) throws IOException
	{
		String filePath = FilePath(fileName);
		fileInput= new FileInputStream(new File(filePath));                 
		workBook = new XSSFWorkbook(fileInput);
		XSSFSheet workSheet = GetSheetFromWorkBook(workBook, workSheetName.toString());
		NPANXX npaNXX = new NPANXX();

		try{
			int mtnIndex = -1; int passwordIndex = -1; int ssnIndex = -1;int statusCol = -1;
			Row row = workSheet.getRow(0);
			Iterator<Cell> cellIterator = row.cellIterator();
			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				if(String.valueOf(cell.getStringCellValue()).contains("NGP")){
					mtnIndex = cell.getColumnIndex();
				}
				else if(String.valueOf(cell.getStringCellValue()).contains("Location")){
					passwordIndex = cell.getColumnIndex();
				}
				else if(String.valueOf(cell.getStringCellValue()).contains("NPANXX")){
					ssnIndex = cell.getColumnIndex();
				}
				else if(String.valueOf(cell.getStringCellValue()).contains(Status.STATUS.toString())){
					statusCol = cell.getColumnIndex();
				}
				if(mtnIndex!=-1 && passwordIndex!=-1&&ssnIndex!=-1&&statusCol!=-1){
					break;
				}
			}
			if(statusCol==-1){
				statusCol = (workSheet.getRow(0).getPhysicalNumberOfCells());
				Cell cell = workSheet.getRow(0).createCell(statusCol);
				cell.setCellValue(Status.STATUS.toString());
			}
			for(int i=1;i<workSheet.getPhysicalNumberOfRows();i++)
			{
				Cell cell = null;
				if(workSheet.getRow(i).getCell(statusCol)==null || 
						(workSheet.getRow(i).getCell(statusCol).getCellType()==Cell.CELL_TYPE_BLANK)){
					cell = workSheet.getRow(i).createCell(statusCol);
					cell.setCellValue(Status.UNUSED.toString());
				}
				cell = workSheet.getRow(i).getCell(statusCol);
				if(cell.getStringCellValue().toString().equals(Status.UNUSED.toString())){
					npaNXX.NGP = String.valueOf(workSheet.getRow(i).getCell(mtnIndex).getNumericCellValue());
					npaNXX.Location = workSheet.getRow(i).getCell(passwordIndex).getStringCellValue();
					npaNXX.NPANXX = String.valueOf(workSheet.getRow(i).getCell(ssnIndex).getNumericCellValue());

					cell.setCellValue(Status.INUSE.toString());
					break;
				}
			}
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

		finally{
			WriteAndCloseFile(filePath, fileInput, workBook);
		}

		if(npaNXX==null)
			Log.error("FAILED To get account details; one among MTN/Password/SSN is blank");
		return npaNXX;
	}

	private XSSFSheet GetSheetFromWorkBook(XSSFWorkbook workbook, String sheetName){
		int numOfWorkBooks = 0;
		String sheet = null;
		numOfWorkBooks = workBook.getNumberOfSheets();
		for(int count = 0; count < numOfWorkBooks;count++){
			if(sheetName.toString().toLowerCase().contains(workBook.getSheetName(count).toLowerCase())){
				sheet = workBook.getSheetName(count);
				break;
			}
		}
		if(sheet!=null){
			worksheet = workBook.getSheet(sheet);
		}
		return worksheet;
	}

	private XSSFWorkbook GetWorkBook(FileNames fileName) throws IOException{
		if(fileName.toString().toLowerCase().contains("verizonedgeup")){
			fileInput= new FileInputStream(new File(VerizonEdgeUpSheet));
		}
		else
			fileInput= new FileInputStream(new File(VerizonRegressionNumberPortSheet));
		workBook = new XSSFWorkbook(fileInput);
		return workBook;
	}

	private String FilePath(FileNames fileName)
	{
		String testType = BrowserSettings.readConfig("test-type");
		String filePath = null;
		if(fileName.toString().toLowerCase().contains(FileNames.GlobalTestData.toString())){
			filePath = GlobalTestDataSheet;
		}
		else{
			if(testType.toLowerCase().equals("internal")){
				if(fileName.toString().toLowerCase().contains(FileNames.VerizonEdgeUp.toString().toLowerCase())){
					filePath = VerizonEdgeUpSheetInternal; 
				}
				else
					filePath = VerizonRegressionNumberPortSheetInternal;
			}
			else{
				if(fileName.toString().toLowerCase().contains(FileNames.VerizonEdgeUp.toString().toLowerCase())){
					filePath = VerizonEdgeUpSheet; 
				}
				else
					filePath = VerizonRegressionNumberPortSheet;
			}
		}
		return filePath;
	}

	private void WriteAndCloseFile(String filePath, FileInputStream fileInput, XSSFWorkbook workBook) throws IOException{
		fileInput.close(); 
		FileOutputStream output_file = new FileOutputStream(new File(filePath));                      
		workBook.write(output_file);
		output_file.close();
	}
}
