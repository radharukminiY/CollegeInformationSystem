package isep.fr.collegeinformationsystem.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.model.ProfessorModel;

public class StudentProfessorViewAdapter extends RecyclerView.Adapter<StudentProfessorViewAdapter.MyViewHolder> {

    private static final String TAG = "AdminStudentsListAdapter";

    private ArrayList<ProfessorModel> professorModels;
    private Context context;

    public StudentProfessorViewAdapter(Context context, ArrayList<ProfessorModel> professorModels) {

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.usrName.setText(professorModels.get(position).getProfName());
        holder.usrMail.setText(professorModels.get(position).getProfMail());
        holder.usrCourse.setText(professorModels.get(position).getProfCourse());
        holder.usrId.setText(professorModels.get(position).getProfId());


        //onclick listner

        holder.usrProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //display profile image in dialoug
                Toast.makeText(context, "profile clicked", Toast.LENGTH_SHORT).show();

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

            usrEdit.setVisibility(View.GONE);
            usrDelete.setVisibility(View.GONE);


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

