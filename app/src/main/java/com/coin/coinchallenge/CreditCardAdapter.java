package com.coin.coinchallenge;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by joe-work on 3/27/15.
 */
public class CreditCardAdapter extends ArrayAdapter<CreditCard> {


    private static final String TAG = CreditCardAdapter.class.getSimpleName();
    Typeface type = Typeface.createFromAsset(MainActivity.getContext().getAssets(), "helveticaneue-webfont.ttf");
    private Context context;
    private List<CreditCard> cardList;
    public ImageLoader imageLoader;

    public class CreditCardHolder {
        FrameLayout rootView;
        TextView mNameView;
        TextView mNumberView;
        TextView mExpView;
        ImageView mCardBackground;
    }


    public CreditCardAdapter(final Context context, List<CreditCard> cards) {
        super(context, R.layout.credit_card_item, cards);
        this.context = context;
        this.cardList = cards;
        //create the image loader util
        imageLoader = new ImageLoader(MainActivity.getInstance().getApplicationContext());

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CreditCardHolder holder;

        //make sure to be efficient
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.credit_card_item, parent, false);
            holder = new CreditCardHolder();
            holder.rootView = (FrameLayout) convertView.findViewById(R.id.cc_root);
            holder.mCardBackground = (ImageView) convertView.findViewById(R.id.cc_image);
            holder.mNameView = (TextView) convertView.findViewById(R.id.cc_name);
            holder.mNumberView = (TextView) convertView.findViewById(R.id.cc_number);
            holder.mExpView = (TextView) convertView.findViewById(R.id.cc_exp_date);
            convertView.setTag(holder);
        } else {
            holder = (CreditCardHolder) convertView.getTag();
        }

        //Get the object and set the object
        CreditCard singleCard = cardList.get(position);

        holder.mNameView.setText(singleCard.getFullDisplayName());
        holder.mNameView.setTypeface(type);
        holder.mNumberView.setText(singleCard.getDisplayCCNumber());
        holder.mNumberView.setTypeface(type);
        holder.mExpView.setText(singleCard.getDisplayExpDate());
        holder.mExpView.setTypeface(type);
        if (singleCard.background_image_url != null) {
            //do the async caching and view setting.
            imageLoader.DisplayImage(singleCard.background_image_url.toString(), holder.mCardBackground);
        }
        convertView.requestLayout();
        return convertView;
    }

}
