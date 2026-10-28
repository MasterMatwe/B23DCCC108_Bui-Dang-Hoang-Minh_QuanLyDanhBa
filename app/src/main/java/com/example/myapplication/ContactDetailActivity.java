package com.example.myapplication;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public final class ContactDetailActivity extends BaseActivity {
    public static final String EXTRA_CONTACT_ID = "com.example.myapplication.CONTACT_ID";
    private final ContactRepository repository = ContactRepository.getInstance();
    private long contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLayout(R.layout.activity_contact_detail);
        contactId = getIntent().getLongExtra(EXTRA_CONTACT_ID, -1);
        Contact contact = repository.findById(contactId);
        if (contact == null) {
            Toast.makeText(this, R.string.contact_missing, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ((TextView) findViewById(R.id.detail_avatar)).setText(contact.getInitial());
        ((TextView) findViewById(R.id.detail_name)).setText(contact.getFullName());
        ((TextView) findViewById(R.id.detail_phone)).setText(contact.getPhone());
        ((TextView) findViewById(R.id.detail_group)).setText(contact.getGroup());
        ((TextView) findViewById(R.id.detail_gender)).setText(contact.getGender());
        ((TextView) findViewById(R.id.detail_note)).setText(contact.getNote().isEmpty()
                ? getString(R.string.no_note) : contact.getNote());
        CheckBox favorite = findViewById(R.id.detail_favorite);
        favorite.setChecked(contact.isFavorite());
        favorite.setOnCheckedChangeListener((button, checked) -> repository.setFavorite(contactId, checked));
        findViewById(R.id.back_button).setOnClickListener(view -> finish());
        findViewById(R.id.return_button).setOnClickListener(view -> finish());
        findViewById(R.id.dial_button).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact.getPhone(), null));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException exception) {
                Toast.makeText(this, R.string.no_dialer, Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.delete_button).setOnClickListener(view -> new AlertDialog.Builder(this)
                .setTitle(R.string.delete_contact)
                .setMessage(getString(R.string.confirm_delete, contact.getFullName()))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    repository.delete(contactId);
                    Toast.makeText(this, R.string.contact_deleted, Toast.LENGTH_SHORT).show();
                    finish();
                }).show());
    }
}
