package com.theironyard.androidcontacts2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    ArrayAdapter<String> contactsList;
    ListView contacts;
    EditText contactName;
    EditText contactPhoneNumber;
    Button addContact;
    static final int CONTACT_PAGE = 1;
    final String FILENAME = "androidcontacts.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contacts = (ListView) findViewById(R.id.listView);
        contactName = (EditText) findViewById(R.id.contactName);
        contactPhoneNumber = (EditText) findViewById(R.id.contactPhoneNumber);
        addContact = (Button) findViewById(R.id.addContact);

        contactsList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        loadContact();
        contacts.setAdapter(contactsList);

        addContact.setOnClickListener(this);
        contacts.setOnItemLongClickListener(this);

        contacts.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View action) {
        String addName = contactName.getText().toString();
        String addNumber = contactPhoneNumber.getText().toString();

        contactsList.add(addName + " [" + addNumber + "]");

        contactName.setText("");
        contactPhoneNumber.setText("");

        saveContact();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final String individualContact = contactsList.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("To Do");
        builder.setMessage("Are you sure you want to remove this contact?");
        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                contactsList.remove(individualContact);
                saveContact();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing here
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this, ContactPage.class);
        intent.putExtra("contactname", contactsList.getItem(position));
        intent.putExtra("position", position);


        startActivityForResult(intent, CONTACT_PAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTACT_PAGE) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", 0);

                String contactDetailed = data.getStringExtra("contactDetailed");
                String newContact = data.getStringExtra("editContactName");
                String newNumber = data.getStringExtra("editContactNumber");

                contactsList.remove(contactDetailed);
                contactsList.add(newContact + " [" + newNumber + "]");

                saveContact();
            }
        }
    }

    private void saveContact() {
        try {
            FileOutputStream outputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < contactsList.getCount(); i++) {
                String contactsListItem = contactsList.getItem(i);
                builder.append(contactsListItem + "\n");
            }
            outputStream.write(builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContact() {
        try {
            FileInputStream inputStream = openFileInput(FILENAME);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            int position = 0;
            while (reader.ready()) {
                String contactLine = reader.readLine();
                contactsList.add(contactLine);

                position++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
