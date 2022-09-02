//TODO: Always put your name/number at the top of your code

package application;

// Standard JavaFX imports (never swing, never awt!!!)
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.Stage;

//Imports for components in this application.
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;

//Support for date entry.
import javafx.scene.control.DatePicker;

//Icons etc.
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
//Layout, containers etc.
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

//Support for quitting.
import javafx.application.Platform;


//Date handling.
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


//Currency output formatting.
import java.text.NumberFormat;
import java.util.Locale;

//Alerts...
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class InterestCalculator extends Application {

	//Components that need class scope.
	Label lblCapital, lblInterestRate, lblInvTerm;
	TextField txtfCapital, txtfInterestRate, txtfInvTerm;
	Button btnQuit, btnCalculate, btnDialog;
	CheckBox chkSimple, chkCompound;
	TextArea txtMain;

	public InterestCalculator() {
		//Instantiate components with 'new'.
		lblCapital = new Label("Capital:");
		
		lblInterestRate = new Label("Interest rate:");
		lblInvTerm = new Label("Investment term (yrs):");
		
		txtfCapital = new TextField();
		txtfInterestRate = new TextField();
		txtfInvTerm = new TextField();
				
		btnQuit = new Button("Quit");
		btnCalculate = new Button("Calculate");
		btnDialog = new Button("...");
		
		//Set button sizes.
		btnQuit.setMinWidth(80);
		btnCalculate.setMinWidth(80);
		
		chkSimple = new CheckBox("Simple interest");
		chkCompound = new CheckBox("Compound interest");
				
		txtMain = new TextArea();
				
	}//constructor()
	
	@Override
	public void init() { //Event handling: Respond to program events.
		//Clicking btnQUit quits the application.
		btnQuit.setOnAction(click -> Platform.exit()); // closes entire app
		
		//Handle events on Dialog button
		btnDialog.setOnAction(click -> showTermDialog());
		
		//Handle events on the calculate button
		//Calculate the interest due, and show analysis in main text area
		btnCalculate.setOnAction(click -> showInterestAnalysis());
				
	}//init()
	
	private void showInterestAnalysis() {
		// local variables to store values from the user
		double capital = 0;
		double intRate = 0;
		double years = 0;
		
		// get the user data, assign to the variables above
		// try because user may not have entered some values (or entered wrong type)
		try {
			capital = Double.parseDouble(txtfCapital.getText());
			intRate = Double.parseDouble(txtfInterestRate.getText());
			years = Double.parseDouble(txtfInvTerm.getText());
		}
		catch(NumberFormatException nfe) {
			System.err.print("No data or incorrect type of data entered!" + nfe.toString());
			txtMain.setText("Error: You must enter a valid number in all textfields!");
		}
		
		//check if the simple interest checkbox is selected
		if(chkSimple.isSelected()) {
			//show the simple interest analysis in txtMain
			showSimpleInterest(capital, intRate, years);
		}
		
		//check if the compound interest checkbox is selected
		if(chkCompound.isSelected()) {
			//display the compound interest
			showCompoundInterest(capital, intRate, years);
		}
		
	}//showInterestA()
	
	// method to show simple interest
	private void showSimpleInterest(double cap, double irate, double yrs) {
		double interest = 0;
		double increasedCapital;
		String analysisString = "";
		
		// amount of interest = amount * irate/100 * years;
		double interestAmount = getSimpleInterest(cap, irate, yrs);
		
		// get the increased capital by adding the interest
		increasedCapital = cap + interestAmount;
		
		Locale currLocale = Locale.getDefault();
		System.out.println(currLocale.getDisplayCountry());
		System.out.println(currLocale.getDisplayLanguage());
		NumberFormat currFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
		
		// create analysis string
		analysisString = "Simple Interest:" + "\n\tYears: " + yrs + "\n\tInitial Capital: " 
						+ currFormat.format(cap) + "\n\tInterest Earned: " + currFormat.format(interestAmount) + "\n\tFinal Amount: " 
						+ currFormat.format(increasedCapital) + "\n\n";
		
		//only clear txtMain if the previous was the error message
		if(txtMain.getText().contains("Error")) {
			// clear the error message
			txtMain.clear();
		}
		//show analysis in txtMain in the main ui
		txtMain.appendText(analysisString); // append it not overwrite it!	
	}//showSimpleInterest()
	
	// method for the compound interest
	private void showCompoundInterest(double cap, double irate, double yrs) {
		double interestAmt = 0;
		double increasedCapital = cap;
		double totalInterest = 0;
		String analysisString = "";
		
		// use a loop to apply simple interest repeatedly
		// increasing capital amount repeatedly = compound interest
		
		for(int i =1; i<=yrs; i++) {
			interestAmt = getSimpleInterest(increasedCapital, irate, 1);
			totalInterest += interestAmt; // total interest increases
			increasedCapital += interestAmt;
		}
		
		//Format the currency
	
		Locale currLocale = Locale.getDefault();
		System.out.println(currLocale.getDisplayCountry());
		System.out.println(currLocale.getDisplayLanguage());
		// don't see euros?
		//use Locale.GERMANY instead 
		NumberFormat currFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
		
		// try formatting the string even more?
		// maybe bold, colours etc?
		
		// create analysis
		analysisString += "\nCompound Interest:" + "\nYear: " + yrs + "\n\tInitial Capital: " + currFormat.format(cap) + "\n\tInterest Earned: "
				+ currFormat.format(totalInterest) + "\n\tFinal Amount: " + currFormat.format(increasedCapital) + "\n\n";
		
		// show the analysis string in the main text area
		txtMain.appendText(analysisString);
		
		//another way to append (not recommend)
		//txtMain.setText(txtMain.getText() + analysisString);
		
	}//showCompoundInt()
	
	// method to calculate the actual interest
	private double getSimpleInterest(double cap, double irate, double yrs) {
		double simpleInterest = 0; 
		
		// 5 % = 5/100
		
		//perform calculations to get the simple interest
		simpleInterest = cap * (irate/100) * yrs; //irate is a percentage!
		
		return simpleInterest;
	}
	
	private void showTermDialog() {
		// create our own dialog
		// create a secondary stage (second window)
		Stage termDialogStage = new Stage();
		
		// set the title of the dialog
		termDialogStage.setTitle("Select Investment Term:");
		
		// default width & height
		termDialogStage.setHeight(300);
		termDialogStage.setWidth(850);
		
		//create a layout for the dialog
		VBox vbDialogMain = new VBox();
		
		// subcontainers
		GridPane gpDialog = new GridPane();
		HBox hbDialogBtns = new HBox();
		
		// add subcontainers to the main layout
		vbDialogMain.getChildren().addAll(gpDialog, hbDialogBtns);
		
		
		//Create components for the dialog
		Label lblTermStart = new Label("Investment Start Date:");
		Label lblTermEnd = new Label("Investment End Date:");
		
		//datepickers support uniform format date entry
		DatePicker dpStart = new DatePicker(); 
		DatePicker dpEnd = new DatePicker();
		
		//default dates (avoid null dates)
		dpStart.setValue(LocalDate.now());
		dpEnd.setValue(LocalDate.now());
		
		// no timetravel! startdate 
		
		// buttons 
		Button btnCancel = new Button("Cancel");
		Button btnOk = new Button("Ok");
		
		//add controls to the layout
		gpDialog.add(lblTermStart, 0, 0, 1, 1); // cols, rows, colspan, rowspan
		gpDialog.add(dpStart, 1, 0, 2, 1);
		gpDialog.add(lblTermEnd, 0, 1);
		gpDialog.add(dpEnd, 1, 1);
		
		//Add buttons
		hbDialogBtns.getChildren().addAll(btnCancel, btnOk);
		
		//Set spacing and padding
		vbDialogMain.setPadding(new Insets(20));
		vbDialogMain.setSpacing(20);
		hbDialogBtns.setPadding(new Insets(20,0,0,0));
		hbDialogBtns.setSpacing(20);
		gpDialog.setVgap(20);
		gpDialog.setHgap(10);
		
		//Right align the buttons
		hbDialogBtns.setAlignment(Pos.BASELINE_RIGHT);
		
		//TODO: add your own style either inline or in the css stylesheet
		
		// example style - inline, basic
		vbDialogMain.setStyle("-fx-font-size: 20pt; -fx-font-family: \"Gill Sans\";-fx-text-inner-color: blue;"
				+ "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #65A9B8, #0E6E82);");
	
		
		//Event handling for the dialog
		// cancel should close only the dialog
		btnCancel.setOnAction(click -> termDialogStage.close()); 
		
		// ok should take the dates and return amount in years to main UI
		btnOk.setOnAction(click -> {
			long years = 0;
			LocalDate startDate, endDate;
			
			try {
				// get the dates entered by the user
				startDate = dpStart.getValue();
				endDate = dpEnd.getValue();
				
				//invalid date
				if(startDate.isAfter(endDate)) {
					Alert alertDates = new Alert(AlertType.ERROR);
					
					//Configure the alert
					alertDates.setTitle("Date entry Error");
					alertDates.setHeaderText("Invalid Dates Entered.");
					alertDates.setContentText("The end date occurs after the start date.");
					
					// Show the alert
					alertDates.showAndWait(); // show alert and wait for user response
					//linux fix
					alertDates.setResizable(true);
				}//if
				
				//valid date
				else {//then its valid+
				 // how much time in years between these two dates?
				 years = ChronoUnit.YEARS.between(startDate, endDate);
				 
					// simple check to disallow negative numbers
					if(years < 0) {
						years = 0;
						//output to the user? 
						System.out.println("Start date should be before the end date.");
					}
					// set the interval in years back into the main UI textfield
					txtfInvTerm.setText(years + "");
					
					//close the dialog
					termDialogStage.close();
					
				}//else
			}//try
			catch(NullPointerException npe) {
				Alert alertEmpty = new Alert(AlertType.ERROR);
				
				//Configure the alert
				alertEmpty.setTitle("Missing dates");
				alertEmpty.setHeaderText("No dates selected.");
				alertEmpty.setContentText("Dates cannot be empty. To choose your own time in years, exit the dialog and enter in the main UI textbox.");
				
				// Show the alert
				alertEmpty.showAndWait(); // show alert and wait for user response
				
				//Linux fix
				alertEmpty.setResizable(true);
			}//catch
			
		
		}); //end of start button event handling
		
		//scene, show stage etc
		Scene s = new Scene(vbDialogMain); //take in main container of dialog
		
		termDialogStage.setScene(s);
		
		termDialogStage.show();
			
	}//showTermDialog()
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		//User interface construction.
		//Set the title.
		primaryStage.setTitle("Interest Calculator v1.0");
		
		//Add an appropriate icon.
		try {
			primaryStage.getIcons().add(new Image("./Assets/ledger1.png"));
		}
		
		catch(IllegalArgumentException oopsie) {
			System.out.println("Image for icon is not in your project assets folder.");
			System.out.println(oopsie.getStackTrace());
		}
		
		//Set the width and height
		primaryStage.setWidth(700);
		primaryStage.setHeight(500);
		
		//Create a layout.
		VBox vbMain = new VBox(); //Main container.
		vbMain.setPadding(new Insets(10));
		vbMain.setSpacing(10);
		
		GridPane gp = new GridPane(); //Contains textfields, labels, a button and checkboxes.
		//gridpane spacing
		gp.setHgap(10);								
		gp.setVgap(10);
		
		HBox hbButtons = new HBox();//Just contains the Quit and Calculate buttons.
		hbButtons.setSpacing(10);
		
		//Put the gp into the main container.
		vbMain.getChildren().add(gp);
		
		//Add components to the layout.
		gp.add(lblCapital, 0, 0);
		gp.add(txtfCapital, 1, 0);
		
		gp.add(lblInterestRate, 0, 1);
		gp.add(txtfInterestRate, 1, 1);
		
		gp.add(lblInvTerm, 0, 2);
		gp.add(txtfInvTerm, 1, 2);
		gp.add(btnDialog, 2, 2);
		
		gp.add(chkSimple, 1, 3);
		gp.add(chkCompound, 1, 4);
		
		vbMain.getChildren().add(txtMain);
		
		//Add the buttons to the buttons hbox.
		hbButtons.getChildren().addAll(btnQuit, btnCalculate);
		
		//Now, add the button box to the main container.
		vbMain.getChildren().add(hbButtons);
		hbButtons.setAlignment(Pos.BASELINE_RIGHT);
		
		//Create a scene.
		Scene s = new Scene(vbMain);
		
		//Set the scene.
		primaryStage.setScene(s);
		
		//TODO: add a new style to the intrcalc stylesheet
		s.getStylesheets().add("./Assets/intrcalc_style.css");

		//Show the stage.
		primaryStage.show();
		
	}//start()

	public static void main(String[] args) {
		//Launch the application.
		launch(args);
	
	}//main()

}//class