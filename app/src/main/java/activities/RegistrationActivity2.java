package activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bcs.bookexchangev2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Calendar;

import classes.User;
import controllers.DataBaseManager;
import controllers.ImageManager;

public class RegistrationActivity2 extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;
    private DataBaseManager dataBaseManager = new DataBaseManager();
    MainActivity activity = new MainActivity();
    ImageManager imageManager = new ImageManager();

    private ImageButton datepickerdialogbutton = null;
    private TextView burthDateEditText;
    private TextView selecteddate= null;
    private Button createAccountBtn = null;
    private Button addProfilImageBtn = null;
    private ImageView profilPhoto = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        burthDateEditText = findViewById(R.id.burthDateEditText);
        createAccountBtn = findViewById(R.id.registerBtn);
        burthDateEditText = findViewById(R.id.burthDateEditText);
        profilPhoto = findViewById(R.id.profile_image);


        /********************************        Boite de dialog datepicker           **********************************/

        datepickerdialogbutton =  findViewById(R.id.button1);
        selecteddate =  findViewById(R.id.burthDateEditText);

        datepickerdialogbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getFragmentManager(), "Date Picker Dialog");

            }
        });

        /******************************  bouton d'ajout d'une photo de profil *******************************************/

        addProfilImageBtn = findViewById(R.id.add_profil_photo);
        addProfilImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        /******************************* bouton création du compte ******************************************************/
        final User user  = (User) this.getIntent().getSerializableExtra("user");
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(user);
            }
        });

    }

    private void selectImage() {

        final CharSequence[] options = { "Prendre une photo", "Selectionner depuis la gallerie","Annuler" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity2.this);
        builder.setTitle("Selectionner une photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Prendre une photo"))
                {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 1);//zero can be replaced with any action code
                }
                else if (options[item].equals("Selectionner depuis la gallerie"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Annuler")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){

                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    profilPhoto.setImageBitmap(photo);
                }

                break;
            case 2:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        profilPhoto.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void createAccount(final User myUser) {
        Log.d(TAG, "createAccount:" + myUser.getMailAdress());

        // [START create_user_with_email]
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(myUser.getMailAdress(), myUser.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            activity.updateUI(user);
                            if (!burthDateEditText.getText().toString().equals("Date de naissance"))
                            {
                                myUser.setAge(burthDateEditText.getText().toString());
                            }
                            Bitmap profilBitmap = ((BitmapDrawable)profilPhoto.getDrawable()).getBitmap();
                            if(profilBitmap!= null)
                            {
                                myUser.setProfilPhotoUri(imageManager.encodeBitmap(profilBitmap));
                            }
                            dataBaseManager.writeNewUser(myUser);
                            Intent intent = new Intent(RegistrationActivity2.this, ProfilActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        } else {

                            try
                            {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthWeakPasswordException weakPassword)
                            {
                                Log.d(TAG, "onComplete: weak_password");
                                Toast.makeText(RegistrationActivity2.this, "Le mot de passe doit contenir plus de 6 caracteres",
                                        Toast.LENGTH_SHORT).show();
                                // TODO: take your actions!
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                            {
                                Log.d(TAG, "onComplete: malformed_email");
                                Toast.makeText(RegistrationActivity2.this, "Veuillez introduire une adresse mail valide",
                                        Toast.LENGTH_SHORT).show();

                                // TODO: Take your action
                            }
                            catch (FirebaseAuthUserCollisionException existEmail)
                            {
                                Log.d(TAG, "onComplete: exist_email");
                                Toast.makeText(RegistrationActivity2.this, "Cette adresse mail est deja utilisée",
                                        Toast.LENGTH_SHORT).show();

                                // TODO: Take your action
                            }
                            catch (Exception e)
                            {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                            }
                            activity.updateUI(null);
                        }
                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]

    }



    public static class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year,month,day);
            return datepickerdialog;
        }


        public static String getMonthName(int month){
            switch(month+1){
                case 1:
                    return "Janvier";

                case 2:
                    return "Fevrier";

                case 3:
                    return "Mars";

                case 4:
                    return "Avril";

                case 5:
                    return "Mai";

                case 6:
                    return "Join";

                case 7:
                    return "Juillet";

                case 8:
                    return "Aout";

                case 9:
                    return "Septembre";

                case 10:
                    return "Octobre";

                case 11:
                    return "Novembre";

                case 12:
                    return "Decembre";
            }

            return "";
        }

        public void onDateSet(DatePicker view, int year, int month, int day){

            TextView textview = getActivity().findViewById(R.id.burthDateEditText);
            textview.setText(day + "  " + getMonthName(month) + "  " + year);
        }
    }
}
