package com.example.pranav.stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TopologyFragment extends Fragment {
    public static String TAG= TopologyFragment.class.getSimpleName();
    DemoView demoview;
    public TopologyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedTopologyState) {

        super.onCreate(savedTopologyState);
        Log.d(TAG, "---TopologyFragment---");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedTopologyState) {
        demoview = new DemoView(getActivity());

        
        return demoview;
    }
    private class DemoView extends View{
        private int[] x_index =new int[3];
        private int[] y_index=new int[3];

        int width=getActivity().getResources().getDisplayMetrics().widthPixels -50;
        int height=getActivity().getResources().getDisplayMetrics().heightPixels -50;

        int half_width=(int)width/2 -40;
        int half_height=(int)height/2 -90;

        public DemoView(Context context){
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Log.d(TAG, String.valueOf(width));
            Log.d(TAG, String.valueOf(height));
            Log.d(TAG, String.valueOf(half_width));
            Log.d(TAG, String.valueOf(half_height));
            Log.d(TAG, "------------------------------");
            x_index[0]=30;
            y_index[0]=30;

            x_index[1]=half_width;
            y_index[1]=half_height;

            x_index[2]=width-120;
            y_index[2]=height-260;

            // custom drawing code here
            Paint paint = new Paint();
            Bitmap node= BitmapFactory.decodeResource(getResources(), R.drawable.node);
            Bitmap network= BitmapFactory.decodeResource(getResources(), R.drawable.network);
            paint.setColor(getContext().getResources().getColor(R.color.lightGrey));
            canvas.drawPaint(paint);
            paint.setAntiAlias(true);

            /*
            canvas.drawBitmap(node, x_index[0], y_index[0], paint);
            canvas.drawBitmap(node,x_index[2],y_index[0],paint);
            canvas.drawBitmap(node,x_index[0],y_index[2],paint);
            canvas.drawBitmap(node,x_index[2],y_index[2],paint);
            */

            /*
            paint.setColor(getContext().getResources().getColor(R.color.grey));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            paint.setAntiAlias(true);

            canvas.drawLine(50, 50, half_width, half_height, paint);
            canvas.drawLine(half_width, 50, half_width, half_height - 100, paint);
            canvas.drawLine(width - 50, 50, half_width, half_height - 100, paint);


            paint.setStrokeWidth(30);
            paint.setColor(getContext().getResources().getColor(R.color.darkGreen));
            paint.setStyle(Paint.Style.FILL);

            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(half_width, half_height, 25, paint);

            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(50, 50, 25, paint);


            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(half_width, 50, 25, paint);

            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(width - 50, 50, 25, paint);
            paint.setStrokeWidth(2);
            paint.setTextSize(35.0f);
            canvas.drawText("Hello", half_width / 2, half_height / 2, paint);
*/

            //paint.setStyle(Paint.Style.STROKE);

            int y=0,count=9,list=0;
            for (int i = 0; i <x_index.length ; i++) {
                for (int j = 0; j <y_index.length ; j++) {
                    paint.setStyle(Paint.Style.FILL);
                    paint.setStrokeWidth(60);
                    paint.setColor(getContext().getResources().getColor(R.color.lightBlue_var));
                   list=list++;
                    if(list >= count)
                        break;
                    canvas.drawLine(x_index[1] + 70, y_index[1] + 50, x_index[i] + 60, y_index[j] + 50, paint);
                   if((i== x_index.length/2) && (j==y_index.length/2))
                       continue;
                   else
                       canvas.drawBitmap(node, x_index[i], y_index[j], paint);


                    Log.d(TAG, "-------------loop-----------------");
                    Log.d(TAG, String.valueOf(i));
                    Log.d(TAG,String.valueOf(j));
                    Log.d(TAG,String.valueOf(x_index[i]));
                    Log.d(TAG,String.valueOf(y_index[j]));
                    Log.d(TAG, "---------------loop end---------------");
                    paint.setTextSize(50.0f);
                    paint.setStrokeWidth(60);
                    paint.setColor(getContext().getResources().getColor(R.color.colorAccent));
                    canvas.drawText("Hello", x_index[i] -50, y_index[i], paint);
                }

                if(i < InstancesFragment.InstanceList.size()) {
                }
                paint.setTextSize(50.0f);
                paint.setStrokeWidth(80);
                paint.setColor(Color.WHITE);
                canvas.drawBitmap(network,x_index[1]-20,y_index[1]-40,paint);
                canvas.drawText("Nova", x_index[1]+20 , y_index[1]+60, paint);
            }

            /*
            // draw blue circle with anti aliasing turned off
            paint.setAntiAlias(false);
            paint.setColor(Color.BLUE);
            canvas.drawCircle(20, 20, 15, paint);

            //Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.globe);
            //canvas.drawBitmap(bitmap,200,300,paint);

            canvas.drawLine(20, 20, 200, 300, paint);
             float x,y;


            // draw red rectangle with anti aliasing turned off
            paint.setAntiAlias(false);
            paint.setColor(Color.RED);
            canvas.drawRect(100, 5, 200, 30, paint);

            // draw the rotated text
            //canvas.rotate(-45);
            for (int i = 0; i < InstancesFragment.InstanceList.size(); i++) {
                String text=InstancesFragment.InstanceList.get(0).getInstanceName().toString();
                canvas.drawText(text, half_width/2, half_height/2, paint);

            }
            //undo the rotate
            canvas.restore();
            */



        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void drawLine(Canvas canvas,int startx, int starty,int stopx,int stopy){

    }

}

