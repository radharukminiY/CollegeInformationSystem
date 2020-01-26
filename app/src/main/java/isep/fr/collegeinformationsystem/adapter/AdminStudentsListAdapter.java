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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.util.ArrayList;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceRequest.DeleteStudentService;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.activity.admin.AdminStudentUpdateActivity;
import isep.fr.collegeinformationsystem.interfaces.CircleTransform;
import isep.fr.collegeinformationsystem.interfaces.ItemClickListener;
import isep.fr.collegeinformationsystem.model.StudentModel;

public class AdminStudentsListAdapter extends RecyclerView.Adapter<AdminStudentsListAdapter.MyViewHolder> {

    private static final String TAG = "AdminStudentsList";

    private ArrayList<StudentModel> userListData;
    private Context context;

    public AdminStudentsListAdapter(Context context, ArrayList<StudentModel> userListData) {

        this.context = context;
        this.userListData = userListData;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_card_layout, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        holder.usrName.setText(userListData.get(position).getUserName());
        holder.usrMail.setText(userListData.get(position).getUserMail());
        holder.usrCourse.setText(userListData.get(position).getUserCourse());
        holder.usrId.setText(userListData.get(position).getUserId());

        String imgUrl = Constants.PROFILE_IMG_URL + userListData.get(position).getUserProfile();

        Log.d(TAG, "user pro url " + imgUrl);

        Glide.with(context)
                .load(imgUrl)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                        .transform(new CircleTransform(context)))
                .into(holder.usrProfile);


        //onclick listner
/*
        holder.usrProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //display profile image in dialoug
                Toast.makeText(context, "profile clicked", Toast.LENGTH_SHORT).show();

            }
        });*/

        holder.usrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StudentModel studentModel = userListData.get(position);

                Intent intent = new Intent(context, AdminStudentUpdateActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Constants.USER_ID, studentModel.getUserId());
                intent.putExtra(Constants.USER_NAME, studentModel.getUserName());
                intent.putExtra(Constants.USER_MAIL, studentModel.getUserMail());
                intent.putExtra(Constants.USER_MOBILE_NUM, studentModel.getUserMobile());
                intent.putExtra(Constants.USER_GENDER, studentModel.getUserGender());
                intent.putExtra(Constants.USER_PASSWORD, studentModel.getUserPass());
                intent.putExtra(Constants.USER_PROFILE, studentModel.getUserProfile());
                intent.putExtra(Constants.USER_DEPT, studentModel.getUserCourse());
                context.startActivity(intent);

            }
        });

        holder.usrDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteStudentService deleteStudentService = new DeleteStudentService(context, userListData.get(position).getUserId(), new deleteStudentResponse(userListData.get(position)));
                deleteStudentService.DeleteStudentService();


            }
        });


    }

    @Override
    public int getItemCount() {
        return userListData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView usrName, usrMail, usrId, usrCourse;
        private ImageView usrProfile, usrEdit, usrDelete;

        ItemClickListener itemClickListener;


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

        /*@Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(view, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }
*/

    }

    private class deleteStudentResponse implements ServerResponseListener {
        StudentModel studentModel;

        public deleteStudentResponse(StudentModel studentModel) {
            this.studentModel = studentModel;
        }

        @Override
        public void onResponseData(Object resultObj) {
            Log.d(TAG, "student responce = " + resultObj.toString());

            try {
                JSONObject object = new JSONObject(resultObj.toString());

                String status = object.getString("status");

                if (status.equals("0")) {
                    userListData.remove(studentModel);
                    notifyDataSetChanged();
                    Toast.makeText(context, "student deleted successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "student deleted failed", Toast.LENGTH_SHORT).show();

                }


            } catch (Exception e) {
                Log.d(TAG, "getting string status  error - " + e.toString());
                e.printStackTrace();
            }


        }
    }

}
