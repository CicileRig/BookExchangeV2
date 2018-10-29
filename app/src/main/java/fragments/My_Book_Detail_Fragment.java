package fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bcs.bookexchangev2.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import classes.Book;
import controllers.DataBaseManager;
import controllers.ImageManager;

public class My_Book_Detail_Fragment extends Fragment {

    private TextView book_title ;
    private TextView book_authors ;
    private TextView book_categories ;
    private TextView book_isbn ;
    private TextView book_description;
    private TextView book_language;
    private ImageView book_image ;
    private Button readMoreButton;

    private ImageManager imageManager = new ImageManager();
    private Button deleteBookBtn ;
    private DataBaseManager dataBaseManager = new DataBaseManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.my_book_detail_fragment_layout,container, false);

        deleteBookBtn = view.findViewById(R.id.deleteBookBtn);

        final Book book = (Book) getArguments().getSerializable("book");

        book_title = view.findViewById(R.id.book_name);
        book_title.setText(book.getTitle());

        book_authors = view.findViewById(R.id.book_authors);
        book_authors.setText(book.authorsToString());

        book_categories= view.findViewById(R.id.book_categorie);
        if(book.getCategories()!= null)
        {
            book_categories.setText(book.categoriesToString());
        }else{
            book_categories.setVisibility(View.GONE);
        }

        book_description= view.findViewById(R.id.book_description);
        if(book.getDescription() != null)
        {

            book_description.setText(book.getDescription());
            book_description.setMaxLines(2);

        }else{
            book_description.setText("Aucune description");
        }
        final Boolean[] interup = {false};
        readMoreButton = view.findViewById(R.id.readMoreButton);
        readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(interup[0] == false )
                {
                    book_description.setMaxLines(100);
                    interup[0] = true;
                    readMoreButton.setText("Lire moins");
                }else{
                    book_description.setMaxLines(2);
                    interup[0] = false;
                    readMoreButton.setText("...Lire la suite");
                }
            }
        });


        book_language = view.findViewById(R.id.book_language);
        if(book.getLanguage() != null){
            book_language.setText("Disponible en "+book.getLanguage());
        }else
        {
            book_language.setVisibility(View.GONE);
        }

        book_image= view.findViewById(R.id.book_image);
        if(book.getImageURL() != null)
        {
            try {
                URL url = new URL(book.getImageURL());
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                book_image.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /**************************************** Delete book action ***************************************/
        deleteBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseManager.deleteBookFromUser(book);
                getActivity().getFragmentManager().popBackStack();
            }
        });
        return view;
    }

}
