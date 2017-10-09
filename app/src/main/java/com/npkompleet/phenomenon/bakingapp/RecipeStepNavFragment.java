package com.npkompleet.phenomenon.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepNavFragment extends Fragment {

    @BindView(R.id.left_nav)
    TextView left_nav;

    @BindView(R.id.right_nav)
    TextView right_nav;

    public static final String INDEX_PARAM = "index";
    public static final String MAX_INDEX_PARAM = "max";

    private int index;
    private int maxIndex;


    private OnNavButtonClickListener mListener;

    private View.OnClickListener navClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mListener.onNavClicked(view);
            onNavClicked();
        }
    };

    public RecipeStepNavFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(INDEX_PARAM);
            maxIndex = getArguments().getInt(MAX_INDEX_PARAM);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(INDEX_PARAM, index);
        outState.putInt(MAX_INDEX_PARAM, maxIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_recipe_step_nav, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null){
            index= savedInstanceState.getInt(INDEX_PARAM);
            maxIndex= savedInstanceState.getInt(MAX_INDEX_PARAM);
        }

        
        right_nav.setOnClickListener(navClickListener);
        left_nav.setOnClickListener(navClickListener);
        
        if (index == maxIndex) right_nav.setVisibility(View.INVISIBLE);
        if (index ==0) left_nav.setVisibility(View.INVISIBLE);
        return rootView;
    }

    public void onNavClicked() {
        if (RecipeStepDetailActivity.index !=0) {
            left_nav.setVisibility(View.VISIBLE);
        }else{
            left_nav.setVisibility(View.INVISIBLE);
        }

        if (RecipeStepDetailActivity.index != maxIndex){
            right_nav.setVisibility(View.VISIBLE);
        } else{
            right_nav.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNavButtonClickListener) {
            mListener = (OnNavButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNavButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnNavButtonClickListener {
        void onNavClicked(View view);
    }
}
