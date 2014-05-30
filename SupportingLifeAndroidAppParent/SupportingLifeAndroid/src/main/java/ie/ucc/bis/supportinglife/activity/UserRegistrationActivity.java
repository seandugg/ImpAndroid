package ie.ucc.bis.supportinglife.activity;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.validation.CroutonValidationFailedRenderer;
import ie.ucc.bis.supportinglife.validation.Field;
import ie.ucc.bis.supportinglife.validation.Form;
import ie.ucc.bis.supportinglife.validation.NotEmptyValidation;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Activity responsible for registering HSA user on initial launch
 * of the application.
 *
 * @author timothyosullivan
 */

public class UserRegistrationActivity extends SupportingLifeBaseActivity {

	private EditText userLogin;
	private EditText userPassword;
	private Button clearButton;
	private Button submitButton;
	
	// Form used for validation
    private Form form;
	
	/**
	 * OnCreate method is called when the activity is first created.
	 * 
	 * This is where all of the normal static set up should occur
	 * e.g. create views, bind data to lists, etc.
	 * 
	 * The method also provides a Bundle parameter containing the activity's
	 * 
	 * previously frozen state (if there was one).
	 * 
	 * This method is always followed by onStart().
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_registration);
		
		initGraphicalElements();
		initValidationForm();
		initCallbacks();
		registerValidationErrorRenderer();
	}
	
	private void initGraphicalElements() {
		setUserLogin((EditText) findViewById(R.id.user_registration_login_id));
		setUserPassword((EditText) findViewById(R.id.user_registration_login_password));
		setClearButton((Button) findViewById(R.id.user_registration_clear_button));
		setSubmitButton((Button) findViewById(R.id.user_registration_submit_button));
	}

	private void initValidationForm() {
        setForm(new Form(this));
        getForm().setValidationFailedRenderer(new CroutonValidationFailedRenderer(this));

        getForm().addField(Field.using(getUserLogin(), getResources().getString(R.string.user_registration_login_id)).validate(NotEmptyValidation.build(this)));
        getForm().addField(Field.using(getUserPassword(), getResources().getString(R.string.user_registration_login_password)).validate(NotEmptyValidation.build(this)));
    }
	
    private void initCallbacks() {
        getSubmitButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // TODO
               Toast.makeText(getApplicationContext(), "submit button clicked", Toast.LENGTH_SHORT).show();
               submitLoginDetails();
            }
        });
    }
    
    private void registerValidationErrorRenderer() {
    	getForm().setValidationFailedRenderer(new CroutonValidationFailedRenderer(this));
    }
    
	private void submitLoginDetails() {
		if (getForm().isValid()) {
            Crouton.makeText(this, "valid form input", Style.CONFIRM).show();
            
            // TODO: TEMP - MOVE TO A MORE LOGICAL PLACE
			// record hsa user type
//    		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
//    		SharedPreferences.Editor preferenceEditor = settings.edit();
//			preferenceEditor.putString(USER_TYPE_KEY, HSA_USER);
//			preferenceEditor.commit();
        }		
	}
	
	/**
	 * onResume method is called when the activity will start interacting with the user.
	 * 
	 * At this point your activity is at the top of the activity stack, with user input going to it.
	 * 
	 * This method is always followed by onPause().
	 *
	 */
	@Override
	protected void onResume () {
		super.onResume ();
	}
	
	/**
	 * Determine if this activity should display an ActionBar when it is
	 * shown.
	 * 
	 * @return boolean
	 */
	@Override
	protected boolean shouldDisplayActionBar() {
		return false;
	}
	
	/**
	 * Click Handler: Handler the click of a user registration button
	 * 
	 * @param view View
	 * @return void
	 */
	public void onClickUserRegistrationButton(View view) {
		int id = view.getId();
				
		switch(id) {
			case R.id.user_registration_submit_button:
				
				// TODO authenticate the user
				
				startActivity(new Intent(getApplicationContext(), HomeActivity.class));	
				// configure the activity animation transition effect
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);	
				break;
			case R.id.user_registration_clear_button:
				break;	
			default : 
				break;
		} // end of switch
	
	}

	public EditText getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(EditText userLogin) {
		this.userLogin = userLogin;
	}

	public EditText getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(EditText userPassword) {
		this.userPassword = userPassword;
	}

	public Button getClearButton() {
		return clearButton;
	}

	public void setClearButton(Button clearButton) {
		this.clearButton = clearButton;
	}

	public Button getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(Button submitButton) {
		this.submitButton = submitButton;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
} // end class
