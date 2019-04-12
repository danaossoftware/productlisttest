package bluetoothprintertest.dn.com.productlisttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        String email = Util.read(this, "email", "").trim();
        // Cek apakah user id kosong di SharedPreferences
        if (email.equals("")) {
            // User ID kosong, masuk ke activity Login
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this, HomeActivity.class));
        }
        finish();
    }
}
