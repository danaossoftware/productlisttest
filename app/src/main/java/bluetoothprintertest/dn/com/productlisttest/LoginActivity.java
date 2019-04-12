package bluetoothprintertest.dn.com.productlisttest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity {
    EditText emailField, passwordField;
    boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
    }

    public void login(View view) {
        final String email = emailField.getText().toString().trim();
        final String password = passwordField.getText().toString();
        if (email.equals("") || password.equals("")) {
            Util.show(this, R.string.text3);
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage(getResources().getString(R.string.text4));
        dialog.show();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .build();
                    RequestBody params = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("email", email)
                            .addFormDataPart("password", password)
                            .build();
                    Request req = new Request.Builder()
                            .url(Constants.BASE_URL+"login")
                            .post(params)
                            .build();
                    final String response = client.newCall(req).execute().body().string();
                    Util.log("Response: "+response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject obj = new JSONObject(response);
                                String message = obj.getString("message");
                                dialog.dismiss();
                                if (message.trim().equals("success")) {
                                    Util.write(LoginActivity.this, "email", email);
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    Util.show(LoginActivity.this, message);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void viewPassword(View view) {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            passwordField.setTransformationMethod(null);
        } else {
            passwordField.setTransformationMethod(new PasswordTransformationMethod());
        }
    }
}
