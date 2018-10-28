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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import activities.MainActivity;
import classes.Book;
import classes.User;

public class DataBaseManager {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser ;
    private FirebaseDatabase database ;
    private DatabaseReference myUsersRef ;
    JsonUtil jsonUtil = new JsonUtil();
    User user ;
    SecurityManager securityManager = new SecurityManager();

    public DataBaseManager(){
        mAuth= FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myUsersRef = database.getReference("users");
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

        Map<String, Object> postValues = book.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/users/"+userId+"/books/" + securityManager.md5Hash(book.getId()), postValues);
        mDatabase.updateChildren(childUpdates);
    }


    public void getUserById(final ResultGetter<User> getter)
    {

        myUsersRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if(user.getName() != null)
                    user.setName( user.getName().toString());
                if(user.getSurname() != null)
                    user.setSurname(user.getSurname().toString());
                if(user.getAge() != null)
                    user.setAge(user.getAge().toString());
                if(user.getProfilPhotoUri() != null)
                    user.setProfilPhotoUri(user.getProfilPhotoUri().toString());
                user.setMailAdress(user.getMailAdress().toString());
                user.setPassword(user.getPassword().toString());
                getter.onResult(user);

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getUsersList(final ResultGetter<ArrayList<String>> getter)
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


    public void deleteBookFromUser(Book book) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(firebaseUser.getUid().toString()).child("books").child(securityManager.md5Hash(book.getId())).removeValue();
    }
    
    public interface ResultGetter<T> {
        void onResult(T t);
    }
}
