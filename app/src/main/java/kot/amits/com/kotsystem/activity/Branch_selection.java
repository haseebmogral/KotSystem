package kot.amits.com.kotsystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.poovam.pinedittextfield.SquarePinField;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.R;

public class Branch_selection extends AppCompatActivity implements View.OnClickListener {
    Spinner spinner;
    Button nextstep;
    SquarePinField pin_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_selection);
        spinner = (Spinner) findViewById(R.id.spinner);
        nextstep=(Button)findViewById(R.id.next);
        pin_number=(SquarePinField)findViewById(R.id.pin_number);
        pin_number.setTransformationMethod(new AsteriskPasswordTransformationMethod());


        nextstep.setOnClickListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("MIMS Cafe,Kasaragod");
        categories.add("MIMS Cafe,Uppala");
        categories.add("MIMS Cafe,Kanhagngad");
        categories.add("MIMS Cafe,Kochi");


        // Creating adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_adapter,categories);

        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1 );

        // attaching data adapter to spinner
        spinner.setAdapter(adapter);

    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };

    @Override
    public void onClick(View v) {

        if (v==nextstep){

            if (pin_number.getText().toString().equals("")){

                Toast.makeText(this, "Please enter pin number", Toast.LENGTH_SHORT).show();
            }
            else {

                String n=pin_number.getText().toString();
                Toast.makeText(this, n, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Branch_selection.this,select_item.class);
                startActivity(intent);
            }
        }

    }
}
