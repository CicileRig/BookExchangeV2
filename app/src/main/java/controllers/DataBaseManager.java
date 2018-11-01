package controllers;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import activities.MainActivity;
import classes.Book;
import classes.Event;
import classes.User;

public class DataBaseManager {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser ;
    private FirebaseDatabase database ;
    private DatabaseReference myUsersRef ;
    private DatabaseReference myEventsRef ;
    JsonUtil jsonUtil = new JsonUtil();
    User user ;
    SecurityManager securityManager = new SecurityManager();

    public DataBaseManager(){
        mAuth= FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myUsersRef = database.getReference("users");
        myEventsRef = database.getReference("events");
    }

    public void writeNewUser(User user) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userFirebase = mAuth.getCurrentUser();

        String  userId =  userFirebase.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(userId).setValue(user);

        Map<String, Object> postValues = user.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/users/" + userId, postValues);
        mDatabase.updateChildren(childUpdates);

    }

    public void addBookToCurentUser(Book book)
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userFirebase = mAuth.getCurrentUser();
        String  userId =  userFirebase.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        book.setSoumissionDate(year+"-"+month+"-"+day);
        Map<String, Object> postValues = book.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/users/"+userId+"/books/" + securityManager.md5Hash(book.getId()), postValues);
        mDatabase.updateChildren(childUpdates);
    }

    public void addNewEvent(Event event)
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userFirebase = mAuth.getCurrentUser();
        String  userId =  userFirebase.getUid();
        event.setCreatorId(userId);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> postValues = event.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/events/" + securityManager.md5Hash(event.getEvent_name()), postValues);
        mDatabase.updateChildren(childUpdates);
    }

    public void getEventList(final ResultGetter<ArrayList<Event>> getter)
    {
        final ArrayList<Event> eventList = new ArrayList<>();
        myEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap value = (HashMap)dataSnapshot.getValue();
                Set cles = value.keySet();
                Iterator it = cles.iterator();

                while (it.hasNext() ){
                    String key = (String)it.next();
                    final Event event = new Event();
                    Map<String, Object> postValues = (Map)value.get(key);
                    event.setEvent_name(postValues.get("name").toString());
                    event.setEvent_date(postValues.get("date").toString());
                    event.setEvent_hour(postValues.get("hour").toString());
                    event.setEvent_place(postValues.get("place").toString());
                    event.setEvent_description(postValues.get("description").toString());
                    event.setCreatorId(postValues.get("creatorId").toString());
                    if(postValues.get("imageUrl") != null)
                        event.setEvent_image_url(postValues.get("imageUrl").toString());
                    eventList.add(event);
                }
                getter.onResult(eventList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getUsersIsbnList(final ResultGetter<ArrayList<String>> getter)
    {

        final ArrayList<String> usersIdList = new ArrayList<>();
        myUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap value = (HashMap)dataSnapshot.getValue();
                Set cles = value.keySet();
                Iterator it = cles.iterator();

                while (it.hasNext() ){
                    String key = (String)it.next();
                        usersIdList.add(key);
                }
                getter.onResult(usersIdList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUsersHavingBook(final String bookId, final ResultGetter<ArrayList<User>> getter)
    {

        final ArrayList<User> usersIdList = new ArrayList<>();
        myUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap value = (HashMap)dataSnapshot.getValue();
                Set cles = value.keySet();
                Iterator it = cles.iterator();

                while (it.hasNext() ){
                    String key = (String)it.next();
                    final User user = new User();
                    user.setId(key);
                    Map<String, Object> postValues = (Map)value.get(key);
                    user.setName(postValues.get("name").toString());
                    user.setSurname(postValues.get("surname").toString());
                    user.setAdress(postValues.get("adress").toString());
                    user.setMailAdress(postValues.get("mailAdress").toString());
                    if(postValues.get("profilPhotoUri") != null)
                        user.setProfilPhotoUri(postValues.get("profilPhotoUri").toString());

                                checkIfUserHaveBook(bookId, user, new DataBaseManager.ResultGetter<Boolean>(){
                                    @Override
                                    public void onResult(Boolean trouv) {
                                        if( trouv == true){
                                            Log.d("user", user.getId());
                                            usersIdList.add(user);
                                            getter.onResult(usersIdList);
                                        }
                                    }
                                });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUserById(String userId, final ResultGetter<User> getter)
    {

        myUsersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                getter.onResult(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUserIsbnBooksList(String userId, final ResultGetter<ArrayList<Book>> getter)
    {
        final ArrayList<Book> bookIsbnList= new ArrayList<>();
        myUsersRef = database.getReference("users").child(userId).child("books");
        myUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot != null){
                    HashMap value = (HashMap)dataSnapshot.getValue();
                    if (value != null){
                        Set cles = value.keySet();
                        Iterator it = cles.iterator();
                        while (it.hasNext() ){
                            String key = (String)it.next();
                            Map<String, Object> postValues = (Map)value.get(key);
                            Book book = new Book();
                            book.setId(postValues.get("isbn_13").toString());
                            book.setSoumissionDate(postValues.get("soumission_date").toString());
                            bookIsbnList.add(book);
                        }
                        getter.onResult(bookIsbnList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAllBooks( final ResultGetter<ArrayList<Book>> getter)
    {
        myUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap value = (HashMap)dataSnapshot.getValue();
                Set cles = value.keySet();
                Iterator it = cles.iterator();
                final ArrayList<Book>[] resultBooksList =(ArrayList<Book>[])new ArrayList[1];
                resultBooksList[0] = new ArrayList<>();
                while (it.hasNext() ){

                    getUserIsbnBooksList((String)it.next(), new DataBaseManager.ResultGetter<ArrayList<Book>>() {
                                @Override
                                public void onResult(ArrayList<Book> booksList) {
                                    resultBooksList[0] = addListToAnother(resultBooksList[0], booksList);
                                }
                    });
                }
                getter.onResult(resultBooksList[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deleteBookFromUser(Book book) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(firebaseUser.getUid().toString()).child("books").child(securityManager.md5Hash(book.getId())).removeValue();
    }

    public void checkIfUserHaveBook(final String isbnBook, final User user, final ResultGetter<Boolean> getter){

        myUsersRef = database.getReference("users").child(user.getId()).child("books");
        myUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot != null){
                    HashMap value = (HashMap)dataSnapshot.getValue();
                    if (value != null){
                        Set cles = value.keySet();
                        Iterator it = cles.iterator();
                        Boolean trouv = false;
                        while (it.hasNext() & !trouv){
                            String key = (String)it.next();
                            Map<String, Object> postValues = (Map)value.get(key);
                            if(isbnBook.equals(postValues.get("isbn_13").toString())){
                                Log.d("comparaison : ", isbnBook+" "+postValues.get("isbn_13").toString());
                                trouv = true ;
                            }
                        }
                        getter.onResult(trouv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public interface ResultGetter<T> {
        void onResult(T t);
    }

    public ArrayList<Book> addListToAnother(ArrayList<Book> globalList, ArrayList<Book> listToAdd)
    {
        ArrayList<Book> result = globalList;
        Iterator<Book> it = listToAdd.iterator();
        while (it.hasNext()) {

            Book book = it.next();
            if (!result.contains(book)) {
                result.add(book);
            }
        }
        return result;
    }
}
