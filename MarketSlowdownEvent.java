public class MarketSlowdownEvent implements GameEvent {

    @Override
    public void apply(Company company) {
        company.reduceCash(5000);
        System.out.println("ZDARZENIE: Spowolnienie rynku! Firma traci 5000 gotówki.");
    }
}
