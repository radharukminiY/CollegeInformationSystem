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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceRequest.DeleteDepartmentService;
import isep.fr.collegeinformationsystem.WebServiceRequest.DeleteStaffService;
import isep.fr.collegeinformationsystem.WebServiceRequest.DeleteStudentService;
import isep.fr.collegeinformationsystem.WebServiceRequest.GetAllStaffInfoService;
import isep.fr.collegeinformationsystem.WebServiceRequest.GetAllStudentInfoService;
import isep.fr.collegeinformationsystem.WebServiceUtil.ConnectivityReceiver;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.activity.admin.AdminDepartmentViewActivity;
import isep.fr.collegeinformationsystem.activity.admin.AdminDeptUpdateActivity;
import isep.fr.collegeinformationsystem.activity.admin.AdminProfessorActivity;
import isep.fr.collegeinformationsystem.model.AdminCourseModel;

public class AdminDepartViewAdapter extends RecyclerView.Adapter<AdminDepartViewAdapter.MyViewHolder> {

    private static final String TAG = "AdminDepartViewAdapter";

    private ArrayList<AdminCourseModel> courseListData;
    private Context context;
    ArrayList<String> staffeId;
    ArrayList<String> studentId;

    public AdminDepartViewAdapter(Context context, ArrayList<AdminCourseModel> courseListData) {

        this.context = context;
        this.courseListData = courseListData;
        this.staffeId = new ArrayList<>();
        this.studentId = new ArrayList<>();

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

                if (ConnectivityReceiver.isConnected()) {

                    staffeId.clear();
                    studentId.clear();
                    AdminDepartmentViewActivity.showProgress(context);

                    DeleteDepartmentService deleteDepartmentService = new DeleteDepartmentService(context, courseListData.get(position).getCourseId(), new deleteDeptResponse(courseListData.get(position)));
                    deleteDepartmentService.DeleteDepartmentService();
                } else {
                    Toast.makeText(context, "please check your network ", Toast.LENGTH_SHORT).show();
                }


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


                    getAllStaffAndDelete(courseModels.getCourseShortName());
                    getAllStudentData(courseModels.getCourseShortName());

                    for (int i = 0; i < staffeId.size(); i++) {
                        clearAllStaffData(staffeId.get(i));
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            System.out.println(e);
                        }
                    }

                    for (int i = 0; i < studentId.size(); i++) {
                        clearAllStudentData(studentId.get(i));
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            System.out.println(e);
                        }

                    }

                    AdminDepartmentViewActivity.hideProgress();

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

        private void getAllStaffAndDelete(String deptShortName) {
            GetAllStaffInfoService getAllStaffInfoService = new GetAllStaffInfoService(context, "", new getAllStaffDataResponce(deptShortName));
            getAllStaffInfoService.GetAllStaffInfoService();
        }

        private void getAllStudentData(String deptShortName) {
            GetAllStudentInfoService getAllStuInfoService = new GetAllStudentInfoService(context, "", new getAllStudentResponce(deptShortName));
            getAllStuInfoService.GetAllStudentInfoService();
        }

        private void clearAllStaffData(String staffId) {
            DeleteStaffService deleteStaffService = new DeleteStaffService(context, staffId, new deleteStaffResponse());
            deleteStaffService.DeleteStaffService();
        }

        private void clearAllStudentData(String studentId) {
            DeleteStudentService getAllStuInfoService = new DeleteStudentService(context, studentId, new deleteStudentResponse());
            getAllStuInfoService.DeleteStudentService();
        }


        private class getAllStaffDataResponce implements ServerResponseListener {

            String deptName;

            public getAllStaffDataResponce(String deptShortName) {

                this.deptName = deptShortName;
            }

            @Override
            public void onResponseData(Object resultObj) {
                Log.d(TAG, "get all staff responce = " + resultObj.toString());

                try {
                    JSONObject object = new JSONObject(resultObj.toString());

                    String status = object.getString("status");


                    if (status.equals("0")) {

                        JSONArray jsonArray = object.getJSONArray(Constants.DATA);

                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject staffObj = jsonArray.getJSONObject(i);

                                String staffId = staffObj.getString(Constants.STAFF_ID);
                                String staffName = staffObj.getString(Constants.STAFF_NAME);
                                String staffMail = staffObj.getString(Constants.STAFF_MAIL_ID);
                                String staffQualify = staffObj.getString(Constants.STAFF_QUALIFICATION);
                                String staffDept = staffObj.getString(Constants.STAFF_DEPT);
                                String staffDealSub = staffObj.getString(Constants.STAFF_DEAL_SUB);
                                String staffMob = staffObj.getString(Constants.STAFF_MOBILE_NUM);
                                String staffGender = staffObj.getString(Constants.STAFF_GENDER);
                                String staffImage = staffObj.getString(Constants.STAFF_PROFILE);

                                if (staffDept.contains("\r\n\r\n")) {
                                    staffDept = staffDept.replace("\r\n\r\n", "");
                                }

                                if (deptName != null && deptName.equals(staffDept)) {

                                    staffeId.add(staffId);

                                }


                            }


                        } else {
                            // Nodata is getting please set no data in the list
                        }

                    } else {

                    }


                } catch (Exception e) {
                    Log.d(TAG, "getting string status  error - " + e.toString());
                    e.printStackTrace();
                }


            }
        }


        private class getAllStudentResponce implements ServerResponseListener {

            String deptName;

            public getAllStudentResponce(String deptShortName) {
                this.deptName = deptShortName;
            }

            @Override
            public void onResponseData(Object resultObj) {
                Log.d(TAG, "student responce = " + resultObj.toString());

                try {
                    JSONObject object = new JSONObject(resultObj.toString());

                    String status = object.getString("status");


                    if (status.equals("0")) {

                        JSONArray jsonArray = object.getJSONArray(Constants.DATA);

                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                //String deptId = staffObj.getString(Constants.DEP);
                                JSONObject jobj = jsonArray.getJSONObject(i);
                                String stuId = jobj.getString(Constants.USER_ID);
                                String studentDept = jobj.getString(Constants.USER_DEPT);

                                if (studentDept != null && !studentDept.isEmpty() && studentDept.equals(studentDept)) {
                                    studentId.add(stuId);
                                }

                            }

                        } else {
                            // Nodata is getting please set no data in the list
                        }

                    } else {

                    }


                } catch (Exception e) {
                    Log.d(TAG, "getting string status  error - " + e.toString());
                    e.printStackTrace();
                }


            }
        }

        private class deleteStaffResponse implements ServerResponseListener {


            @Override
            public void onResponseData(Object resultObj) {
                Log.d(TAG, "staff response = " + resultObj.toString());

                try {
                    JSONObject object = new JSONObject(resultObj.toString());

                    String status = object.getString("status");


                    if (status.equals("0")) {
                        //progressDoalog.dismiss();


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


        private class deleteStudentResponse implements ServerResponseListener {

            @Override
            public void onResponseData(Object resultObj) {
                Log.d(TAG, "student responce = " + resultObj.toString());

                try {
                    JSONObject object = new JSONObject(resultObj.toString());

                    String status = object.getString("status");

                    if (status.equals("0")) {

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

}

