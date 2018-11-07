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

import classes.Book;

public class Book_List_Adapter extends ArrayAdapter<Book>  implements View.OnClickListener{
    private ArrayList<Book> dataSet;
    Context mContext;
    Activity activity;

    // View lookup cache
    private static class ViewHolder {
        TextView bookTitle;
        TextView bookAuthors;
        TextView bookSoumissionDate;
        ImageView bookImage ;
    }

    public Book_List_Adapter(ArrayList<Book> data, Activity context) {
        super(context, R.layout.row_book_item, data);
        this.dataSet = data;
        this.mContext=activity;
        this.activity = context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Book book=(Book) object;

        switch (v.getId())
        {
            case R.id.book_name:
                Snackbar.make(v, "Release date " +book.getTitle(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Book book = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_book_item, parent, false);
            viewHolder.bookTitle =  convertView.findViewById(R.id.book_name);
            viewHolder.bookAuthors =  convertView.findViewById(R.id.book_authors);
            viewHolder.bookSoumissionDate =  convertView.findViewById(R.id.dateSoumissionTextview);
            viewHolder.bookImage =  convertView.findViewById(R.id.event_image_r);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

/*        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);*/
        lastPosition = position;

        viewHolder.bookTitle.setText(book.getTitle());
        viewHolder.bookAuthors.setText(book.getAuthors());
        if(book.getSoumissionDate() !=  null){
            viewHolder.bookSoumissionDate.setText("Dernière publication: "+book.getSoumissionDate());
        }else{
            viewHolder.bookSoumissionDate.setText("");
        }

            if(book.getImageURL() != null){
                Picasso.with(activity)
                        .load(book.getImageURL())
                        .into(viewHolder.bookImage);
            }

        // Return the completed view to render on screen
        return convertView;
    }
}
