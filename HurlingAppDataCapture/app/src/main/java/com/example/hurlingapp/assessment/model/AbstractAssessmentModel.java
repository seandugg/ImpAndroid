package com.example.hurlingapp.assessment.model;

// import com.example.hurlingapp.analytics.DataAnalytic;
// import com.example.hurlingapp.assessment.model.review.ReviewItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.os.Bundle;

public abstract class AbstractAssessmentModel extends AbstractModel implements ModelCallbacks {
	private List<ModelCallbacks> modelListeners = new ArrayList<ModelCallbacks>();
    private AssessmentPageList assessmentPages;

    protected abstract AssessmentPageList configureAssessmentPageList();

    public AbstractAssessmentModel(Context context) {
	super(context);
	setApplicationContext(context);
        setAssessmentPages(configureAssessmentPageList());
    }

    public void onPageDataChanged(AbstractAssessmentPage page) {
	for (ModelCallbacks modelCallback : getModelListeners()) {
		modelCallback.onPageDataChanged(page);
	}
    }

    public void onPageTreeChanged() {
	for (ModelCallbacks modelCallback : getModelListeners()) {
		modelCallback.onPageTreeChanged();
        }
    }

    public AbstractAssessmentPage findAssessmentPageByKey(String key) {
        return getAssessmentPages().findAssessmentPageByKey(key);
    }

	public Bundle save() {
        Bundle bundle = new Bundle();
        saveAssessmentPages(bundle);
        return bundle;
	}

	public void load(Bundle savedValues) {
		loadAssessmentPages(savedValues);
	}

    public void loadAssessmentPages(Bundle savedValues) {
        for (String key : savedValues.keySet()) {
		if (getAssessmentPages() != null) {
			AbstractAssessmentPage assessmentPage = getAssessmentPages().findAssessmentPageByKey(key);
			if (assessmentPage != null) {
				assessmentPage.resetPageData(savedValues.getBundle(key));
			}
		}
        }
    }

    public Bundle saveAssessmentPages(Bundle bundle) {
        for (AbstractAssessmentPage assessmentpage : getAssessmentPageSequence()) {
            bundle.putBundle(assessmentpage.getKey(), assessmentpage.getPageData());
        }
        return bundle;
    }

    public void registerListener(ModelCallbacks listener) {
        getModelListeners().add(listener);
    }

    public void unregisterListener(ModelCallbacks listener) {
	getModelListeners().remove(listener);
    }

    public List<AbstractAssessmentPage> getAssessmentPageSequence() {
        ArrayList<AbstractAssessmentPage> flattened = new ArrayList<AbstractAssessmentPage>();
        getAssessmentPages().flattenCurrentPageSequence(flattened);
        return flattened;
    }

    /*
    public ArrayList<ReviewItem> gatherAssessmentReviewItems() {
		ArrayList<ReviewItem> reviewItems = new ArrayList<ReviewItem>();
        for (AbstractAssessmentPage assessmentPage : getAssessmentPageSequence()) {
		assessmentPage.getReviewItems(reviewItems);
        }

        Collections.sort(reviewItems, new Comparator<ReviewItem>() {
            public int compare(ReviewItem a, ReviewItem b) {
                return a.getWeight() > b.getWeight() ? +1 : a.getWeight() < b.getWeight() ? -1 : 0;
            }
        });
		return reviewItems;
	}
	*/

	public AssessmentPageList getAssessmentPages() {
		return assessmentPages;
	}

	public void setAssessmentPages(AssessmentPageList assessmentPages) {
		this.assessmentPages = assessmentPages;
	}

	public List<ModelCallbacks> getModelListeners() {
		return modelListeners;
	}

	public void setModelListeners(List<ModelCallbacks> modelListeners) {
		this.modelListeners = modelListeners;
	}
}
