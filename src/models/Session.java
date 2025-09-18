package models;

public class Session {
    private static Employee currentUser;

    public static void setCurrentUser(Employee employee) {
        currentUser = employee;
    }

    public static Employee getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
