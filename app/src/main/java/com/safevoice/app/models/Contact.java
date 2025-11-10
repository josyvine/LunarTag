package com.safevoice.app.models;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple data model class (POJO) to represent an emergency contact.
 * It includes helper methods for converting the object to and from a JSONObject,
 * which is useful for storing it in SharedPreferences.
 */
public class Contact {

    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_PHONE = "phoneNumber";

    private String name;
    private String phoneNumber;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Converts this Contact object into a JSONObject.
     *
     * @return A JSONObject representation of the contact, or null on error.
     */
    @Nullable
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JSON_KEY_NAME, this.name);
            jsonObject.put(JSON_KEY_PHONE, this.phoneNumber);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a Contact object from a JSONObject.
     *
     * @param jsonObject The JSONObject to parse.
     * @return A new Contact object, or null if parsing fails.
     */
    @Nullable
    public static Contact fromJSONObject(JSONObject jsonObject) {
        try {
            String name = jsonObject.getString(JSON_KEY_NAME);
            String phone = jsonObject.getString(JSON_KEY_PHONE);
            return new Contact(name, phone);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Overriding equals and hashCode is important for managing lists of contacts,
    // for example, to correctly find and remove a specific contact.
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contact contact = (Contact) obj;
        return name.equals(contact.name) && phoneNumber.equals(contact.phoneNumber);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        return result;
    }
          }
