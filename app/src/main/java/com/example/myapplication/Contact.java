package com.example.myapplication;

/** One in-memory contact. Identity remains stable when other contacts are deleted. */
public final class Contact {
    private final long id;
    private final String fullName;
    private final String phone;
    private final String group;
    private final String gender;
    private final boolean favorite;
    private final String note;

    public Contact(long id, String fullName, String phone, String group,
                   String gender, boolean favorite, String note) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.group = group;
        this.gender = gender;
        this.favorite = favorite;
        this.note = note;
    }

    public long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getGroup() { return group; }
    public String getGender() { return gender; }
    public boolean isFavorite() { return favorite; }
    public String getNote() { return note; }

    public String getInitial() {
        String name = fullName.trim();
        if (name.isEmpty()) return "?";
        String[] parts = name.split("\\s+");
        String lastName = parts[parts.length - 1];
        return lastName.substring(0, lastName.offsetByCodePoints(0, 1))
                .toUpperCase(java.util.Locale.forLanguageTag("vi"));
    }

    public Contact withFavorite(boolean value) {
        return new Contact(id, fullName, phone, group, gender, value, note);
    }
}
