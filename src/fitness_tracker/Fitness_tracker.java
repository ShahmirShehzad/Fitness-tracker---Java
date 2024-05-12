/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package fitness_tracker;

/**
 *
 * @author shehz
 */
public class Fitness_tracker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        login adminLoginFrame = new login();
        adminLoginFrame.setVisible(true);
        adminLoginFrame.pack();
        adminLoginFrame.setLocationRelativeTo(null); //center
    }
    
}
