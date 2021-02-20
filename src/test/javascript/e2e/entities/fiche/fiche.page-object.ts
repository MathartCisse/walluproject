import { element, by, ElementFinder } from 'protractor';

export class FicheComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fiche div table .btn-danger'));
  title = element.all(by.css('jhi-fiche div h2#page-heading span')).first();
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

export class FicheUpdatePage {
  pageTitle = element(by.id('jhi-fiche-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  groupeSanguinInput = element(by.id('field_groupeSanguin'));
  poidsInput = element(by.id('field_poids'));
  tailleInput = element(by.id('field_taille'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setGroupeSanguinInput(groupeSanguin: string): Promise<void> {
    await this.groupeSanguinInput.sendKeys(groupeSanguin);
  }

  async getGroupeSanguinInput(): Promise<string> {
    return await this.groupeSanguinInput.getAttribute('value');
  }

  async setPoidsInput(poids: string): Promise<void> {
    await this.poidsInput.sendKeys(poids);
  }

  async getPoidsInput(): Promise<string> {
    return await this.poidsInput.getAttribute('value');
  }

  async setTailleInput(taille: string): Promise<void> {
    await this.tailleInput.sendKeys(taille);
  }

  async getTailleInput(): Promise<string> {
    return await this.tailleInput.getAttribute('value');
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

export class FicheDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fiche-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fiche'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
