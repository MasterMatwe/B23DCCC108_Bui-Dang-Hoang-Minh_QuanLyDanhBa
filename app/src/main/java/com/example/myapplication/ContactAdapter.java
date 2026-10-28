package com.example.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public final class ContactAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final ArrayList<Contact> contacts = new ArrayList<>();
    private final int[] avatarColors = {0xff7650a7, 0xff9253a1, 0xff6150a6, 0xff815bc3};

    public ContactAdapter(Context context) { inflater = LayoutInflater.from(context); }

    public void submit(ArrayList<Contact> items) {
        contacts.clear();
        contacts.addAll(items);
        notifyDataSetChanged();
    }

    @Override public int getCount() { return contacts.size(); }
    @Override public Contact getItem(int position) { return contacts.get(position); }
    @Override public long getItemId(int position) { return getItem(position).getId(); }
    @Override public boolean hasStableIds() { return true; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) row = inflater.inflate(R.layout.item_contact, parent, false);
        Contact contact = getItem(position);
        TextView avatar = row.findViewById(R.id.contact_avatar);
        avatar.setText(contact.getInitial());
        avatar.setBackgroundTintList(ColorStateList.valueOf(avatarColors[(int) ((contact.getId() - 1) % avatarColors.length)]));
        ((TextView) row.findViewById(R.id.contact_name)).setText(contact.getFullName());
        ((TextView) row.findViewById(R.id.contact_phone)).setText(contact.getPhone());
        ((TextView) row.findViewById(R.id.contact_group)).setText(contact.getGroup());
        row.findViewById(R.id.contact_star).setVisibility(contact.isFavorite() ? View.VISIBLE : View.GONE);
        return row;
    }
}
