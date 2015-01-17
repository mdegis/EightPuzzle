package com.compsci.mdegis.eightpuzzlesolver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class PlaceholderFragment extends Fragment implements Animation.AnimationListener {

    private static final String LOG_TAG = "MD-debugger";

    EditText i1, i2, i3, i4, i5, i6, i7, i8, i9;
    EditText[] editTxList = new EditText[9];
    Button coz;
    Animation moveRight, moveLeft, moveUp, moveDown;

    View.OnClickListener handler1 = new View.OnClickListener() {
        public void onClick(View v) {

            Toast.makeText(
                    getActivity(),
                    Arrays.toString(getData()),
                    Toast.LENGTH_SHORT
            ).show();
/*
            AnimationSet as = new AnimationSet(true);
            as.addAnimation(moveRight);
            as.addAnimation(moveDown);
            as.setFillAfter(true);

            i1.startAnimation(as);
            i2.startAnimation(moveLeft);
            i5.startAnimation(moveUp);

            animateDiff'in dogru calistigi kanıtı:
            int[] initial = {1, 2, 3, 4, 5, 6, 7, 8, 0};
            int[] afterOneMove = {1, 2, 3, 4, 5, 0, 7, 8, 6};
            animateDiff(initial, afterOneMove);
            Denemek için bu bölümü uncomment ettikten sonra sağıyı comment out
*/
            int[][]solvedData ={
                {4, 1, 2, 5, 8, 3, 7, 0, 6},
                {4, 1, 2, 5, 0, 3, 7, 8, 6},
                {4, 1, 2, 0, 5, 3, 7, 8, 6},
                {0, 1, 2, 4, 5, 3, 7, 8, 6},
                {1, 0, 2, 4, 5, 3, 7, 8, 6},
                {1, 2, 0, 4, 5, 3, 7, 8, 6},
                {1, 2, 3, 4, 5, 0, 7, 8, 6},
                {1, 2, 3, 4, 5, 6, 7, 8, 0}
            };

            /*
             * fakeSolve ile aynı işi yapan kod bölümü:
             * SORUN: kod senkronize olarak çalışmıyor,
             * setPuzzle veya animasyonlar sırayla gerçekleşmiyor,
             * thread içersinde uyutulsalar bile.
             * YAPILMASI GEREKEN: işlemlerin sırasıyla gerçekleştirip,
             * her birinin sonucu ekranda gözukten sonra sonraki
             * işleme geçilmesi gerekli.
             */
            for (int i =1; i<solvedData.length; i++){
                setPuzzle(solvedData[i-1]);
                Log.i(LOG_TAG, Arrays.toString(getData()));
                animateDiff(solvedData[i - 1], solvedData[i]);
                }
        }
    };

    public int[] getData(){
        int[] data = new int[9];
        for (int i=0; i<data.length;i++)
            data[i] = Integer.parseInt(editTxList[i].getText().toString());
        return data;
    }

    public synchronized void setPuzzle(int[] state){
        for (int i=0; i<state.length;i++)
            editTxList[i].setText(String.valueOf(state[i]));
    }

    public void randomPuzzle(){
        // MD style
        int[] initial = {1, 2, 3, 4, 5, 6, 7, 8, 0};
        Random rnd = new Random();
        for (int i = initial.length - 1; i > 0; i--){
            int index = rnd.nextInt(i + 1);
            int a = initial[index];
            initial[index] = initial[i];
            initial[i] = a;
        }
        setPuzzle(initial);
    }

    public int find(int[] array, int value){
        for(int i=0; i<array.length; i++)
            if(array[i] == value)
                return i;
        // Amk Java; bu deger geri donderse GG WP ama sorun yok 0 HER ZAMAN liste içinde.
        // TODO: Exception belki?
        return Integer.MIN_VALUE;
    }

    public void animateDiff(int[] a, int[] b){
        /*
         * a: şuanki state
         * b: sonraki state
         * 0'ın (boşluğun) bir sonraki state'de nasıl değiştiğini tespit edip,
         * otomatik olarak yer değiştirmesi sağlayan fonksiyon.
         * Test için handler1 daki commente bkz.
         */
        int indexOne = find(a, 0);
        int indexTwo = find(b, 0);
        int move = indexOne - indexTwo;

        Log.i(LOG_TAG, String.valueOf(indexOne)+ indexTwo + move);

        switch (move){
            case -1:
                editTxList[indexOne].startAnimation(moveRight);
                editTxList[indexTwo].startAnimation(moveLeft);
                break;
            case 1:
                editTxList[indexOne].startAnimation(moveLeft);
                editTxList[indexTwo].startAnimation(moveRight);
                break;
            case -3:
                editTxList[indexOne].startAnimation(moveDown);
                editTxList[indexTwo].startAnimation(moveUp);
                break;
            case 3:
                editTxList[indexOne].startAnimation(moveUp);
                editTxList[indexTwo].startAnimation(moveDown);
                break;
            default:
                break;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                randomPuzzle();
                return true;
            default:
                break;
        }

        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        coz = (Button)v.findViewById(R.id.btn_coz);
        coz.setOnClickListener(handler1);

        i1 = (EditText)v.findViewById(R.id.i1);
        i2 = (EditText)v.findViewById(R.id.i2);
        i3 = (EditText)v.findViewById(R.id.i3);
        i4 = (EditText)v.findViewById(R.id.i4);
        i5 = (EditText)v.findViewById(R.id.i5);
        i6 = (EditText)v.findViewById(R.id.i6);
        i7 = (EditText)v.findViewById(R.id.i7);
        i8 = (EditText)v.findViewById(R.id.i8);
        i9 = (EditText)v.findViewById(R.id.i9);
        editTxList = new EditText[]{i1, i2, i3, i4, i5, i6, i7, i8, i9};

        moveRight = AnimationUtils.loadAnimation(getActivity(), R.anim.right);
        moveLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.left);
        moveUp = AnimationUtils.loadAnimation(getActivity(), R.anim.up);
        moveDown = AnimationUtils.loadAnimation(getActivity(), R.anim.down);
        moveRight.setAnimationListener(this);
        moveLeft.setAnimationListener(this);
        moveUp.setAnimationListener(this);
        moveDown.setAnimationListener(this);

        int[] initial = {4, 1, 2, 5, 8, 3, 7, 0, 6};
        setPuzzle(initial);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        Log.i(LOG_TAG, "ANIMATION STARTED");

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.i(LOG_TAG, "ANIMATION ENDED");
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}