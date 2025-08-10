package com.example.hurlingapp.assessment.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.hurlingapp.R;
import com.example.hurlingapp.assessment.model.AbstractAssessmentPage;
import com.example.hurlingapp.assessment.model.HurlingAssessmentModel;
import com.example.hurlingapp.assessment.model.ModelCallbacks;
import com.example.hurlingapp.ui.PitchView;
import android.view.MotionEvent;
import android.app.Activity;
import android.graphics.PointF;

public class PitchFragment extends Fragment {

    private static final String ARG_KEY = "key";

    private ModelCallbacks callbacks;
    private String key;
    private AbstractAssessmentPage page;
    private PitchView pitchView;

    public static PitchFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        PitchFragment fragment = new PitchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        key = args.getString(ARG_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hurling_pitch, container, false);

        pitchView = (PitchView) rootView.findViewById(R.id.pitch_view);
        pitchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();

                    PointF tap = new PointF(x, y);
                    pitchView.addTap(tap);

                    HurlingAssessmentModel model = (HurlingAssessmentModel)callbacks.getWizardModel();
                    if (model.getStartLocation() == null) {
                        model.setStartLocation(tap);
                    } else if (model.getEndLocation() == null) {
                        model.setEndLocation(tap);
                        callbacks.onPageDataChanged(page);
                    } else {
                        // both locations are set, so clear them and start over
                        pitchView.clearTaps();
                        pitchView.addTap(tap);
                        model.setStartLocation(tap);
                        model.setEndLocation(null);
                    }
                }
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ModelCallbacks)) {
            throw new ClassCastException("Activity must implement ModelCallbacks");
        }

        callbacks = (ModelCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }
}
