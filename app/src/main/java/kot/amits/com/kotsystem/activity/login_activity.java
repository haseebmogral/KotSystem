package kot.amits.com.kotsystem.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.poovam.pinedittextfield.SquarePinField;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

import static kot.amits.com.kotsystem.DBhelper.DBmanager.*;

public class login_activity extends AppCompatActivity {
    SquarePinField pin_number;
    Button nextstep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_activity);
        pin_number=(SquarePinField)findViewById(R.id.pin_number);
        pin_number.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        nextstep=(Button)findViewById(R.id.next);

        sharedpreferences = getSharedPreferences(DBmanager.sharedpreference_name, Context.MODE_PRIVATE);

        nextstep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pin_number.getText().toString().equals(sharedpreferences.getString(DBmanager.sharedpreference_password,""))){
                    Intent intent=new Intent(login_activity.this,Manager_Dashboard.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(login_activity.this, "Password error", Toast.LENGTH_SHORT).show();
                }
            }
        });




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

}
