import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;

public class M06PA1 extends Application 
{

    //Database connection info
    String url = "jdbc:mysql://localhost:3306/StaffDB"; 
    String user = "root"; 
    String pass = ""; 

    //text input fields
    TextField tfId = new TextField();
    TextField tfLastName = new TextField();
    TextField tfFirstName = new TextField();
    TextField tfMi = new TextField();
    TextField tfAddress = new TextField();
    TextField tfCity = new TextField();
    TextField tfState = new TextField();
    TextField tfTelephone = new TextField();
    TextField tfEmail = new TextField();

    // Buttons
    Button btView = new Button("View");
    Button btInsert = new Button("Insert");
    Button btUpdate = new Button("Update");
    Button btClear = new Button("Clear");

    Connection conn; // Database connection

    public void start(Stage stage) 
    {

        GridPane pane = new GridPane(); // Create a grid layout
        pane.setHgap(5); // Horizontal spacing between cells
        pane.setVgap(5); // Vertical spacing

        //ID
        pane.add(new Label("ID"), 0, 0); // Label
        pane.add(tfId, 1, 0); // Input box

        //Last,First,Middle
        pane.add(new Label("Last Name"), 0, 1);
        pane.add(tfLastName, 1, 1);
        pane.add(new Label("First Name"), 2, 1);
        pane.add(tfFirstName, 3, 1);
        pane.add(new Label("MI"), 4, 1);
        tfMi.setMaxWidth(40); 
        pane.add(tfMi, 5, 1);

        //Address
        pane.add(new Label("Address"), 0, 2);
        pane.add(tfAddress, 1, 2, 5, 1); 

        //city, state
        pane.add(new Label("City"), 0, 3);
        pane.add(tfCity, 1, 3);
        pane.add(new Label("State"), 2, 3);
        tfState.setMaxWidth(40); 
        tfState.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() > 2) tfState.setText(oldText);
        });
        pane.add(tfState, 3, 3);

        //telephone
        pane.add(new Label("Telephone"), 0, 4);
        tfTelephone.setMaxWidth(120);
        pane.add(tfTelephone, 1, 4);

        //email
        pane.add(new Label("Email"), 0, 5);
        pane.add(tfEmail, 1, 5, 5, 1); // Span multiple columns

        //buttons
        pane.add(btView, 0, 6);
        pane.add(btInsert, 1, 6);
        pane.add(btUpdate, 2, 6);
        pane.add(btClear, 3, 6);

        // Event handlers for buttons
        btView.setOnAction(e -> viewStaff()); // View staff info by ID
        btInsert.setOnAction(e -> insertStaff()); // Insert new staff record
        btUpdate.setOnAction(e -> updateStaff()); // Update existing record
        btClear.setOnAction(e -> clearFields()); // Clear all input fields

        // Connect to database
        try {
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection Found."); //Connection found
        } catch (SQLException ex) {
            showAlert("Database Connection Error", ex.getMessage()); //Connection error
        }

        Scene scene = new Scene(pane, 700, 300); //scene window
        stage.setTitle("Exercise M06PA1"); //title
        stage.setScene(scene); //create scene
        stage.show(); //display
    }

    // Method to view staff info by ID
    void viewStaff() 
    {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Staff WHERE id=?");
            ps.setString(1, tfId.getText()); //id input
            ResultSet rs = ps.executeQuery(); //query get
            if (rs.next()) { //find record
                tfLastName.setText(rs.getString("lastName"));
                tfFirstName.setText(rs.getString("firstName"));
                tfMi.setText(rs.getString("mi"));
                tfAddress.setText(rs.getString("address"));
                tfCity.setText(rs.getString("city"));
                tfState.setText(rs.getString("state"));
                tfTelephone.setText(rs.getString("telephone"));
                tfEmail.setText(rs.getString("email"));
            } else { showAlert("Error", "No record with matching ID " + tfId.getText()); } //error if id not found
        } catch (SQLException ex) {
            showAlert("Error", ex.getMessage());
        }
    }

    //create new staff record
    void insertStaff() 
    {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Staff (id,lastName,firstName,mi,address,city,state,telephone,email) VALUES (?,?,?,?,?,?,?,?,?)" );
            ps.setString(1, tfId.getText());
            ps.setString(2, tfLastName.getText());
            ps.setString(3, tfFirstName.getText());
            ps.setString(4, tfMi.getText());
            ps.setString(5, tfAddress.getText());
            ps.setString(6, tfCity.getText());
            ps.setString(7, tfState.getText());
            ps.setString(8, tfTelephone.getText());
            ps.setString(9, tfEmail.getText());

            int rows = ps.executeUpdate(); //update database
            if (rows > 0) showAlert("Database Update", "Successfull insert!");
        } catch (SQLException ex) {
            showAlert("Error", ex.getMessage());
        }
    }

    //update staff record
    void updateStaff() 
    {
        try {
            PreparedStatement ps = conn.prepareStatement( "UPDATE Staff SET lastName=?,firstName=?,mi=?,address=?,city=?,state=?,telephone=?,email=? WHERE id=?" );
            ps.setString(1, tfLastName.getText());
            ps.setString(2, tfFirstName.getText());
            ps.setString(3, tfMi.getText());
            ps.setString(4, tfAddress.getText());
            ps.setString(5, tfCity.getText());
            ps.setString(6, tfState.getText());
            ps.setString(7, tfTelephone.getText());
            ps.setString(8, tfEmail.getText());
            ps.setString(9, tfId.getText());

            int rows = ps.executeUpdate();
            if (rows > 0) showAlert("Success", "Updated!"); //succesful update
            else showAlert("Error", "No record with matching ID " + tfId.getText()); //error
        } catch (SQLException ex) {
            showAlert("Error", ex.getMessage());
        }
    }

    //clear all input fields
    void clearFields() 
    {
        tfId.setText("");
        tfLastName.setText("");
        tfFirstName.setText("");
        tfMi.setText("");
        tfAddress.setText("");
        tfCity.setText("");
        tfState.setText("");
        tfTelephone.setText("");
        tfEmail.setText("");
    }

    //method box for alerts
    void showAlert(String title, String msg) 
    {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch(args); // Launch JavaFX app
    }
}
