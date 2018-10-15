package controllers;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import classes.User;

public class DataBaseManager {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser ;
    private FirebaseDatabase database ;
    private DatabaseReference myUsersRef ;
    JsonUtil jsonUtil = new JsonUtil();
    User user ;

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

    public void getUserById(final ResultGetter<User> getter)
    {

        myUsersRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                        user.setName( user.getName().toString());
                        user.setSurname(user.getSurname().toString());
                        user.setAge(user.getAge().toString());
                        user.setMailAdress(user.getMailAdress().toString());
                        user.setPassword(user.getPassword().toString());
                        getter.onResult(user);
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getUsersList(final ResultGetter<User> getter)
    {

        myUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean trouv = false;
                HashMap value = (HashMap)dataSnapshot.getValue();
                Set cles = value.keySet();
                Iterator it = cles.iterator();

                while (it.hasNext() & !trouv){
                    String key = (String)it.next();
                    Map<String, Object> postValues = (Map)value.get(key);

                    if(key.equals(firebaseUser.getUid().toString())){

                        user = new User("","","","","","","","");
                        user.setName( postValues.get("name").toString());
                        user.setSurname(postValues.get("surname").toString());
                        user.setAge(postValues.get("age").toString());
                        user.setMailAdress(postValues.get("mailAdress").toString());
                        user.setPassword(postValues.get("password").toString());

                        getter.onResult(user);
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
}
