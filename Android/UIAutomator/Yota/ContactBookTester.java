package com.example.contactsbook;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

/* ********************************************************************************************** */
/* DISCLAIMER: there is no POM usage here because of leak of time, it's a just a demo of          */
/* UIAutomator testing for Yota assessment, and looks a little bit ugly, and due to that          */
/* I apologize about it.                                                                          */
/* JUnit is not used here, because in assessment was not described that is required               */
/* It's better to split POM files, test driver and each test into separate files
/* ********************************************************************************************** */

public class ContactBookTester extends InstrumentationTestCase {
    private UiDevice device;
    @Override
    public void setUp() throws Exception {
        super.setUp();
        // init device instrumentation
        device = UiDevice.getInstance(getInstrumentation());
        // Simulate a short press on the HOME button.
        device.pressHome();
        // to make tests stable, add implicit wait
        device.waitForIdle(10000);
        // open all apps menu, we need to swipe screen up in Oreo and laters,
        // because we have no Apps button anymore
        UiObject2 allAppsButton = device.findObject(By.clazz("android.view.ViewGroup"));
        allAppsButton.swipe(Direction.UP,1.0f);
        // wait for apps menu displayed, then open our application
        device.wait(Until.hasObject(By.text("ContactsBook")), 3000);
        UiObject2 contactsBookButton = device.findObject(By.text("ContactsBook"));
        contactsBookButton.clickAndWait(Until.newWindow(),3000);
        // there is no protection of not-visible case (many apps are installed, but it's just a demo
    }

    public void test_01_Fill() throws Exception {
        // if something will not be found, test will crash
        // firstly, check that we have items on our form after login
        // we don't care that here we can get from navigation menu using INPUT option, just
        // checking that after application startup filling data is working
        device.wait(Until.hasObject(By.text("Name")), 3000);
        device.wait(Until.hasObject(By.text("Phone")), 3000);
        device.wait(Until.hasObject(By.text("SAVE")), 3000);
        // fill data afterwards
        UiObject2 nameField = device.findObject(By.text("Name"));
        UiObject2 phoneField = device.findObject(By.text("Phone"));
        UiObject2 saveButton = device.findObject(By.text("SAVE"));
        // I'm sorry for hardcode, I know, that's better to use dictionaries, params or test data
        // generators. Anyway, we just filling data and saving contact
        nameField.setText("aaabbbcc");
        phoneField.setText("+71234567890");
        saveButton.click();
        // after button click we'll get notification "Contact saved". It's better to check it here,
        // but in our case, skip this check and check it via previous steps and no exceptions
        // NOTE: we doesn't check dupes and overwriting of contacts, it should be added to
        // test model. Current behaviour - is that item will be added once
    }

    public void test_02_Addition() throws Exception {
        // We'll check that data in previous test case is stored properly
        // Actually it's a bad style
        // navigate to data window
        device.wait(Until.hasObject(By.clazz
                ("android.widget.ImageButton")), 3000);
        UiObject2 menuButton = device.findObject(By.clazz
                ("android.widget.ImageButton"));
        menuButton.clickAndWait(Until.newWindow(),3000);
        device.wait(Until.hasObject(By.clazz
                ("android.widget.ImageButton")), 3000);
        UiObject2 dataButton = device.findObject(By.text("Data"));
        dataButton.clickAndWait(Until.newWindow(),3000);
        // ok, we're probably at data window, let's check it
        device.wait(Until.hasObject(By.clazz("android.widget.TextView")), 3000);
        device.wait(Until.hasObject(By.res
                ("com.example.contactsbook:id/contactName")), 3000);
        device.wait(Until.hasObject(By.res
                ("com.example.contactsbook:id/contactPhone")), 3000);
        // then we need a gather data and compare to our expectations
        // we're using only one data item, therefore we're not checking complex cases
        // of course, we need to extend our testing model with multiple data items test
        // but it this assessment demo we'll skip it. In good tests we need to create E2E sequences
        // with data preparation prequisites and teardown of tests (remove test data)
        UiObject2 contactName = device.findObject(By.res
                ("com.example.contactsbook:id/contactName"));
        UiObject2 contactPhone = device.findObject(By.res
                ("com.example.contactsbook:id/contactPhone"));
        // check it via assert. We'll use hardcode, but it's better to parametrize it
        assertEquals("aaabbbcc", contactName.getText());
        assertEquals("+71234567890", contactPhone.getText());
        // double-check in one test case is not recommended, but we'll use it here - it's a very
        // simple test
    }

    public void test_03_Delete() throws Exception {
        // We'll check that data can be deleted
        // navigate to data window
        device.wait(Until.hasObject(By.clazz
                ("android.widget.ImageButton")), 3000);
        UiObject2 menuButton = device.findObject(By.clazz
                ("android.widget.ImageButton"));
        menuButton.clickAndWait(Until.newWindow(),3000);
        device.wait(Until.hasObject(By.clazz
                ("android.widget.ImageButton")), 3000);
        UiObject2 dataButton = device.findObject(By.text("Data"));
        dataButton.clickAndWait(Until.newWindow(),3000);
        // ok, we're probably at data window, let's check it
        device.wait(Until.hasObject(By.clazz("android.widget.TextView")), 3000);
        device.wait(Until.hasObject(By.res
                ("com.example.contactsbook:id/contactName")), 3000);
        device.wait(Until.hasObject(By.res
                ("com.example.contactsbook:id/contactPhone")), 3000);
        device.wait(Until.hasObject(By.res
                ("com.example.contactsbook:id/fab_remove_all")), 3000);
        UiObject2 removeAll = device.findObject(By.res
                ("com.example.contactsbook:id/fab_remove_all"));
        // click button and wait for new frame
        removeAll.click();
        device.wait(Until.hasObject(By.clazz("android.widget.FrameLayout")), 3000);
        UiObject2 remText = device.findObject(By.res("com.example.contactsbook:id/snackbar_text"));
        assertEquals("All contacts deleted", remText.getText());
        // we just checking that text is appearing, but it's better to add check that data were
        // removed from data grid also
    }

    public void test_04_Undo() throws Exception {
        // We'll check that data can be deleted
        // navigate to data window
        device.wait(Until.hasObject(By.clazz
                ("android.widget.ImageButton")), 3000);
        UiObject2 menuButton = device.findObject(By.clazz
                ("android.widget.ImageButton"));
        menuButton.clickAndWait(Until.newWindow(),3000);
        device.wait(Until.hasObject(By.clazz
                ("android.widget.ImageButton")), 3000);
        UiObject2 dataButton = device.findObject(By.text("Data"));
        dataButton.clickAndWait(Until.newWindow(),3000);
        // ok, we're probably at data window, let's check it
        device.wait(Until.hasObject(By.clazz("android.widget.TextView")), 3000);
        device.wait(Until.hasObject(By.res
                ("com.example.contactsbook:id/fab_remove_all")), 3000);
        UiObject2 removeAll = device.findObject(By.res
                ("com.example.contactsbook:id/fab_remove_all"));
        // click delete button and wait for new frame
        removeAll.click();
        device.wait(Until.hasObject(By.clazz("android.widget.FrameLayout")), 3000);
        // click undo button
        // make sure that we have undo
        device.wait(Until.hasObject(By.res
                ("com.example.contactsbook:id/snackbar_action")), 3000);
        UiObject2 undoButton = device.findObject(By.res("com.example.contactsbook:id/snackbar_action"));
        undoButton.click();
        // we'll use old data that we filled
        device.wait(Until.hasObject(By.clazz("android.widget.TextView")), 3000);
        device.wait(Until.hasObject(By.res
                ("com.example.contactsbook:id/contactName")), 3000);
        device.wait(Until.hasObject(By.res
                ("com.example.contactsbook:id/contactPhone")), 3000);
        // then we need a gather data and compare to our expectations
        // we're using only one data item, therefore we're not checking complex cases
        // of course, we need to extend our testing model with multiple data items test
        // but it this assessment demo we'll skip it. In good tests we need to create E2E sequences
        // with data preparation perquisites and teardown of tests (remove test data)
        UiObject2 contactName = device.findObject(By.res
                ("com.example.contactsbook:id/contactName"));
        UiObject2 contactPhone = device.findObject(By.res
                ("com.example.contactsbook:id/contactPhone"));
        // check it via assert. We'll use hardcode, but it's better to parametrize it
        assertEquals("aaabbbcc", contactName.getText());
        assertEquals("+71234567890", contactPhone.getText());
        // NOTE: there is the bug, if you try to delete empty list, app will crash
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        // go back to home screen
        device.pressHome();
        // to make tests stable, add implicit wait
        device.waitForIdle(10000);
    }
}