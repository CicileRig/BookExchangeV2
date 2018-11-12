package com.example.bcs.bookexchangev2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import activities.ProfilActivity;
import activities.RegistrationActivity2;
import classes.User;
import controllers.DataBaseManager;
import controllers.ImageManager;

public class ModifyProfil extends AppCompatActivity {

    private TextView user_name;
    private TextView user_surname;
    private TextView user_adress;
    private TextView user_phone_number;
    private ImageView user_profil_url;
    private Button register_modifications_btn;
    private Button change_photo_btn;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    private DataBaseManager dataBaseManager =  new DataBaseManager();
    private ImageManager imageManager = new ImageManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profil);

        //views :
        user_name = findViewById(R.id.nameEditText);
        user_surname  = findViewById(R.id.surnameEditText);
        user_adress = findViewById(R.id.adressPostale);
        user_phone_number = findViewById(R.id.numeroTel);
        user_profil_url = findViewById(R.id.profile_image);
        change_photo_btn = findViewById(R.id.add_profil_photo);
        register_modifications_btn = findViewById(R.id.save_modif_btn);

        // get current user from firebase auth
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        /************************************* Display current informations of user *********************************/
        dataBaseManager.getUserById(firebaseUser.getUid().toString(), new DataBaseManager.ResultGetter<User>() {
            @Override
            public void onResult(User user) {
                user_name.setText(user.getName());
                user_surname.setText(user.getSurname());
                user_adress.setText(user.getAdress());

                if(user.getPhoneNumber()!= null)
                    user_phone_number.setText(user.getPhoneNumber());
                else
                    user_phone_number.setText("");

                if(user.getProfilPhotoUri()!= null)
                    user_profil_url.setImageBitmap(imageManager.decodeBase64(user.getProfilPhotoUri()));

            }
        });
        /********************************************     Change profil photo     **************************************/
        change_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        /*******************************************   Register new modifications     **********************************/
        register_modifications_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseManager.getUserById(firebaseUser.getUid().toString(), new DataBaseManager.ResultGetter<User>() {
                    @Override
                    public void onResult(User user) {

                        if(!user.getName().equals(user_name.getText())){
                            user.setName(user_name.getText().toString());
                        }
                        if(!user.getSurname().equals(user_surname.getText())){
                            user.setSurname(user_surname.getText().toString());
                        }
                        if(!user.getAdress().equals(user_adress.getText())){
                            user.setAdress(user_adress.getText().toString());
                        }

                        user.setPhoneNumber(user_phone_number.getText().toString());

                        Bitmap profilBitmap = ((BitmapDrawable)user_profil_url.getDrawable()).getBitmap();
                        user.setProfilPhotoUri(imageManager.encodeBitmap(profilBitmap));

                        dataBaseManager.modifyUser(user);
                        SharedPreferences.Editor editor = getSharedPreferences("SESSION", MODE_PRIVATE).edit();

                        // Storing name in pref
                        editor.putString("name", user.getName());
                        // Storing surname in pref
                        editor.putString("surname", user.getSurname());
                        // Storing image url in pref
                        editor.putString("image_url", user.getProfilPhotoUri());

                        // commit changes
                        editor.apply();
                        startActivity(new Intent(ModifyProfil.this, ProfilActivity.class));
                    }
                });
            }
        });



    }

    private void selectImage() {

        final CharSequence[] options = { "Prendre une photo", "Selectionner depuis la gallerie","Annuler" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyProfil.this);
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
                    user_profil_url.setImageBitmap(photo);
                }

                break;
            case 2:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        user_profil_url.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


}
