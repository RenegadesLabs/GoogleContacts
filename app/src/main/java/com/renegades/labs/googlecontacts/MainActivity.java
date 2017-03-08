package com.renegades.labs.googlecontacts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;
    Button signOutButton;
    Button addContactsButton;
    ListView contactsListView;
    RadioGroup firstRadioGroup;
    RadioGroup secondRadioGroup;
    LinearLayout orderLL;
    String accId;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);
        signOutButton = (Button) findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(this);
        addContactsButton = (Button) findViewById(R.id.add_contact);
        addContactsButton.setOnClickListener(this);
        contactsListView = (ListView) findViewById(R.id.contacts_list);
        orderLL = (LinearLayout) findViewById(R.id.order_ll);

        firstRadioGroup = (RadioGroup) findViewById(R.id.first_radio_group);
        firstRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                updateUI(true, i, secondRadioGroup.getCheckedRadioButtonId());
            }
        });
        secondRadioGroup = (RadioGroup) findViewById(R.id.second_radio_group);
        secondRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                updateUI(true, firstRadioGroup.getCheckedRadioButtonId(), i);
            }
        });

        Intent intent = getIntent();
        String isSignedIn = intent.getStringExtra("flag");
        if (isSignedIn != null) {
            signIn();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.add_contact:
                Intent intent = new Intent(this, AddContact.class);
                intent.putExtra("accId", accId);
                startActivity(intent);
                break;
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            accId = acct.getId();
            updateUI(true, 0, 0);
        } else {
            updateUI(false, 0, 0);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false, 0, 0);
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void updateUI(boolean signedIn, int firstId, int secondId) {
        if (signedIn) {
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            addContactsButton.setVisibility(View.VISIBLE);
            orderLL.setVisibility(View.VISIBLE);

            int id1 = firstId;
            int id2 = secondId;
            if (contactsListView.getChildCount() == 0){
                id1 = 0;
                id2 = 0;
            }
            List<String> contacts = getContactsFromDB(id1, id2);
            BaseAdapter adapter = new MyListViewAdapter(this, contacts);
            contactsListView.setAdapter(adapter);
            contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                    intent.putExtra("Id", i);
                    startActivity(intent);
                }
            });
            contactsListView.setVisibility(View.VISIBLE);
        } else {
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
            addContactsButton.setVisibility(View.GONE);
            contactsListView.setVisibility(View.GONE);
            orderLL.setVisibility(View.GONE);
        }
    }

    private List<String> getContactsFromDB(int firstId, int secondId) {
        List<String> contacts = new ArrayList<>();
        String sqlQuery = "SELECT first, last FROM contacts";
        if (firstId != 0 || secondId != 0) {
            if (firstId == R.id.radioFirst) {
                sqlQuery += " WHERE accId='" + accId + "' ORDER BY first";
            } else if (firstId == R.id.radioLast) {
                sqlQuery += " WHERE accId='" + accId + "' ORDER BY last";
            } else if (firstId == R.id.radioEmail) {
                sqlQuery += " Left OUTER JOIN emails ON contacts.id=emails.contactId"
                        + " WHERE accId='" + accId + "'"
                        + " GROUP BY contacts.id"
                        + " ORDER BY COUNT(emails.email)";
            } else if (firstId == R.id.radioPhone) {
                sqlQuery += " Left OUTER JOIN phones ON contacts.id=phones.contactId"
                        + " WHERE accId='" + accId + "'"
                        + " GROUP BY contacts.id"
                        + " ORDER BY COUNT(phones.phone)";
            }
            if (secondId == R.id.radioAsc) {
                sqlQuery += " ASC";
            } else if (secondId == R.id.radioDesc) {
                sqlQuery += " DESC";
            }
            sqlQuery += ";";
        } else {
            sqlQuery += " WHERE accId='" + accId + "';";
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sqlQuery, null);

        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(c.getString(c.getColumnIndex(cn)) + " ");
                    }
                    Log.d(TAG, str);
                    contacts.add(str);
                } while (c.moveToNext());
            }
            c.close();
        } else {
            Log.d(TAG, "Cursor is null");
        }
        db.close();

        return contacts;
    }
}
