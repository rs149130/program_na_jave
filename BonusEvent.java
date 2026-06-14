public class BonusEvent implements GameEvent {

    @Override
    public void apply(Company company) {
        company.addCash(3000);
        System.out.println("ZDARZENIE: Nieoczekiwana premia! Firma zyskuje 3000 gotówki.");
    }
}
