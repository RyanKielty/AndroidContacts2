package com.theironyard.androidcontacts2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContactPage extends AppCompatActivity implements View.OnClickListener {

    TextView name;
    EditText editContactName;
    EditText editContactNumber;

    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);

        name = (TextView) findViewById(R.id.textViewContact);
        editContactName = (EditText) findViewById(R.id.nameChange);
        editContactNumber = (EditText) findViewById(R.id.newNumber);

        String contactName = getIntent().getExtras().getString("contactname", "");
        name.setText(getIntent().getExtras().getString("contactname", ""));
        position = getIntent().getExtras().getInt("position", 0);

        Button saveChangesButton = (Button) findViewById(R.id.editChanges);
        saveChangesButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final Intent returnIntent = new Intent();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Commit Changes?");
        builder.setMessage("Is this change really necessary?");
        builder.setPositiveButton(android.R.string.yes, new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                returnIntent.putExtra("editContactName", editContactName.getText().toString());
                returnIntent.putExtra("editContactNumber", editContactNumber.getText().toString());
                returnIntent.putExtra("position", position);

                returnIntent.putExtra("contactDetailed", name.getText().toString());

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        builder.setNegativeButton(android.R.string.no, new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing here - "Cancel" function on confirmation prompt
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
