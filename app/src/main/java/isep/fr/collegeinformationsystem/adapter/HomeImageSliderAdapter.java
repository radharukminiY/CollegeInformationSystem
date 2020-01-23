package isep.fr.collegeinformationsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import isep.fr.collegeinformationsystem.R;

public class HomeImageSliderAdapter extends SliderViewAdapter<HomeImageSliderAdapter.SliderAdapterVH> {

    private Context context;

    public HomeImageSliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        switch (position) {
            case 0:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.app_home)
                        .into(viewHolder.imageViewBackground);
                break;
            case 1:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.app_home1)
                        .into(viewHolder.imageViewBackground);
                break;
            case 2:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.app_home2)
                        .into(viewHolder.imageViewBackground);
                break;
            default:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.app_home3)
                        .into(viewHolder.imageViewBackground);
                break;

        }

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 4;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        private SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}