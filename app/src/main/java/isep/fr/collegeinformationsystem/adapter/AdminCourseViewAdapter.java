package isep.fr.collegeinformationsystem.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.interfaces.ItemClickListener;
import isep.fr.collegeinformationsystem.model.AdminCourseModel;

public class AdminCourseViewAdapter extends RecyclerView.Adapter<AdminCourseViewAdapter.MyViewHolder> {

    private static final String TAG = "AdminStudentsListAdapter";

    private ArrayList<AdminCourseModel> courseListData;
    private Context context;

    public AdminCourseViewAdapter(Context context, ArrayList<AdminCourseModel> courseListData) {

        this.context = context;
        this.courseListData = courseListData;

    }


    @NonNull
    @Override
    public AdminCourseViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_course_card_layout, viewGroup, false);

        return new AdminCourseViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCourseViewAdapter.MyViewHolder holder, int position) {


        holder.usrName.setText(courseListData.get(position).getCourseShortName());
        holder.usrMail.setText(courseListData.get(position).getSubjects());
        holder.usrCourse.setText(courseListData.get(position).getCourseName());
        holder.usrId.setText(String.valueOf(position+1));

         holder.usrEdit.setVisibility(View.GONE);
        holder.usrDelete.setVisibility(View.GONE);


        /*holder.usrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call service to update user data
                Toast.makeText(context, "Edit clicked", Toast.LENGTH_SHORT).show();

            }
        });

        holder.usrDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call service to delete uer data
                Toast.makeText(context, "Delete clicked", Toast.LENGTH_SHORT).show();

            }
        });*/



    }

    public AdminCourseModel getItem(int position) {

        AdminCourseModel adminCourseModel = courseListData.get(position);
        return adminCourseModel;
    }


    @Override
    public int getItemCount() {
        return courseListData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private TextView usrName, usrMail, usrId,usrCourse;
        private ImageView usrProfile, usrEdit, usrDelete;

        ItemClickListener itemClickListener;


        public MyViewHolder(View itemView) {
            super(itemView);

            usrProfile = (ImageView) itemView.findViewById(R.id.imv_usr_pfl_img);
            usrName = (TextView) itemView.findViewById(R.id.tv_usr_name);
            usrMail = (TextView) itemView.findViewById(R.id.tv_user_mail);
            usrId = (TextView) itemView.findViewById(R.id.tv_cour_id);
            usrCourse = (TextView) itemView.findViewById(R.id.tv_user_course);


            //action
            usrEdit = (ImageView) itemView.findViewById(R.id.imv_user_edit);
            usrDelete = (ImageView) itemView.findViewById(R.id.imv_user_delete);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        /*@Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(view, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }
*/

    }

}
