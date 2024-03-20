import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlotModelTest {
    public SlotModel slotModel;

    /*
     * test if return "Winner!!" when same symbols in first row.
     */
    @Test
    public void firstRowSameSymbolShouldReturnWinner() {
        slotModel = new SlotModel(new MainMenuModel(), 100);
        slotModel.setSlot(0, 0);
        slotModel.setSlot(1, 0);
        slotModel.setSlot(2, 0);
        assertEquals("Winner!!", slotModel.matchCheck());

    }

    /*
     * Test if return "Bad Luck!!" when there is no matches symbols.
     */
    @Test
    public void noMatchSymbolShouldReturnBadLuck() {
        slotModel = new SlotModel(new MainMenuModel(), 100);
        slotModel.setSlot(0, 0);
        slotModel.setSlot(1, 5);
        slotModel.setSlot(2, 2);
        slotModel.setSlot(3, 7);
        slotModel.setSlot(4, 6);
        slotModel.setSlot(5, 5);
        slotModel.setSlot(6, 1);
        slotModel.setSlot(7, 9);
        slotModel.setSlot(8, 3);
        assertEquals("Bad Luck!!", slotModel.matchCheck());

    }

    /*
     * Test if it returns 99 dollars when the player doesn't win.
     */
    @Test
    public void noMatchShouldLose1Dollar() {
        slotModel = new SlotModel(new MainMenuModel(), 100);
        //Need to specify which game mode in order to make an accurate test.
        MainMenuView.gamemode = "Simulated Casino";
        slotModel.setSlot(0, 0);
        slotModel.setSlot(1, 5);
        slotModel.setSlot(2, 2);
        slotModel.setSlot(3, 7);
        slotModel.setSlot(4, 6);
        slotModel.setSlot(5, 5);
        slotModel.setSlot(6, 1);
        slotModel.setSlot(7, 9);
        slotModel.setSlot(8, 3);
        slotModel.matchCheck();
        assertEquals(99, slotModel.getMoney());
    }

    /*
     * Test if it returns 52 dollars when there is one winning row (normal symbol).
     */
    @Test
    public void oneRowSameNormalSymbolShouldWin2Dollar() {
        slotModel = new SlotModel(new MainMenuModel(), 50);
        //Need to specify which game mode in order to make an accurate test.
        MainMenuView.gamemode = "Simulated Casino";
        //winning row
        slotModel.setSlot(0, 5);
        slotModel.setSlot(1, 5);
        slotModel.setSlot(2, 5);
        //also set symbols for others slots to correctly simulate the slot
        slotModel.setSlot(3, 0);
        slotModel.setSlot(4, 2);
        slotModel.setSlot(5, 4);
        slotModel.setSlot(6, 5);
        slotModel.setSlot(7, 6);
        slotModel.setSlot(8, 7);
        slotModel.matchCheck();
        assertEquals(52, slotModel.getMoney());
    }

    /*
     * Test if it returns 54 dollars when there is one winning row (special symbol).
     */
    @Test
    public void oneRowSameSpecialSymbolShouldWin4Dollar() {
        slotModel = new SlotModel(new MainMenuModel(), 50);
        //Need to specify which game mode in order to make an accurate test.
        MainMenuView.gamemode = "Simulated Casino";
        //winning row
        slotModel.setSlot(0, 1);
        slotModel.setSlot(1, 1);
        slotModel.setSlot(2, 1);
        //also set symbols for others slots to correctly simulate the slot
        slotModel.setSlot(3, 0);
        slotModel.setSlot(4, 2);
        slotModel.setSlot(5, 4);
        slotModel.setSlot(6, 5);
        slotModel.setSlot(7, 6);
        slotModel.setSlot(8, 7);
        slotModel.matchCheck();
        assertEquals(55, slotModel.getMoney());
    }

    /*
     * Test if it returns 110 dollars when there is one winning row and the betting is 2 dollars (special symbol).
     */
    @Test
    public void oneRowSameSpecialSymbolShouldWin10DollarIf2DollarsBetting() {
        slotModel = new SlotModel(new MainMenuModel(), 100);
        //Need to specify which game mode in order to make an accurate test.
        MainMenuView.gamemode = "Simulated Casino";
        slotModel.setBettingMoney(2);
        //winning row
        slotModel.setSlot(0, 10);
        slotModel.setSlot(1, 10);
        slotModel.setSlot(2, 10);
        //also set symbols for others slots to correctly simulate the slot
        slotModel.setSlot(3, 0);
        slotModel.setSlot(4, 2);
        slotModel.setSlot(5, 4);
        slotModel.setSlot(6, 5);
        slotModel.setSlot(7, 6);
        slotModel.setSlot(8, 7);
        slotModel.matchCheck();
        assertEquals(110, slotModel.getMoney());
    }

    /*
     * Test if it returns 125 dollars when there is one winning row and the betting is 5 dollars (special symbol).
     */
    @Test
    public void oneRowSameSpecialSymbolShouldWin25DollarIf5DollarsBetting() {
        slotModel = new SlotModel(new MainMenuModel(), 100);
        //Need to specify which game mode in order to make an accurate test.
        MainMenuView.gamemode = "Simulated Casino";
        slotModel.setBettingMoney(5);
        //winning row
        slotModel.setSlot(0, 10);
        slotModel.setSlot(1, 10);
        slotModel.setSlot(2, 10);
        //also set symbols for others slots to correctly simulate the slot
        slotModel.setSlot(3, 0);
        slotModel.setSlot(4, 2);
        slotModel.setSlot(5, 4);
        slotModel.setSlot(6, 5);
        slotModel.setSlot(7, 6);
        slotModel.setSlot(8, 7);
        slotModel.matchCheck();
        assertEquals(125, slotModel.getMoney());
    }

    /*
     * Test if it stills return 100 dollars when "free play" is chosen.
     */
    @Test
    public void freePlayOptionShouldNotLoseMoney() {
        slotModel = new SlotModel(new MainMenuModel(), 100);
        //Need to specify which game mode in order to make an accurate test.
        MainMenuView.gamemode = "Freeplay";
        slotModel.setSlot(0, 10);
        slotModel.setSlot(1, 9);
        slotModel.setSlot(2, 8);
        slotModel.setSlot(3, 0);
        slotModel.setSlot(4, 2);
        slotModel.setSlot(5, 4);
        slotModel.setSlot(6, 5);
        slotModel.setSlot(7, 6);
        slotModel.setSlot(8, 7);
        slotModel.matchCheck();
        assertEquals(100, slotModel.getMoney());
    }

}