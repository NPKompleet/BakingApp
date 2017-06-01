package com.example.phenomenon.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.phenomenon.bakingapp.RecipeStepDetailActivity.index;

public class RecipeStepNavFragment extends Fragment {

    @BindView(R.id.left_nav)
    TextView left_nav;

    @BindView(R.id.right_nav)
    TextView right_nav;

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";

    private int mParam1;
    private int mParam2;


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



 /*   public static RecipeStepNavFragment newInstance(int param1, int param2) {
        RecipeStepNavFragment fragment = new RecipeStepNavFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_recipe_step_nav, container, false);
        ButterKnife.bind(this, rootView);
        Toast.makeText(getContext(), "frag_index: "+ mParam1, Toast.LENGTH_SHORT).show();
        

        
        right_nav.setOnClickListener(navClickListener);
        left_nav.setOnClickListener(navClickListener);
        
        if (mParam1==mParam2) right_nav.setVisibility(View.INVISIBLE);
        if (mParam1==0) left_nav.setVisibility(View.INVISIBLE);
        return rootView;
    }

    public void onNavClicked() {
        /*if (mListener != null) {
            mListener.onNavClicked();
        }*/
        if (index!=0) {
            left_nav.setVisibility(View.VISIBLE);
        }else{
            left_nav.setVisibility(View.INVISIBLE);
        }

        if (index!=mParam2){
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
