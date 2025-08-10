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
import com.example.hurlingapp.domain.ActionDetail;
import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.app.Activity;

public class OutcomeFragment extends Fragment {

    private static final String ARG_KEY = "key";

    private ModelCallbacks callbacks;
    private String key;
    private AbstractAssessmentPage page;
    private ListView listView;
    private List<String> outcomes;

    public static OutcomeFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        OutcomeFragment fragment = new OutcomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_hurling_outcome, container, false);

        listView = (ListView) rootView.findViewById(R.id.outcome_list);

        ActionDetail selectedActionDetail = ((HurlingAssessmentModel)callbacks.getWizardModel()).getSelectedActionDetail();
        if (selectedActionDetail != null) {
            outcomes = selectedActionDetail.getOutcomes();
        } else {
            outcomes = new ArrayList<String>();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, outcomes);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedOutcome = outcomes.get(position);
                ((HurlingAssessmentModel)callbacks.getWizardModel()).setSelectedOutcome(selectedOutcome);
                callbacks.onPageDataChanged(page);
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
