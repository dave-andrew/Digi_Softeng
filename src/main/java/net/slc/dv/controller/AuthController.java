package net.slc.dv.controller;

import java.net.InetAddress;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import net.slc.dv.database.AuthQuery;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.model.User;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;

public class AuthController {

    private final AuthQuery authQuery;

    public AuthController() {
        this.authQuery = new AuthQuery();
    }

    public String checkRegister(String username, String email, String pass, String confirmPass, String dob) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (username.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            return TextStorage.getText(Text.PLEASE_FILL_ALL_THE_FIELDS);
        }

        if (!pass.equals(confirmPass)) {
            return TextStorage.getText(Text.NEW_PASSWORD_AND_CONFIRM_PASSWORD_MUST_BE_SAME);
        }

        if (pass.length() < 8) {
            return TextStorage.getText(Text.PASSWORD_MUST_BE_GREATER_THAN_8_CHARACTERS);
        }

        if (!email.contains("@") && !email.contains(".")) {
            return TextStorage.getText(Text.INVALID_EMAIL_FORMAT);
        }

        LocalDate dateOfBirth;

        try {
            dateOfBirth = LocalDate.parse(dob, formatter);
        } catch (Exception e) {
            return TextStorage.getText(Text.INVALID_DATE_FORMAT);
        }

        int age = LocalDate.now().getYear() - dateOfBirth.getYear();

        if (age < 17) {
            return TextStorage.getText(Text.AGE_MUST_BE_GREATER_THAN_17);
        }

        User user = new User(username, email, pass, dob);
        try {
            authQuery.register(user);
        } catch (SQLException e) {
            return TextStorage.getText(Text.EMAIL_ALREADY_EXISTS);
        }
        return TextStorage.getText(Text.REGISTER_SUCCESS);
    }

    public String checkLogin(String email, String pass, boolean remember) {
        if (email.isEmpty() || pass.isEmpty()) {
            return TextStorage.getText(Text.PLEASE_FILL_ALL_THE_FIELDS);
        }

        User user = authQuery.login(email, pass);

        if (user == null) {
            return TextStorage.getText(Text.EMAIL_OR_PASSWORD_IS_INCORRECT);
        }

        if (remember) {
            authQuery.rememberMe(user);
        }

        LoggedUser.initialize(user);
        return TextStorage.getText(Text.LOGIN_SUCCESS);
    }

    public String checkAuth() {
        return authQuery.checkAuth();
    }

    public void removeAuth() {
        String computerName;
        try {
            computerName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            computerName = System.getenv("COMPUTERNAME");
        }

        authQuery.deleteAuthData(computerName);
    }
}
