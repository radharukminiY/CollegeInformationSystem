package isep.fr.collegeinformationsystem.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceRequest.DeleteDepartmentService;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.activity.admin.AdminDeptUpdateActivity;
import isep.fr.collegeinformationsystem.activity.admin.AdminProfessorActivity;
import isep.fr.collegeinformationsystem.model.AdminCourseModel;

import org.json.JSONObject;

import java.util.ArrayList;

public class AdminDepartViewAdapter extends RecyclerView.Adapter<AdminDepartViewAdapter.MyViewHolder> {

    private static final String TAG = "AdminDepartViewAdapter";

    private ArrayList<AdminCourseModel> courseListData;
    private Context context;


    public AdminDepartViewAdapter(Context context, ArrayList<AdminCourseModel> courseListData) {

        this.context = context;
        this.courseListData = courseListData;

    }


    @NonNull
    @Override
    public AdminDepartViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_course_card_layout, viewGroup, false);

        return new AdminDepartViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminDepartViewAdapter.MyViewHolder holder, final int position) {


        holder.usrName.setText(courseListData.get(position).getCourseShortName());
        holder.usrMail.setText(courseListData.get(position).getSubjects());
        holder.usrCourse.setText(courseListData.get(position).getCourseName());
        holder.usrId.setText(String.valueOf(position + 1));

       /* holder.usrEdit.setVisibility(View.GONE);
        holder.usrDelete.setVisibility(View.GONE);*/

        holder.usrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call service to update user data

                AdminCourseModel adminCourseModel = courseListData.get(position);

                Intent intent = new Intent(context, AdminDeptUpdateActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Constants.DEPT_ID, adminCourseModel.getCourseId());
                intent.putExtra(Constants.DEPT_NAME, adminCourseModel.getCourseName());
                intent.putExtra(Constants.DEPT_SHORT_NAME, adminCourseModel.getCourseShortName());
                intent.putExtra(Constants.DEPT_SUBJECTS, adminCourseModel.getSubjects());
                intent.putExtra(Constants.DEPT_SYLL_DOC, adminCourseModel.getCoursePdfFile());
                context.startActivity(intent);



            }
        });

        holder.usrDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteDepartmentService deleteDepartmentService = new DeleteDepartmentService(context, courseListData.get(position).getCourseId(), new deleteDeptResponse(courseListData.get(position)));
                deleteDepartmentService.DeleteDepartmentService();
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminCourseModel adminCourseModel = courseListData.get(position);

                String deptShortNmae = null;
                deptShortNmae = adminCourseModel.getCourseShortName();

                Intent intent = new Intent(context, AdminProfessorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Constants.DEPT_SHORT_NAME, deptShortNmae);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return courseListData.size();
    }


    public AdminCourseModel getItem(int position) {

        AdminCourseModel adminCourseModel = courseListData.get(position);
        return adminCourseModel;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView usrName, usrMail, usrId, usrCourse;
        private ImageView usrProfile, usrEdit, usrDelete;
        private RelativeLayout relativeLayout;


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
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.ll_user_parent);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }


    }

    private class deleteDeptResponse implements ServerResponseListener {
        AdminCourseModel courseModels;

        public deleteDeptResponse(AdminCourseModel courseModels) {
            this.courseModels = courseModels;
        }

        @Override
        public void onResponseData(Object resultObj) {
            Log.d(TAG, "user responce = " + resultObj.toString());

            try {
                JSONObject object = new JSONObject(resultObj.toString());

                String status = object.getString("status");


                if (status.equals("0")) {
                    //progressDoalog.dismiss();
                    Log.d(TAG, "dept deleted successfully ");

                    courseListData.remove(courseModels);
                    notifyDataSetChanged();
                    Toast.makeText(context, "department deleted successfully", Toast.LENGTH_SHORT).show();


                } else {
                    //progressDoalog.dismiss();
                    Log.d(TAG, "dept deleted failed ");
                    Toast.makeText(context, "department deleted have problem", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                //progressDoalog.dismiss();
                Log.d(TAG, "getting string status  error - " + e.toString());
                e.printStackTrace();
            }


        }
    }

}

