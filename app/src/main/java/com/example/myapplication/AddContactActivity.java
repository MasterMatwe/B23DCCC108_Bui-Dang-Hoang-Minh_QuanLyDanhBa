package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public final class AddContactActivity extends BaseActivity {
    private final ContactRepository repository = ContactRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLayout(R.layout.activity_add_contact);
        findViewById(R.id.back_button).setOnClickListener(view -> finish());
        Spinner group = findViewById(R.id.group);
        ArrayAdapter<CharSequence> groups = ArrayAdapter.createFromResource(this,
                R.array.contact_groups, android.R.layout.simple_spinner_item);
        groups.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group.setAdapter(groups);
        findViewById(R.id.save_button).setOnClickListener(view -> save());
    }

    private void save() {
        EditText nameInput = findViewById(R.id.full_name);
        EditText phoneInput = findViewById(R.id.phone);
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        nameInput.setError(null);
        phoneInput.setError(null);
        if (!ContactValidator.isNameValid(name)) {
            nameInput.setError(getString(R.string.name_required));
            nameInput.requestFocus();
            return;
        }
        if (!ContactValidator.isPhoneValid(phone)) {
            phoneInput.setError(getString(R.string.phone_invalid));
            phoneInput.requestFocus();
            return;
        }
        if (repository.containsPhone(phone)) {
            phoneInput.setError(getString(R.string.phone_duplicate));
            phoneInput.requestFocus();
            return;
        }
        Spinner group = findViewById(R.id.group);
        RadioGroup gender = findViewById(R.id.gender);
        RadioButton selection = findViewById(gender.getCheckedRadioButtonId());
        CheckBox favorite = findViewById(R.id.favorite);
        EditText note = findViewById(R.id.note);
        try {
            Contact contact = repository.add(name, phone, group.getSelectedItem().toString(),
                    selection.getText().toString(), favorite.isChecked(), note.getText().toString());
            Intent result = new Intent();
            result.putExtra(ContactDetailActivity.EXTRA_CONTACT_ID, contact.getId());
            setResult(RESULT_OK, result);
            finish();
        } catch (IllegalArgumentException exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
