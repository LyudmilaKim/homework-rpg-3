package com.narxoz.rpg.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BattleEngine {
    private static BattleEngine instance;
    private Random random = new Random(1L);

    private BattleEngine() {
    }

    public static BattleEngine getInstance() {
        if (instance == null) {
            instance = new BattleEngine();
        }
        return instance;
    }

    public BattleEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public void reset() {
        this.random = new Random(1L);
    }

    public EncounterResult runEncounter(List<Combatant> teamA, List<Combatant> teamB) {
        EncounterResult result = new EncounterResult();

        if (teamA == null || teamB == null || teamA.isEmpty() || teamB.isEmpty()) {
            result.setWinner("None");
            result.setRounds(0);
            result.addLog("Battle cannot start: one or both teams are empty or null");
            return result;
        }

        List<Combatant> sideA = new ArrayList<>(teamA);
        List<Combatant> sideB = new ArrayList<>(teamB);

        result.addLog("=== BATTLE START ===");
        result.addLog(sideA.size() + " vs " + sideB.size());

        int rounds = 0;

        while (hasAlive(sideA) && hasAlive(sideB)) {
            rounds++;
            result.addLog("\n--- Round " + rounds + " ---");

            performAttacks(sideA, sideB, result);
            sideB.removeIf(c -> !c.isAlive());

            if (!hasAlive(sideB)) break;

            performAttacks(sideB, sideA, result);
            sideA.removeIf(c -> !c.isAlive());
        }

        String winner = hasAlive(sideA) ? "Heroes (Team A)" : "Enemies (Team B)";
        result.setWinner(winner);
        result.setRounds(rounds);

        result.addLog("\n=== BATTLE OVER ===");
        result.addLog("Winner: " + winner + " after " + rounds + " rounds");

        return result;
    }

    private boolean hasAlive(List<Combatant> team) {
        for (Combatant c : team) {
            if (c.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private void performAttacks(List<Combatant> attackers, List<Combatant> defenders, EncounterResult result) {
        List<Combatant> aliveAttackers = new ArrayList<>();
        for (Combatant c : attackers) {
            if (c.isAlive()) aliveAttackers.add(c);
        }

        List<Combatant> aliveDefenders = new ArrayList<>();
        for (Combatant c : defenders) {
            if (c.isAlive()) aliveDefenders.add(c);
        }

        if (aliveDefenders.isEmpty()) {
            return;
        }

        for (Combatant attacker : aliveAttackers) {
            if (aliveDefenders.isEmpty()) break;

            int targetIdx = random.nextInt(aliveDefenders.size());
            Combatant target = aliveDefenders.get(targetIdx);

            int damage = attacker.getAttackPower();

            boolean isCrit = random.nextDouble() < 0.10;
            if (isCrit) {
                damage *= 2;
                result.addLog(attacker.getName() + " → " + target.getName() + " CRITICAL " + damage);
            } else {
                result.addLog(attacker.getName() + " → " + target.getName() + " " + damage);
            }

            target.takeDamage(damage);

            if (!target.isAlive()) {
                result.addLog("  → " + target.getName() + " defeated");
                aliveDefenders.remove(target);
            }
        }
    }
}