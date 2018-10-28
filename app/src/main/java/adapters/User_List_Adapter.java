package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bcs.bookexchangev2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import classes.User;
import controllers.ImageManager;

public class User_List_Adapter extends ArrayAdapter<User>  implements View.OnClickListener{
    private ArrayList<User> dataSet;
    Context mContext;
    Activity activity;
    private ImageManager imageManager = new ImageManager();

    // View lookup cache
    private static class ViewHolder {
        TextView userName;
        TextView userAdress;
        ImageView userImage ;
    }

    public User_List_Adapter(ArrayList<User> data, Activity activity) {
        super(activity, R.layout.row_user_item, data);
        this.dataSet = data;
        this.mContext=activity;
        this.activity = activity;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        User user=(User) object;

        switch (v.getId())
        {
            case R.id.user_name_e:
                Snackbar.make(v, "Release date " +user.getMailAdress(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_user_item, parent, false);
            viewHolder.userName =  convertView.findViewById(R.id.user_name_e);
            viewHolder.userAdress =  convertView.findViewById(R.id.user_adress_e);
            viewHolder.userImage =  convertView.findViewById(R.id.user_image_e);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.userName.setText(user.getName()+ " " +user.getSurname());
        viewHolder.userAdress.setText(user.getAdress());

        if(user.getProfilPhotoUri() != null)
            viewHolder.userImage.setImageBitmap(imageManager.decodeBase64(user.getProfilPhotoUri()));

        // Return the completed view to render on screen
        return convertView;
    }
}
