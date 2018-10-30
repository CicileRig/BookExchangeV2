package adapters;

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

import java.util.ArrayList;

import classes.User;
import controllers.ImageManager;


public class User_List_Adapter extends ArrayAdapter<User> implements View.OnClickListener{

    private ArrayList<User> dataSet;
    Context mContext;
    ImageManager imageManager = new ImageManager();

    public User_List_Adapter(ArrayList<User> data, Context context) {
        super(context, R.layout.row_user_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView adress;
        ImageView imageView;
    }

    @Override
    public void onClick(View view) {

        int position=(Integer) view.getTag();
        Object object= getItem(position);
        User user=(User)object;

        switch (view.getId())
        {
            case R.id.user_adress_e:
                Snackbar.make(view, "Release date " +user.getAdress(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }
    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_user_item, parent, false);
            viewHolder.name =  convertView.findViewById(R.id.user_name_e);
            viewHolder.adress =  convertView.findViewById(R.id.user_adress_e);
            viewHolder.imageView =  convertView.findViewById(R.id.user_image_e);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.name.setText(dataModel.getName()+" "+dataModel.getSurname());
        viewHolder.adress.setText(dataModel.getAdress());

        if(dataModel.getProfilPhotoUri() != null)
            viewHolder.imageView.setImageBitmap(imageManager.decodeBase64(dataModel.getProfilPhotoUri()));

        // Return the completed view to render on screen
        return convertView;
    }
}

