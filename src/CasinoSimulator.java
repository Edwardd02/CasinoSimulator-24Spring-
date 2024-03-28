public class CasinoSimulator {
    public static void main(String[] args) {
        MainMenuModel menuModel = new MainMenuModel();
        TempleAlertModel templeAlertModel = new TempleAlertModel(menuModel);

        try {
            templeAlertModel.fetchLatestTemplePost(); // This will fetch and print the latest post from r/Temple
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}