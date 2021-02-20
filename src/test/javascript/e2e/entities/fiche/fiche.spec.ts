import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FicheComponentsPage, FicheDeleteDialog, FicheUpdatePage } from './fiche.page-object';

const expect = chai.expect;

describe('Fiche e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ficheComponentsPage: FicheComponentsPage;
  let ficheUpdatePage: FicheUpdatePage;
  let ficheDeleteDialog: FicheDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Fiches', async () => {
    await navBarPage.goToEntity('fiche');
    ficheComponentsPage = new FicheComponentsPage();
    await browser.wait(ec.visibilityOf(ficheComponentsPage.title), 5000);
    expect(await ficheComponentsPage.getTitle()).to.eq('walluApp.fiche.home.title');
    await browser.wait(ec.or(ec.visibilityOf(ficheComponentsPage.entities), ec.visibilityOf(ficheComponentsPage.noResult)), 1000);
  });

  it('should load create Fiche page', async () => {
    await ficheComponentsPage.clickOnCreateButton();
    ficheUpdatePage = new FicheUpdatePage();
    expect(await ficheUpdatePage.getPageTitle()).to.eq('walluApp.fiche.home.createOrEditLabel');
    await ficheUpdatePage.cancel();
  });

  it('should create and save Fiches', async () => {
    const nbButtonsBeforeCreate = await ficheComponentsPage.countDeleteButtons();

    await ficheComponentsPage.clickOnCreateButton();

    await promise.all([
      ficheUpdatePage.setGroupeSanguinInput('groupeSanguin'),
      ficheUpdatePage.setPoidsInput('5'),
      ficheUpdatePage.setTailleInput('5'),
    ]);

    expect(await ficheUpdatePage.getGroupeSanguinInput()).to.eq(
      'groupeSanguin',
      'Expected GroupeSanguin value to be equals to groupeSanguin'
    );
    expect(await ficheUpdatePage.getPoidsInput()).to.eq('5', 'Expected poids value to be equals to 5');
    expect(await ficheUpdatePage.getTailleInput()).to.eq('5', 'Expected taille value to be equals to 5');

    await ficheUpdatePage.save();
    expect(await ficheUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ficheComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Fiche', async () => {
    const nbButtonsBeforeDelete = await ficheComponentsPage.countDeleteButtons();
    await ficheComponentsPage.clickOnLastDeleteButton();

    ficheDeleteDialog = new FicheDeleteDialog();
    expect(await ficheDeleteDialog.getDialogTitle()).to.eq('walluApp.fiche.delete.question');
    await ficheDeleteDialog.clickOnConfirmButton();

    expect(await ficheComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
