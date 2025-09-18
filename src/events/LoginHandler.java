package events;

import components.LoginForm;
import components.MainAppFrame;
import models.Employee;
import models.Session;
import services.AuthService;

import javax.swing.*;
import java.sql.SQLException;

public class LoginHandler {

    private LoginForm loginForm;

    public LoginHandler(LoginForm form) {
        this.loginForm = form;
        attachListener();
    }

    private void attachListener() {
        loginForm.getLoginButton().addActionListener(_ -> handleLogin());
    }

    private void handleLogin() {
        String username = loginForm.getUsernameField().getText();
        String password = new String(loginForm.getPasswordField().getPassword());

        try {
            Employee employee = AuthService.login(username, password);
            if (employee != null) {
                Session.setCurrentUser(employee);
                JOptionPane.showMessageDialog(loginForm, "Welcome, " + employee.getFullName());
                
                // Open main app and close login
                new MainAppFrame().setVisible(true);
                loginForm.dispose();
            } else {
                loginForm.getStatusLabel().setText("Invalid username or password");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            loginForm.getStatusLabel().setText("Database error");
        }
    }
}
