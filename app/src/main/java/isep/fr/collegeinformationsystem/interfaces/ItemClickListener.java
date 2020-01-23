package isep.fr.collegeinformationsystem.interfaces;

import android.view.View;

/**
 * Created by lenovo on 7/18/2018.
 */

public interface ItemClickListener {
    void onItemClick(View v, int pos);

    void onItemLongClick(View v, int pos);
}
