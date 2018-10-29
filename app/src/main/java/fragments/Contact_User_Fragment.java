package fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bcs.bookexchangev2.R;

import classes.User;
import controllers.ImageManager;

public class Contact_User_Fragment extends Fragment {

    private Button buttonSend;
    private EditText textTo;
    private EditText textSubject;
    private EditText textMessage;
    private ImageButton contact_by_mailBtn;
    private ImageButton contact_by_callBtn;
    private LinearLayout linearLayout;
    private ImageView user_image_e ;
    private TextView user_name_e;
    private TextView user_adress_e;

    private ImageManager imageManager= new ImageManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.contact_user_fragment_layout,container, false);

        // Views :
        buttonSend =  view.findViewById(R.id.buttonSend);
        textTo = view.findViewById(R.id.editTextTo);
        textSubject = view.findViewById(R.id.editTextSubject);
        textMessage = view.findViewById(R.id.editTextMessage);
        contact_by_mailBtn = view.findViewById(R.id.contact_by_mail);
        contact_by_callBtn = view.findViewById(R.id.contact_by_call);
        linearLayout  = view.findViewById(R.id.send_form_layout);
        user_image_e = view.findViewById(R.id.user_image_e);
        user_name_e = view.findViewById(R.id.user_name_e);
        user_adress_e = view.findViewById(R.id.user_adress_e);

        linearLayout.setVisibility(View.GONE);

        final User user = (User) getArguments().getSerializable("user");

        user_image_e.setImageBitmap(imageManager.decodeBase64(user.getProfilPhotoUri()));
        user_name_e.setText(user.getName()+ " "+ user.getSurname());
        user_adress_e.setText(user.getAdress());


        /***************************** Initialisations ******************************************************/
        user.setMailAdress("sylia.righi@hotmail.com");
        final String  bookTitle = getArguments().getString("book");
        textTo.setText(user.getMailAdress());
        textSubject.setText("BookExchange@Demander le livre "+bookTitle);
        textMessage.setText("Je suis interessé(e) par votre offre du livre "+bookTitle);

        /****************************** call action *********************************************************/
        contact_by_callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "je suis ici", Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:0377778888"));
                startActivity(callIntent);
            }
        });

        /****************************** Send mail action ****************************************************/
        contact_by_mailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });


        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String to = textTo.getText().toString();
                String subject = textSubject.getText().toString();
                String message = textMessage.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));

            }
        });

        return view;
    }

}
