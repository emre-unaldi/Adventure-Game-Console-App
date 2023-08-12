import java.util.Random;

public abstract class BattleLocation extends Location {
    private Monster monster;
    private String award;
    private int maxMonster;

    public BattleLocation(Player player, String name, Monster monster, String award, int maxMonster) {
        super(player, name);
        this.monster = monster;
        this.award = award;
        this.maxMonster = maxMonster;
    }

    @Override
    public boolean onLocation() {
        if (!isAreaClean()) {
            System.out.println("");
            System.out.println("Şuan buradasınız : " + this.getName());
            System.out.println("Dikkatli Ol ! Burada " + this.randomMonsterNumber() + " tane " + this.getMonster().getName() + " yaşıyor !");
            System.out.println("<S>avaş | <K>aç ");

            String selecBattleCase = input.nextLine().toUpperCase();

            if (selecBattleCase.equals("S") && combat(this.getMaxMonster())) {
                System.out.println(this.getName() + " tüm düşmanları yendiniz !");
                addAwardInventory();
                return true;
            }

            if (this.getPlayer().getHealth() <= 0) {
                System.out.println("Öldünüz !!!");
                return false;
            }
        }
        return true;
    }

    public boolean isAreaClean() {
        String[] wonAwards = this.getPlayer().getInventory().getAward();
        boolean isClean = false;

        for (String award : wonAwards) {
            if (award == this.award) {
                System.out.println(this.getName() + " Bölgesi daha önce tamamen temizlendi");
                System.out.println("Tekrar giremezsiniz !");
                isClean = true;
            }
        }

        return isClean;
    }

    public void addAwardInventory() {
        String[] wonAwards = this.getPlayer().getInventory().getAward();

        for (int i = 0; i < wonAwards.length; i++) {
            if (wonAwards[i] == null) {
                wonAwards[i] = this.award;
                break;
            }
        }

        System.out.println(this.getName() + " bölgesinin " + this.award + " ödülünü kazandınız ");
        System.out.println("Kazanılan Bölge Ödülleri");
        System.out.println("*******************************************");
        for (String award : wonAwards) {
            if (award != null) {
                System.out.println(award);
            }
        }
        System.out.println("*******************************************");
    }

    public void afterHit() {
        System.out.println("*******************************************");
        System.out.println("Canınız : " + this.getPlayer().getHealth());
        System.out.println(this.getMonster().getName() + " Canı : " + this.getMonster().getHealth());
        System.out.println("*******************************************");
    }

    public boolean combat(int maxMonster) {
        boolean randomFinish = false;
        for (int i = 1; i <= maxMonster; i++) {
            this.getMonster().setHealth(this.getMonster().getOriginalHealth());
            playerStats();
            monsterStats(i);
            while (this.getPlayer().getHealth() > 0 && this.getMonster().getHealth() > 0) {
                System.out.println("<V>ur | <K>aç");
                String selectCombatCase = input.nextLine().toUpperCase();

                if (selectCombatCase.equals("V")) {
                    if (!randomFinish) {
                        if (randomHit() == 0) {
                            System.out.println("İlk vuruşu yaptınız !!!");
                            playerHit();
                        } else {
                            System.out.println("ilk vuruşu canavar yaptı !!!");
                            monsterHit();
                        }
                        randomFinish = true;
                    } else {
                        playerHit();
                        if (this.getMonster().getHealth() > 0) {
                            monsterHit();
                        }
                    }
                } else {
                    return false;
                }
            }

            if (this.getMonster().getHealth() < this.getPlayer().getHealth()) {
                if (this.getName().equals("Maden")) {
                    System.out.println("------------------------------------------------------------");
                    System.out.println(i + ". Düşmanı yendiniz !");
                    randomTool();
                    System.out.println("------------------------------------------------------------");
                } else {
                    System.out.println("------------------------------------------------------------");
                    System.out.println(i + ". Düşmanı yendiniz !");
                    System.out.println(this.getMonster().getAward() + " para kazandınız !");
                    this.getPlayer().setMoney(this.getPlayer().getMoney() + this.getMonster().getAward());
                    System.out.println("Güncel paranız : " + this.getPlayer().getMoney());
                    System.out.println("------------------------------------------------------------");
                }
            } else {
                return false;
            }

        }
        return true;
    }

    public void dropMessage(String name) {
        System.out.println(name + " Kazandınız");
    }

    public void randomTool() {
        Random random = new Random();
        int randomNumber = random.nextInt(101);

        if (randomNumber > 0 && randomNumber <= 15) {
            dropMessage("Silah");
            randomWeapon();
        } else if (randomNumber > 15 && randomNumber <= 30) {
            dropMessage("Zırh");
            randomArmor();
        } else if(randomNumber > 30 && randomNumber <= 55) {
            dropMessage("Para");
            randomMoney();
        } else {
            System.out.println("Eşya düşmedi !!!");
        }
    }

    public int randomNumber() {
        Random random = new Random();
        return random.nextInt(101);
    }

    public void randomWeapon() {
        int random = randomNumber();

        if (random > 0 && random <= 20) {
            dropWeapon(3);
        } else if (random > 20 && random <= 50) {
            dropWeapon(2);
        } else {
            dropWeapon(1);
        }
    }

    public void dropWeapon(int id) {
        Weapon selectedWeapon = Weapon.getWeaponObjectById(id);
        this.getPlayer().getInventory().setWeapon(selectedWeapon);
        System.out.println(selectedWeapon.getName() + " Kazandınız !!!");
    }

    public void randomArmor() {
        int random = randomNumber();

        if (random > 0 && random <= 20) {
            dropArmor(3);
        } else if (random > 20 && random <= 50) {
            dropArmor(2);
        } else {
            dropArmor(1);
        }
    }

    public void dropArmor(int id) {
        Armor selectedArmor = Armor.getArmorObjectById(id);
        this.getPlayer().getInventory().setArmor(selectedArmor);
        System.out.println(selectedArmor.getName() + " Kazandınız !!!");
    }

    public void randomMoney() {
        int random = randomNumber();

        if (random > 0 && random <= 20) {
            dropMoney(10);
        } else if (random > 20 && random <= 50) {
            dropMoney(5);
        } else {
            dropMoney(1);
        }
    }

    public void dropMoney(int money) {
        this.getPlayer().setMoney(this.getPlayer().getMoney() + money);
        System.out.println(money + " para kazandınız !");
        System.out.println("Güncel paranız : " + this.getPlayer().getMoney());
    }

    public void playerStats() {
        System.out.println("--------------------- Oyuncu Değerleri ---------------------");
        System.out.println("------------------------------------------------------------");
        System.out.println("Sağlık : " + this.getPlayer().getHealth());
        System.out.println("Silah : " + this.getPlayer().getInventory().getWeapon().getName());
        System.out.println("Hasar : " + this.getPlayer().getTotalDamage());
        System.out.println("Zırh : " + this.getPlayer().getInventory().getArmor().getName());
        System.out.println("Bloklama : " + this.getPlayer().getInventory().getArmor().getBlock());
        System.out.println("Para : " + this.getPlayer().getMoney());
        System.out.println("------------------------------------------------------------");
    }

    public void monsterStats(int i) {
        System.out.println("---------------------- " + i + "." + this.getMonster().getName() + " Değerleri ---------------------");
        System.out.println("------------------------------------------------------------");
        System.out.println("Sağlık : " + this.getMonster().getHealth());
        System.out.println("Hasar : " + this.getMonster().getDamage());
        System.out.println("Ödül : " + this.getMonster().getAward());
        System.out.println("------------------------------------------------------------");
    }

    public void playerHit() {
        System.out.println(this.getPlayer().getTotalDamage() + " gücünde vurdunuz");
        monster.setHealth(this.getMonster().getHealth() - this.getPlayer().getTotalDamage());
        afterHit();
    }

    public void monsterHit() {
        System.out.println();
        System.out.println("Canavar size vurdu !");
        int monsterDamage = this.getMonster().getDamage() - this.getPlayer().getInventory().getArmor().getBlock();
        if (monsterDamage < 0) {
            monsterDamage = 0;
        }
        this.getPlayer().setHealth(this.getPlayer().getHealth() - monsterDamage);
        afterHit();
    }

    public int randomMonsterNumber() {
        Random random = new Random();
        return random.nextInt(this.getMaxMonster()) + 1;
    }

    public int randomHit() {
        Random random = new Random();
        return random.nextInt(2);
    }

    public int getMaxMonster() {
        return maxMonster;
    }

    public void setMaxMonster(int maxMonster) {
        this.maxMonster = maxMonster;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }
}
