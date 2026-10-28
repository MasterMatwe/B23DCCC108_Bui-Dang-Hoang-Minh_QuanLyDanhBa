package com.example.myapplication;

import java.util.ArrayList;

public final class ContactRepository {
    private static final ContactRepository INSTANCE = new ContactRepository();
    private final ArrayList<Contact> contacts = new ArrayList<>();
    private long nextId = 1;

    ContactRepository() {
        add("Nguyễn Văn An", "0901234567", "Bạn bè", "Nam", false, "");
        add("Trần Thị Bình", "0912345678", "Gia đình", "Nữ", true, "Chị gái, hay gọi buổi tối");
        add("Lê Minh Châu", "0987654321", "Công việc", "Khác", false, "");
    }

    public static ContactRepository getInstance() { return INSTANCE; }

    public ArrayList<Contact> getAll() { return new ArrayList<>(contacts); }

    public Contact findById(long id) {
        for (Contact contact : contacts) {
            if (contact.getId() == id) return contact;
        }
        return null;
    }

    public boolean containsPhone(String phone) {
        for (Contact contact : contacts) {
            if (contact.getPhone().equals(phone)) return true;
        }
        return false;
    }

    public Contact add(String name, String phone, String group, String gender,
                       boolean favorite, String note) {
        if (!ContactValidator.isNameValid(name) || !ContactValidator.isPhoneValid(phone)) {
            throw new IllegalArgumentException("Thông tin liên hệ không hợp lệ");
        }
        if (containsPhone(phone)) throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        Contact contact = new Contact(nextId++, name.trim(), phone, group, gender,
                favorite, note == null ? "" : note.trim());
        contacts.add(contact);
        return contact;
    }

    public boolean setFavorite(long id, boolean favorite) {
        for (int index = 0; index < contacts.size(); index++) {
            if (contacts.get(index).getId() == id) {
                contacts.set(index, contacts.get(index).withFavorite(favorite));
                return true;
            }
        }
        return false;
    }

    public boolean delete(long id) {
        for (int index = 0; index < contacts.size(); index++) {
            if (contacts.get(index).getId() == id) {
                contacts.remove(index);
                return true;
            }
        }
        return false;
    }

    public int size() { return contacts.size(); }

    public int favoriteCount() {
        int count = 0;
        for (Contact contact : contacts) if (contact.isFavorite()) count++;
        return count;
    }
}
