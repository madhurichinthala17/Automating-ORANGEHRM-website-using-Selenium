package com.orangehrm.pageobjects;

import com.orangehrm.actiondrivers.Action;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class StartPage extends BaseClass {

    @FindBy(xpath="//input[@id='txtUsername']")
    WebElement username;

    @FindBy(xpath="//input[@id='txtPassword']")
    WebElement password;

    @FindBy(tagName="//button[contains(text(), 'Login')]")
    WebElement loginBtn;

    public StartPage()
    {
        PageFactory.initElements(driver,this);
    }

    public HomePage login() throws  Throwable
    {
        Action.type(username,prop.getProperty("username"));
        Action.type(password,prop.getProperty("password"));
        Action.click(driver,loginBtn);
        return new HomePage();
    }
}
