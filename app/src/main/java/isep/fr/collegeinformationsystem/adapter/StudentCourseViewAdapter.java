package isep.fr.collegeinformationsystem.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceUtil.ConnectivityReceiver;
import isep.fr.collegeinformationsystem.interfaces.ItemClickListener;
import isep.fr.collegeinformationsystem.model.AdminCourseModel;

public class StudentCourseViewAdapter extends RecyclerView.Adapter<StudentCourseViewAdapter.MyViewHolder> {

    private static final String TAG = "AdminStudentsListAdapter";

    private ArrayList<AdminCourseModel> courseListData;
    private Context context;

    public StudentCourseViewAdapter(Context context, ArrayList<AdminCourseModel> courseListData) {

        this.context = context;
        this.courseListData = courseListData;

    }


    @NonNull
    @Override
    public StudentCourseViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_course_card_layout, viewGroup, false);

        return new StudentCourseViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentCourseViewAdapter.MyViewHolder holder, final int position) {


        holder.usrName.setText(courseListData.get(position).getCourseShortName());
        holder.usrMail.setText(courseListData.get(position).getSubjects());
        holder.usrCourse.setText(courseListData.get(position).getCourseName());
        holder.usrId.setText(String.valueOf(position + 1));


        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectivityReceiver.isConnected()) {
                    AdminCourseModel adminCourseModel = courseListData.get(position);

                    String fileUrl = Constants.SYL_DOC_DOWNLOAD_URL + adminCourseModel.getCoursePdfFile();

                    String fileName = adminCourseModel.getCourseShortName() + ".pdf";

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
                    request.setDescription("Some descrition");
                    request.setTitle(fileName);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                    DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                }else{
                    Toast.makeText(context,"please check your network",Toast.LENGTH_SHORT).show();
                }

            }
        });


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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView usrName, usrMail, usrId, usrCourse;
        private ImageView usrProfile, download;

        ItemClickListener itemClickListener;


        public MyViewHolder(View itemView) {
            super(itemView);

            usrProfile = (ImageView) itemView.findViewById(R.id.imv_usr_pfl_img);
            usrName = (TextView) itemView.findViewById(R.id.tv_usr_name);
            usrMail = (TextView) itemView.findViewById(R.id.tv_user_mail);
            usrId = (TextView) itemView.findViewById(R.id.tv_cour_id);
            usrCourse = (TextView) itemView.findViewById(R.id.tv_user_course);


            //action
            download = (ImageView) itemView.findViewById(R.id.imv_user_download);


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
