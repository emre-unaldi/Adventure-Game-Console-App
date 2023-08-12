public class SafeHouse extends NormalLocation {
    public SafeHouse(Player player) {
        super(player, "Güvenli Ev");
    }

    @Override
    public boolean onLocation() {
        if (isWinGame()) {
            System.out.println("Tebrikler " + this.getPlayer().getName() +  " Oyunu Kazandın !!!");
        } else {
            System.out.println("Güvenli evdesiniz !");
            System.out.println("Canınız Yenilendi !");
            this.getPlayer().setHealth(this.getPlayer().getOriginalHealth());
        }
        return true;
    }

    public boolean isWinGame() {
        String[] wonAwards = this.getPlayer().getInventory().getAward();
        boolean isWin = true;

        for (String award : wonAwards) {
            if (award == null) {
                isWin = false;
            }
        }
        return isWin;
    }
}
