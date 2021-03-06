package com.nirajsarode.heal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DocPatientPrescriptionsItemsActivity extends AppCompatActivity {


    ListView mPrescItemListView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;

    List<PrescriptionItem> prescitemlist;

    String mPresId;
    String mUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_items_user);


        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mPresId = getIntent().getExtras().get("PresId").toString();
        mUserID = getIntent().getExtras().get("UId").toString();

        mPrescItemListView = (ListView) findViewById(R.id.prescitems);
        prescitemlist = new ArrayList<PrescriptionItem>();
        final String user_id = mAuth.getCurrentUser().getUid();
        String uEmail = mAuth.getCurrentUser().getEmail();

        final TextView dateTV = (TextView) findViewById(R.id.pescdate);

        final TextView descTV = (TextView) findViewById(R.id.pescdesc);

        final TextView docTV = (TextView) findViewById(R.id.pescdoc);

        final TextView locTV = (TextView) findViewById(R.id.pescloc);

        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mPresId = getIntent().getExtras().get("PresId").toString();
        mPrescItemListView = (ListView) findViewById(R.id.prescitems);
        prescitemlist = new ArrayList<PrescriptionItem>();;
        mFireStore.collection("patients").document(mUserID).collection("prescriptions").document(mPresId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String date = (String) documentSnapshot.get("date");
                String des = (String) documentSnapshot.get("description");
                String doc = (String) documentSnapshot.get("doctor");
                String loc = (String) documentSnapshot.get("location");

                descTV.setText(des);
                docTV.setText(doc);
                locTV.setText(loc);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPrescItemListView.setAdapter(null);
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
        prescitemlist = new ArrayList<PrescriptionItem>();
        final String user_id = mAuth.getCurrentUser().getUid();
        String uEmail = mAuth.getCurrentUser().getEmail();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mFireStore.collection("patients").document(mUserID).collection("prescriptions").document(mPresId).collection("medicines").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if(!documentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = documentSnapshots.getDocuments();
                    for (DocumentSnapshot d: list){
                        PrescriptionItem a = d.toObject(PrescriptionItem.class);
                        prescitemlist.add(a);
                    }

                    PrescriptionItemList adapter = new PrescriptionItemList(DocPatientPrescriptionsItemsActivity.this,prescitemlist);
                    mPrescItemListView.setAdapter(adapter);

                }
            }
        });


    }
}
