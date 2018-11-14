package activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bcs.bookexchangev2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import classes.User;
import controllers.DataBaseManager;
import controllers.MyBounceInterpolator;


public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{

    private static final String TAG = "EmailPassword";


    private EditText mEmailField = null;
    private EditText mPasswordField = null;
    private Animation myAnim = null;
    private Button btnLogIn;
    final String IS_USER_LOGIN = "IsUserLoggedIn";

    int PRIVATE_MODE = 0;
    private DataBaseManager dataBaseManager = new DataBaseManager();

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);
        btnLogIn = findViewById(R.id.emailSignInButton);
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.emailCreateAccountButton).setOnClickListener(this);

        //add annimation to buttons
        myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        myAnim.setInterpolator(interpolator);
        btnLogIn.startAnimation(myAnim);

        mAuth = FirebaseAuth.getInstance();

        // Check User Session Manager
        SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);
        if (restoredText != null) {
            Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
            startActivity(intent);
        }


    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed()
    {

    }



    public void signIn(final String email, final String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            dataBaseManager.getUserById(user.getUid(), new DataBaseManager.ResultGetter<User>() {
                                @Override
                                public void onResult(User user) {

                                    SharedPreferences.Editor editor = getSharedPreferences("SESSION", MODE_PRIVATE).edit();

                                    // Storing login value as TRUE
                                    editor.putBoolean(IS_USER_LOGIN, true);

                                    // Storing name in pref
                                    editor.putString("name", user.getName());

                                    // Storing surname in pref
                                    editor.putString("surname", user.getSurname());

                                    // Storing image url in pref
                                    editor.putString("image_url", user.getProfilPhotoUri());

                                    // commit changes
                                    editor.apply();


                                }
                            });

                            Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Adresse mail ou mot de passe incorrect",
                                    Toast.LENGTH_SHORT).show();
                            mPasswordField.setText("");
                            updateUI(null);
                        }

                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
                /*Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intent);*/
        } else {

        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.emailCreateAccountButton) {
            Intent t = new Intent(this, RegistrationActivity.class);
            startActivity(t);
        } else if (i == R.id.emailSignInButton) {
            Button btn = findViewById(i);
            btn.startAnimation(myAnim);
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());

        }
    }
}

