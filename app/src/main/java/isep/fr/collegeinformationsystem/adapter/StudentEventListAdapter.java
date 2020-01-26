package isep.fr.collegeinformationsystem.adapter;

import android.content.Context;
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
import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.model.EventsModel;

import java.util.ArrayList;

public class StudentEventListAdapter extends RecyclerView.Adapter<StudentEventListAdapter.MyViewHolder>{
    private static final String TAG = "AdminStudentsListAdapter";

    private ArrayList<EventsModel> eventListData;
    private Context context;

    public StudentEventListAdapter(Context context, ArrayList<EventsModel> eventListData) {

        this.context = context;
        this.eventListData = eventListData;

    }


    @NonNull
    @Override
    public StudentEventListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_card_layout, viewGroup, false);

        return new StudentEventListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentEventListAdapter.MyViewHolder holder, int position) {


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

        holder.eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //display profile image in dialoug
                Toast.makeText(context, "profile clicked", Toast.LENGTH_SHORT).show();

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

            eventEdit.setVisibility(View.GONE);
            eventDelete.setVisibility(View.GONE);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }


    }

}