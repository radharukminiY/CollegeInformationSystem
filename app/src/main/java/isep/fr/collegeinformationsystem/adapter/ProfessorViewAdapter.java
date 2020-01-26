package isep.fr.collegeinformationsystem.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceRequest.DeleteStaffService;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.activity.admin.AdminStaffUpdateActivity;
import isep.fr.collegeinformationsystem.model.ProfessorModel;

public class ProfessorViewAdapter extends RecyclerView.Adapter<ProfessorViewAdapter.MyViewHolder> {

    private static final String TAG = "AdminStudentsList";

    private ArrayList<ProfessorModel> professorModels;
    private Context context;

    public ProfessorViewAdapter(Context context, ArrayList<ProfessorModel> professorModels) {

        this.context = context;
        this.professorModels = professorModels;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.professor_card_layout, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        holder.usrName.setText(professorModels.get(position).getProfName());
        holder.usrMail.setText(professorModels.get(position).getProfMail());
        holder.usrCourse.setText(professorModels.get(position).getProfCourse());
        holder.usrId.setText(professorModels.get(position).getProfId());


        //onclick listner

       /* holder.usrProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //display profile image in dialoug
                Toast.makeText(context, "profile clicked", Toast.LENGTH_SHORT).show();

            }
        });*/

        holder.usrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfessorModel professorModel = professorModels.get(position);

                Intent intent = new Intent(context, AdminStaffUpdateActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra(Constants.STAFF_ID, professorModel.getProfId());
                intent.putExtra(Constants.STAFF_NAME, professorModel.getProfName());
                intent.putExtra(Constants.STAFF_MOBILE_NUM, professorModel.getProfMobile());
                intent.putExtra(Constants.STAFF_MAIL_ID, professorModel.getProfMail());
                intent.putExtra(Constants.STAFF_GENDER, professorModel.getProfGender());
                intent.putExtra(Constants.STAFF_DEPT, professorModel.getProfCourse());
                intent.putExtra(Constants.STAFF_DEAL_SUB, professorModel.getProfSubject());
                intent.putExtra(Constants.STAFF_QUALIFICATION, professorModel.getProfQualify());
                intent.putExtra(Constants.STAFF_PROFILE, professorModel.getProfProfile());
                context.startActivity(intent);

            }
        });

        holder.usrDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfessorModel professorModel = professorModels.get(position);

                DeleteStaffService deleteStaffService = new DeleteStaffService(context, professorModel.getProfId(), new deleteStaffResponse(professorModel));
                deleteStaffService.DeleteStaffService();


            }
        });


    }

    @Override
    public int getItemCount() {
        return professorModels.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ProfessorModel getItem(int pos) {

        return professorModels.get(pos);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView usrName, usrMail, usrId, usrCourse;
        private ImageView usrProfile, usrEdit, usrDelete;


        public MyViewHolder(View itemView) {
            super(itemView);

            usrProfile = (ImageView) itemView.findViewById(R.id.imv_usr_pfl_img);
            usrName = (TextView) itemView.findViewById(R.id.tv_usr_name);
            usrMail = (TextView) itemView.findViewById(R.id.tv_user_mail);
            usrId = (TextView) itemView.findViewById(R.id.tv_user_id);
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


    }

    private class deleteStaffResponse implements ServerResponseListener {
        ProfessorModel staffModels;

        public deleteStaffResponse(ProfessorModel staffModels) {
            this.staffModels = staffModels;
        }

        @Override
        public void onResponseData(Object resultObj) {
            Log.d(TAG, "staff response = " + resultObj.toString());

            try {
                JSONObject object = new JSONObject(resultObj.toString());

                String status = object.getString("status");


                if (status.equals("0")) {
                    //progressDoalog.dismiss();
                    Log.d(TAG, "dept deleted successfully ");

                    professorModels.remove(staffModels);
                    notifyDataSetChanged();
                    Toast.makeText(context, "staff deleted successfully", Toast.LENGTH_SHORT).show();


                } else {
                    //progressDoalog.dismiss();
                    Log.d(TAG, "staff deleted failed ");
                    Toast.makeText(context, "staff deleted have problem", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                //progressDoalog.dismiss();
                Log.d(TAG, "getting string status  error - " + e.toString());
                e.printStackTrace();
            }


        }
    }

}
