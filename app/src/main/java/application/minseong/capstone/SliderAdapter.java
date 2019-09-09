package application.minseong.capstone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SliderAdapter extends PagerAdapter{

    Context context;
    LayoutInflater inflater;
    Button visibleBtn;
    private Activity activity;

    public int[] imagesArray = {R.drawable.ic_phone_forwarded_black_24dp,R.drawable.ic_my_location_black_24dp,R.drawable.youtube,R.drawable.ic_whatshot_black_24dp};
    public String[] titleArray = {"Emergency Call","GPS","Youtube","Alarm"};
    public String[] descriptionArray = {"Use it in an emergency!",
            "Sign up for GPS and let us know \n where you are!",
            "Check out how to cope with any emergency",
            "Sound the alarm to alert you from the danger!"};

    public int[] backgroundColorArray = {Color.rgb(55,55,55)
            ,Color.rgb(239,85,85)
            ,Color.rgb(110,49,89)
            ,Color.rgb(1,188,212)
    };



    public SliderAdapter(Context context){
        this.context = context;
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return (view==object);
    }

    @Override
    public int getCount(){
        return titleArray.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,Object object){
        container.removeView((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position){

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide,container,false);
        View v = null;
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);
        ImageView imageView = (ImageView)view.findViewById(R.id.slideimg);
        TextView t1_title = (TextView)view.findViewById(R.id.txtTitle);
        TextView t2_desc = (TextView)view.findViewById(R.id.txtDescription);
        Button visibleBtn = (Button)view.findViewById(R.id.visibleBtn);

        if(position == 3){
            visibleBtn.setVisibility(View.VISIBLE);
            visibleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,MainActivity.class));
                    //activity.finish();
                }
            });

        }else{
            visibleBtn.setVisibility(View.GONE);
        }

        linearLayout.setBackgroundColor(backgroundColorArray[position]);
        imageView.setImageResource(imagesArray[position]);
        t1_title.setText(titleArray[position]);
        t2_desc.setText(descriptionArray[position]);

        container.addView(view);

        return view;
    }

}
