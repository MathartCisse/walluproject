import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PatientComponentsPage, PatientDeleteDialog, PatientUpdatePage } from './patient.page-object';

const expect = chai.expect;

describe('Patient e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let patientComponentsPage: PatientComponentsPage;
  let patientUpdatePage: PatientUpdatePage;
  let patientDeleteDialog: PatientDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Patients', async () => {
    await navBarPage.goToEntity('patient');
    patientComponentsPage = new PatientComponentsPage();
    await browser.wait(ec.visibilityOf(patientComponentsPage.title), 5000);
    expect(await patientComponentsPage.getTitle()).to.eq('walluApp.patient.home.title');
    await browser.wait(ec.or(ec.visibilityOf(patientComponentsPage.entities), ec.visibilityOf(patientComponentsPage.noResult)), 1000);
  });

  it('should load create Patient page', async () => {
    await patientComponentsPage.clickOnCreateButton();
    patientUpdatePage = new PatientUpdatePage();
    expect(await patientUpdatePage.getPageTitle()).to.eq('walluApp.patient.home.createOrEditLabel');
    await patientUpdatePage.cancel();
  });

  it('should create and save Patients', async () => {
    const nbButtonsBeforeCreate = await patientComponentsPage.countDeleteButtons();

    await patientComponentsPage.clickOnCreateButton();

    await promise.all([
      patientUpdatePage.setFirstNameInput('firstName'),
      patientUpdatePage.setLastNameInput('lastName'),
      patientUpdatePage.setEmailInput('email'),
      patientUpdatePage.setPhoneNumberInput('phoneNumber'),
      patientUpdatePage.setAgeInput('5'),
      patientUpdatePage.setSalaryInput('5'),
      patientUpdatePage.setIdCardNumberInput('5'),
      patientUpdatePage.patientSelectLastOption(),
    ]);

    expect(await patientUpdatePage.getFirstNameInput()).to.eq('firstName', 'Expected FirstName value to be equals to firstName');
    expect(await patientUpdatePage.getLastNameInput()).to.eq('lastName', 'Expected LastName value to be equals to lastName');
    expect(await patientUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    expect(await patientUpdatePage.getPhoneNumberInput()).to.eq('phoneNumber', 'Expected PhoneNumber value to be equals to phoneNumber');
    expect(await patientUpdatePage.getAgeInput()).to.eq('5', 'Expected age value to be equals to 5');
    expect(await patientUpdatePage.getSalaryInput()).to.eq('5', 'Expected salary value to be equals to 5');
    expect(await patientUpdatePage.getIdCardNumberInput()).to.eq('5', 'Expected idCardNumber value to be equals to 5');

    await patientUpdatePage.save();
    expect(await patientUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await patientComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Patient', async () => {
    const nbButtonsBeforeDelete = await patientComponentsPage.countDeleteButtons();
    await patientComponentsPage.clickOnLastDeleteButton();

    patientDeleteDialog = new PatientDeleteDialog();
    expect(await patientDeleteDialog.getDialogTitle()).to.eq('walluApp.patient.delete.question');
    await patientDeleteDialog.clickOnConfirmButton();

    expect(await patientComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
