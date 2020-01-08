package isep.fr.collegeinformationsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import isep.fr.collegeinformationsystem.R;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView roundUserIcon;
    private EditText username, userPass;
    private ImageView showPass;
    private TextView forgotPass,gustLogIn;
    private Button logIn;

    private boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //set user/admin profile pic
        roundUserIcon = (ImageView) findViewById(R.id.profile_image);
        username = (EditText) findViewById(R.id.user_edt_username);
        userPass = (EditText) findViewById(R.id.user_edt_pass);
        showPass = (ImageView) findViewById(R.id.imv_show_pass);

        forgotPass = (TextView) findViewById(R.id.tv_forgetPass);
        logIn = (Button) findViewById(R.id.btn_login);
        gustLogIn = (TextView) findViewById(R.id.tv_guest);

        showPass.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
        logIn.setOnClickListener(this);
        gustLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_show_pass:
                if (show) {
                    userPass.setTransformationMethod(new PasswordTransformationMethod());
                    if (userPass.length() != 0) {
                        userPass.setSelection(userPass.length());
                    }
                    show = false;
                } else {

                    userPass.setTransformationMethod(null);
                    if (userPass.length() != 0) {
                        userPass.setSelection(userPass.length());
                    }
                    show = true;
                }
                Toast.makeText(getApplicationContext(), "show clicked ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_forgetPass:
                Toast.makeText(getApplicationContext(), "forgot clicked ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_login:
            /*if(username.getText().toString() != null && !username.getText().toString().isEmpty()){

            }else{

            }*/
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                Toast.makeText(getApplicationContext(), "logIn clicked ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tv_guest:
            /*if(username.getText().toString() != null && !username.getText().toString().isEmpty()){

            }else{

            }*/
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                Toast.makeText(getApplicationContext(), "logIn clicked ", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}

