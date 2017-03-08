package com.renegades.labs.googlecontacts;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddContact extends AppCompatActivity {
    LinearLayout emailLL;
    LinearLayout phoneLL;
    EditText firstNameText;
    EditText lastNameText;
    EditText emailText;
    EditText phoneText;
    Button addEmail;
    Button addPhone;
    Button submit;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        dbHelper = new DBHelper(this);
        emailLL = (LinearLayout) findViewById(R.id.email_ll);
        phoneLL = (LinearLayout) findViewById(R.id.phone_ll);
        firstNameText = (EditText) findViewById(R.id.first_name_text);
        lastNameText = (EditText) findViewById(R.id.last_name_text);
        emailText = (EditText) findViewById(R.id.email_text);
        phoneText = (EditText) findViewById(R.id.phone_text);
        addEmail = (Button) findViewById(R.id.add_email);
        addPhone = (Button) findViewById(R.id.add_phone);
        submit = (Button) findViewById(R.id.submit_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstNameText.getText().toString();
                String lastName = lastNameText.getText().toString();
                String email = emailText.getText().toString();
                String phoneNumber = phoneText.getText().toString();

                if (firstName.equals("") || firstName.equals("Enter First Name")) {
                    firstNameText.setText("Enter First Name");
                } else if (lastName.equals("") || firstName.equals("Enter Last Name")) {
                    lastNameText.setText("Enter Last Name");
                } else {

                    Contact contact = new Contact();
                    contact.setFirstName(firstName);
                    contact.setLastName(lastName);

                    if (!email.equals("")) {
                        contact.addEmail(email);
                        if (emailLL.getChildCount() > 1) {
                            for (int i = 1; i < emailLL.getChildCount(); i++) {
                                EditText editText = (EditText) emailLL.getChildAt(i);
                                String additionalEmail = editText.getText().toString();
                                if (!additionalEmail.equals("")) {
                                    contact.addEmail(additionalEmail);
                                }
                            }
                        }
                    }
                    if (!phoneNumber.equals("")) {
                        contact.addPhone(phoneNumber);
                        if (phoneLL.getChildCount() > 1) {
                            for (int i = 1; i < phoneLL.getChildCount(); i++) {
                                EditText editText = (EditText) phoneLL.getChildAt(i);
                                String additionalPhone = editText.getText().toString();
                                if (!additionalPhone.equals("")) {
                                    contact.addPhone(additionalPhone);
                                }
                            }
                        }
                    }

                    addToDB(contact);

                    Intent intent = new Intent(AddContact.this, MainActivity.class);
                    intent.putExtra("flag", "true");
                    startActivity(intent);
                }
            }
        });

        addEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText newEditText = new EditText(AddContact.this);
                newEditText.setHint("email");
                emailLL.addView(newEditText);
            }
        });

        addPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText newEditText = new EditText(AddContact.this);
                newEditText.setHint("phone number");
                phoneLL.addView(newEditText);
            }
        });
    }

    private void addToDB(Contact contact) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.clear();

        Intent intent = getIntent();
        String accId = intent.getStringExtra("accId");
        cv.put("accId", accId);
        cv.put("first", contact.getFirstName());
        cv.put("last", contact.getLastName());
        int id = (int) db.insert("contacts", null, cv);

        cv.clear();
        for (int i = 0; i < contact.getEmails().size(); i++) {
            cv.put("email", contact.getEmails().get(i));
            cv.put("contactId", id);
            db.insert("emails", null, cv);
            cv.clear();
        }

        cv.clear();
        for (int i = 0; i < contact.getPhoneNumbers().size(); i++) {
            cv.put("phone", contact.getPhoneNumbers().get(i));
            cv.put("contactId", id);
            db.insert("phones", null, cv);
            cv.clear();
        }

        db.close();
    }
}
