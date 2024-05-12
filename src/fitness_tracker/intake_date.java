/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package fitness_tracker;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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
public class intake_date extends javax.swing.JFrame {

    /**
     * Creates new form intake_date
     */
    private static int uid;
    public intake_date(int id) {
        initComponents();
        this.uid = id;
        populateDates();
        dateCombo.addActionListener(e -> {
            fetchCalories();
            fetchCaloriesBurn();
            fetchCaloriesNet();
            fetchCaloriesGoal();
                });
    }

    private void populateDates() {
        ArrayList<String> dateList = fetchDates();
        if (dateList != null) {
            for (String date : dateList) {
                dateCombo.addItem(date);
            }
        } else {
            System.out.println("No dates fetched from the database.");
        }
    }
    
    private ArrayList<String> fetchDates() {
        ArrayList<String> dateList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:MySQL://localhost:3306/fit", "root", "")) {
            String query = "SELECT DISTINCT DATE(intake_date) AS intake_date FROM calorieintake WHERE user_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, getuserID());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String date = resultSet.getString("intake_date");
                        dateList.add(date);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return dateList;
    }
    
    private void fetchCalories() {
        String selectedDate = (String) dateCombo.getSelectedItem();
        double total = 0;
        if (selectedDate != null) {
            try (Connection connection = DriverManager.getConnection("jdbc:MySQL://localhost:3306/fit", "root", "")) {
                String sql = "SELECT SUM(total_calories_consumed) AS total_calories FROM calorieintake WHERE user_id = ? AND DATE(intake_date) = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, getuserID());
                    statement.setString(2, selectedDate);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            int totalCalories = (int) resultSet.getDouble("total_calories");
                            totalCalcon.setText(String.valueOf(totalCalories));
                        } else {
                            totalCalcon.setText("No entries");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void fetchCaloriesBurn() {
        String selectedDate = (String) dateCombo.getSelectedItem();
        double total = 0;
        if (selectedDate != null) {
            try (Connection connection = DriverManager.getConnection("jdbc:MySQL://localhost:3306/fit", "root", "")) {
                String sql = "SELECT SUM(calories_burned) AS total_calories FROM exercise WHERE user_id = ? AND DATE(exercise_date) = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, getuserID());
                    statement.setString(2, selectedDate);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            int totalCalories = (int) resultSet.getDouble("total_calories");
                            totalCalburn.setText(String.valueOf(totalCalories));
                        } else {
                            totalCalburn.setText("No entries");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void fetchCaloriesNet() {
    String selectedDate = (String) dateCombo.getSelectedItem();
    int totalCaloriesConsumed = 0;
    int totalCaloriesBurned = 0;
    if (selectedDate != null) {
        try (Connection connection = DriverManager.getConnection("jdbc:MySQL://localhost:3306/fit", "root", "")) {
            // Query to fetch total calories consumed
            String queryConsumed = "SELECT SUM(total_calories_consumed) AS total_calories FROM calorieintake WHERE user_id = ? AND DATE(intake_date) = ?";
            try (PreparedStatement statementConsumed = connection.prepareStatement(queryConsumed)) {
                // Set user_id and date parameters
                int userId = getuserID();
                statementConsumed.setInt(1, userId);
                statementConsumed.setString(2, selectedDate);

                // Execute query to fetch total calories consumed
                //double totalCaloriesConsumed = 0;
                try (ResultSet resultSetConsumed = statementConsumed.executeQuery()) {
                    if (resultSetConsumed.next()) {
                        totalCaloriesConsumed = (int) resultSetConsumed.getDouble("total_calories");
                    }
                }

                // Query to fetch total calories burned
                String queryBurned = "SELECT SUM(calories_burned) AS total_calories FROM exercise WHERE user_id = ? AND DATE(exercise_date) = ?";
                try (PreparedStatement statementBurned = connection.prepareStatement(queryBurned)) {
                    // Set user_id and date parameters
                    statementBurned.setInt(1, userId);
                    statementBurned.setString(2, selectedDate);

                    // Execute query to fetch total calories burned
                    //double totalCaloriesBurned = 0;
                    try (ResultSet resultSetBurned = statementBurned.executeQuery()) {
                        if (resultSetBurned.next()) {
                            totalCaloriesBurned = (int) resultSetBurned.getDouble("total_calories");
                        }
                    }
                }
            }
            int netCalories = (int) (totalCaloriesConsumed - totalCaloriesBurned);
            netCal.setText(String.valueOf(netCalories));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
    
    private void fetchCaloriesGoal() {
    String selectedDate = (String) dateCombo.getSelectedItem();
    if (selectedDate != null) {
        try (Connection connection = DriverManager.getConnection("jdbc:MySQL://localhost:3306/fit", "root", "")) {
            // Query to fetch total calories consumed
            String queryConsumed = "SELECT SUM(total_calories_consumed) AS total_calories FROM calorieintake WHERE user_id = ?";
            try (PreparedStatement statementConsumed = connection.prepareStatement(queryConsumed)) {
                // Set user_id parameter
                int userId = getuserID();
                statementConsumed.setInt(1, userId);

                // Execute query to fetch total calories consumed
                double totalCaloriesConsumed = 0;
                try (ResultSet resultSetConsumed = statementConsumed.executeQuery()) {
                    if (resultSetConsumed.next()) {
                        totalCaloriesConsumed = resultSetConsumed.getDouble("total_calories");
                    }
                }

                // Query to fetch total calories burned
                String queryBurned = "SELECT SUM(calories_burned) AS total_calories FROM exercise WHERE user_id = ?";
                try (PreparedStatement statementBurned = connection.prepareStatement(queryBurned)) {
                    // Set user_id parameter
                    statementBurned.setInt(1, userId);

                    // Execute query to fetch total calories burned
                    double totalCaloriesBurned = 0;
                    try (ResultSet resultSetBurned = statementBurned.executeQuery()) {
                        if (resultSetBurned.next()) {
                            totalCaloriesBurned = resultSetBurned.getDouble("total_calories");
                        }
                    }

                    // Calculate net calories
                    double netCalories = totalCaloriesConsumed - totalCaloriesBurned;

                    // Query to fetch total calories goal
                    String queryGoal = "SELECT estcalories FROM weightlossgoals WHERE user_id = ?";
                    try (PreparedStatement statementGoal = connection.prepareStatement(queryGoal)) {
                        // Set user_id parameter
                        statementGoal.setInt(1, userId);

                        // Execute query to fetch total calories goal
                        double totalCaloriesGoal = 0;
                        try (ResultSet resultSetGoal = statementGoal.executeQuery()) {
                            if (resultSetGoal.next()) {
                                totalCaloriesGoal = resultSetGoal.getDouble("estcalories");
                            }
                        }

                        // Calculate goal calories
                        int goalCal = (int) (totalCaloriesGoal - (-(netCalories)));
                        leftCal.setText(String.valueOf(goalCal));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    
    public void close(){
        WindowEvent closeWindow = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeWindow);
    }
    
    public static int getuserID(){
        return uid;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        dateCombo = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        totalCalcon = new javax.swing.JTextField();
        totalCalburn = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        leftCal = new javax.swing.JTextField();
        netCal = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jToggleButton2 = new javax.swing.JToggleButton();

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("View Calorie Intake History");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(0, 51, 51));

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        dateCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[0]));
        dateCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateComboActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Select date");

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Total Calories consumed ");

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Total Calories burned ");

        totalCalcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalCalconActionPerformed(evt);
            }
        });

        totalCalburn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalCalburnActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Net Calories gained/Lost");

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Calories left to complete goal");

        netCal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netCalActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Daily Net Calorie Summary");

        jToggleButton2.setBackground(new java.awt.Color(255, 51, 51));
        jToggleButton2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jToggleButton2.setText("Exit");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(304, 304, 304)
                .addComponent(jLabel6)
                .addContainerGap(275, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jToggleButton2)
                .addGap(399, 399, 399))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(125, 125, 125)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel11)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel8)
                                .addComponent(jLabel7))
                            .addGap(50, 50, 50)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(totalCalcon, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(totalCalburn, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(128, 128, 128)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel10)
                                .addComponent(jLabel9))
                            .addGap(50, 50, 50)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(netCal, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(leftCal, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(43, 43, 43)
                            .addComponent(dateCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(79, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
                .addComponent(jToggleButton2)
                .addGap(34, 34, 34))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(93, 93, 93)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dateCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(netCal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(leftCal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(totalCalcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(totalCalburn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGap(94, 94, 94)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dateComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateComboActionPerformed

    private void totalCalconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalCalconActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_totalCalconActionPerformed

    private void totalCalburnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalCalburnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalCalburnActionPerformed

    private void netCalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netCalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_netCalActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed

        close();
        intake_history iFrame = new intake_history(getuserID());
        iFrame.setVisible(true);
        iFrame.pack();
        iFrame.setLocationRelativeTo(null); //center
    }//GEN-LAST:event_jToggleButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(intake_date.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(intake_date.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(intake_date.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(intake_date.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new intake_date(getuserID()).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> dateCombo;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JTextField leftCal;
    private javax.swing.JTextField netCal;
    private javax.swing.JTextField totalCalburn;
    private javax.swing.JTextField totalCalcon;
    // End of variables declaration//GEN-END:variables
}
