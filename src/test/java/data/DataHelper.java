package data;

import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;


public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfoFirstUser() {
        return new AuthInfo("vanya", "qwerty123");
    }

    public static AuthInfo getAuthInfoSecondUser() {
        return new AuthInfo("sasha", "123qwerty");
    }

    public static AuthInfo getAuthInfoWrongPassword() {
        return new AuthInfo("sasha", "321123");
    }

    public static AuthInfo getAuthInfoWrongLogin() {
        return new AuthInfo("kolya", "123qwerty");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getVerificationCodeFor() {
        val verificationCodeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1;";
        String verificationCode;
        val runner = new QueryRunner();

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            val code = runner.query(conn, verificationCodeSQL, new ScalarHandler<>());
            verificationCode = (String) code;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new VerificationCode(verificationCode);
    }

    public static VerificationCode getWrongCode() {
        return new VerificationCode("111111");

    }

    public static void clearDB() {

        val deleteAuthCodes = "DELETE FROM auth_codes;";
        val deleteCardTransactions = "DELETE FROM card_transactions;";
        val deleteCards = "DELETE FROM cards;";
        val deleteUsers = "DELETE FROM users;";
        val runner = new QueryRunner();

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            runner.update(conn, deleteAuthCodes);
            runner.update(conn, deleteCardTransactions);
            runner.update(conn, deleteCards);
            runner.update(conn, deleteUsers);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
