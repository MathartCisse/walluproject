import { element, by, ElementFinder } from 'protractor';

export class PatientComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-patient div table .btn-danger'));
  title = element.all(by.css('jhi-patient div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class PatientUpdatePage {
  pageTitle = element(by.id('jhi-patient-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  firstNameInput = element(by.id('field_firstName'));
  lastNameInput = element(by.id('field_lastName'));
  emailInput = element(by.id('field_email'));
  phoneNumberInput = element(by.id('field_phoneNumber'));
  ageInput = element(by.id('field_age'));
  salaryInput = element(by.id('field_salary'));
  idCardNumberInput = element(by.id('field_idCardNumber'));

  patientSelect = element(by.id('field_patient'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setFirstNameInput(firstName: string): Promise<void> {
    await this.firstNameInput.sendKeys(firstName);
  }

  async getFirstNameInput(): Promise<string> {
    return await this.firstNameInput.getAttribute('value');
  }

  async setLastNameInput(lastName: string): Promise<void> {
    await this.lastNameInput.sendKeys(lastName);
  }

  async getLastNameInput(): Promise<string> {
    return await this.lastNameInput.getAttribute('value');
  }

  async setEmailInput(email: string): Promise<void> {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput(): Promise<string> {
    return await this.emailInput.getAttribute('value');
  }

  async setPhoneNumberInput(phoneNumber: string): Promise<void> {
    await this.phoneNumberInput.sendKeys(phoneNumber);
  }

  async getPhoneNumberInput(): Promise<string> {
    return await this.phoneNumberInput.getAttribute('value');
  }

  async setAgeInput(age: string): Promise<void> {
    await this.ageInput.sendKeys(age);
  }

  async getAgeInput(): Promise<string> {
    return await this.ageInput.getAttribute('value');
  }

  async setSalaryInput(salary: string): Promise<void> {
    await this.salaryInput.sendKeys(salary);
  }

  async getSalaryInput(): Promise<string> {
    return await this.salaryInput.getAttribute('value');
  }

  async setIdCardNumberInput(idCardNumber: string): Promise<void> {
    await this.idCardNumberInput.sendKeys(idCardNumber);
  }

  async getIdCardNumberInput(): Promise<string> {
    return await this.idCardNumberInput.getAttribute('value');
  }

  async patientSelectLastOption(): Promise<void> {
    await this.patientSelect.all(by.tagName('option')).last().click();
  }

  async patientSelectOption(option: string): Promise<void> {
    await this.patientSelect.sendKeys(option);
  }

  getPatientSelect(): ElementFinder {
    return this.patientSelect;
  }

  async getPatientSelectedOption(): Promise<string> {
    return await this.patientSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class PatientDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-patient-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-patient'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
