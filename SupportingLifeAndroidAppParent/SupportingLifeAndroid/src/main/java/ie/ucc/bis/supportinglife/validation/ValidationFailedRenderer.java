package ie.ucc.bis.supportinglife.validation;

public interface ValidationFailedRenderer {

    void showErrorMessage(ValidationResult validationResult);
    void clear();	
}
