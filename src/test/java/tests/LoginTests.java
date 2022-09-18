package tests;

import data.DataHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.DashboardPage;
import page.LoginPage;
import lombok.val;
import page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

class LoginTest {
    @BeforeEach
    void login() {
        open("http://localhost:9999");
    }

    @AfterAll
    static void clearDB() {
        DataHelper.clearDB();
    }


    @Test
    void shouldEnterFirstRegisteredUser() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfoFirstUser();
        loginPage.validLogin(authInfo);
        var verificationPage = new VerificationPage();
        verificationPage.validVerify(DataHelper.getVerificationCodeFor());
        val dashboardPage = new DashboardPage();

    }

    @Test
    void shouldEnterSecondRegisteredUser() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfoSecondUser();
        loginPage.validLogin(authInfo);
        var verificationPage = new VerificationPage();
        verificationPage.validVerify(DataHelper.getVerificationCodeFor());
        val dashboardPage = new DashboardPage();
    }

    @Test
    void notShouldEnterWithWrongLogin() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfoWrongLogin();
        loginPage.validLogin(authInfo);
        loginPage.getLoginNotification();

    }

    @Test
    void notShouldEnterWithWrongPassword() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfoWrongPassword();
        loginPage.validLogin(authInfo);
        loginPage.getLoginNotification();

    }

    @Test
    void shouldBlockedPageAfterThreeWrongLogin() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfoFirstUser();
        loginPage.validLogin(authInfo);
        var verificationPage = new VerificationPage();
        verificationPage.validVerify(DataHelper.getWrongCode());
        login();
        loginPage.validLogin(authInfo);
        verificationPage.validVerify(DataHelper.getWrongCode());
        login();
        loginPage.validLogin(authInfo);
        verificationPage.validVerify(DataHelper.getWrongCode());
        login();
        loginPage.validLogin(authInfo);
        verificationPage.validVerify(DataHelper.getWrongCode());
        verificationPage.getBlockedVerificationNotification();
    }

}