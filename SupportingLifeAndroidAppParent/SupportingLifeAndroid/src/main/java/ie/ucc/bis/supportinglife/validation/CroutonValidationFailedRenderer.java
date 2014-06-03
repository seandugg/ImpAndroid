package ie.ucc.bis.supportinglife.validation;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.widget.EditText;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class CroutonValidationFailedRenderer implements ValidationFailedRenderer {

	private Activity activity;
	private List<Crouton> croutons = new ArrayList<Crouton>();
	
	public CroutonValidationFailedRenderer(Activity activity) {
		setActivity(activity);
	}
	
	@Override
	public void showErrorMessage(ValidationResult validationResult) {
		Crouton crouton = Crouton.makeText(getActivity(), validationResult.getMessage(), Style.ALERT);
		getCroutons().add(crouton);
        crouton.show();
	}
	
	@Override
	public void clear(EditText textView) {
		textView.setError(null);
	}

	@Override
	public void clearAll() {
		for (Crouton crouton : getCroutons()) {
			crouton.cancel();
        }
		getCroutons().clear();
    }

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public List<Crouton> getCroutons() {
		return croutons;
	}

	public void setCroutons(List<Crouton> croutons) {
		this.croutons = croutons;
	}
}
