package com.hubit.hurrydriver.Viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hubit.hurrydriver.R;


public class viewHolderForRunningBId extends RecyclerView.ViewHolder {
    public View mview;

    public TextView statusTv;
    public CardView cardView;


    public viewHolderForRunningBId(@NonNull View itemView) {

        super(itemView);

        mview = itemView;
        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mclicklistener.onItemClick(v , getAdapterPosition());

            }
        });


    }

    private  static  viewHolderForRunningTrip.Clicklistener mclicklistener ;

    public void setData(Context context, String postId, String userId, String userNotificationID, String driverId, String driverNotificationID,
                        String toLoc, String fromLoc, String timeDate, String carModl, String driverName,
                        String status, String carLicNum, String fare, String carType,
                        String reqDate, String tripDetails, String returntime, String numOfppl, String rideType) {


        TextView dateView = mview.findViewById(R.id.dateOfRows);
        //  TextView fareView = mview.findViewById(R.id.fareRow);


        TextView locaTo = mview.findViewById(R.id.locationTos);
        TextView locaFrom = mview.findViewById(R.id.locationFroms);
        TextView statusTv = mview.findViewById(R.id.statusRows);
        cardView = mview.findViewById(R.id.tripModule_card);


        dateView.setText(timeDate);
        //    fareView.setText(fare);
        locaTo.setText(toLoc);
        locaFrom.setText(fromLoc);
        statusTv.setText(status);


    }

    public   interface  Clicklistener {

        void onItemClick( View view , int postion );

    }

    public  static  void  setOnClickListener (viewHolderForRunningTrip.Clicklistener clickListener ){


        mclicklistener = clickListener ;


    }


}
