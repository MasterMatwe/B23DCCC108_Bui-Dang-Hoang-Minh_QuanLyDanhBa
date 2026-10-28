package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public final class MainActivity extends BaseActivity {
    private static final int REQUEST_ADD_CONTACT = 100;
    private final ContactRepository repository = ContactRepository.getInstance();
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLayout(R.layout.activity_main);
        adapter = new ContactAdapter(this);
        ListView list = findViewById(R.id.contact_list);
        list.setAdapter(adapter);
        list.setEmptyView(findViewById(R.id.empty_state));
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ContactDetailActivity.class);
            intent.putExtra(ContactDetailActivity.EXTRA_CONTACT_ID, adapter.getItem(position).getId());
            startActivity(intent);
        });
        findViewById(R.id.add_contact_button).setOnClickListener(view ->
                startActivityForResult(new Intent(this, AddContactActivity.class), REQUEST_ADD_CONTACT));
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.submit(repository.getAll());
        ((TextView) findViewById(R.id.total_count)).setText(String.valueOf(repository.size()));
        ((TextView) findViewById(R.id.favorite_count)).setText(String.valueOf(repository.favoriteCount()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_CONTACT && resultCode == RESULT_OK && data != null) {
            Contact contact = repository.findById(data.getLongExtra(ContactDetailActivity.EXTRA_CONTACT_ID, -1));
            if (contact != null) {
                Toast.makeText(this, getString(R.string.contact_added, contact.getFullName()), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
