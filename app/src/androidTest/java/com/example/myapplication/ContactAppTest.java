package com.example.myapplication;

import android.content.Intent;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ContactAppTest {
    private void capture(String name) {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        android.os.SystemClock.sleep(350);
        android.graphics.Bitmap image = InstrumentationRegistry.getInstrumentation().getUiAutomation().takeScreenshot();
        assertNotNull(image);
        java.io.File directory = new java.io.File(InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getExternalFilesDir(null), "screenshots");
        assertTrue(directory.isDirectory() || directory.mkdirs());
        try (java.io.FileOutputStream stream = new java.io.FileOutputStream(new java.io.File(directory, name + ".png"))) {
            assertTrue(image.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream));
        } catch (java.io.IOException exception) {
            throw new AssertionError(exception);
        } finally {
            image.recycle();
        }
    }

    @Before public void launchLoginWithFreshContacts() {
        android.app.Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.runOnMainSync(() -> {
            ContactRepository repository = ContactRepository.getInstance();
            for (Contact contact : repository.getAll()) repository.delete(contact.getId());
            repository.add("Nguyễn Văn An", "0901234567", "Bạn bè", "Nam", false, "");
            repository.add("Trần Thị Bình", "0912345678", "Gia đình", "Nữ", true, "Chị gái, hay gọi buổi tối");
            repository.add("Lê Minh Châu", "0987654321", "Công việc", "Khác", false, "");
        });
        Intent intent = new Intent(instrumentation.getTargetContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        instrumentation.startActivitySync(intent);
    }

    private void login() {
        onView(withId(R.id.username)).perform(replaceText("admin"));
        onView(withId(R.id.password)).perform(replaceText("123456"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(scrollTo(), click());
        onView(withId(R.id.total_count)).check(matches(withText("3")));
        onView(withId(R.id.favorite_count)).check(matches(withText("1")));
    }

    @Test public void emptyAndWrongCredentialsAreRejected() {
        capture("01-login");
        onView(withId(R.id.login_button)).perform(scrollTo(), click());
        onView(withId(R.id.username)).check(matches(hasErrorText("Vui lòng nhập tên đăng nhập")));
        onView(withId(R.id.password)).check(matches(hasErrorText("Vui lòng nhập mật khẩu")));
        onView(withId(R.id.username)).perform(replaceText("admin"));
        onView(withId(R.id.password)).perform(replaceText("wrong"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(scrollTo(), click());
        onView(withId(R.id.login_error)).check(matches(isDisplayed()));
        capture("02-login-error");
        login();
    }

    @Test public void addValidatesInputAndDisplaysSavedContact() {
        login();
        capture("03-contact-list");
        onView(withId(R.id.add_contact_button)).perform(click());
        capture("04-add-contact");
        onView(withId(R.id.save_button)).perform(scrollTo(), click());
        onView(withId(R.id.full_name)).check(matches(hasErrorText("Vui lòng nhập họ và tên")));
        onView(withId(R.id.full_name)).perform(scrollTo(), replaceText("Test Contact"));
        onView(withId(R.id.phone)).perform(scrollTo(), replaceText("123"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(scrollTo(), click());
        onView(withId(R.id.phone)).check(matches(hasErrorText("Số điện thoại phải có 10 chữ số và bắt đầu bằng 0")));
        onView(withId(R.id.phone)).perform(scrollTo(), replaceText("0901234567"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(scrollTo(), click());
        onView(withId(R.id.phone)).check(matches(hasErrorText("Số điện thoại đã tồn tại trong danh bạ")));
        capture("05-duplicate-error");
        onView(withId(R.id.phone)).perform(scrollTo(), replaceText("0900000001"), closeSoftKeyboard());
        onView(withId(R.id.favorite)).perform(scrollTo(), click());
        onView(withId(R.id.save_button)).perform(scrollTo(), click());
        onView(withId(R.id.total_count)).check(matches(withText("4")));
        onView(withId(R.id.favorite_count)).check(matches(withText("2")));
        capture("06-contact-added");
        onData(org.hamcrest.Matchers.instanceOf(Contact.class)).inAdapterView(withId(R.id.contact_list)).atPosition(3).perform(click());
        onView(withId(R.id.detail_name)).check(matches(withText("Test Contact")));
        onView(withId(R.id.detail_phone)).check(matches(withText("0900000001")));
        onView(withId(R.id.detail_gender)).check(matches(withText("Nam")));
        onView(withId(R.id.detail_favorite)).check(matches(isChecked()));
        capture("07-contact-detail");
    }

    @Test public void favoriteAndDeleteUpdateMainScreen() {
        login();
        onView(withText("Trần Thị Bình")).perform(click());
        onView(withId(R.id.detail_favorite)).perform(scrollTo(), click());
        onView(withId(R.id.return_button)).perform(scrollTo(), click());
        onView(withId(R.id.favorite_count)).check(matches(withText("0")));
        onView(withText("Trần Thị Bình")).perform(click());
        onView(withId(R.id.detail_favorite)).perform(scrollTo(), click());
        onView(withId(R.id.delete_button)).perform(scrollTo(), click());
        onView(withText("Hủy")).perform(click());
        onView(withId(R.id.detail_name)).check(matches(withText("Trần Thị Bình")));
        onView(withId(R.id.delete_button)).perform(scrollTo(), click());
        onView(allOf(withId(android.R.id.button1), withText("Xóa"))).perform(click());
        onView(withId(R.id.total_count)).check(matches(withText("2")));
        onView(withId(R.id.favorite_count)).check(matches(withText("0")));
    }

    @Test public void cancelAddAndDeleteAllContacts() {
        login();
        onView(withId(R.id.add_contact_button)).perform(click());
        onView(withId(R.id.full_name)).perform(replaceText("Not saved"), closeSoftKeyboard());
        onView(withId(R.id.back_button)).perform(click());
        onView(withId(R.id.total_count)).check(matches(withText("3")));
        for (int i = 0; i < 3; i++) {
            onData(org.hamcrest.Matchers.instanceOf(Contact.class)).inAdapterView(withId(R.id.contact_list)).atPosition(0).perform(click());
            onView(withId(R.id.delete_button)).perform(scrollTo(), click());
            onView(withId(android.R.id.button1)).perform(click());
        }
        onView(withId(R.id.total_count)).check(matches(withText("0")));
        onView(withId(R.id.favorite_count)).check(matches(withText("0")));
        onView(withId(R.id.empty_state)).check(matches(isDisplayed()));
        capture("09-empty-list");
    }

    @Test public void dialOpensPhoneWithCorrectNumber() {
        login();
        onView(withText("Trần Thị Bình")).perform(click());
        capture("08-sample-detail");
        onView(withId(R.id.dial_button)).perform(scrollTo(), click());
        android.app.UiAutomation automation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
        boolean found = false;
        for (int attempt = 0; attempt < 20 && !found; attempt++) {
            android.os.SystemClock.sleep(250);
            android.view.accessibility.AccessibilityNodeInfo root = automation.getRootInActiveWindow();
            found = root != null && !"com.example.myapplication".contentEquals(root.getPackageName())
                    && hasPhone(root, "0912345678");
        }
        assertTrue("Dialer should display the selected contact phone", found);
        capture("10-dialer");
        automation.performGlobalAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK);
    }

    private boolean hasPhone(android.view.accessibility.AccessibilityNodeInfo node, String phone) {
        CharSequence text = node.getText();
        if (text != null && text.toString().replaceAll("[^0-9]", "").contains(phone)) return true;
        for (int i = 0; i < node.getChildCount(); i++) {
            android.view.accessibility.AccessibilityNodeInfo child = node.getChild(i);
            if (child != null && hasPhone(child, phone)) return true;
        }
        return false;
    }
}
