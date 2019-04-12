package bluetoothprintertest.dn.com.productlisttest.bannerfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import bluetoothprintertest.dn.com.productlisttest.R;
import bluetoothprintertest.dn.com.productlisttest.Util;

public class BannerFragment extends Fragment {
    View view;
    ImageView img;
    String imageURL = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.banner, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        img = view.findViewById(R.id.img);
        try {
            Bundle args = getArguments();
            if (args != null) {
                imageURL = args.getString("image_url");
                if (!imageURL.trim().equals("")) {
                    Picasso.get().load(imageURL).into(img);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
