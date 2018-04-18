package com.jainil.calendar;

import com.chirag.todo.Utility.ToDoItem;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * JUnit tests to verify functionality of ToDoItem class.
 */
public class TestTodoItem extends TestCase {
    private final Date CURRENT_DATE = new Date();
    private final String TEXT_BODY = "This is some text";
    private final boolean REMINDER_OFF = false;
    private final boolean REMINDER_ON = true;

     /**
      * Check we can construct a ToDoItem object using the three parameter constructor
      */
    public void testThreeParameterConstructor() {
        ToDoItem toDoItem = getToDoItem(REMINDER_OFF);
        assertEquals(TEXT_BODY, toDoItem.getToDoText());
        assertEquals(REMINDER_OFF, toDoItem.hasReminder());
        assertEquals(CURRENT_DATE, toDoItem.getToDoDate());
    }

     /**
      * Ensure we can marshall ToDoItem objects to Json
      */
    public void testObjectMarshallingToJson() {
        ToDoItem toDoItem = getToDoItem(REMINDER_ON);

        try {
            JSONObject json = toDoItem.toJSON();
            assertEquals(TEXT_BODY, json.getString("todotext"));
            assertEquals(REMINDER_ON, json.getBoolean("todoreminder"));
            assertEquals(String.valueOf(CURRENT_DATE.getTime()), json.getString("tododate"));
        } catch (JSONException e) {
            fail("Exception thrown during test execution: " + e.getMessage());
        }
    }

    /**
    * Ensure we can create ToDoItem objects from Json data by using the json constructor
    */
    public void testObjectUnmarshallingFromJson() {
        ToDoItem originalItem = getToDoItem(REMINDER_OFF);

        try {
            JSONObject json = originalItem.toJSON();
            ToDoItem itemFromJson = new ToDoItem(json);

            assertEquals(originalItem.getToDoText(), itemFromJson.getToDoText());
            assertEquals(originalItem.getToDoDate(), itemFromJson.getToDoDate());
            assertEquals(originalItem.hasReminder(), itemFromJson.hasReminder());
            assertEquals(originalItem.getIdentifier(), itemFromJson.getIdentifier());

        } catch (JSONException e) {
            fail("Exception thrown during test execution: " + e.getMessage());
        }
    }

    private ToDoItem getToDoItem(boolean hasReminder) {
        return new ToDoItem(TEXT_BODY, hasReminder, CURRENT_DATE);
    }
}
