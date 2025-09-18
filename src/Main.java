// import javax.swing.*;

// import components.MainAppFrame;

// public class Main {
//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> {
//             MainAppFrame frame = new MainAppFrame();
//             frame.setVisible(true);
//             frame.showCard("POS"); // Start on POS screen
//         });
//     }
// }


import javax.swing.*;
import components.LoginForm;
import events.LoginHandler;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            new LoginHandler(loginForm); // attach login logic
        });
    }
}

