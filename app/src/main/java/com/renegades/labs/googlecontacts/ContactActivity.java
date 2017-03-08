package com.renegades.labs.googlecontacts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {
    TextView firstText;
    TextView lastText;
    ListView emailsListView;
    ListView phonesListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        firstText = (TextView) findViewById(R.id.first_name_text_view);
        lastText = (TextView) findViewById(R.id.last_name_text_view);
        emailsListView = (ListView) findViewById(R.id.emails_list_view);
        phonesListView = (ListView) findViewById(R.id.phones_list_view);

        Intent intent = getIntent();
        int id = intent.getIntExtra("Id", 0);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlQuery = "SELECT first, last FROM contacts WHERE id=" + id + ";";
        Cursor c = db.rawQuery(sqlQuery, null);
        if (c != null) {
            if (c.moveToFirst()) {
                firstText.setText(c.getString(0));
                lastText.setText(c.getString(1));
            }
            c.close();
        }

        sqlQuery = "SELECT email FROM emails WHERE contactId=" + id + ";";
        c = db.rawQuery(sqlQuery, null);
        ArrayList<ListItem> emails = new ArrayList<>();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    ListItem listItem = new ListItem(c.getString(0), 0);
                    emails.add(listItem);
                } while (c.moveToNext());
            }
            c.close();
        }
        BaseAdapter adapter = new MyListViewAdapter(this, emails);
        emailsListView.setAdapter(adapter);

        sqlQuery = "SELECT phone FROM phones WHERE contactId=" + id + ";";
        c = db.rawQuery(sqlQuery, null);
        ArrayList<ListItem> phones = new ArrayList<>();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    ListItem listItem = new ListItem(c.getString(0), 0);
                    phones.add(listItem);
                } while (c.moveToNext());
            }
            c.close();
        }
        adapter = new MyListViewAdapter(this, phones);
        phonesListView.setAdapter(adapter);

        db.close();

    }
}
