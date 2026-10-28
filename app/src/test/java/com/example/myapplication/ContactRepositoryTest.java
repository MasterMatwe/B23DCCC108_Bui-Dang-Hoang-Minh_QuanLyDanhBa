package com.example.myapplication;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContactRepositoryTest {
    private ContactRepository repository;

    @Before public void setup() { repository = new ContactRepository(); }

    @Test public void sampleContactsHaveCorrectStatistics() {
        assertEquals(3, repository.size());
        assertEquals(1, repository.favoriteCount());
        assertEquals("0901234567", repository.getAll().get(0).getPhone());
        assertEquals("A", repository.getAll().get(0).getInitial());
        assertEquals("B", repository.getAll().get(1).getInitial());
        assertEquals("C", repository.getAll().get(2).getInitial());
    }

    @Test public void addPreservesPhoneAndAllFields() {
        Contact contact = repository.add("  Đặng Thị Hà  ", "0900000001", "Khác", "Nữ", true, "  Bạn học  ");
        assertEquals(4, repository.size());
        assertEquals(2, repository.favoriteCount());
        assertEquals("Đặng Thị Hà", contact.getFullName());
        assertEquals("0900000001", contact.getPhone());
        assertEquals("Khác", contact.getGroup());
        assertEquals("Nữ", contact.getGender());
        assertEquals("Bạn học", contact.getNote());
    }

    @Test public void favoriteChangesAndDeletionUpdateStatistics() {
        long id = repository.getAll().get(0).getId();
        assertTrue(repository.setFavorite(id, true));
        assertEquals(2, repository.favoriteCount());
        assertTrue(repository.findById(id).isFavorite());
        assertTrue(repository.setFavorite(id, false));
        assertEquals(1, repository.favoriteCount());
        assertTrue(repository.setFavorite(id, true));
        assertTrue(repository.delete(id));
        assertEquals(2, repository.size());
        assertEquals(1, repository.favoriteCount());
        assertNull(repository.findById(id));
    }

    @Test public void duplicatePhoneDoesNotModifyList() {
        assertThrows(IllegalArgumentException.class, () ->
                repository.add("Người khác", "0901234567", "Khác", "Nam", false, ""));
        assertEquals(3, repository.size());
    }

    @Test public void invalidContactCannotEnterRepository() {
        assertThrows(IllegalArgumentException.class, () ->
                repository.add("  ", "0900000001", "Khác", "Nam", false, ""));
        assertThrows(IllegalArgumentException.class, () ->
                repository.add("Tên", "1900000001", "Khác", "Nam", false, ""));
        assertEquals(3, repository.size());
    }

    @Test public void identitiesStayStableAfterDeletionAndAddition() {
        long remainingId = repository.getAll().get(2).getId();
        repository.delete(repository.getAll().get(0).getId());
        Contact added = repository.add("Tên mới", "0900000002", "Khác", "Nam", false, "");
        assertEquals("Lê Minh Châu", repository.findById(remainingId).getFullName());
        assertNotEquals(remainingId, added.getId());
    }

    @Test public void deletingAllContactsLeavesValidEmptyState() {
        for (Contact contact : repository.getAll()) repository.delete(contact.getId());
        assertEquals(0, repository.size());
        assertEquals(0, repository.favoriteCount());
        assertTrue(repository.getAll().isEmpty());
        assertFalse(repository.delete(-1));
        assertFalse(repository.setFavorite(-1, true));
        Contact added = repository.add("Liên hệ mới", "0900000003", "Bạn bè", "Nam", false, "");
        assertNotNull(repository.findById(added.getId()));
    }

    @Test public void returnedListCannotAccidentallyClearRepository() {
        repository.getAll().clear();
        assertEquals(3, repository.size());
    }
}
