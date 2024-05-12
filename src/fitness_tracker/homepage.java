/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package fitness_tracker;
                                /*HOMEPAGE IMPLEMENTATION*/
// GPT
/*
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
*/
// GPT

import static fitness_tracker.update_acc.getuserID;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JLabel;
//import java.sql.SQLException;
import javax.swing.JOptionPane;
//import java.util.regex.Pattern;
/**
 *
 * @author USMAN SOHAIL
 */

// for diplaying charts

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
//--------------------------------------------
public class homepage extends javax.swing.JFrame {

    /**
     * Creates new form homepage
     */
        /**
     * @param args the command line arguments
     */
      private ChartPanel chartPanel; // Declare the ChartPanel as a class member

     private static int loggedInUserId; // Store the ID of the logged-in user
    private static final int DEFAULT_USER_ID = 2; // Set a default user ID if none is passed
  Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    
    
 public homepage(int uid) {
     this.loggedInUserId = uid;
     //------------------------------------------------------------
             
     //--------------------------------------------------------        
        initComponents();
        displayUserData();
        displayCalorieChart(); // Call the method to display the calorie chart
        
        // Add chart panel directly to jPanel1
        jPanel2.setLayout(new BorderLayout()); // Set layout manager to BorderLayout
        jPanel2.add(chartPanel, BorderLayout.CENTER); // Add the chart panel to the center of jPanel1
        jPanel3.setLayout(new BorderLayout()); // Set layout manager to BorderLayout

        displayWeightLossProgress();

        displayUserDetails();
        displayExerciseDetails();

 }
    
    private static int getuserID(){
        return loggedInUserId;
    }

private void displayUserData() {
    try {
        
        // Establish database connection
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fit", "root", "");
        
        // Query to retrieve user data based on the logged-in user ID
        String query = "SELECT * FROM users WHERE user_id = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, loggedInUserId); // Use the logged-in user ID to fetch data
        resultSet = statement.executeQuery();
        
        fitfolio.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 24)); // Set font to bold, size 24
fitfolio.setText("Fit Folio"); // Set the text to "Fit Folio"
//fitfolio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // Center align the text horizontally

        if (resultSet.next()) {
            // Display user data in JLabels or JTextFields
            String username = resultSet.getString("username");
            int age = resultSet.getInt("age");
            // Display other user data as needed
            
            // Iterate over components in jPanel1 to find the JLabel with text "name"
            for (Component component : jPanel1.getComponents()) {
                if (component instanceof JLabel) {
                    JLabel label = (JLabel) component;
                    if ("name".equals(label.getText())) {
                        label.setText("ID: " + username);
                        break; // Once found, no need to continue iterating
                    }
                }
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    } finally {
        // Close database resources
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
    
}

private void displayCalorieChart() {
    try {
        // Establish database connection
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fit", "root", "");

        // Query to retrieve calorie intake and expenditure for a user
        String intakeQuery = "SELECT intake_date, SUM(calories_per_quantity * quantity) AS total_calories FROM CalorieIntake WHERE user_id = ? GROUP BY intake_date";
        String expenditureQuery = "SELECT expenditure_date, SUM(calories_burned) AS total_calories FROM CalorieExpenditure WHERE user_id = ? GROUP BY expenditure_date";

        PreparedStatement intakeStatement = connection.prepareStatement(intakeQuery);
        intakeStatement.setInt(1, loggedInUserId); // Use the logged-in user ID to fetch data
        ResultSet intakeResultSet = intakeStatement.executeQuery();

        PreparedStatement expenditureStatement = connection.prepareStatement(expenditureQuery);
        expenditureStatement.setInt(1, loggedInUserId); // Use the logged-in user ID to fetch data
        ResultSet expenditureResultSet = expenditureStatement.executeQuery();

        // Create dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Add calorie intake data to the dataset
        while (intakeResultSet.next()) {
            System.out.println("Intake Date: " + intakeResultSet.getString("intake_date") + ", Total Calories: " + intakeResultSet.getDouble("total_calories")); // Debugging output
            dataset.addValue(intakeResultSet.getDouble("total_calories"), "Calorie Intake", intakeResultSet.getString("intake_date"));
        }

        // Add calorie expenditure data to the dataset
        while (expenditureResultSet.next()) {
            System.out.println("Expenditure Date: " + expenditureResultSet.getString("expenditure_date") + ", Total Calories: " + expenditureResultSet.getDouble("total_calories")); // Debugging output
            dataset.addValue(expenditureResultSet.getDouble("total_calories"), "Calorie Expenditure", expenditureResultSet.getString("expenditure_date"));
        }

        // Create chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Calorie Intake vs. Expenditure", // Chart title
                "Date", // X-axis label
                "Calories", // Y-axis label
                dataset
        );

        // Create ChartPanel
        chartPanel = new ChartPanel(chart);
 
        // Set the minimum size of the chart panel
        chartPanel.setMinimumSize(new Dimension(300, 200)); // Set your preferred minimum size here
        chartPanel.setMaximumSize(new Dimension(300, 250)); // Set your preferred maximum size here

    } catch (Exception e) {
        e.printStackTrace();
    }
}


private void displayWeightLossProgress() {
    try {
        // Establish database connection
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fit", "root", "");

        // Query to retrieve weight loss goal data based on the logged-in user ID
        String query = "SELECT * FROM WeightLossGoals WHERE user_id = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, loggedInUserId); // Use the logged-in user ID to fetch data
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            double currentWeight = resultSet.getDouble("current_weight");
            double targetWeight = resultSet.getDouble("target_weight");
            double weightDifference = targetWeight - currentWeight;
            double progress;

            // Calculate progress based on weight difference
            if (weightDifference == 0) {
                progress = 100;
            } else if (weightDifference > 0) {
                progress = 100 * (1 - (weightDifference / targetWeight));
            } else {
                progress = 100 * (1 - (Math.abs(weightDifference) / currentWeight));
            }

            // Ensure progress is within the range [0, 100]
            progress = Math.max(0, Math.min(100, progress));

            // Create circular progress bar
            JPanel progressPanel = new JPanel(new BorderLayout());
            progressPanel.setPreferredSize(new Dimension(200, 200));

            // Create a label for the heading
            JLabel headingLabel = new JLabel("Goal Progress:");
            headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
            progressPanel.add(headingLabel, BorderLayout.NORTH);

           // Create the circular progress bar
JProgressBar circularProgressBar = new JProgressBar();
circularProgressBar.setMinimum(0);
circularProgressBar.setMaximum(100);
circularProgressBar.setValue((int) progress);
circularProgressBar.setStringPainted(true);
circularProgressBar.setString(new DecimalFormat("##").format(progress) + "%");
circularProgressBar.setForeground(Color.GREEN); // Color for the progress
circularProgressBar.setBorderPainted(false);
circularProgressBar.setFont(new Font("Arial", Font.BOLD, 25));
circularProgressBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
    protected Color getSelectionBackground() {
        return Color.black;
    }

    protected Color getSelectionForeground() {
        return Color.white;
    }
});

// Create JLabels to display current and target weights
JLabel currentWeightLabel = new JLabel("Current Weight: " + currentWeight + " kg");
JLabel targetWeightLabel = new JLabel("Target Weight: " + targetWeight + " kg");
currentWeightLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Adjust font size
targetWeightLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Adjust font size

// Create a panel to hold the text labels
JPanel weightLabelsPanel = new JPanel(new GridLayout(2, 1)); // 2 rows, 1 column
weightLabelsPanel.add(currentWeightLabel);
weightLabelsPanel.add(targetWeightLabel);
weightLabelsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Add padding to the top

// Add components to the progress panel
progressPanel.add(headingLabel, BorderLayout.NORTH); // Add heading label
progressPanel.add(circularProgressBar, BorderLayout.CENTER); // Add progress bar
progressPanel.add(weightLabelsPanel, BorderLayout.SOUTH); // Add text labels with padding at the top

            // Add the circular progress bar and weight labels to the progress panel
            progressPanel.add(circularProgressBar, BorderLayout.CENTER); // Add the progress bar to the center
            progressPanel.add(weightLabelsPanel, BorderLayout.SOUTH); // Add the weight labels below the progress bar

            // Add progress panel to jPanel3
            jPanel3.removeAll(); // Clear jPanel3 before adding the new progress panel
            jPanel3.add(progressPanel);
            jPanel3.revalidate(); // Refresh the jPanel3 to reflect the changes
            jPanel3.repaint(); // Repaint the jPanel3
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    } finally {
        // Close database resources
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
}

//----------------------------------------
private void displayUserDetails() {
    try {
        // Establish database connection
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fit", "root", "");

        // Query to retrieve user details based on the logged-in user ID
        String query = "SELECT username, age, height_ft, height_inches, weight FROM Users WHERE user_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, loggedInUserId); // Use the logged-in user ID to fetch data
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String username = resultSet.getString("username");
            int age = resultSet.getInt("age");
            double heightFt = resultSet.getDouble("height_ft");
            int heightInches = resultSet.getInt("height_inches");
            double weight = resultSet.getDouble("weight");

            // Update the text of JLabels using their variable names and set font properties
            nameLabel.setText("Name: " + username);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 17)); // Example font settings
            ageLabel.setText("Age: " + age);
            ageLabel.setFont(new Font("Arial", Font.BOLD, 17)); // Example font settings
            heightLabel.setText("Height: " + heightFt + " ft " + heightInches + " inches");
            heightLabel.setFont(new Font("Arial", Font.BOLD, 17)); // Example font settings
            weightLabel.setText("Weight: " + weight + " kg");
            weightLabel.setFont(new Font("Arial", Font.BOLD, 17)); // Example font settings
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

private void displayExerciseDetails() {
    try {
        // Establish database connection
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fit", "root", "");

        // Query to retrieve exercise details based on the logged-in user ID
        String query = "SELECT exercise_name, intensity_level, met, duration_minutes FROM Exercise WHERE user_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, loggedInUserId); // Use the logged-in user ID to fetch data
        ResultSet resultSet = statement.executeQuery();

        // Create a StringBuilder to store exercise details
        StringBuilder exerciseDetails = new StringBuilder();

        // Append exercise details to the StringBuilder
        while (resultSet.next()) {
            String exerciseName = resultSet.getString("exercise_name");
            String intensityLevel = resultSet.getString("intensity_level");
            double met = resultSet.getDouble("met");
            int durationMinutes = resultSet.getInt("duration_minutes");

            exerciseDetails.append("Exercise: ").append(exerciseName).append("\n");
            exerciseDetails.append("Intensity: ").append(intensityLevel).append("\n");
            exerciseDetails.append("MET: ").append(met).append("\n");
            exerciseDetails.append("Duration: ").append(durationMinutes).append(" minutes\n\n");
            exerciseDetails.append("------------------------------------------------------------\n");
        }

        // Set the text of the JTextArea with exercise details
        exerciseTextArea1.setFont(new Font("Arial", Font.BOLD, 14)); // Set font to bold and size 14
        exerciseTextArea1.setText(exerciseDetails.toString());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

public void close(){
        WindowEvent closeWindow = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeWindow);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelUsername = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        ageLabel = new javax.swing.JLabel();
        weightLabel = new javax.swing.JLabel();
        heightLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        exerciseTextArea1 = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        fitfolio = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jToggleButton6 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(51, 153, 255));

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 700));

        jLabelUsername.setBackground(new java.awt.Color(51, 153, 255));
        jLabelUsername.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jLabelUsername.setForeground(new java.awt.Color(255, 255, 255));
        jLabelUsername.setText("name");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 132, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 121, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(18, 119, 110));

        nameLabel.setText("nameLabel");

        ageLabel.setText("jLabel1");

        weightLabel.setText("jLabel1");

        heightLabel.setText("jLabel1");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                    .addComponent(ageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(weightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(heightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(150, 150, 150))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(heightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 102, Short.MAX_VALUE))
        );

        exerciseTextArea1.setBackground(new java.awt.Color(50, 134, 147));
        exerciseTextArea1.setColumns(20);
        exerciseTextArea1.setRows(5);
        jScrollPane2.setViewportView(exerciseTextArea1);

        jPanel6.setBackground(new java.awt.Color(73, 216, 230));

        fitfolio.setBackground(new java.awt.Color(106, 163, 182));
        fitfolio.setText("jLabel1");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addComponent(fitfolio, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(154, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addComponent(fitfolio, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        jPanel5.setBackground(new java.awt.Color(0, 255, 204));

        jToggleButton1.setBackground(new java.awt.Color(51, 153, 255));
        jToggleButton1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jToggleButton1.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton1.setText("Home");
        jToggleButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton3.setBackground(new java.awt.Color(51, 153, 255));
        jToggleButton3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jToggleButton3.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton3.setText("Recommendations");
        jToggleButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jToggleButton5.setBackground(new java.awt.Color(51, 153, 255));
        jToggleButton5.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jToggleButton5.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton5.setText("Goals");
        jToggleButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });

        jToggleButton4.setBackground(new java.awt.Color(51, 153, 255));
        jToggleButton4.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jToggleButton4.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton4.setText("Log out");
        jToggleButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });

        jComboBox1.setBackground(new java.awt.Color(51, 153, 255));
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"Calorie intake","Exercises"}));
        jComboBox1.setSelectedIndex(0);
        jComboBox1.setSelectedItem(0);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jToggleButton6.setBackground(new java.awt.Color(51, 153, 255));
        jToggleButton6.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jToggleButton6.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton6.setText("Account");
        jToggleButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jToggleButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(37, 37, 37)
                .addComponent(jToggleButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(40, 40, 40)
                .addComponent(jToggleButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1)
                    .addComponent(jToggleButton3)
                    .addComponent(jToggleButton5)
                    .addComponent(jToggleButton4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton6))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(159, 159, 159)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 233, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 911, Short.MAX_VALUE)
                        .addComponent(jLabelUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(290, 290, 290)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabelUsername)
                .addGap(3, 3, 3)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabelUsername.getAccessibleContext().setAccessibleName("jLabelUsername");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1012, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        close();
        homepage homeFrame = new homepage(getuserID());
        homeFrame.setVisible(true);
        homeFrame.pack();
        homeFrame.setLocationRelativeTo(null); //center
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed

        close();
        recPlan loginFrame = new recPlan(getuserID());
        loginFrame.setVisible(true);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null); //center
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed

        close();
        Goals homeFrame = new Goals(getuserID());
        homeFrame.setVisible(true);
        homeFrame.pack();
        homeFrame.setLocationRelativeTo(null); //center
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        close();
        login loginFrame = new login();
        loginFrame.setVisible(true);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null); //center
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        int selectedIndex = jComboBox1.getSelectedIndex();

        if(selectedIndex == 0){
            close();
            tracker_calories_intake intakeFrame = new tracker_calories_intake(getuserID());
            intakeFrame.setVisible(true);
            intakeFrame.pack();
            intakeFrame.setLocationRelativeTo(null); //center
        }
        else if (selectedIndex == 1){
            close();
            exercises exerciseFrame = new exercises(getuserID());
            exerciseFrame.setVisible(true);
            exerciseFrame.pack();
            exerciseFrame.setLocationRelativeTo(null); //center
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
        // TODO add your handling code here:
        int userID = getuserID();
        close();
        update_acc updateFrame = new update_acc(userID);
        updateFrame.setVisible(true);
        updateFrame.pack();
        updateFrame.setLocationRelativeTo(null); //center
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new homepage(getuserID()).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ageLabel;
    private javax.swing.JTextArea exerciseTextArea1;
    private javax.swing.JLabel fitfolio;
    private javax.swing.JLabel heightLabel;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabelUsername;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel weightLabel;
    // End of variables declaration//GEN-END:variables
}
