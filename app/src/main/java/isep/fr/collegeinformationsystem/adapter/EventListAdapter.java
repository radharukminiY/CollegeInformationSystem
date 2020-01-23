package isep.fr.collegeinformationsystem.adapter;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


import org.json.JSONObject;

import java.util.ArrayList;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceRequest.DeleteEventService;
import isep.fr.collegeinformationsystem.WebServiceUtil.ConnectivityReceiver;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.activity.admin.AdminEventUpdateActivity;
import isep.fr.collegeinformationsystem.model.EventsModel;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder>{
    private static final String TAG = "EventListAdapter";

    private ArrayList<EventsModel> eventListData;
    private Context context;

    public EventListAdapter(Context context, ArrayList<EventsModel> eventListData) {

        this.context = context;
        this.eventListData = eventListData;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_card_layout, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        holder.eventTitle.setText(eventListData.get(position).getEventTitle());
        holder.eventDiscreet.setText(eventListData.get(position).getEventDescription());
        holder.eventHall.setText(eventListData.get(position).getEventHall());
        holder.eventDate.setText(eventListData.get(position).getEventDateTime());

        String url = Constants.EVENT_IMG_URL+eventListData.get(position).getEventImage();


        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).centerCrop())
                .into(holder.eventImage);

        //onclick listner

       /* holder.eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //display profile image in dialoug
                Toast.makeText(context, "profile clicked", Toast.LENGTH_SHORT).show();

            }
        });*/

        holder.eventEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EventsModel eventsModel = eventListData.get(position);

                Intent intent = new Intent(context, AdminEventUpdateActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra(Constants.EVENT_ID, eventsModel.getEventId());
                intent.putExtra(Constants.EVENT_TITLE, eventsModel.getEventTitle());
                intent.putExtra(Constants.EVENT_DATE_TIME, eventsModel.getEventDateTime());
                intent.putExtra(Constants.EVENT_PLACE, eventsModel.getEventAddress());
                intent.putExtra(Constants.EVENT_HALL, eventsModel.getEventHall());
                intent.putExtra(Constants.EVENT_TYPE, eventsModel.getEventType());
                intent.putExtra(Constants.EVENT_DISCRIPTION, eventsModel.getEventDescription());
                intent.putExtra(Constants.EVENT_IMAGE, eventsModel.getEventImage());
                context.startActivity(intent);

                //call service to update user data
                Toast.makeText(context, "Edit clicked", Toast.LENGTH_SHORT).show();

            }
        });

        holder.eventDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ConnectivityReceiver.isConnected()){

                    EventsModel eventsModel = eventListData.get(position);


                    DeleteEventService deleteEventService = new DeleteEventService(context,eventsModel.getEventId(),new deleteEventResponse(eventsModel));
                    deleteEventService.DeleteEventService();

                }else{
                    Toast.makeText(context,"Please Check your Network ",Toast.LENGTH_SHORT).show();
                }

/*
                if(ConnectivityReceiver.isConnected()){

                }else{
                    Toast.makeText(context,"Please Check your Network ",Toast.LENGTH_SHORT).show();
                }*/





                //call service to delete uer data
                Toast.makeText(context, "Delete clicked", Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public int getItemCount() {
        return eventListData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private TextView eventTitle, eventDiscreet, eventHall,eventDate;
        private ImageView eventImage, eventEdit, eventDelete;


        public MyViewHolder(View itemView) {
            super(itemView);

            eventImage = (ImageView) itemView.findViewById(R.id.imv_event_image);

            eventTitle = (TextView) itemView.findViewById(R.id.tv_event_title);
            eventDiscreet = (TextView) itemView.findViewById(R.id.tv_event_description);
            eventHall = (TextView) itemView.findViewById(R.id.tv_event_address);
            eventDate = (TextView) itemView.findViewById(R.id.tv_event_date);


            //action
            eventEdit = (ImageView) itemView.findViewById(R.id.imv_user_edit);
            eventDelete = (ImageView) itemView.findViewById(R.id.imv_user_delete);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }


    }

    private class deleteEventResponse implements ServerResponseListener {
        EventsModel eventsModel;

        public deleteEventResponse(EventsModel eventsModel) {
            this.eventsModel = eventsModel;
        }

        @Override
        public void onResponseData(Object resultObj) {
            Log.d(TAG, "event delete response = " + resultObj.toString());

            try {
                JSONObject object = new JSONObject(resultObj.toString());

                String status = object.getString("status");


                if (status.equals("0")) {
                    //progressDoalog.dismiss();
                    Log.d(TAG, "event deleted successfully ");

                    eventListData.remove(eventsModel);
                    notifyDataSetChanged();
                    Toast.makeText(context, "department deleted successfully", Toast.LENGTH_SHORT).show();


                } else {
                    //progressDoalog.dismiss();
                    Log.d(TAG, "event deleted failed ");
                    Toast.makeText(context, "event deleted have problem", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                //progressDoalog.dismiss();
                Log.d(TAG, "getting string status  error - " + e.toString());
                e.printStackTrace();
            }


        }
    }

}
