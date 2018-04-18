package com.jainil.calendar;

import android.content.Context;
import android.test.ActivityUnitTestCase;

import com.chirag.todo.Main.MainActivity;
import com.chirag.todo.Utility.StoreRetrieveData;
import com.chirag.todo.Utility.ToDoItem;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;

/**
 * Test cases for StoreRetrieveData class
 */
public class TestStoreRetrieveData extends ActivityUnitTestCase<MainActivity> {

    private MainActivity mMainActivity;
    private ArrayList<ToDoItem> mOriginalData;
    ArrayList<ToDoItem> mTestData;

    public TestStoreRetrieveData() {
        super(MainActivity.class);

        // Create some test data
        mTestData = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            mTestData.add(new ToDoItem(
                    "item" + i,
                    false,
                    new Date()));
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
        mOriginalData = new ArrayList<>();

        // Save the original data and wipe out the storage
        StoreRetrieveData dataStorage = getDataStorage();
        try {
            ArrayList<ToDoItem> items = dataStorage.loadFromFile();

            if (items.size() > 0) {
                mOriginalData.clear();
                mOriginalData.addAll(items);

                items.clear();
                dataStorage.saveToFile(items);
            }

        } catch (Exception e) {
            fail("Couldn't store data: " + e.getMessage());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // Let's restore the data we might have wiped out during setUp()...
        StoreRetrieveData dataStorage = getDataStorage();
        dataStorage.saveToFile(mOriginalData);
    }

    /**
     * We should have an empty data storage at hand for the starters
     */
    public void testPreconditions() {
        StoreRetrieveData dataStorage = getDataStorage();

        ArrayList<ToDoItem> items = null;
        try {
            items = dataStorage.loadFromFile();
        } catch (Exception e) {
            fail("Couldn't read from data storage: " + e.getMessage());
        }

        assertEquals(0, items.size());
    }

    /**
     * Write items to data storage and ensure those same items can be retrieved from the storage.
     */
    public void testWritingToAndReadingFromTheDataStorage() {
        StoreRetrieveData dataStorage = getDataStorage();
        ArrayList<ToDoItem> retrievedItems = new ArrayList<>();

        // Persist the test data
        try {
            dataStorage.saveToFile(mTestData);
        } catch (Exception e) {
            fail("Couldn't store data: " + e.getMessage());
        }

        // Read from storage
        try {
            retrievedItems = dataStorage.loadFromFile();
        } catch (Exception e) {
            fail("Couldn't read from data storage: " + e.getMessage());
        }

        // We should have equal amount of items than what we just stored
        assertEquals(mTestData.size(), retrievedItems.size());

        // The content should be same as well...
        for (ToDoItem retrievedItem : retrievedItems) {
            // We want to be sure every single item in data storage can also be found from
            // our test data collection
            boolean found = false;
            for (ToDoItem testItem : mTestData) {

                // Check the items are same
                if (retrievedItem.getIdentifier().equals(testItem.getIdentifier()) &&
                        retrievedItem.getToDoText().equals(testItem.getToDoText()) &&
                        retrievedItem.hasReminder() == testItem.hasReminder() &&
                        retrievedItem.getToDoDate().equals(testItem.getToDoDate())) {

                    found = true;
                    break;
                }
            }

            if (!found) {
                fail("Content mis-match between test data and data retrieved from the storage!");
            }
        }
    }

    /**
     * Ensure JSONArray conversion works as intended
     */
    public void testArrayListToJsonArrayConversion() {
        try {
            JSONArray array = StoreRetrieveData.toJSONArray(mTestData);
            assertEquals(mTestData.size(), array.length());
        } catch (Exception e) {
            fail("Exception thrown when converting to JSONArray: " + e.getMessage());
        }
    }

    private StoreRetrieveData getDataStorage() {
        Context context = getInstrumentation().getTargetContext();
        return new StoreRetrieveData(context, MainActivity.FILENAME);
    }
}
