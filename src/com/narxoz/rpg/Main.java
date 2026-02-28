package com.narxoz.rpg;

import com.narxoz.rpg.adapter.EnemyCombatantAdapter;
import com.narxoz.rpg.adapter.HeroCombatantAdapter;
import com.narxoz.rpg.battle.BattleEngine;
import com.narxoz.rpg.battle.Combatant;
import com.narxoz.rpg.battle.EncounterResult;
import com.narxoz.rpg.enemy.Goblin;
import com.narxoz.rpg.hero.Mage;
import com.narxoz.rpg.hero.Warrior;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== RPG Battle Engine Demo ===\n");

        Warrior warrior = new Warrior("Arthas");
        Mage mage = new Mage("Jaina");
        Goblin goblin = new Goblin();

        System.out.println("Before battle:");
        System.out.printf("  %-10s HP: %d%n", warrior.getName(), warrior.getHealth());
        System.out.printf("  %-10s HP: %d%n", mage.getName(), mage.getHealth());
        System.out.printf("  %-10s HP: %d%n", goblin.getTitle(), goblin.getHealth());
        System.out.println();

        List<Combatant> heroes = new ArrayList<>();
        heroes.add(new HeroCombatantAdapter(warrior));
        heroes.add(new HeroCombatantAdapter(mage));

        List<Combatant> enemies = new ArrayList<>();
        enemies.add(new EnemyCombatantAdapter(goblin));

        BattleEngine engineA = BattleEngine.getInstance();
        BattleEngine engineB = BattleEngine.getInstance();
        System.out.println("Singleton check: " + (engineA == engineB));
        System.out.println();

        engineA.setRandomSeed(42L);
        EncounterResult result = engineA.runEncounter(heroes, enemies);

        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║              BATTLE SUMMARY                ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.printf ("  Winner : %s%n", result.getWinner());
        System.out.printf ("  Rounds  : %d%n", result.getRounds());
        System.out.println("╚════════════════════════════════════════════╝\n");

        System.out.println("Battle Log:");
        System.out.println("────────────────────────────────────────────");
        for (String line : result.getBattleLog()) {
            System.out.println(line);
        }
        System.out.println("────────────────────────────────────────────");

        System.out.println("\nAfter battle:");
        System.out.printf("  %-10s HP: %d%n", warrior.getName(), warrior.getHealth());
        System.out.printf("  %-10s HP: %d%n", mage.getName(), mage.getHealth());
        System.out.printf("  %-10s HP: %d%n", goblin.getTitle(), goblin.getHealth());

        System.out.println("\n=== Demo complete ===");
    }
}