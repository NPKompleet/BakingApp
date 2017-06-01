package com.example.phenomenon.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeStepInstructionFragment} interface
 * to handle interaction events.
 * Use the {@link RecipeStepInstructionFragment} factory method to
 * create an instance of this fragment.
 */
public class RecipeStepInstructionFragment extends Fragment {

    public static final String ARG_PARAM = "instruction";

    private String mParam1;

    @BindView(R.id.recipe_step_instruction_detail)
    TextView textView;

    //private OnFragmentInteractionListener mListener;

    public RecipeStepInstructionFragment() {
        // Required empty public constructor
    }


   /* public static RecipeStepInstructionFragment newInstance(String param1, String param2) {
        RecipeStepInstructionFragment fragment = new RecipeStepInstructionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);*/
        View rootView= inflater.inflate(R.layout.fragment_recipe_step_instruction_detail, container, false);
        ButterKnife.bind(this, rootView);
        textView.setText(mParam1);
        return rootView;
    }



   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

   /* @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
