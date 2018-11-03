package activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bcs.bookexchangev2.R;

import java.io.IOException;
import java.util.Calendar;

import classes.Event;
import controllers.DataBaseManager;
import controllers.ImageManager;

public class Add_Event_Activity extends AppCompatActivity {

    private TextView event_date;
    private ImageButton selectDateBtn;

    private TextView event_time;
    private ImageButton selectTimeBtn;

    private ImageView imageView;
    private Button addImageBtn;

    private Button createEventBtn;
    private EditText event_name;
    private EditText event_place;
    private EditText event_description;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private DataBaseManager dataBaseManager = new DataBaseManager();
    private ImageManager imageManager = new ImageManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event_);

        // view :
        selectDateBtn =  findViewById(R.id.event_date_btn);
        event_date =  findViewById(R.id.event_date);
        selectTimeBtn = findViewById(R.id.event_time_btn);
        event_time = findViewById(R.id.event_time);
        addImageBtn = findViewById(R.id.add_event_image);
        imageView = findViewById(R.id.event_image);
        createEventBtn = findViewById(R.id.createEventButton);
        event_name = findViewById(R.id.event_name);
        event_place = findViewById(R.id.event_place);
        event_description = findViewById(R.id.event_description);

        /********************************        Boite de dialog datepicker           **********************************/

        selectDateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getFragmentManager(), "Date Picker Dialog");

            }
        });

        /******************************      Boite de dialogue timePicker  **************************************/

        selectTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Add_Event_Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                event_time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        /************************************ Ajouter une image de l'evenement **************************************/
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        /*************************** Creer l'evenement **************************************************************/
        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event = new Event();
                event.setEvent_name(event_name.getText().toString());
                event.setEvent_date(event_date.getText().toString());
                event.setEvent_hour(event_time.getText().toString());
                event.setEvent_place(event_place.getText().toString());
                event.setEvent_description(event_description.getText().toString());
                Bitmap profilBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                if(profilBitmap!= null)
                {
                    event.setEvent_image_url(imageManager.encodeBitmap(profilBitmap));
                }
                dataBaseManager.addNewEvent(event);
                Toast.makeText(Add_Event_Activity.this, "Evenement créé avec succes", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Add_Event_Activity.this, NavigationActivity.class);
                intent.putExtra("item_id",R.id.nav_event);
                startActivity(intent);
            }
        });

    }

    private void selectImage() {

        final CharSequence[] options = { "Prendre une photo", "Selectionner depuis la gallerie","Annuler" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Add_Event_Activity.this);
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
                    imageView.setImageBitmap(photo);
                    addImageBtn.setText("");
                }

                break;
            case 2:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        imageView.setImageBitmap(bitmap);
                        addImageBtn.setText("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed()
    {
        super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
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

            TextView textview = getActivity().findViewById(R.id.event_date);
            textview.setText(day + "  " + getMonthName(month) + "  " + year);
        }
    }
}
