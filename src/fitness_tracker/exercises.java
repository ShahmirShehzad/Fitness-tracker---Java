/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package fitness_tracker;

import static fitness_tracker.tracker_calories_intake.getuserID;
import static fitness_tracker.update_acc.getuserID;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.lang.System.Logger.Level;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author shehz
 */
public class exercises extends javax.swing.JFrame {

    /**
     * Creates new form exercises
     */
    private static int userid;
    public exercises(int userID) {
        this.userid = userID;
        initComponents();
        populatenameItems();
        populateintensityItems();
        populatemetItems();
        populatedurationItems();
        
        nameComboBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> combo = (JComboBox<String>) e.getSource();
            int selectedIndex = combo.getSelectedIndex();
            if (selectedIndex >= 0) { // Ensure a valid selection
                String selectedFood = (String) combo.getSelectedItem();
                if (selectedFood.equals("Enter Custom Food...")) {
                    // Allow the user to enter a custom food item
                    combo.setEditable(true);
                    // Set the quantity and calorie combo boxes to editable as well
                    intensityComboBox.setEditable(true);
                    metComboBox.setEditable(true);
                    durationComboBox.setEditable(true);
                } else {
                    // Disable editing of the combo boxes
                    combo.setEditable(false);
                    intensityComboBox.setEditable(false);
                    metComboBox.setEditable(false);
                    durationComboBox.setEditable(false);
                    // Select corresponding items
                    selectCorrespondingItems(selectedIndex);
                }
            }
        }
    });
        
        intensityComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> combo = (JComboBox<String>) e.getSource();
                String selectedFood = (String) combo.getSelectedItem();
                if (selectedFood.equals("Enter Custom Food...")) {
                    // Allow the user to edit the selected item
                    combo.setEditable(true);
                    // Select all text in the combo box
                    ((JTextField) combo.getEditor().getEditorComponent()).selectAll();
                } else {
                    // Disable editing of the combo box
                    combo.setEditable(false);
                }
            }
        });
        
        metComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> combo = (JComboBox<String>) e.getSource();
                String selectedFood = (String) combo.getSelectedItem();
                if (selectedFood.equals("Enter Custom Food...")) {
                    // Allow the user to edit the selected item
                    combo.setEditable(true);
                    // Select all text in the combo box
                    ((JTextField) combo.getEditor().getEditorComponent()).selectAll();
                } else {
                    // Disable editing of the combo box
                    combo.setEditable(false);
                }
            }
        });
        
        durationComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> combo = (JComboBox<String>) e.getSource();
                String selectedFood = (String) combo.getSelectedItem();
                if (selectedFood.equals("Enter Custom Food...")) {
                    // Allow the user to edit the selected item
                    combo.setEditable(true);
                    // Select all text in the combo box
                    ((JTextField) combo.getEditor().getEditorComponent()).selectAll();
                } else {
                    // Disable editing of the combo box
                    combo.setEditable(false);
                }
            }
        });
        
        // Add action listener to enterButton
        enterExercise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected food item
                String selectedname = (String) nameComboBox.getSelectedItem();
                String selectedIntensity = (String) intensityComboBox.getSelectedItem();
                String selectedMet = (String) metComboBox.getSelectedItem();
                String selectedDur = (String) durationComboBox.getSelectedItem();
                if (!selectedname.isEmpty() && !selectedname.equals("Enter Custom Food...")) {
                    // Save the selected food item to the database
                    saveExerciseItem(selectedname,selectedIntensity,selectedMet,selectedDur);
                }
            }
        });
    }
    
    // Method to select corresponding items in quantity and calorie combo boxes
    private void selectCorrespondingItems(int selectedIndex) {
        String selectedname = (String) nameComboBox.getSelectedItem();
        String selectedIntensity = (String) intensityComboBox.getItemAt(selectedIndex);
        String selectedMet = (String) metComboBox.getItemAt(selectedIndex);
        String selectedDur = (String) durationComboBox.getItemAt(selectedIndex);
        if (selectedname != null && !selectedname.equals("Enter Custom Food...")) {
            intensityComboBox.setSelectedItem(selectedIntensity);
            metComboBox.setSelectedItem(selectedMet);
            durationComboBox.setSelectedItem(selectedDur);
        }
    }

    public static int getuserID(){
        return userid;
    }
    
    private void populatenameItems() {
        // Connect to the database and fetch food items
        ArrayList<String> foodItems = fetchnameitems();
        
        // Add fetched food items to the JComboBox
        for (String item : foodItems) {
            nameComboBox.addItem(item);
        }
        
        // Add an option for entering custom food
        nameComboBox.addItem("Enter Custom Food...");
    }
    
    private void populateintensityItems() {
        // Connect to the database and fetch food items
        ArrayList<String> foodItems = fetchintensityitems();
        
        // Add fetched food items to the JComboBox
        for (String item : foodItems) {
            intensityComboBox.addItem(item);
        }
        
        // Add an option for entering custom food
        intensityComboBox.addItem("Enter Custom Food...");
    }
    
    private void populatemetItems() {
        // Connect to the database and fetch food items
        ArrayList<String> foodItems = fetchmetitems();
        
        // Add fetched food items to the JComboBox
        for (String item : foodItems) {
            metComboBox.addItem(item);
        }
        
        // Add an option for entering custom food
        metComboBox.addItem("Enter Custom Food...");
    }
    
    private void populatedurationItems() {
        // Connect to the database and fetch food items
        ArrayList<String> foodItems = fetchdurationitems();
        
        // Add fetched food items to the JComboBox
        for (String item : foodItems) {
            durationComboBox.addItem(item);
        }
        
        // Add an option for entering custom food
        durationComboBox.addItem("Enter Custom Food...");
    }
    
    public void close(){
        WindowEvent closeWindow = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeWindow);
    }
    
    private ArrayList<String> fetchnameitems(){
        try{
            ArrayList<String> names = new ArrayList<>();
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:MySQL://localhost:3306/fit";
            String user = "root";
            String pass = "";
            Connection con = DriverManager.getConnection(url,user,pass);
            
            String query3 = "SELECT exercise_name FROM exerciseinfo";
            PreparedStatement preparedStatement3 = con.prepareStatement(query3);
            ResultSet rs = preparedStatement3.executeQuery();
            
            while(rs.next()) {
                String name = rs.getString(("exercise_name"));
                names.add(name);
            }
            
            return names;
            
        } catch (Exception e){
            System.out.println("Error " + e.getMessage());
        }
        return null;
    }
    
    private ArrayList<String> fetchintensityitems(){
        try{
            ArrayList<String> intensity = new ArrayList<>();
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:MySQL://localhost:3306/fit";
            String user = "root";
            String pass = "";
            Connection con = DriverManager.getConnection(url,user,pass);
            
            String query3 = "SELECT intensity_level FROM exerciseinfo";
            PreparedStatement preparedStatement3 = con.prepareStatement(query3);
            ResultSet rs = preparedStatement3.executeQuery();
            
            while(rs.next()) {
                String name = rs.getString(("intensity_level"));
                intensity.add(name);
            }
            
            return intensity;
            
        } catch (Exception e){
            System.out.println("Error " + e.getMessage());
        }
        return null;
    }
    
    private ArrayList<String> fetchmetitems(){
        try{
            ArrayList<String> met = new ArrayList<>();
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:MySQL://localhost:3306/fit";
            String user = "root";
            String pass = "";
            Connection con = DriverManager.getConnection(url,user,pass);
            
            String query3 = "SELECT met FROM exerciseinfo";
            PreparedStatement preparedStatement3 = con.prepareStatement(query3);
            ResultSet rs = preparedStatement3.executeQuery();
            
            while(rs.next()) {
                String name = rs.getString(("met"));
                met.add(name);
            }
            
            return met;
            
        } catch (Exception e){
            System.out.println("Error " + e.getMessage());
        }
        return null;
    }
    
    private ArrayList<String> fetchdurationitems(){
        try{
            ArrayList<String> dur = new ArrayList<>();
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:MySQL://localhost:3306/fit";
            String user = "root";
            String pass = "";
            Connection con = DriverManager.getConnection(url,user,pass);
            
            String query3 = "SELECT corresponding_duration_minutes FROM exerciseinfo";
            PreparedStatement preparedStatement3 = con.prepareStatement(query3);
            ResultSet rs = preparedStatement3.executeQuery();
            
            while(rs.next()) {
                String name = rs.getString(("corresponding_duration_minutes"));
                dur.add(name);
            }
            
            return dur;
            
        } catch (Exception e){
            System.out.println("Error " + e.getMessage());
        }
        return null;
    }
    
    private void saveExerciseItem(String selectedFood, String selectedIntensity, String selectedMet, String selectedDuration) {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/fit";
        String user = "root";
        String pass = "";
        Connection con = DriverManager.getConnection(url, user, pass);
        int uID = getuserID();

        String query = "INSERT INTO exercise (user_id, exercise_name, intensity_level, met, duration_minutes, exercise_date) VALUES (?, ?, ?, ?, ?, CURDATE())";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, uID);
        pstmt.setString(2, selectedFood);
        pstmt.setString(3, selectedIntensity);
        pstmt.setString(4, selectedMet);
        pstmt.setString(5, selectedDuration);

        int rowsInserted = pstmt.executeUpdate();
        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(null, "Action Successful");
        }

        con.close();
    } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "An error occurred while saving exercise item. Please check logs for details.");
    }
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jToggleButton6 = new javax.swing.JToggleButton();
        nameComboBox = new javax.swing.JComboBox<>();
        intensityComboBox = new javax.swing.JComboBox<>();
        metComboBox = new javax.swing.JComboBox<>();
        durationComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        enterExercise = new javax.swing.JToggleButton();
        jToggleButton9 = new javax.swing.JToggleButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        jPanel2.setBackground(new java.awt.Color(0, 51, 51));

        jPanel1.setBackground(new java.awt.Color(0, 255, 204));

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
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"Calorie intake", "Calorie expenditure"}));
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addGap(44, 44, 44)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jToggleButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1)
                    .addComponent(jToggleButton3)
                    .addComponent(jToggleButton5)
                    .addComponent(jToggleButton4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton6))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        nameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameComboBoxActionPerformed(evt);
            }
        });

        intensityComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[0]));

        metComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[0]));

        durationComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[0]));
        durationComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                durationComboBoxActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Exercises");

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Enter details");

        enterExercise.setBackground(new java.awt.Color(51, 153, 255));
        enterExercise.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        enterExercise.setForeground(new java.awt.Color(255, 255, 255));
        enterExercise.setText("Enter exercise details");
        enterExercise.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        enterExercise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterExerciseActionPerformed(evt);
            }
        });

        jToggleButton9.setBackground(new java.awt.Color(51, 153, 255));
        jToggleButton9.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jToggleButton9.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton9.setText("View exercise history");
        jToggleButton9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToggleButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton9ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Name");

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Intensity");

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Duration");

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("MET");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(nameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(intensityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(metComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(durationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(212, 212, 212))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(enterExercise, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(360, 360, 360)
                .addComponent(jToggleButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(387, 387, 387)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(durationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(metComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(intensityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56)
                .addComponent(enterExercise)
                .addGap(38, 38, 38)
                .addComponent(jToggleButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(131, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        recPlan homeFrame = new recPlan(getuserID());
        homeFrame.setVisible(true);
        homeFrame.pack();
        homeFrame.setLocationRelativeTo(null); //center
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

    private void nameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameComboBoxActionPerformed

    private void durationComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_durationComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_durationComboBoxActionPerformed

    private void enterExerciseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterExerciseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_enterExerciseActionPerformed

    private void jToggleButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton9ActionPerformed
        // TODO add your handling code here:
        close();
        exercise_history ehistoryFrame = new exercise_history(getuserID());
        ehistoryFrame.setVisible(true);
        ehistoryFrame.pack();
        ehistoryFrame.setLocationRelativeTo(null); //center
    }//GEN-LAST:event_jToggleButton9ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed

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
            java.util.logging.Logger.getLogger(exercises.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(exercises.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(exercises.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(exercises.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new exercises(getuserID()).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> durationComboBox;
    private javax.swing.JToggleButton enterExercise;
    private javax.swing.JComboBox<String> intensityComboBox;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton9;
    private javax.swing.JComboBox<String> metComboBox;
    private javax.swing.JComboBox<String> nameComboBox;
    // End of variables declaration//GEN-END:variables
}
