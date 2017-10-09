package com.npkompleet.phenomenon.bakingapp;

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

    public static final String INSTRUCTION = "instruction";

    private String instruction;

    @BindView(R.id.recipe_step_instruction_detail)
    TextView textView;


    public RecipeStepInstructionFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            instruction = getArguments().getString(INSTRUCTION);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_recipe_step_instruction_detail, container, false);
        ButterKnife.bind(this, rootView);
        textView.setText(instruction);
        return rootView;
    }



}
