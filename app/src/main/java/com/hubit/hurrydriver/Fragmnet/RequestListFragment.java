package com.hubit.hurrydriver.Fragmnet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hubit.hurrydriver.Activity.Activity_Bid_Page_Driver;
import com.hubit.hurrydriver.Constants.constants;
import com.hubit.hurrydriver.Models.modelForCarRequest;
import com.hubit.hurrydriver.R;
import com.hubit.hurrydriver.Viewholders.viewholderForReqList;

public class RequestListFragment extends Fragment {

    RecyclerView mrecyclerview;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference mref;

    FirebaseRecyclerOptions<modelForCarRequest> options;
    FirebaseRecyclerAdapter<modelForCarRequest, viewholderForReqList> firebaseRecyclerAdapter;

    View view;
    String uid = FirebaseAuth.getInstance().getUid();


    public RequestListFragment() {


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_request_list, container, false);

        mref = FirebaseDatabase.getInstance().getReference(constants.carRequestLink); // db link

        mref.keepSynced(true);

        mrecyclerview = view.findViewById(R.id.recyclerViewOnFramentRequestList);

        linearLayoutManager = new LinearLayoutManager(getContext());


        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        mrecyclerview.setLayoutManager(linearLayoutManager);
        mrecyclerview.setHasFixedSize(true);


        loadDataToFireBase();

        return view;

    }

    private void loadDataToFireBase() {

        options = new FirebaseRecyclerOptions.Builder<modelForCarRequest>().setQuery(mref, modelForCarRequest.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<modelForCarRequest, viewholderForReqList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewholderForReqList viewholdersForCurrentTrip, final int i, @NonNull modelForCarRequest model) {


                viewholdersForCurrentTrip.setDataToView(getContext(),
                        model.getPostId(), model.getUserId(), model.getUserNotificationID(), model.getDriverId(), model.getDriverNotificationID(),
                        model.getToLoc(), model.getFromLoc(), model.getTimeDate(), model.getCarModl(), model.getDriverName(),
                        model.getStatus(), model.getCarLicNum(), model.getFare(), model.getCarType(),
                        model.getReqDate(), model.getTripDetails(), model.getReturnTimee(), model.getNumOfPpl(), model.getRideType());

                if (model.getStatus().toLowerCase().equals("completed")
                        || model.getStatus().toLowerCase().contains("by user")
                        || model.getStatus().toLowerCase().contains("accepted")) {

                    viewholdersForCurrentTrip.itemView.setVisibility(View.GONE);
                    viewholdersForCurrentTrip.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                } else {

                    viewholdersForCurrentTrip.itemView.setVisibility(View.VISIBLE);
                    viewholdersForCurrentTrip.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                }

            }

            @NonNull
            @Override
            public viewholderForReqList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View iteamVIew = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_trip_view_module, parent, false);
                final viewholderForReqList viewholders = new viewholderForReqList(iteamVIew);


                viewholderForReqList.setOnClickListener(new viewholderForReqList.Clicklistener() {
                    @Override
                    public void onItemClick(View view, final int postion) {

                        final String DriverName = getItem(postion).getDriverName();
                        final String Status = getItem(postion).getStatus();
                        final String PostID = getItem(postion).getPostId();
                        final String userNottificaionId = getItem(postion).getUserNotificationID();
                        final String timeOfTrip = getItem(postion).getTimeDate();
                        final String fromLoc = getItem(postion).getFromLoc();
                        final String toLoc = getItem(postion).getToLoc();
                        final String numberOfppl = getItem(postion).getNumOfPpl();
                        final String description = getItem(postion).getTripDetails();
                        final String returnDate = getItem(postion).getReturnTimee();
                        final String rideType = getItem(postion).getRideType();


                        // you have to check for driver has already bid on this or not
                        // if he didnt then   give him access

                        mref.child(PostID).child("bids").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                if (!dataSnapshot.hasChild(uid)) {
                                    Intent io = new Intent(getContext(), Activity_Bid_Page_Driver.class);

                                    io.putExtra("drivername", DriverName);
                                    io.putExtra("status", Status);
                                    io.putExtra("postid", PostID);
                                    io.putExtra("usernottificationid", userNottificaionId);
                                    io.putExtra("timeoftrip", timeOfTrip);
                                    io.putExtra("fromloc", fromLoc);
                                    io.putExtra("toloc", toLoc);
                                    io.putExtra("numberofppl", numberOfppl);
                                    io.putExtra("des", description);
                                    io.putExtra("returndate", returnDate);
                                    io.putExtra("ridetype", rideType);
                                    io.putExtra("model", getItem(postion));

                                    startActivity(io);


                                } else {

                                    Log.d("TAG", "onDataChange: " + uid);
                                    Toast.makeText(getContext(), "Sorry You Already Has Bidded On This", Toast.LENGTH_SHORT)
                                            .show();


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });


                return viewholders;
            }
        };
        mrecyclerview.setLayoutManager(linearLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mrecyclerview.setAdapter(firebaseRecyclerAdapter);


    }
}
