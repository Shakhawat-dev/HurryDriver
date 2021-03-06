package com.hubit.hurrydriver.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hubit.hurrydriver.Constants.constants;
import com.hubit.hurrydriver.Models.modelForCarRequest;
import com.hubit.hurrydriver.R;
import com.hubit.hurrydriver.Viewholders.viewHolderForRunningBId;




public class Activity_On_UnsuccessfulBidList extends AppCompatActivity {
    RecyclerView mrecyclerview  ;
    LinearLayoutManager linearLayoutManager ;
    DatabaseReference mref , checkRef  , reqRef ;

    FirebaseRecyclerOptions<modelForCarRequest> options ;
    FirebaseRecyclerAdapter<modelForCarRequest, viewHolderForRunningBId> firebaseRecyclerAdapter ;
    String uid = "TEST" , stauts = null  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__on__unsuccessful_bid_list);
        uid  = FirebaseAuth.getInstance().getUid() ;


        mref = FirebaseDatabase.getInstance().getReference(constants.driverProfileLink).child(uid).child(constants.driverBidDir); // db link
        checkRef = FirebaseDatabase.getInstance().getReference(constants.carRequestLink);
        reqRef = FirebaseDatabase.getInstance().getReference(constants.carRequestLink);



        mrecyclerview = findViewById(R.id.recyclerViewUnsuccessFulBidList) ;

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());


        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        mrecyclerview.setLayoutManager(linearLayoutManager) ;
        mrecyclerview.setHasFixedSize(true);

        mref.keepSynced(true);

        loadDataToFireBase()  ;




    }
    private void loadDataToFireBase() {

        Query firebaseSearchQuery = reqRef.orderByChild("status").startAt("Driver Found").endAt("Driver Found" + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<modelForCarRequest>().setQuery(firebaseSearchQuery , modelForCarRequest.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<modelForCarRequest, viewHolderForRunningBId>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final viewHolderForRunningBId viewholdersForCurrentTrip, final int i, @NonNull final modelForCarRequest model) {


                String postid  = getItem(viewholdersForCurrentTrip.getAdapterPosition()).getPostId();



                checkRef.child(postid).child("bids").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(uid)){



                            viewholdersForCurrentTrip.setData(getApplicationContext() ,
                                    model.getPostId() , model.getUserId()  ,model.getUserNotificationID()   , model.getDriverId()  , model.getDriverNotificationID() ,
                                    model.getToLoc() , model.getFromLoc() ,  model.getTimeDate() , model.getCarModl() , model.getDriverName() ,
                                    model.getStatus()  , model.getCarLicNum() , model.getFare() , model.getCarType() ,
                                    model.getReqDate() , model.getTripDetails() , model.getReturnTimee() , model.getNumOfPpl() ,model.getRideType() );


                        }
                        else {


                            // viewholdersForCurrentTrip.cardView.setVisibility(View.GONE);

                            viewholdersForCurrentTrip.setData(getApplicationContext() ,
                                    "TEST" , "TEST"   ,"TEST"    , "TEST"   , "TEST"  ,
                                    "TEST" , "TEST"  ,  "TEST" ,"TEST"  , "TEST" ,
                                    "TEST"   ,"TEST" , "TEST"  , "TEST"  ,
                                    "TEST" , "TEST"  , "TEST" , "TEST"  ,"TEST"  );



                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




/*

                checkRef.child("-LkY6rB33_rXkZiC5Kpa").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            modelForCarRequest modelForCarRequest = dataSnapshot.getValue(modelForCarRequest.class) ;

                            stauts = modelForCarRequest.getStatus() ;



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    if ( stauts.contains("Bid Found")){

                       //TODO set The Data to the  viewholder

                        viewholdersForCurrentTrip.setDataToView(getApplicationContext() ,
                                model.getPostId() , model.getUserId()  ,model.getUserNotificationID()   , model.getDriverId()  , model.getDriverNotificationID() ,
                                model.getToLoc() , model.getFromLoc() ,  model.getTimeDate() , model.getCarModl() , model.getDriverName() ,
                                model.getStatus()  , model.getCarLicNum() , model.getFare() , model.getCarType() ,
                                model.getReqDate() , model.getTripDetails() , model.getReturnTimee() , model.getNumOfPpl() ,model.getRideType() );



                    }
                    else {

                        // TODO hide the card view


                    }





*/






            }

            @NonNull
            @Override
            public viewHolderForRunningBId onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View iteamVIew = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_trip_view_module, parent, false);
                final viewHolderForRunningBId viewholders = new viewHolderForRunningBId(iteamVIew);






                return viewholders;
            }
        };
        mrecyclerview.setLayoutManager(linearLayoutManager) ;
        firebaseRecyclerAdapter.startListening();
        mrecyclerview.setAdapter(firebaseRecyclerAdapter);






    }
}
